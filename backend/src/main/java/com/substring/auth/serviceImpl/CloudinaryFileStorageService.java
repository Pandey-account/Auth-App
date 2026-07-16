package com.substring.auth.serviceImpl;

import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.substring.auth.services.FileStorageService;
import lombok.RequiredArgsConstructor;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class CloudinaryFileStorageService implements FileStorageService {

	private final Cloudinary cloudinary;

	@Override
	public String saveFile(MultipartFile file) {

		try {

			Map  uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

			return uploadResult.get("secure_url").toString();

		} catch (Exception e) {

			throw new RuntimeException(e);

		}

	}

	@Override
	public String deleteFile(String imageUrl) {

		return imageUrl;

	}

	@Override
	public ResponseEntity<Resource> getProfilePicture(Authentication authentication) {

		throw new UnsupportedOperationException("Cloudinary images are served directly");

	}

}
