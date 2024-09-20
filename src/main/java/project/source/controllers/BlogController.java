package project.source.controllers;

import java.nio.file.*;

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
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Blog;
import project.source.respones.ApiResponse;
import project.source.services.implement.BlogService;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping ("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    @GetMapping("")
    public ResponseEntity<ApiResponse> getBlogsToPages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogsPage = blogService.getBlogs(pageable);

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(blogsPage.getContent())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/newBlog")
    public ResponseEntity<?> createBlog(@Valid @RequestBody BlogDTO blogDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        Blog blog = blogService.saveBlog(blogDTO);
        return ResponseEntity.ok( "Blog submit successfully " + blog.toString());
    }

    @PutMapping("/updateBlog/{id}")
    public ResponseEntity<ApiResponse> updateBlog(@PathVariable Long id, @Valid @RequestBody BlogDTO blogDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            ApiResponse apiResponse = ApiResponse.builder()
                    .data(errors)
                    .message("Validation failed")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.badRequest().body(apiResponse);
        }

        Blog updatedBlog = blogService.updateBlog(id, blogDTO);
        if (updatedBlog == null) {
            throw new NotFoundException("Can not resolve blog id: " + id);
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .data(updatedBlog)
                .message("Edit blog successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("deleteBlog/{id}")
    public ResponseEntity<ApiResponse> deleteBlog(@PathVariable long id) {
        blogService.deleteBlog(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Blog deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }



}
