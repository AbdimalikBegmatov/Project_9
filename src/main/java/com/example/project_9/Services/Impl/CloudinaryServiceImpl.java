package com.example.project_9.Services.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.project_9.Services.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {

        if (file.isEmpty() || !Objects.equals(file.getContentType(), "image/jpeg")){
            throw new MultipartException("File not found or not support format");
        }

        var result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) result.get("secure_url");
    }

    @Override
    public boolean deleteImage(String publicId) throws IOException {
        var result = cloudinary.uploader().destroy(publicId,ObjectUtils.emptyMap());
        return (boolean) result.get("result");
    }
}
