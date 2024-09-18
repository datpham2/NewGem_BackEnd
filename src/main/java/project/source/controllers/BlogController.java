package project.source.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.source.models.entities.Blog;
import project.source.respones.ApiResponse;
import project.source.services.BlogService;

import java.util.List;

@RestController
@RequestMapping ("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    @GetMapping("")
    public ResponseEntity<ApiResponse> getStudentsToPages(
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

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody Blog blog, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errors);
        }
        blog = blogService.saveBlog(blog);
        return ResponseEntity.ok( "Success " + blog.toString());
    }

    @PutMapping("/{id}")

}
