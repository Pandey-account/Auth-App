package com.substring.auth.services;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveFile(MultipartFile file);

    String deleteFile(String fileName);

    ResponseEntity<?> getProfilePicture(
            Authentication authentication
    ) throws IOException;
}
