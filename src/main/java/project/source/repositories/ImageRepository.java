package project.source.repositories;

import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import project.source.models.entities.Image;

import java.util.List;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{

}
