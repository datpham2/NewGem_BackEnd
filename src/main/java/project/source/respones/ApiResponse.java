package project.source.respones;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ApiResponse {
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
}
