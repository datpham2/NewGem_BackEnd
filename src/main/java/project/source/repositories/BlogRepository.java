package project.source.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.source.models.entities.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

}
