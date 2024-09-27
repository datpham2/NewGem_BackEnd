package project.source.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import project.source.models.enums.ImageDirectory;
import project.source.respones.ApiResponse;
import project.source.services.implement.BlogService;
import project.source.services.implement.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping ("/image")
@RequiredArgsConstructor
@Tag(name = "Image", description = "Operations related to images")
public class ImageController {
    private final ImageService imageService;
    private final BlogService blogService;
//    private final HotelService hotelService;
//    private final RoomService roomService;

    @Operation(
            method = "GET",
            summary = "Get all images in the database (might need admin authentication)" ,
            description = "Send a request to get all the images data ('imageId', 'imageURL', 'imageDirectory') existed in the database")
    @GetMapping("")
    public ResponseEntity<ApiResponse> getImagesToPages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Image> imagesPage = imageService.getAllImages(pageable);

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(imagesPage.getContent())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

//    @GetMapping("/getAllImage/{id}")
//    public ResponseEntity<ApiResponse> getAllImage(@PathVariable Long id) {
//        ApiResponse apiResponse = ApiResponse.builder()
//                .data(studentService.getAllStudentImages(id))
//                .status(HttpStatus.OK.value())
//                .message("Get successfully")
//                .build();
//        return ResponseEntity.ok(apiResponse);
//    }


    @Operation(
            method = "POST",
            summary = "Post the image body to create a new image",
            description = "Send a request with image body ('imageId', 'imageURL', 'imageDirectory') to build and save a new object for class 'Image')")
    @PostMapping("")
    public ResponseEntity<?> createImage(@Valid @RequestBody Long id, ImageDTO imageDTO, ImageDirectory imageDirectory, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        Image image = imageService.saveImage(id, imageDTO, ImageDirectory.Blog);
        return ResponseEntity.ok("Add picture successfully " + image.toString());
    }

    @Operation(
            method = "PUT",
            summary = "Put the new image body to an existed image",
            description = "Send a request to find and save a new body ('imageId', 'imageURL', 'imageDirectory') for a targeted 'imageId', respond with a rejecting message if not valid" )
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

    @Operation(
            method = "DELETE",
            summary = "Set an image status to inactive",
            description = "Send a request to set the 'status' enumerate from a targeted 'imageId' to 'INACTIVE'" )
    @DeleteMapping("deleteImage/{id}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable long id) {
        imageService.deleteImage(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Media deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(
            method = "POST",
            summary = "upload an image to the database",
            description = "Send a request to upload a file to the local server as an object and store it under an 'imageId'" )
    @PostMapping(value = "/uploadsImage/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadImage(@PathVariable(value = "id") Long id, @RequestParam(value = "source") ImageDirectory imageDirectory,@ModelAttribute("files") List<MultipartFile> files) throws IOException {
        List<Image> Images = new ArrayList<>();
        int count = 0;
        blogService.getBlogById(id);
        for (MultipartFile file : files) {
            if (file != null) {
                if (file.getSize() == 0) {
                    count++;
                    continue;
                }
            }
            String fileName = storeFile(file);
            ImageDTO imageDTO = ImageDTO.builder()
                    .imageURL(fileName)
                    .blog_id(id)
                    .build();
            Image blogImage = imageService.saveImage(id, imageDTO,  imageDirectory);
            Images.add(blogImage);
        }
        if (count == 1) {
            throw new IllegalArgumentException("File rong");
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Upload Image Url successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        java.nio.file.Path uploadDdir = Paths.get("upload");
        if (!Files.exists(uploadDdir)) {
            Files.createDirectory(uploadDdir);
        }
        java.nio.file.Path destination = Paths.get(uploadDdir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @Operation(
            method = "GET",
            summary = "Get an image with its name",
            description = "Send a request to get the image data with corresponding parameters ('imageName') " )
    @GetMapping("getimage/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("upload/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("upload/notfound.jpeg").toUri()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }


        //    @GetMapping("/{imageName}")
//    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
//        try {
//            java.nio.file.Path imagePath = Paths.get( "upload/"+imageName);
//            UrlResource resource = new UrlResource(imagePath.toUri());
//            if (resource.exists()) {
//                return ResponseEntity.ok()
//                        .contentType(MediaType.IMAGE_JPEG)
//                        .body(resource);
//            } else {
//// logger.info(imageName + " not found");
//                return ResponseEntity.ok()
//                        .contentType(MediaType.IMAGE_JPEG)
//                        .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
////return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
////logger.error("Error occurred while retrieving image: " + e.getMessage
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    private String storeFile(MultipartFile file) throws IOException {
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        String uniqueFileName = UUID.randomUUID().toString() +  "_" + fileName;
//        Path uploadDir = Paths.get("uploads");
//        if (!Files.exists(uploadDir)) {
//            Files.createDirectory(uploadDir);
//        }
//        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
//
//        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
//        return uniqueFileName;
//    }
//
//
//
//    @PostMapping("/uploads/{id}")
//    public ResponseEntity<ApiResponse> uploads(@PathVariable Long id, @Valid @RequestBody ImageDTO imageDTO, ImageDirectory imageDirectory, BindingResult result) {
//        if (result.hasErrors()) {
//            List<String> errors = result.getFieldErrors().stream()
//                    .map(FieldError::getDefaultMessage).toList();
//            ApiResponse apiResponse = ApiResponse.builder()
//                    .data(errors)
//                    .message("Validation failed")
//                    .status(HttpStatus.BAD_REQUEST.value())
//                    .build();
//            return ResponseEntity.badRequest().body(apiResponse);
//        }
//
//        ApiResponse apiResponse = ApiResponse.builder()
//                .status(HttpStatus.OK.value())
//                .message("Upload successfully")
//                .data(imageService.saveImage(imageDTO, imageDirectory))
//                .build();
//        return ResponseEntity.ok(apiResponse);
//    }
//}
    }
}
