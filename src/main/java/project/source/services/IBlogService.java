package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.source.dtos.BlogDTO;
import project.source.models.entities.Blog;

import java.util.List;

public interface IBlogService {
    Page<Blog> getBlogs(Pageable pageable);
    Blog saveBlog(BlogDTO blogDTO);
    Blog getBlogById(Long id);
    List<Blog> getBlogs();
    void deleteBlog(Long id);
    public Blog updateBlog(Long id, BlogDTO blogDTO);
}
