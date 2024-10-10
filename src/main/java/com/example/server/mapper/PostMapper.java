package com.example.server.mapper;

import com.example.server.dtos.request.PostRequest;
import com.example.server.dtos.request.PriceRequest;
import com.example.server.dtos.response.PostResponse;
import com.example.server.models.Post;
import com.example.server.models.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "attribute", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "province", ignore = true)
    @Mapping(target = "area", ignore = true)
    @Mapping(target = "price", ignore = true)
    Post postToPost(PostRequest request);

    PostResponse postToPostResponse(Post post);

    @Mapping(target = "user", ignore = true)
    void postToUpdatePost(@MappingTarget Post post, PostRequest request);
}
