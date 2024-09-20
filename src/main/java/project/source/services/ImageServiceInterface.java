package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.source.models.entities.Image;
import project.source.models.enums.ImageDirectory;

import java.util.List;

public interface ImageServiceInterface {
//    Page<Image> getAllImage(Pageable pageable);
    Image getImageById(Long id);
//    List<Image> getImages();
    void deleteImage(Long id);
    public Image updateImage(Long id, Image image);
    public ImageDirectory addImageDirectory (Long id, ImageDirectory imageDirectory);
    Image saveImage(Image image, ImageDirectory imageDirectory);
}
