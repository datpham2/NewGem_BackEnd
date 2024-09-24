package project.source.models.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import project.source.models.enums.ImageDirectory;
import project.source.models.enums.Status;

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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Column(name = "image_url", length = 300)
    private String imageURL;

//    @Column(name = "image_name", length = 300)
//    private String imageName;

//    @NotNull(message = "Can not resolve this media directory path")
    @Enumerated(EnumType.STRING)
    private ImageDirectory imageDirectory;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    Status status;

    @Column(name = "source_id")
    Long sourceId;
}

//Id: 01, nguyễn Văn Mèo
//studentImage
//01, neo.jpg, 01