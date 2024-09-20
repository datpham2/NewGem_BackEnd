package project.source.services.implement;


import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.source.dtos.BlogDTO;
import project.source.models.entities.Blog;
import project.source.models.enums.Status;
import project.source.repositories.BlogRepository;
import project.source.services.IBlogService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class BlogService implements IBlogService {
    private final BlogRepository blogRepository;

    @Override
    public Blog getBlogById(Long id){
        return blogRepository.getById(id);
    }

    @Override
    public List<Blog> getBlogs(){
        return blogRepository.findAll();
    }

    @Override
    public void deleteBlog(Long id) {
        Blog blog = getBlogById(id);
        blog.setStatus(Status.INACTIVE);
    }

    @Override
    public Page<Blog> getBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Blog saveBlog(BlogDTO blogDTO){
          Blog  blog = Blog.builder()
                .title(blogDTO.getTitle())
                .content(blogDTO.getContent())
                .build();
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, BlogDTO blogDTO) {
        Blog updatedBlog = getBlogById(id);
        updatedBlog.setTitle(blogDTO.getTitle());
        updatedBlog.setContent(blogDTO.getContent());
        updatedBlog.setCreatedAt(blogDTO.getCreatedAt());
        return blogRepository.save(updatedBlog);
    }
}
