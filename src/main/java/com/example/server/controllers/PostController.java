package com.example.server.controllers;

import com.example.server.dtos.request.DeleteImageRequest;
import com.example.server.dtos.request.ImageRequest;
import com.example.server.dtos.request.JsoupRequest;
import com.example.server.dtos.request.PostRequest;
import com.example.server.dtos.response.ImageResponse;
import com.example.server.dtos.response.JsoupResponse;
import com.example.server.dtos.response.PostListResponse;
import com.example.server.dtos.response.PostResponse;
import com.example.server.enums.PostStatusEnum;
import com.example.server.models.Image;
import com.example.server.repositories.*;
import com.example.server.services.CloudinaryService;
import com.example.server.services.PostService;
import com.example.server.services.ScraperService;
import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostService postService;
    ImageRepository imageRepository;
    CloudinaryService cloudinaryService;
    ScraperService scraperService;

    @GetMapping
    ResponseEntity<PostListResponse> getPosts(
            @RequestParam(defaultValue = "", required = false) String priceCode,
            @RequestParam(defaultValue = "", required = false) String areaCode,
            @RequestParam(defaultValue = "", required = false) String categoryCode,
            @RequestParam(defaultValue = "", required = false) String provinceCode,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "4") int limit,
            @RequestParam(name = "priceNumber", required = false) List<Float> priceNumbers
    ) {
        PageRequest pageRequest = PageRequest.of(
                offset, limit,
                Sort.by("id").ascending()
        );

        Float minPrice = (priceNumbers != null && priceNumbers.size() > 0) ? priceNumbers.get(0) : null;
        Float maxPrice = (priceNumbers != null && priceNumbers.size() > 1) ? priceNumbers.get(1) : null;

        Page<PostResponse> postPage = postService.getPosts(pageRequest, priceCode, areaCode, categoryCode, provinceCode, minPrice, maxPrice);

        long totalPage = postPage.getTotalElements();
        List<PostResponse> postResponses = postPage.getContent();

        return ResponseEntity.status(HttpStatus.OK).body(PostListResponse.builder().postResponse(postResponses).totalPage(totalPage).build());
    }


    @GetMapping("/new-posts")
    ResponseEntity<List<PostResponse>> getNewPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getNewPosts());
    }

    @PostMapping("/image")
    ResponseEntity<List<ImageResponse>> uploadImage(@PathVariable("id") String id, @ModelAttribute("files") List<MultipartFile> files) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(postService.uploadImage(id, files));
    }

    @PostMapping("/create-post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostResponse> createPost(
            @ModelAttribute PostRequest postRequest,
            @RequestPart("files") List<MultipartFile> files
    ) throws Exception {
        PostResponse postResponse = postService.createPost(postRequest, PostStatusEnum.ACTIVE);

        List<ImageResponse> images = postService.uploadImage(postResponse.getId(), files);

        postResponse.setImages(images);

        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @PostMapping("/getPostsOfUser")
    public ResponseEntity<PostListResponse> getPostsOfUser(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "5") int limit
    ){
        String extractedToken = token.substring(7);
        PageRequest pageRequest = PageRequest.of(
                offset, limit,
                Sort.by("id").ascending()
        );

        Page<PostResponse> pageResponse = postService.getPostsOfUser(pageRequest, extractedToken);

        long totalPage = pageResponse.getTotalElements();
        List<PostResponse> postResponses = pageResponse.getContent();

        return ResponseEntity.status(HttpStatus.OK).body(PostListResponse.builder().postResponse(postResponses).totalPage(totalPage).build());
    }

    @PutMapping("/{postId}")
    ResponseEntity<PostResponse> updatePost(
            @PathVariable("postId") String postId,
            @ModelAttribute PostRequest postRequest,
            @RequestPart("files") Optional<List<MultipartFile>> files
    ) throws Exception {
        PostResponse postResponse = postService.updatePost(postId, postRequest);

        if(files.isPresent() && !files.get().isEmpty()) {
            List<ImageResponse> imageResponses = postService.updateImage(postId, files.get());
            postResponse.setImages(imageResponses);
        }

        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @PostMapping(value = "/delete-img")
    ResponseEntity<String> deleteImage(@RequestBody DeleteImageRequest request) {
        Image image = imageRepository.getImageByImageUrl(request.getImage());
        cloudinaryService.deleteFile(image.getCloudinaryImageId());
        imageRepository.deleteImageByImage(request.getImage());

        return ResponseEntity.status(HttpStatus.OK).body("Delete successful");
    }

    @DeleteMapping("/{postId}")
    ResponseEntity<String> deletePostById(@PathVariable("postId") String postId) {
        postService.deletePostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
    }

    @GetMapping("/get-post/{postId}")
    ResponseEntity<PostResponse> getPostById(@PathVariable("postId") String postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(postId));
    }

    @GetMapping("/scraper")
    public Set<JsoupResponse> getModel() throws IOException {
        return  scraperService.getModel();
    }
}
