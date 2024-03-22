package com.example.project_9.Dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DataResponseDTO<T> {
    private int allPageCount;
    private int currentPage;
    private boolean hasPreviewPage;
    private boolean hasNextPage;
    private List<T> data;

    public DataResponseDTO(int allPageCount, int currentPage, boolean hasPreviewPage, boolean hasNextPage, List<T> data) {
        this.allPageCount = allPageCount;
        this.currentPage = currentPage;
        this.hasPreviewPage = hasPreviewPage;
        this.hasNextPage = hasNextPage;
        this.data = data;
    }
}
