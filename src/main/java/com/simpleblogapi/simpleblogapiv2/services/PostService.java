package com.simpleblogapi.simpleblogapiv2.services;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostUploadRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.PostResponse;
import com.simpleblogapi.simpleblogapiv2.entities.Category;
import com.simpleblogapi.simpleblogapiv2.entities.Post;
import com.simpleblogapi.simpleblogapiv2.entities.User;
import com.simpleblogapi.simpleblogapiv2.enums.PostStatus;
import com.simpleblogapi.simpleblogapiv2.exceptions.AppException;
import com.simpleblogapi.simpleblogapiv2.exceptions.ErrorCode;
import com.simpleblogapi.simpleblogapiv2.mappers.PostMapper;
import com.simpleblogapi.simpleblogapiv2.repositories.CategoryRepository;
import com.simpleblogapi.simpleblogapiv2.repositories.PostRepository;
import com.simpleblogapi.simpleblogapiv2.repositories.UserRepository;
import com.simpleblogapi.simpleblogapiv2.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService implements IPostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostMapper postMapper;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PostResponse createPost(PostCreateRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Post post = postMapper.toPost(request);
        post.setAuthor(user);
        post.setStatus(PostStatus.UNAPPROVED);
        post.setCategory(category);
        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PostResponse uploadThumbnail(String id, PostUploadRequest request) throws IOException {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND));
        // Get role
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst().orElseThrow().getAuthority();
        if (role.equals("ROLE_USER")) {
            checkAccessPermission(post);
        }

        if (post.getThumbnail() == null) {
            post.setThumbnail(request.getThumbnail());
        } else {
            Path oldImagePath = Paths.get("uploads", post.getThumbnail());
            Files.deleteIfExists(oldImagePath);
            post.setThumbnail(request.getThumbnail());
        }

        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    public PostResponse getPostById(String id) {
        return postMapper.toPostResponse(postRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND)));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toPostResponse).toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PostResponse updatePost(String id, PostUpdateRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND));
        // Get role
        String role = JwtTokenUtil.getRole();
        if (role.equals("ROLE_USER")) {
            checkAccessPermission(post);
        }

        if(request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                    () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            post.setCategory(category);
        }
        if(!request.getTitle().isEmpty()) {
            post.setTitle(request.getTitle());
        }
        if(!request.getContent().isEmpty()) {
            post.setContent(request.getContent());
        }
        if(request.getStatus() != null) {
            post.setStatus(request.getStatus());
        }
        // refactor
        // postMapper.toPost(post, request);


        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void deletePost(String id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND));
        // Get role
        String role = JwtTokenUtil.getRole();
        if (role.equals("ROLE_USER")) {
          checkAccessPermission(post);
        }
        postRepository.delete(post);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<PostResponse> getMyPosts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<PostResponse> postResponses =  postRepository.findByAuthor(user).stream()
                .map(postMapper::toPostResponse).toList();
        log.info("User {} has {} posts", email, postResponses.size());
        return postResponses;
    }

    @Override
    public List<PostResponse> getPostsByStatus(PostStatus status) {
        return postRepository.findByStatus(status).stream()
                .map(postMapper::toPostResponse).toList();
    }

    private void checkAccessPermission(Post post) {
        if (!post.getAuthor().getEmail()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
    }
}
