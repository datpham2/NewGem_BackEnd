package project.source.controllers;

import java.nio.file.*;

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
import project.source.dtos.RoomDTO;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Blog;
import project.source.respones.ApiResponse;
import project.source.respones.PageResponse;
import project.source.services.implement.BlogService;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping ("/blog")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "Operations related to blogs")
public class BlogController {
    private final BlogService blogService;

    @Operation(
            method = "GET",
            summary = "Get all blogs in the database",
            description = "Send a request to get all the bills data ('blogId', 'title', 'content', 'images', 'authorId') existed in the database")
    @GetMapping("")
    public ResponseEntity<ApiResponse> getBlogsToPages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogsPage = blogService.getBlogs(pageable);
        List<BlogDTO> roomDTOList = blogsPage.getContent().stream().map(BlogDTO::fromBlog).toList();

        PageResponse<List<BlogDTO>> response = PageResponse.<List<BlogDTO>>builder()
                .totalPage(blogsPage.getTotalPages())
                .pageNo(blogsPage.getNumber())
                .pageSize(blogsPage.getSize())
                .items(roomDTOList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            method = "GET",
            summary = "Get a blog with id",
            description = "Send a request to get the blog data ('blogId', 'title', 'content', 'images', 'authorId') with corresponding 'blogId' ")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> index(@PathVariable(value = "id" ) Long id) {
        Blog blog = blogService.getBlogById(id);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Successfully get blog by id " + id)
                .data(BlogDTO.fromBlog(blog))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @Operation(
            method = "POST",
            summary = "Post the blog body to create a new blog ",
            description = "Send a request with blog body ('blogId', 'title', 'content', 'images', 'authorId') to build and save a new object for class 'Blog')")
    @PostMapping("/newBlog")
    public ResponseEntity<?> createBlog(@Valid @RequestBody BlogDTO blogDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        Blog blog = blogService.saveBlog(blogDTO);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                        .message("Blog submit successfully ")
                .data(BlogDTO.fromBlog(blog))
                .build());
//        return ResponseEntity.ok( "Blog submit successfully " + blog.toString());
    }

    @Operation(
            method = "PUT",
            summary = "Put the new blog body to an existed blog",
            description = "Send a request to find and save a new body ('title', 'content', 'images') for a targeted 'blogId', respond with a rejecting message if not valid" )
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
                .data(BlogDTO.fromBlog(updatedBlog))
                .message("Edit blog successfully")
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok().body(apiResponse);
    }



    @Operation(
            method = "DELETE",
            summary = "Set a blog status to inactive",
            description = "Set the 'status' enumerate from a targeted 'blogId' to 'INACTIVE'" )
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
