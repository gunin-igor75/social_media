package com.github.guninigor75.social_media.dto.activity;

import com.github.guninigor75.social_media.validation.ValidationColumnNamePageDto;
import com.github.guninigor75.social_media.validation.ValidationColumnSortPageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Data
@Schema(description = "Request for MessagesController")
public class PageDtoMessage {

    @Schema(description = "start page", example = "1")
    @PositiveOrZero
    private Integer pageNo = 0;

    @Schema(description = "number of pages", example = "10")
    @PositiveOrZero
    private Integer pageSize = 10;

    @Schema(description = "sort", example = "DESC")
    @ValidationColumnSortPageDto(valueColumns = {Sort.Direction.ASC, Sort.Direction.DESC})
    private Sort.Direction sort;

    @Schema(description = "column name for sort", example = "id")
    @ValidationColumnNamePageDto(valueColumns = {"id", "content", "createdAt"})
    private String column;

    public Pageable getPageable(PageDtoMessage dto) {
        Integer page = Objects.nonNull(dto.getPageNo()) && dto.pageNo >= 0 ? dto.getPageNo() : this.pageNo;
        Integer size = Objects.nonNull(dto.getPageSize()) && dto.pageSize >= 0 ? dto.getPageSize() : this.pageSize;
        Sort.Direction sort = Objects.nonNull(dto.getSort()) ? dto.getSort() : Sort.Direction.DESC;
        String column = Objects.nonNull(dto.getColumn())? dto.getColumn() : "createdAt";
        return PageRequest.of(page, size, sort, column);
    }
}
