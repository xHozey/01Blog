package com._Blog.Backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary)  {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file, String folder) {
        try {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folder,
                        "resource_type", "auto"
                )
        );
        return (String) uploadResult.get("secure_url");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
