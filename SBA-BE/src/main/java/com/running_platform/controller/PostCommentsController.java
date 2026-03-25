package com.running_platform.controller;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.PostCommentRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.PostCommentsResponse;
import com.running_platform.service.PostCommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/comments")
@RequiredArgsConstructor
public class PostCommentsController {
    private final PostCommentsService postCommentsService;

    @PostMapping
    public ApiResponse<PostCommentsResponse> createComment(@RequestBody PostCommentRequest request,
                                                           Authentication authentication){
        PostCommentsResponse data = postCommentsService.createComment(request, authentication);
        return ApiResponse.<PostCommentsResponse>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }

    @GetMapping("/{postId}")
    public ApiResponse<List<PostCommentsResponse>> getComments(@PathVariable Long postId) {
        List<PostCommentsResponse> data = postCommentsService.getCommentsByPost(postId);
        return ApiResponse.<List<PostCommentsResponse>>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }

    @GetMapping("/{postId}/count")
    public ApiResponse<Long> countCommentFromPost(@PathVariable Long postId){
        Long data = postCommentsService.countCommentFromPost(postId);
        return ApiResponse.<Long>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .data(data)
                .build();
    }

}
