package project.source.respones;

import lombok.*;
import project.source.models.entities.Blog;
import project.source.models.entities.Image;
import project.source.models.enums.ImageDirectory;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class ImageResponse {
    private Long id;
    private String imageURL;
    private ImageDirectory imageDirectory;

    public ImageResponse fromImage (Image image) {
        ImageResponse imageResponse = ImageResponse.builder()
                .id(image.getId())
                .imageURL(image.getImageURL())
                .imageDirectory(image.getImageDirectory())
                .build();
        return imageResponse;
    }
}
