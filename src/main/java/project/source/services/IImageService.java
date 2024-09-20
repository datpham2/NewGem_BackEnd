package project.source.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.source.dtos.ImageDTO;
import project.source.models.entities.Image;
import project.source.dtos.ImageDTO;
import project.source.models.enums.ImageDirectory;

public interface IImageService {
    public Page<Image> getAllImages(Pageable pageable);
//    List<Image> getImages();
    Image getImageById(Long id);
    void deleteImage(Long id);
    public Image updateImage(Long id, ImageDTO imageDTO);
    public Image saveImage (ImageDTO imageDTO);
    public Image setImageDirectory(Image image, ImageDirectory imageDirectory);
}
