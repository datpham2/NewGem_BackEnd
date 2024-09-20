package project.source.respones;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse {
    int status;

    String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object data;
}
