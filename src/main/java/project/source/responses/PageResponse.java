package project.source.responses;

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
}
