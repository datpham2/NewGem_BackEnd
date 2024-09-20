package project.source.models.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import project.source.models.enums.ImageDirectory;

@Entity
@Table(name="Gallery")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image extends BaseEntity<Long>{

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

//    @ManyToOne
//    @JoinColumn(name = "hotel_id")
//    private Hotel hotel;

//    @ManyToOne
//    @JoinColumn(name = "room_id")
//    private Room room;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Column(name = "image_url", length = 300)
    private String imageURL;

    @NotNull(message = "Can not resolve this media directory path")
    @Enumerated(EnumType.STRING)
    private ImageDirectory imageDirectory;
}

//Id: 01, nguyễn Văn Mèo
//studentImage
//01, neo.jpg, 01