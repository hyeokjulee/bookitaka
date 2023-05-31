package com.bookitaka.NodeulProject.sheet;

import java.util.ArrayList;
import java.util.List;

public class SheetPageInfo {

    private int pageNum;       // 현재 페이지 번호
    private int amount;        // 한 페이지당 게시물 수
    private int total;         // 총 게시물 수
    private int totalPage;     // 총 페이지 수
    private int startPage;     // 시작 페이지 번호
    private int endPage;       // 마지막 페이지 번호

    public SheetPageInfo(SheetCri cri, int total) {
        this.pageNum = cri.getPageNum();
        this.amount = cri.getAmount();
        this.total = total;

        this.totalPage = (int) Math.ceil((double) total / amount);
        this.endPage = (int) (Math.ceil((double) pageNum / 10)) * 10;
        this.startPage = endPage - 9;

        if (endPage > totalPage) {
            this.endPage = totalPage;
        }
    }

    // Getter 메서드


    public int getPageNum() {
        return pageNum;
    }

    public int getAmount() {
        return amount;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    // 이전 페이지 여부를 확인하는 메서드
    public boolean hasPreviousPage() {
        return pageNum > 1;
    }

    // 다음 페이지 여부를 확인하는 메서드
    public boolean hasNextPage() {
        return pageNum < totalPage;
    }

    // 페이지 번호 리스트를 반환하는 메서드
    public List<Integer> getPageList() {
        List<Integer> pageList = new ArrayList<>();

        for (int i = startPage; i <= endPage; i++) {
            pageList.add(i);
        }

        return pageList;
    }

}
