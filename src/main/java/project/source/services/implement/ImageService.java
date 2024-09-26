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
    private final BlogService blogService;
//    private final UserService userService;
//    private final HotelService hotelService;
//    private final RoomService roomService;

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
        public Image saveImage(Long id, ImageDTO imageDTO, ImageDirectory imageDirectory) {
            Long newId = null;
            if (imageDirectory.equals(ImageDirectory.Blog)){
                 newId = blogService.getBlogById(id).getId();
//            }else if (imageDirectory.equals(ImageDirectory.User)) {
////                newId = userService.getUser(id).getId();
//            } else if (imageDirectory.equals(ImageDirectory.Hotel)){
//                newId = hotelService.getHotelById(id).getId();
//            } else if (imageDirectory.equals(ImageDirectory.Room)){
//                newId = roomService.getRoomById(id).getId();
            }else {
                newId = null;
            }

        Image image = Image.builder()
                .imageURL(imageDTO.getImageURL())
                .imageDirectory(imageDirectory)
                .status(Status.ACTIVE)
                .sourceId(newId)
                .blog(blogService.getBlogById(id))
                .build();
        return imageRepository.save(image);
    }






    //    @Override
//    public Image saveImage (ImageDTO imageDTO, ImageDirectory imageDirectory){
//        Image newImage = Image.builder()
//                .imageURL(imageDTO.getImageURL())
//                .imageDirectory(imageDirectory)
//                .status(Status.ACTIVE)
//                .build();
//        return imageRepository.save(newImage);
//    };

//    @Override
//    public List<StudentImage> getAllStudentImages(Long studentId) {
//        return studentImageRepository  .findByStudentId(studentId);
//    }


    //    @Override
//    public Image setImageDirectory(Image updatedImage, ImageDirectory currentImageDirectory) {
//        updatedImage.setImageDirectory(currentImageDirectory);
//        return imageRepository.save(updatedImage);
//    };

//    @Override
//    public Image saveImage(Long studentI, StudentImageDTO studentImageDTO) {
//      Student student = getStudentById(studentId);

}
