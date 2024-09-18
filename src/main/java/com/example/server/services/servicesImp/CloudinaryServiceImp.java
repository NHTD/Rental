package com.example.server.services.servicesImp;

import com.cloudinary.Cloudinary;
import com.example.server.dtos.response.CloudinaryResponse;
import com.example.server.services.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryServiceImp implements CloudinaryService {

    Cloudinary cloudinary;

    @Override
    public CloudinaryResponse uploadFile(MultipartFile file, String fileName) throws Exception {
        try{
            Map<String, Object> map = new HashMap<>();
            map.put("public_id", "nhtd/product/" + fileName);

            Map result = cloudinary.uploader().upload(file.getBytes(), map);
            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder().publicId(publicId).url(url).build();
        }catch (Exception e) {
            throw new Exception("Failed to upload file");
        }
    }
}
