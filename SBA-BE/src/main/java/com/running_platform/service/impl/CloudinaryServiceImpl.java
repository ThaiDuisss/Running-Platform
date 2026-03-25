package com.running_platform.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.running_platform.enums.UploadFolder;
import com.running_platform.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

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

            Map<String, Object> map = Map.of(
                    "folder", uploadFolder.getPath(),
                    "public_id", publicId,
                    "resource_type", "auto",
                    "overwrite", true
            );
            Map<String, Object> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), map);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            log.error("Cloudinary upload failed for file {}", multipartFile.getOriginalFilename(), e);
            throw new RuntimeException("Error uploading file", e);
        }
    }

    @Override
    public void deleteImg(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
            log.error("Delete cloudinary file failed {}", publicId);
        }
    }

    public String uploadBytes(byte[] bytes, Long postId) {
        try {
            String publicId = "post_" + postId + "_" + UUID.randomUUID();

            Map uploadResult = cloudinary.uploader().upload(
                    bytes,
                    Map.of(
                            "folder", "post_images",
                            "public_id", publicId
                    )
            );

            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            throw new RuntimeException("Upload failed", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile multipartFile, UploadFolder uploadFolder) {
        try {
            String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
            log.info("Uploading file {}", fileName);
            Map<String, Object> map = Map.of(
                    "folder", uploadFolder.getPath(),
                    "public_id", fileName,
                    "resource_type", "auto",
                    "overwrite", true
            );
            Map<String, Object> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), map);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            log.error("Cloudinary upload failed for file {}", multipartFile.getOriginalFilename(), e);
            throw new RuntimeException("Error uploading file", e);
        }
    }

    public String uploadHighlightRouteImage(MultipartFile file, Long routeId) {
        try {
            String publicId = "highlight_route_" + routeId + "_" + UUID.randomUUID();

            Map<String, Object> options = Map.of(
                    "folder", UploadFolder.HIGHLIGHT_ROUTE.getPath(),
                    "public_id", publicId,
                    "resource_type", "auto"
            );

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            String url = uploadResult.get("secure_url").toString();
            log.info("Upload highlight route success: {}", url);

            return url;

        } catch (Exception e) {
            log.error("Upload highlight route failed", e);
            throw new RuntimeException("Upload failed", e);
        }
    }
}
