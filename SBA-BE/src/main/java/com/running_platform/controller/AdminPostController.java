package com.running_platform.controller;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.response.AdminPostResponse;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.service.AdminPostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
@Tag(name = "Admin Manage Posts")
public class AdminPostController {
    private final AdminPostService adminPostService;

    @GetMapping()
    public ApiResponse<Page<AdminPostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<AdminPostResponse> data = adminPostService.getAllPosts(PageRequest.of(page, size));
        return ApiResponse.<Page<AdminPostResponse>>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .message("Get posts successfully")
                .data(data)
                .build();
    }

    @PutMapping("/{id}/approve")
    public ApiResponse<AdminPostResponse> approve(@PathVariable Long id){
        AdminPostResponse data = adminPostService.approvePost(id);
        return ApiResponse.<AdminPostResponse>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .message("Approved successfully")
                .data(data)
                .build();
    }

    @PutMapping("/{id}/reject")
    public ApiResponse<AdminPostResponse> reject(@PathVariable Long id){
        AdminPostResponse data = adminPostService.rejectPost(id);
        return ApiResponse.<AdminPostResponse>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .message("Rejected successfully")
                .data(data)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<AdminPostResponse> delete(@PathVariable Long id){
        AdminPostResponse data = adminPostService.deletePost(id);
        return ApiResponse.<AdminPostResponse>builder()
                .status("SUCCESS")
                .code(ErrorEnum.SUCCESS.getCode())
                .message("Deleted successfully")
                .data(data)
                .build();
    }

}