package com.substring.auth.serviceImpl;

import java.net.URI;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.substring.auth.entities.User;
import com.substring.auth.repositories.UserRepository;
import com.substring.auth.services.FileStorageService;

import lombok.RequiredArgsConstructor;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class CloudinaryFileStorageService
        implements FileStorageService {

    private final Cloudinary cloudinary;

    private final UserRepository userRepository;

    @Override
    public String saveFile(MultipartFile file) {

        try {

            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException(
                        "File is empty"
                );
            }

            Map<?, ?> uploadResult =
                    cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.emptyMap()
                    );

            return uploadResult
                    .get("secure_url")
                    .toString();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Cloudinary upload failed",
                    e
            );
        }
    }

    @Override
    public String deleteFile(String imageUrl) {

        /*
         * TODO:
         * Implement Cloudinary public_id extraction
         * and cloudinary.uploader().destroy(...)
         *
         * For now, don't delete anything.
         */

        return imageUrl;
    }

    @Override
    public ResponseEntity<?> getProfilePicture(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        String imageUrl = user.getImageFile();

        if (imageUrl == null || imageUrl.isBlank()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Profile picture not found");
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(
                URI.create(imageUrl)
        );

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }
}
