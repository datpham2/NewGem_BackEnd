package project.source.dtos;
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

    @ManyToOne
    private Blog blog;


    private String imageURL;

    @NotNull(message = "Can not resolve this media directory path")
    private ImageDirectory imageDirectory;

    @Enumerated(EnumType.STRING)
    Status status;
}

