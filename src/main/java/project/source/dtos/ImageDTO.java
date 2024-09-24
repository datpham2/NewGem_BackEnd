package project.source.dtos;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import project.source.models.entities.Blog;
import project.source.models.enums.ImageDirectory;
import project.source.models.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDTO {

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

//    @ManyToOne
//    @JoinColumn(name = "hotel_id")
//    private Hotel hotel;

//    @ManyToOne
//    @JoinColumn(name = "room_id")
//    private Room room;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonProperty(value = "blog_id")
    private Long  blog_id;


    private String imageURL;
//    private String imageName;

    private ImageDirectory imageDirectory;

    @Enumerated(EnumType.STRING)
    Status status;
}

