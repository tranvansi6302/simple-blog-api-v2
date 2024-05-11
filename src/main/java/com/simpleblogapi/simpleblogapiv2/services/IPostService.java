package com.simpleblogapi.simpleblogapiv2.services;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostUploadRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.PostResponse;
import com.simpleblogapi.simpleblogapiv2.entities.Post;
import com.simpleblogapi.simpleblogapiv2.enums.PostStatus;

import java.io.IOException;
import java.util.List;

public interface IPostService {
    PostResponse createPost(PostCreateRequest request);
    PostResponse uploadThumbnail(String id,PostUploadRequest request) throws IOException;
    PostResponse getPostById(String id);
    List<PostResponse> getAllPosts();
    PostResponse updatePost(String id, PostUpdateRequest request);
    void deletePost(String id);
    List<PostResponse> getMyPosts();
    List<PostResponse> getPostsByStatus(PostStatus status);
}
