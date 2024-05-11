package com.simpleblogapi.simpleblogapiv2.mappers;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.PostResponse;
import com.simpleblogapi.simpleblogapiv2.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(PostCreateRequest request);

    PostResponse toPostResponse(Post post);

    @Mapping(target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "content", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "status", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)


    void toPost(@MappingTarget Post post, PostUpdateRequest request);
}
