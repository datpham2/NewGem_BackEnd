package project.source.respones;
/**
 * @autor An Nguyen
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import project.source.models.entities.Hotel;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HotelResponse {
    private List<Hotel> hotel;
    @JsonProperty(value = "total_page")
    private int totalPage;
}
