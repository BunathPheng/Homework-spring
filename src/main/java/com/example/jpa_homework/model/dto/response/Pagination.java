package com.example.jpa_homework.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagination {
    private int page;
    private int limit;
    private int nextPage;
    private int previousPage;
    private int totalCount;
    private int totalPages;
    private int pagesToShow;
    private int startPage;
    private int endPage;
    private int offset;
}
