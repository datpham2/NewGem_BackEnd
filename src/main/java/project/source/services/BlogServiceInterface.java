package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.source.models.entities.Blog;

import java.util.List;

public interface BlogServiceInterface {
    Page<Blog> getBlogs(Pageable pageable);
    Blog saveBlog(Blog blog);
    Blog getBlogById(Long id);
    List<Blog> getBlogs();
    void deleteBlog(Long id);
    public Blog updateBlog(Long id, Blog blog);
}
