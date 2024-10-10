package com.example.server.services;

import com.example.server.dtos.request.PostRequest;
import com.example.server.dtos.response.ImageResponse;
import com.example.server.dtos.response.PostListResponse;
import com.example.server.dtos.response.PostResponse;
import com.example.server.enums.PostStatusEnum;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest request, PostStatusEnum postStatusEnum);
    Page<PostResponse> getPosts(Pageable pageable, String priceCode, String areaCode, String categoryCode, String provinceCode, Float minPrice, Float maxPrice);
    List<PostResponse> getNewPosts();
    List<ImageResponse> uploadImage(String id, List<MultipartFile> files) throws Exception;
    Page<PostResponse> getPostsOfUser(Pageable pageable, String token);
    PostResponse updatePost(String postId, PostRequest postRequest);
    List<ImageResponse> updateImage(String id, List<MultipartFile> files) throws Exception;
    PostResponse getPostById(String postId);
    void updateExpiredPost();
    void deletePostById(String postId);
}
