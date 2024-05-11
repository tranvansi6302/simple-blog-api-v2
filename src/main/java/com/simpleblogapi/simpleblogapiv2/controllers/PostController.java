package com.simpleblogapi.simpleblogapiv2.controllers;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.PostUploadRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.ApiResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.PostResponse;
import com.simpleblogapi.simpleblogapiv2.enums.PostStatus;
import com.simpleblogapi.simpleblogapiv2.exceptions.AppException;
import com.simpleblogapi.simpleblogapiv2.exceptions.ErrorCode;
import com.simpleblogapi.simpleblogapiv2.services.IPostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v2/posts")
@RequiredArgsConstructor
public class PostController {
    private final IPostService postService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        ApiResponse<List<PostResponse>> response = ApiResponse.<List<PostResponse>>builder()
                .result(postService.getAllPosts())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable String id) {
        ApiResponse<PostResponse> response = ApiResponse.<PostResponse>builder()
                .result(postService.getPostById(id))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @RequestBody @Valid PostCreateRequest request) {
        ApiResponse<PostResponse> response = ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .message("Post created successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable String id,
            @RequestBody @Valid PostUpdateRequest request) {
        ApiResponse<PostResponse> response = ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(id, request))
                .message("Post updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message("Post deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getMyPosts() {
        ApiResponse<List<PostResponse>> response = ApiResponse.<List<PostResponse>>builder()
                .result(postService.getMyPosts())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPostsByStatus(@PathVariable PostStatus status) {
        ApiResponse<List<PostResponse>> response = ApiResponse.<List<PostResponse>>builder()
                .result(postService.getPostsByStatus(status))
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/upload")
    public ResponseEntity<ApiResponse<PostResponse>> uploadThumbnail(
            @PathVariable String id,
            @ModelAttribute("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_IS_REQUIRED);
        }
        if (isImageFile(file)) {
            throw new AppException(ErrorCode.FILE_FORMAT_NOT_SUPPORTED);
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new AppException(ErrorCode.FILE_SIZE_TOO_LARGE);
        }
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst().orElseThrow().getAuthority();
        if (role.equals("ROLE_USER")) {
            PostResponse post = postService.getPostById(id);
            if (!post.getAuthor().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        }
        String thumbnail = storeImage(file);
        PostUploadRequest request = PostUploadRequest.builder()
                .thumbnail(thumbnail)
                .build();
        ApiResponse<PostResponse> response = ApiResponse.<PostResponse>builder()
                .result(postService.uploadThumbnail(id, request))
                .message("Thumbnail uploaded successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    private String storeImage(MultipartFile file) throws IOException {
        if (isImageFile(file) || file.getOriginalFilename() == null) {
            throw new AppException(ErrorCode.FILE_FORMAT_NOT_SUPPORTED);
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFileName = System.currentTimeMillis() + "-" + fileName;
        // Noi upload anh
        Path path = Paths.get("uploads");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path des = Paths.get(path + File.separator + uniqueFileName);
        Files.copy(file.getInputStream(), des, StandardCopyOption.REPLACE_EXISTING); // StandardCopyOption.REPLACE_EXISTING ghi de neu ton tai
        return uniqueFileName;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType == null || !contentType.startsWith("image/");
    }
}
