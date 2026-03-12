package com.running_platform.controller;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.CreatePostRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.PostResponse;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    @PostMapping
    public ApiResponse<PostResponse> createPost(@RequestBody CreatePostRequest request, Authentication authentication) {
        PostResponse data = postService.createPost(request, authentication);
        return ApiResponse.<PostResponse>builder()
                .status("SUCCESS")
                .code(200)
                .message("Create post successfully")
                .data(data)
                .build();
    }

    @GetMapping("/my-posts")
    public ApiResponse<Page<PostResponse>> getPostByUser(Authentication authentication,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size)
    {
        String username = authentication.getName();
        System.out.println("Username: " + username);
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(user);
        System.out.println("List post" + postService.getPostsByUser(user.getId(), PageRequest.of(page, size)));
        Page<PostResponse> data = postService.getPostsByUser(user.getId(), PageRequest.of(page, size));

        return ApiResponse.<Page<PostResponse>>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }

}