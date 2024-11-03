package org.example.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map uploadFile(MultipartFile file, String folderName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("File name is null or invalid.");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        Map uploadedFile = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "format", extension,
                        "resource_type", "raw",
                        "folder", folderName,
                        "public_id", originalFilename
                ));

        printUrl(uploadedFile);

        return uploadedFile;
    }

    public Map uploadImage(MultipartFile file, String folderName) throws IOException {
        Map uploadedFile = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", folderName
                ));

        printUrl(uploadedFile);

        return uploadedFile;
    }

    public Map uploadVideo(MultipartFile file, String folderName) throws IOException {
        Map uploadedFile = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "folder", folderName
                ));

        printUrl(uploadedFile);

        return uploadedFile;
    }

    public Map deleteRaw(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                "resource_type", "raw",
                "invalidate", true
        ));
    }

    private static void printUrl(Map uploadedFile) {
        String url = (String) uploadedFile.get("secure_url");
        System.out.println("url: " + url);
    }
}
