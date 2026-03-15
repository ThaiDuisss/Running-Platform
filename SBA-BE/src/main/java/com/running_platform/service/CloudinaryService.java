package com.running_platform.service;

import com.running_platform.enums.UploadFolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface CloudinaryService {
    String uploadFile(MultipartFile multipartFile, UploadFolder uploadFolder, long entityId);

    void deleteImg(String publicId);
}
