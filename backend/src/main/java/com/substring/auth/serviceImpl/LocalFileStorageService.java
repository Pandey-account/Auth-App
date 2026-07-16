package com.substring.auth.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.substring.auth.entities.User;
import com.substring.auth.repositories.UserRepository;
import com.substring.auth.services.FileStorageService;

@Service
@Profile("local")
public class LocalFileStorageService implements FileStorageService {

	@Value("${file.upload-dir}")
	private String uploadDir;
	@Autowired
	private UserRepository userRepository;

	public String saveFile(MultipartFile file) {

		try {
			String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			Path filePath = uploadPath.resolve(filename);

			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			return filename;

		} catch (Exception ex) {
			throw new RuntimeException("File upload failed ", ex);
		}
	}

	public ResponseEntity<Resource> getProfilePicture(Authentication authentication) throws IOException {

		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Path path = Paths.get("uploads").resolve(user.getImageFile());

		String contentType = Files.probeContentType(path);

		Resource resource = new UrlResource(path.toUri());
//	    System.out.println(resource);

		if (!resource.exists()) {
			throw new RuntimeException("Image not found");
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	@Override
	public String deleteFile(String fileName){

	    try{

	        if(fileName==null || fileName.isBlank()){
	            return "";
	        }

	        Path filePath=Paths.get(uploadDir).resolve(fileName);

	        Files.deleteIfExists(filePath);

	        return fileName;

	    }catch(Exception e){

	        throw new RuntimeException(e);

	    }

	}
}
