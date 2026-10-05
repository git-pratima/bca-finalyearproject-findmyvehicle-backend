package com.pratima.bca.findmyvehicle.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final Cloudinary cloudinary;
    private final String imageFolder;

    public ImageService(
            Cloudinary cloudinary,
            @Value("${cloudinary.cloudinaryImageFolder}") String imageFolder) {
        this.cloudinary = cloudinary;
        this.imageFolder = imageFolder;
    }

    public List<String> uploadVehicleImages(String regNumber, List<MultipartFile> imageFiles) {
        if (regNumber == null || regNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle registration number is required");
        }
        if (imageFiles == null || imageFiles.isEmpty()) {
            return List.of();
        }

        String safeRegNumber = regNumber.trim().replaceAll("[^A-Za-z0-9_-]", "_");
        String vehicleFolder = imageFolder + "/" + safeRegNumber;
        List<String> imageUrls = new ArrayList<>();
        int imageVersion = 1;

        for (MultipartFile imageFile : imageFiles) {
            if (imageFile == null || imageFile.isEmpty()) {
                continue;
            }

            Map<String, Object> uploadOptions = new HashMap<>();
            uploadOptions.put("folder", vehicleFolder);
            uploadOptions.put("public_id", safeRegNumber + "-v" + imageVersion++);
            uploadOptions.put("resource_type", "image");

            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), uploadOptions);
                Object secureUrl = uploadResult.get("secure_url");
                if (!(secureUrl instanceof String)) {
                    throw new IllegalStateException("Cloudinary did not return a secure image URL");
                }
                imageUrls.add((String) secureUrl);
            } catch (IOException exception) {
                throw new IllegalStateException("Failed to upload vehicle image to Cloudinary", exception);
            }
        }

        return imageUrls;
    }

    public String uploadNotificationImage(String regNumber, Long notificationId, MultipartFile imageFile) {
        if (regNumber == null || regNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle registration number is required");
        }
        if (notificationId == null) {
            throw new IllegalArgumentException("Notification ID is required");
        }
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        String safeRegNumber = regNumber.trim().replaceAll("[^A-Za-z0-9_-]", "_");
        Map<String, Object> uploadOptions = new HashMap<>();
        uploadOptions.put("folder", imageFolder + "/" + safeRegNumber + "/notifications");
        uploadOptions.put("public_id", "notification-" + notificationId);
        uploadOptions.put("resource_type", "image");

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), uploadOptions);
            Object secureUrl = uploadResult.get("secure_url");
            if (!(secureUrl instanceof String)) {
                throw new IllegalStateException("Cloudinary did not return a secure notification image URL");
            }
            return (String) secureUrl;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to upload notification image to Cloudinary", exception);
        }
    }

    public String uploadProfileImage(Long userId, MultipartFile imageFile) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required to upload a profile image");
        }
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }

        Map<String, Object> uploadOptions = new HashMap<>();
        uploadOptions.put("folder", imageFolder + "/" + userId);
        uploadOptions.put("public_id", userId + "-profile");
        uploadOptions.put("overwrite", true);
        uploadOptions.put("resource_type", "image");

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), uploadOptions);
            Object secureUrl = uploadResult.get("secure_url");
            if (!(secureUrl instanceof String)) {
                throw new IllegalStateException("Cloudinary did not return a secure profile image URL");
            }
            return (String) secureUrl;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to upload profile image to Cloudinary", exception);
        }
    }
}
