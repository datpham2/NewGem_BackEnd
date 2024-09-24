package project.source.respones;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@Getter
public class PageResponse<T>{
    int pageNo;
    int pageSize;
    int totalPage;
    T items;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object data;
}
