package com.example.server.services.servicesImp;

import com.example.server.dtos.request.ImageRequest;
import com.example.server.dtos.request.PostRequest;
import com.example.server.dtos.response.CloudinaryResponse;
import com.example.server.dtos.response.ImageResponse;
import com.example.server.dtos.response.PostResponse;
import com.example.server.enums.PostStatusEnum;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.mapper.PostMapper;
import com.example.server.models.*;
import com.example.server.repositories.*;
import com.example.server.security.JwtConstant;
import com.example.server.services.CloudinaryService;
import com.example.server.services.PostService;
import com.example.server.utils.FileUploadUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImp implements PostService {

    PostRepository postRepository;
    ProvinceRepository provinceRepository;
    UserRepository userRepository;
    ImageRepository imageRepository;
    AttributeRepository attributeRepository;
    AreaRepository areaRepository;
    CategoryRepository categoryRepository;
    PriceRepository priceRepository;

    PostMapper postMapper;
    CloudinaryService cloudinaryService;

    @Override
    public PostResponse createPost(PostRequest request, PostStatusEnum postStatusEnum) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This user id is not existed"));
        Area area = areaRepository.findAreaByCode(request.getAreaCode())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This area code ({}) is not existed", request.getAreaCode()));
        Price price = priceRepository.findPriceByCode(request.getPriceCode())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This price code ({}) is not existed", request.getPriceCode()));
        Province province = provinceRepository.findProvinceByCode(request.getProvinceCode())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This province code ({}) is not existed", request.getProvinceCode()));
        Category category = categoryRepository.findCategoryByCode(request.getCategoryCode())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This category code ({}) is not existed", request.getCategoryCode()));

        Attribute attribute = Attribute.builder()
                .price(String.valueOf(request.getPriceNumber()))
                .acreage(String.valueOf(request.getAcreage()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        attributeRepository.save(attribute);

        Post post = postMapper.postToPost(request);
        if (PostStatusEnum.ACTIVE.equals(postStatusEnum)) {
            post.setStatus(PostStatusEnum.ACTIVE);
        }
        post.setUser(user);
        post.setAttribute(attribute);
        post.setArea(area);
        post.setPrice(price);
        post.setProvince(province);
        post.setCategory(category);

        return postMapper.postToPostResponse(postRepository.save(post));
    }

    @Override
    public Page<PostResponse> getPosts(Pageable pageable, String priceCode, String areaCode, String categoryCode, String provinceCode, Float minPrice, Float maxPrice) {
        Page<Post> postPage = postRepository.getAllPost(pageable, priceCode, areaCode, categoryCode, provinceCode, minPrice, maxPrice);
        return postPage.map(postMapper::postToPostResponse);
    }


    @Override
    public List<PostResponse> getNewPosts() {
        Pageable pageable = PageRequest.of(0, 7);
        List<Post> posts = postRepository.getNewPostLimit(pageable);
        return posts.stream()
                .map(postMapper::postToPostResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageResponse> uploadImage(String id, List<MultipartFile> files) throws Exception {
        final Post post = postRepository.findById(id)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("Post is not found"));

        List<ImageResponse> images = new ArrayList<>();
        for(MultipartFile file : files) {
            if(file.getSize() == 0){
                continue;
            }
            Image image = new Image();
            image.setOrginalFile(file.getOriginalFilename());

            FileUploadUtils.assertAllowed(file, FileUploadUtils.IMAGE_PATTERN);
            String fileName = FileUploadUtils.getFilename(file.getOriginalFilename());
            CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);

            ImageRequest imageRequest = ImageRequest.builder().image(response.getUrl()).build();
            image.setImage(imageRequest.getImage());
            image.setPost(post);
            image.setCloudinaryImageId(response.getPublicId());
            image.setOrginalFile(file.getOriginalFilename());

            Image image1 = imageRepository.save(image);
            ImageResponse imageResponse = ImageResponse.builder().originalFile(image1.getOrginalFile()).image(image1.getImage()).postId(id).build();

            images.add(imageResponse);
        }
//        image.setImage(response.getUrl());
//        imageRepository.save(image);
//
//        post.setImages(image);
//        post.setCloudinaryImageId(response.getPublicId());

//        postRepository.save(post);
        return images;
    }

    @Override
    public Page<PostResponse> getPostsOfUser(Pageable pageable, String token) {
        SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        boolean isTokenValid = claims.getExpiration().before(new Date());

        if(isTokenValid){
            throw new RentalHomeDataModelNotFoundException("Token is expired");
        }

        String accountType = String.valueOf(claims.get("accountType"));
        if(accountType.isEmpty()){
            throw new RentalHomeDataModelNotFoundException("account type is not existed");
        }

        User user = userRepository.findByAccountType(accountType)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("User is not existed"));

        Page<Post> posts = postRepository.getPostsOfUser(pageable, user.getAccountType());

        return posts.map(postMapper::postToPostResponse);
    }

    @Override
    public PostResponse updatePost(String postId, PostRequest postRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This post is not found"));

        postMapper.postToUpdatePost(post, postRequest);

        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This user id is not existed"));
        Area area = areaRepository.findAreaByCode(postRequest.getAreaCode())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This area code ({}) is not existed", postRequest.getAreaCode()));
        Price price = priceRepository.findPriceByCode(postRequest.getPriceCode())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This price code ({}) is not existed", postRequest.getPriceCode()));
        Province province = provinceRepository.findProvinceByCode(postRequest.getProvinceCode())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This province code ({}) is not existed", postRequest.getProvinceCode()));
        Category category = categoryRepository.findCategoryByCode(postRequest.getCategoryCode())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This category code ({}) is not existed", postRequest.getCategoryCode()));

        post.setUser(user);
        post.setArea(area);
        post.setPrice(price);
        post.setProvince(province);
        post.setCategory(category);
        post.setStatus(PostStatusEnum.ACTIVE);

        // Lưu lại thực thể đã cập nhật
        return postMapper.postToPostResponse(postRepository.save(post));
    }


    @Override
    public List<ImageResponse> updateImage(String postId, List<MultipartFile> files) throws Exception {
       postRepository.findById(postId)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This post is not existed"));

        List<Image> existingImages = imageRepository.getAllOriginalFileInPost(postId);

        Set<String> newFileNames = new HashSet<>();
        for (MultipartFile file : files) {
            if (file.getSize() > 0) {
                newFileNames.add(file.getOriginalFilename());
            }
        }

        for (Image img : existingImages) {
            if (newFileNames.contains(img.getOrginalFile())) {
                cloudinaryService.deleteFile(img.getCloudinaryImageId());
                imageRepository.deleteImageByOriginalFile(img.getOrginalFile(), postId);
            }
        }

        return this.uploadImage(postId, files);
    }

    @Override
    public PostResponse getPostById(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This post is not found"));

        return postMapper.postToPostResponse(post);
    }

    @Override
    @Scheduled(cron = "0 0 23 * * *")
    @Transactional
    public void updateExpiredPost() {
        List<Post> activePosts = postRepository.findByStatus(PostStatusEnum.ACTIVE);
        activePosts.forEach(post -> {
            LocalDateTime createdAt = post.getCreatedAt();
            LocalDateTime expirationDate = createdAt.plusMonths(5);

            if (LocalDateTime.now().isAfter(expirationDate)) {
                post.setStatus(PostStatusEnum.EXPIRED);
                postRepository.save(post);
            }
        });
    }

    @Override
    public void deletePostById(String postId) {
        postRepository.deleteById(postId);
    }
}
