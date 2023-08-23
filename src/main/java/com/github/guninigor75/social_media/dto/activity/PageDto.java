package com.github.guninigor75.social_media.dto.activity;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

@Data
public class PageDto {

    private Integer pageNo = 0;

    private Integer pageSize = 10;

    private Sort.Direction sort = Sort.Direction.DESC;

    private String sortByColumn = "createdAt";

    private List<String> columns;

    public Pageable getPageable(PageDto dto) {
        Integer page = Objects.nonNull(dto.getPageNo()) && dto.pageNo >= 0 ? dto.getPageNo() : this.pageNo;
        Integer size = Objects.nonNull(dto.getPageSize()) && dto.pageSize >= 0 ? dto.getPageSize() : this.pageSize;
        Sort.Direction sort = Objects.nonNull(dto.getSort()) ? dto.getSort() : this.sort;
        String column = Objects.nonNull(dto.getSortByColumn())? dto.getSortByColumn() : this.sortByColumn;
        return PageRequest.of(page, size, sort, column);
    }
}
