package com.example.server.services;

import com.example.server.dtos.response.CloudinaryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    CloudinaryResponse uploadFile(MultipartFile file, String fileName) throws Exception;
}
