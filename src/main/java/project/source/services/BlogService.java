package project.source.services;


import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.source.models.entities.Blog;
import project.source.repositories.BlogRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class BlogService implements BlogServiceInterface{
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
        blogRepository.deleteById(id);
    }

    @Override
    public Page<Blog> getBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Blog saveBlog(Blog blog){
//        Blog blog = Blog.builder()
//                .ten(studentDTO.getTen())
//                .thanhPho(studentDTO.getThanhPho())
//                .ngaySinh(studentDTO.getNgaySinh())
//                .xepLoai(XepLoai.fromTen(studentDTO.getXepLoai()))
//                .build();
//        return studentRepository.save(student);
            blog = Blog.builder()
                .title(blog.getTitle())
                .content(blog.getContent())
                .build();
        return blogRepository.save(blog);
    }
}
