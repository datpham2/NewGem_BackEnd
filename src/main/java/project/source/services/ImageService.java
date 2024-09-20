package project.source.services;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import project.source.models.entities.Blog;
import project.source.models.entities.Image;
import project.source.models.enums.ImageDirectory;
import project.source.repositories.ImageRepository;

@Service
@RequiredArgsConstructor
@Builder
public class ImageService implements ImageServiceInterface {
    private final ImageRepository imageRepository;

    @Override
    public Image saveImage (Image image, ImageDirectory newImageDirectory){
        image = Image.builder()
                .imageURL(image.getImageURL())
                .imageDirectory(newImageDirectory)
                .build();
        return imageRepository.save(image);
    };

    Image getImageById(Long id){

    };

    @Override
    public Image updateImage(Long id, Image newImage){
        Image updatedImage = getImageById(id);
        updatedImage.setImageURL(newImage.getImageURL());
        return imageRepository.save(updatedImage);
    };

    @Override
    public ImageDirectory addImageDirectory (Long id, ImageDirectory imageDirectory);

}
