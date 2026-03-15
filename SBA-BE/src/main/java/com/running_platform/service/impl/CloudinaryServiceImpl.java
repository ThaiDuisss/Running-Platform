package com.running_platform.service.impl;

import com.cloudinary.Cloudinary;
import com.running_platform.enums.UploadFolder;
import com.running_platform.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile multipartFile, UploadFolder uploadFolder, long entityId) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            ;
            log.info("Uploading file {}", fileName);

            String publicId = "user_" + entityId;
            log.info("Public ID {}", publicId);

            Map<String,Object> map = Map.of(
                    "folder", uploadFolder.getPath(),
                    "public_id", publicId,
                    "resource_type", "auto",
                    "overwrite", true
            );
            Map<String,Object> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), map);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            log.error("Cloudinary upload failed for file {}", multipartFile.getOriginalFilename(), e);
            throw new RuntimeException("Error uploading file", e);
        }
    }
    @Override
    public void deleteImg(String publicId) {
        try{
            cloudinary.uploader().destroy(publicId,Map.of());
        } catch (Exception e) {
            log.error("Delete cloudinary file failed {}", publicId);
        }
    }
}
