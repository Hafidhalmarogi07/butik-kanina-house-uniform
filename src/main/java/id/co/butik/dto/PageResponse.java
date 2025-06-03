package id.co.butik.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Data
public class PageResponse<T> {
    private List<T> content;
    private PageableResponse pageable;
    private boolean last;
    private int total_pages;
    private long total_elements;
    private boolean first;
    private int number_of_elements;
    private SortResponse sort;
    private int size;
    private int number;
    private boolean empty;

    public static <T> PageResponse<T> from(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.setContent(page.getContent());
        
        PageableResponse pageableResponse = new PageableResponse();
        pageableResponse.setSort(SortResponse.from(page.getSort()));
        pageableResponse.setPage_size(page.getSize());
        pageableResponse.setPage_number(page.getNumber());
        pageableResponse.setOffset(page.getPageable().getOffset());
        pageableResponse.setUnpaged(page.getPageable().isUnpaged());
        pageableResponse.setPaged(page.getPageable().isPaged());
        response.setPageable(pageableResponse);
        
        response.setLast(page.isLast());
        response.setTotal_pages(page.getTotalPages());
        response.setTotal_elements(page.getTotalElements());
        response.setFirst(page.isFirst());
        response.setNumber_of_elements(page.getNumberOfElements());
        response.setSort(SortResponse.from(page.getSort()));
        response.setSize(page.getSize());
        response.setNumber(page.getNumber());
        response.setEmpty(page.isEmpty());
        
        return response;
    }

    @Data
    public static class PageableResponse {
        private SortResponse sort;
        private int page_size;
        private int page_number;
        private long offset;
        private boolean unpaged;
        private boolean paged;
    }

    @Data
    public static class SortResponse {
        private boolean sorted;
        private boolean unsorted;
        private boolean empty;

        public static SortResponse from(org.springframework.data.domain.Sort sort) {
            SortResponse response = new SortResponse();
            response.setSorted(sort.isSorted());
            response.setUnsorted(sort.isUnsorted());
            response.setEmpty(sort.isEmpty());
            return response;
        }
    }
}