package com.example.demo.board;

import lombok.Data;

public class BoardResponse {

    @Data
    public static class ListDTO {
        private Integer id;
        private String title;
        private String username;

        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.username = board.getUser().getUsername();
        }
    }

    @Data
    public static class ListPageDTO {
        private java.util.List<ListDTO> boards; // 게시글 목록
        private int page;                       // 현재 페이지 (1-index)
        private int prevPage;                   // 이전 페이지 번호
        private int nextPage;                   // 다음 페이지 번호
        private int totalPages;                 // 총 페이지 수
        private java.util.List<PageNumberDTO> pageNumbers; // 페이지 번호 객체 목록
        private boolean isFirst;                // 첫 번째 페이지 여부
        private boolean isLast;                 // 마지막 페이지 여부
        private String keyword;                 // 검색어

        public ListPageDTO(org.springframework.data.domain.Page<Board> boardPage, int page, String keyword) {
            this.boards = boardPage.getContent().stream().map(ListDTO::new).collect(java.util.stream.Collectors.toList());
            this.page = page;
            this.prevPage = page - 1;
            this.nextPage = page + 1;
            this.totalPages = boardPage.getTotalPages();
            this.isFirst = boardPage.isFirst();
            this.isLast = boardPage.isLast();
            this.keyword = keyword;
            
            this.pageNumbers = new java.util.ArrayList<>();
            
            // 5개씩 끊어서 페이지 번호 생성 (1-5, 6-10, ...)
            int blockSize = 5;
            int currentBlock = (page - 1) / blockSize;
            int startPage = currentBlock * blockSize + 1;
            int endPage = Math.min(startPage + blockSize - 1, totalPages);

            for (int i = startPage; i <= endPage; i++) {
                pageNumbers.add(new PageNumberDTO(i, i == page));
            }
        }

        @Data
        @lombok.AllArgsConstructor
        public static class PageNumberDTO {
            private int number;
            private boolean active;
        }
    }

    // RULE: Detail DTO는 상세 정보를 저장한다.
    @Data
    public static class Detail {

    }
}
