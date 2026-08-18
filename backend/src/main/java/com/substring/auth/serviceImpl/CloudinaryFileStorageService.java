package com.substring.auth.serviceImpl;

import java.net.URL;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
                throw new IllegalArgumentException("File is empty");
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

        // TODO: Implement Cloudinary delete later

        return imageUrl;
    }

    @Override
    public ResponseEntity<Resource> getProfilePicture(
            Authentication authentication
    ) {

        try {

            String email = authentication.getName();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found"
                            ));

            String imageUrl = user.getImageFile();

            if (imageUrl == null || imageUrl.isBlank()) {

                return ResponseEntity
                        .notFound()
                        .build();
            }

            URL url = new URL(imageUrl);

            InputStreamResource resource =
                    new InputStreamResource(
                            url.openStream()
                    );

            String contentType =
                    url.openConnection()
                            .getContentType();

            if (contentType == null) {
                contentType =
                        MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity
                    .ok()
                    .header(
                            HttpHeaders.CONTENT_TYPE,
                            contentType
                    )
                    .body(resource);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to load profile picture",
                    e
            );
        }
    }
}
