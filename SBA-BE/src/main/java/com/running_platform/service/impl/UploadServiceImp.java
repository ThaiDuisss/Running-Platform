package com.running_platform.service.impl;

import com.running_platform.service.UploadService;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UploadServiceImp implements UploadService {
    private final ServletContext servletContext;

    @Override
    public String handleUploadFile(MultipartFile file, String targetFolder) {
        // don't upload file
        if (file.isEmpty()) {
            return "";
        }
        // relative path: absolute path
        String rootPath = this.servletContext.getRealPath("/resources");
        String finalName = "";
        try {
            byte[] bytes = file.getBytes();

            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists())
                dir.mkdirs();

            // Create the file on server
            finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);
            // uuid - monggoDB - 100 năm mới cso khả năng trùng
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalName;
    }
}
