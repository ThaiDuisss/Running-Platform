package com.running_platform.controller;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.CommentReactionResponse;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ReactionType;
import com.running_platform.repository.UserRepository;
import com.running_platform.security.CustomUserDetails;
import com.running_platform.service.CommentReactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/posts/comments")
@RequiredArgsConstructor
public class CommentReactionsController {
    private final UserRepository userRepository;
    private final CommentReactionsService service;

    @PostMapping("/{commentId}/react")
    public ApiResponse<CommentReactionResponse> reactPost(@PathVariable Long commentId, @AuthenticationPrincipal CustomUserDetails user, @RequestParam ReactionType type){
        Users users = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new RuntimeException("Please Login!"));
        CommentReactionResponse data = service.reactToComment(commentId,users.getId(), type);
        return ApiResponse.<CommentReactionResponse>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }

    @GetMapping("/{commentId}/count")
    public ApiResponse<Long> getCountReactFromComment(@PathVariable Long commentId){
        Long data = service.countReactFromComment(commentId);
        return ApiResponse.<Long>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }

    @GetMapping("/{commentId}/reactions")
    public ApiResponse<Page<CommentReactionResponse>> getPostReactions(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<CommentReactionResponse> data = service.getReactionsByCommentId(commentId, page, size);
        return ApiResponse.<Page<CommentReactionResponse>>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }
}
