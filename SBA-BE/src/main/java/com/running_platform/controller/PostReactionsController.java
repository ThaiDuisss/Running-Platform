package com.running_platform.controller;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.PostCommentsResponse;
import com.running_platform.dto.response.PostReactionResponse;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ReactionType;
import com.running_platform.repository.UserRepository;
import com.running_platform.security.CustomUserDetails;
import com.running_platform.service.PostReactionsService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/posts")
@RequiredArgsConstructor
public class PostReactionsController {
    private final PostReactionsService service;
    private final UserRepository userRepository;

    @PostMapping("/{postId}/react")
    public ApiResponse<PostReactionResponse> reactPost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails user, @RequestParam ReactionType type){
        Users users = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new RuntimeException("Please Login!"));
        PostReactionResponse data = service.reactPost(users.getId(), postId, type);

        return ApiResponse.<PostReactionResponse>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }

    @GetMapping("/{postId}/count")
    public ApiResponse<Long> getCountReactFromPost(@PathVariable Long postId){
        Long data = service.countReactFromPost(postId);
        return ApiResponse.<Long>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }

    @GetMapping("/{postId}/reactions")
    public ApiResponse<Page<PostReactionResponse>> getPostReactions(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<PostReactionResponse> data = service.getReactionsByPostId(postId, page, size);
        return ApiResponse.<Page<PostReactionResponse>>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }
}
