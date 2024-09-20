package project.source.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.source.dtos.BlogDTO;
import project.source.dtos.ImageDTO;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Blog;
import project.source.models.entities.Image;
import project.source.respones.ApiResponse;
import project.source.services.implement.BlogService;
import project.source.services.implement.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping ("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @GetMapping("")
    public ResponseEntity<ApiResponse> getImagesToPages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Image> imagesPage = imageService.getAllImages(pageable);

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(imagesPage.getContent())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("")
    public ResponseEntity<?> createImage(@Valid @RequestBody ImageDTO imageDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        Image image = imageService.saveImage(imageDTO);
        return ResponseEntity.ok( "Add picture successfully " + image.toString());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable Long id, @Valid @RequestBody ImageDTO imageDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(errors)
                    .message("Validation failed")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }

        Image updatedImage = imageService.updateImage(id, imageDTO);
        if (updatedImage == null) {
            throw new NotFoundException("Can not resolve image id: " + id);
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .data(updatedImage)
                .message("Change picture successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("deleteImage/{id}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable long id) {
        imageService.deleteImage(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Media deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() +  "_" + fileName;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @GetMapping("/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get( "upload/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
// logger.info(imageName + " not found");
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
//return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
//logger.error("Error occurred while retrieving image: " + e.getMessage
            return ResponseEntity.notFound().build();
        }
    }

}
