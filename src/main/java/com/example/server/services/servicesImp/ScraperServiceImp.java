package com.example.server.services.servicesImp;

import com.example.server.dtos.request.ImageRequest;
import com.example.server.dtos.request.PostRequest;
import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.response.*;
import com.example.server.enums.PostStatusEnum;
import com.example.server.enums.UserStatusEnum;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.mapper.PostMapper;
import com.example.server.mapper.UserMapper;
import com.example.server.models.*;
import com.example.server.repositories.*;
import com.example.server.services.ScraperService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Log4j2
public class ScraperServiceImp implements ScraperService {

    @Value("${spring.website.urls}")
    String url;

    final RoleRepository roleRepository;
    final CategoryRepository categoryRepository;
    final ProvinceRepository provinceRepository;
    final PostRepository postRepository;
    final UserRepository userRepository;
    final AreaRepository areaRepository;
    final PriceRepository priceRepository;
    final AttributeRepository attributeRepository;
    final ImageRepository imageRepository;

    final PostMapper postMapper;
    final UserMapper userMapper;

    final AtomicInteger priceCounter = new AtomicInteger(1);
    final AtomicInteger areaCounter = new AtomicInteger(1);
    final AtomicInteger provinceCounter = new AtomicInteger(1);

    final PasswordEncoder passwordEncoder;

    @Override
    public Set<JsoupResponse> getModel() throws IOException {
        Set<JsoupResponse> jsoupResponses = new HashSet<>();
        if(url.contains("https://phongtro123.com")) {
            extractDataFromWeb(jsoupResponses);
        }

        return jsoupResponses;
    }

    private void extractDataFromWeb(Set<JsoupResponse> responses) throws IOException {
        try {
            int page=1;
            Set<String> titleSet = new HashSet<>();
            Random random = new Random();
            while(page<=200) {
                System.out.println("Page: " + page);
                Role role = roleRepository.findRoleByName("USER")
                        .orElseThrow(() -> new RentalHomeDataModelNotFoundException("User role is not found"));
                List<Category> categoryList = categoryRepository.findAll();
                Document document = null;
                if(page==1){
                    document = Jsoup.connect(this.url).get();
                }else {
                    String pageUrl = this.url + "/?page=" + page;
                    document = Jsoup.connect(pageUrl).get();
                }
                Document documentNewUrl = null;
                Elements postElements = document.select(".post-listing .post-item");

                for (Element postElement : postElements) {
                    JsoupResponse jsoupResponse = new JsoupResponse();
                    Element hrefElement = postElement.selectFirst(".post-title a");
                    String phone = "";
                    if(hrefElement != null){
                        String href = hrefElement.attr("href");
                        if (!href.isEmpty()) {
                            String newUrl = url + href;

                            documentNewUrl = Jsoup.connect(newUrl).get();
                            Elements descripElements = documentNewUrl.select(".post-main-content .section-content p");
                            Element titleElement = documentNewUrl.selectFirst(".page-h1 a");

                            if(documentNewUrl.select(".table").size() > 1) {
                                log.info("tableElement: " + documentNewUrl.select(".table").get(1));
                                Element tableElement = documentNewUrl.select(".table").get(1);
                                if(tableElement != null) {
                                    Elements rows = tableElement.select("tr");
                                    Element nameElement = rows.select(".name").get(1);
                                    String name = nameElement.text();
                                    if (name != null && nameElement.text().contains("Điện thoại:")) {
                                        Element phoneElement = rows.select("td").get(3);
                                        phone = phoneElement.text().trim();
                                    }
                                }
                            } else if (postElement.selectFirst(".btn-quick-call") != null) {
                                Element postPhone = postElement.selectFirst(".btn-quick-call");
                                phone = postPhone.text().split(" ")[1];
                            }else {
                                phone="";
                            }

                            String title = titleElement.text();
                            if (titleSet.contains(title)) {
                                continue;
                            }
                            titleSet.add(title);
                            jsoupResponse.setTitle(title);

                            StringBuilder fullDescription = new StringBuilder();
                            for (Element descripElement : descripElements) {
                                fullDescription.append(descripElement.text()).append("\n");
                            }
                            String description = fullDescription.toString();
                            jsoupResponse.setDescription(description);

                        }else {
                            continue;
                        }
                    }else{
                        continue;
                    }

//                    Element postTitle = postElement.selectFirst(".post-title a");
                    Element postPrice = postElement.selectFirst(".post-price");
                    Element postAcreage = postElement.selectFirst(".post-acreage");
                    Element postAddress = postElement.selectFirst(".post-location a");
                    Element postAvatar = postElement.selectFirst(".post-author img");
                    Element postName = postElement.selectFirst(".author-name");
//                    Element postImages = postElement.selectFirst(".post-thumb img");

//                    String phone = "";
//                    if (postElement.selectFirst(".btn-quick-call") != null) {
//                        Element postPhone = postElement.selectFirst(".btn-quick-call");
//                        phone = postPhone.text();
//                    }

                    long priceId = priceCounter.getAndIncrement();
                    long areaId = areaCounter.getAndIncrement();
                    String price = postPrice.text();

                    String area = postAcreage.text();
                    String address = postAddress.text();

                    String avatar = "";
                    if(!postAvatar.attr("src").isEmpty()) {
                        avatar = postAvatar.attr("src");
                    }else {
                        continue;
                    }
                    String name = postName.text();
//                        String images = postImages != null ? postImages.attr("data-src") : postImages.attr("src");

//                        imageResponses.add(ImageResponse.builder().id(UUID.randomUUID().toString()).image(images).postId(jsoupResponse.getId()).build());
//                        jsoupResponse.setImages(imageResponses);

                    jsoupResponse.setId(UUID.randomUUID().toString());

                    jsoupResponse.setStatus(PostStatusEnum.ACTIVE);

                    String province = address.split(",")[1];
                    String provinceTmp = "";
                    if(province.trim().contains("Hồ Chí Minh") || province.trim().contains("Hà Nội") || province.trim().contains("Đà Nẵng") || province.trim().contains("Hải Phòng") || province.trim().contains("Cần Thơ")) {
                        provinceTmp = "Thành phố " + province.trim();
                    }else {
                        provinceTmp = "Tỉnh " + province.trim();
                    }
                    Province province1 = provinceRepository.findProvinceCodeByValue(provinceTmp);
                    if (province1==null){
                        continue;
                    }
                    long provinceId = provinceCounter.getAndIncrement();
                    jsoupResponse.setProvince(Province.builder()
                            .id(provinceId)
                            .code(province1.getCode())
                            .value(province)
                            .build()
                    );

                    StringBuilder sb = new StringBuilder();
                    String split = price.split(" ")[0];
                    double priceD = 0;

                    if(!split.contains("Thỏa")){
                        priceD = Double.parseDouble(split);
                    }

                    if (split.contains(".")) {
                        sb.append("DU1N");
                    } else if (priceD >= 1 && priceD < 2) {
                        sb.append("TU2N");
                    } else if (priceD >= 2 && priceD < 3) {
                        sb.append("TU3N");
                    } else if (priceD >= 3 && priceD < 5) {
                        sb.append("TU5N");
                    } else if (priceD >= 5 && priceD < 7) {
                        sb.append("TU7N");
                    } else if (priceD >= 7 && priceD < 10) {
                        sb.append("TU0N");
                    } else if (priceD >= 10 && priceD < 15) {
                        sb.append("TE1N");
                    } else if (priceD >= 15) {
                        sb.append("TU15N");
                    }else if(split.contains("Thỏa")){
                        sb.append("Thỏa thuận");
                    }else{
                        continue;
                    }
                    jsoupResponse.setPrice(Price.builder()
                            .id(priceId)
                            .code(sb.toString())
                            .value(price)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
                    );

                    StringBuilder sbAreaCode = new StringBuilder();
                    String[] areaSplit = area.split("\\D");
                    for (String areaTmp : areaSplit) {
                        int areaInt = Integer.parseInt(areaTmp);
                        if (areaInt < 20) {
                            sbAreaCode.append("ON2E");
                        } else if (areaInt >= 20 && areaInt < 30) {
                            sbAreaCode.append("2UMD");
                        } else if (areaInt >= 30 && areaInt < 50) {
                            sbAreaCode.append("3UMD");
                        } else if (areaInt >= 50 && areaInt < 70) {
                            sbAreaCode.append("5UMD");
                        } else if (areaInt >= 70 && areaInt < 90) {
                            sbAreaCode.append("7UMD");
                        } else {
                            sbAreaCode.append("EN9E");
                        }
                        jsoupResponse.setAreaNumber(Float.parseFloat(areaTmp));
                    }

                    jsoupResponse.setArea(Area.builder()
                            .id(areaId)
                            .code(sbAreaCode.toString())
                            .value(area)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
                    );

                    jsoupResponse.setAddress(address);
                    jsoupResponse.setStar((int) (Math.random() * 5) + 1);
                    jsoupResponse.setCreatedAt(LocalDateTime.now());
                    jsoupResponse.setPriceNumber(Float.parseFloat(price.split(" ")[0]));

                    jsoupResponse.setUser(
                            User.builder()
                                    .id(UUID.randomUUID().toString())
                                    .accountType(phone)
                                    .name(name)
                                    .avatar(avatar)
                                    .status(UserStatusEnum.VALID)
                                    .phone(phone)
                                    .roles(Collections.singleton(role))
                                    .createdAt(LocalDateTime.now())
                                    .updatedAt(LocalDateTime.now())
                                    .build()
                    );

                    Category randomCategory = categoryList.get(random.nextInt(categoryList.size()));
                    jsoupResponse.setCategory(randomCategory);
                    responses.add(jsoupResponse);

                    UserRequest userRequest = UserRequest.builder()
                            .name(jsoupResponse.getUser().getName())
                            .accountType(jsoupResponse.getUser().getAccountType())
                            .password(passwordEncoder.encode("123456789"))
                            .email(jsoupResponse.getUser().getEmail())
                            .phone(jsoupResponse.getUser().getPhone())
                            .zalo(jsoupResponse.getUser().getPhone())
                            .googleAccountId("")
                            .avatar(avatar)
                            .build();
                    User user = userMapper.userToUser(userRequest);
                    user.setRoles(Collections.singleton(role));
                    user.setId(UUID.randomUUID().toString());
                    User user1 = userRepository.save(user);

                    Attribute attribute = Attribute.builder()
                            .price(price)
                            .acreage(area)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    Attribute attribute1 = attributeRepository.save(attribute);

                    PostRequest postRequest = PostRequest.builder()
//                            .id(jsoupResponse.getId())
                            .title(jsoupResponse.getTitle())
                            .star(jsoupResponse.getStar())
                            .address(jsoupResponse.getAddress())
                            .categoryCode(randomCategory.getCode())
                            .description(jsoupResponse.getDescription())
                            .userId(user1.getId())
                            .priceNumber(jsoupResponse.getPriceNumber())
                            .priceCode(sb.toString())
                            .areaNumber(jsoupResponse.getAreaNumber())
                            .areaCode(sbAreaCode.toString())
                            .provinceCode(province1.getCode())
                            .status(jsoupResponse.getStatus())
                            .attributeId(attribute1.getId())
                            .createdAt(jsoupResponse.getCreatedAt())
                            .build();

                    Post post = postMapper.postToPost(postRequest);
                    User user2 = userRepository.findById(user1.getId())
                            .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This user id is not existed"));
//                    Area area1 = areaRepository.findAreaByCode(sbAreaCode.toString())
//                            .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This area code ({}) is not existed", sbAreaCode.toString()));
                    Optional<Area> areaOptional = areaRepository.findAreaByCode(sbAreaCode.toString());
                    if(!areaOptional.isPresent()) {
                        continue;
                    }
                    Area area1 = areaOptional.get();
                    Price price1 = priceRepository.findPriceByCode(sb.toString())
                            .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This price code ({}) is not existed", sb.toString()));
                    Province province2 = provinceRepository.findProvinceByCode(province1.getCode())
                            .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This province code ({}) is not existed", province1.getCode()));
                    Category category = categoryRepository.findCategoryByCode(randomCategory.getCode())
                            .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This category code ({}) is not existed", randomCategory.getCode()));
//                    if(area1 == null || price1 == null || province2 == null || category==null){
//                        continue;
//                    }

                    post.setUser(user2);
                    post.setArea(area1);
                    post.setPrice(price1);
                    post.setProvince(province2);
                    post.setCategory(category);
                    post.setAttribute(attribute1);
                    Post post1 = postRepository.save(post);
                    log.info(post.getId());
                    log.info(post1.getId());

                    Elements imageElements = documentNewUrl.select(".swiper-wrapper .swiper-slide img");
                    for (Element imageElement : imageElements) {
                        List<ImageResponse> imageResponses = new ArrayList<>();
                        String avatar1 = "";
                        if(!imageElement.attr("src").isEmpty()){
                            avatar1 = imageElement.attr("src");
                        }else{
                            avatar1 = "";
                        }

//                        Post post2 = postRepository.findById(post.getId())
//                                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("Can not find this post id"));

                        ImageRequest imageRequest = ImageRequest.builder().image(avatar1).postId(post1.getId()).build();
                        Image image = Image.builder()
                                .id(UUID.randomUUID().toString())
                                .image(imageRequest.getImage())
                                .post(post1)
                                .build();

                        ImageResponse imageResponse = ImageResponse.builder()
                                .id(image.getId())
                                .image(image.getImage())
                                .postId(post1.getId())
                                .build();

                        if(imageResponses.size()<imageElements.toArray().length) {
                            imageResponses.add(imageResponse);
                            imageRepository.save(image);
                        }

                        jsoupResponse.setImages(imageResponses);
                    }
//                    Thread.sleep(1500);
                }
                page++;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
