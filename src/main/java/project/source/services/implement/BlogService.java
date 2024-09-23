package project.source.services.implement;


import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.source.dtos.BlogDTO;
import project.source.dtos.ImageDTO;
import project.source.models.entities.Blog;
import project.source.models.entities.Image;
import project.source.models.enums.ImageDirectory;
import project.source.models.enums.Status;
import project.source.repositories.BlogRepository;
import project.source.repositories.ImageRepository;
import project.source.services.IBlogService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class BlogService implements IBlogService {
    private final BlogRepository blogRepository;
    private final ImageService imageService;

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
        List<Image> images = blogDTO.getImages();
        List<Image> temp = new ArrayList<>();

        for (Image image : images){
            ImageDTO imageDTO = ImageDTO.builder()
                    .imageDirectory(ImageDirectory.Blog)
                    .imageURL(image.getImageURL())
                    .status(Status.ACTIVE)
                    .build();
            Image newImage = imageService.saveImage(imageDTO,ImageDirectory.Blog);
            temp.add(newImage);
        }

          Blog  newblog = Blog.builder()
                .title(blogDTO.getTitle())
                  .status(Status.ACTIVE)
                .content(blogDTO.getContent())
                  .images(temp)

                .build();
        return blogRepository.save(newblog);
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
