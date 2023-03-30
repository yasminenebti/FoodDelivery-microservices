package com.delivery.app.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService implements FileUpload {
    private final Cloudinary cloudinary;
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        Map<String, String> options = new HashMap<>();
        options.put("resource_type", multipartFile.getContentType().startsWith("image") ? "image" : "video");

        options.put("public_id", UUID.randomUUID().toString());

        Map result = cloudinary.uploader().uploadLarge(
                multipartFile.getInputStream(),
                options
        );

        return result.get("url").toString();
    }
}
