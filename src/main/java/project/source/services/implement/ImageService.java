package project.source.services.implement;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.source.dtos.ImageDTO;
import project.source.models.entities.Blog;
import project.source.models.entities.Image;
import project.source.dtos.ImageDTO;
import project.source.models.enums.ImageDirectory;
import project.source.models.enums.Status;
import project.source.repositories.ImageRepository;
import project.source.services.IImageService;

@Service
@RequiredArgsConstructor
@Builder
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;

    @Override
    public Image saveImage (ImageDTO imageDTO){
        Image newImage = Image.builder()
                .imageURL(imageDTO.getImageURL())
//                .imageDirectory(newImageDirectory)
                .build();
        return imageRepository.save(newImage);
    };

    @Override
    public Page<Image> getAllImages(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("ID does not match anything, no result found"));
    };

    @Override
    public void deleteImage(Long id) {
        Image image = getImageById(id);
        image.setStatus(Status.INACTIVE);
    }

    @Override
    public Image updateImage(Long id, ImageDTO imageDTO){
        Image updatedImage = getImageById(id);
        updatedImage.setImageURL(imageDTO.getImageURL());
        return imageRepository.save(updatedImage);
    };

    @Override
    public Image setImageDirectory(Image updatedImage, ImageDirectory currentImageDirectory) {
        updatedImage.setImageDirectory(currentImageDirectory);
        return imageRepository.save(updatedImage);
    };

}
