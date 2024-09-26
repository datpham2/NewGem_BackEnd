package project.source.services.implement;


import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.source.dtos.BlogDTO;
import project.source.dtos.ImageDTO;
import project.source.exceptions.NotFoundException;
import project.source.models.entities.Blog;
import project.source.models.entities.Image;
import project.source.models.entities.User;
import project.source.models.enums.ImageDirectory;
import project.source.models.enums.Status;
import project.source.repositories.BlogRepository;
import project.source.repositories.ImageRepository;
import project.source.services.IBlogService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Builder
public class BlogService implements IBlogService {
    private final BlogRepository blogRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;

    @Override
    public Blog getBlogById(Long id){
        return blogRepository.findById(id).orElseThrow(()-> new NotFoundException("Can not resolve this blog ID"));
    }

    @Override
    public List<Blog> getBlogs(){
        return blogRepository.findAll();
    }

    @Override
    public void deleteBlog(Long id) {
        Blog blog = getBlogById(id);
        blog.setStatus(Status.INACTIVE);
        blogRepository.save(blog);
    }

    @Override
    public Page<Blog> getBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Blog saveBlog(BlogDTO blogDTO){
        List<Image> images = blogDTO.getImages();
        Set<Image> temp = new HashSet<>();
        User user = userService.getUserById(blogDTO.getUserId());

        Blog newblog = Blog.builder()
                .title(blogDTO.getTitle())
                .status(Status.ACTIVE)
                .content(blogDTO.getContent())
                .user(user)
                .images(null)
                .build();

        for (Image image : images){
            image.setImageDirectory(ImageDirectory.Blog);
            image.setStatus(Status.ACTIVE);
            image.setSourceId(newblog.getId());
            temp.add(image);
            imageRepository.save(image);
        }

        newblog.setImages(temp);

        return blogRepository.save(newblog);
    }

    @Override
    public Blog updateBlog(Long id, BlogDTO blogDTO) {
        Blog updatedBlog = getBlogById(id);
        userService.getUserById(blogDTO.getUserId());
        updatedBlog.setTitle(blogDTO.getTitle());
        updatedBlog.setContent(blogDTO.getContent());
        return blogRepository.save(updatedBlog);
    }
}
