package com.running_platform.controller;

import com.running_platform.dto.response.ApiResponse;
import com.running_platform.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/uploadfile")
@RequiredArgsConstructor
public class UploadFileController {

    private final UploadService uploadService;

    @PostMapping
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("targetFolder") String targetFolder) {
        String url = uploadService.handleUploadFile(file, targetFolder);
        return ApiResponse.<String>builder()
                .status("success")
                .message("File image uploaded successfully")
                .data(url)
                .build();
    }
}

