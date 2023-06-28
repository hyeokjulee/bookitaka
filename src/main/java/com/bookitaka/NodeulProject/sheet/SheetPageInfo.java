package com.bookitaka.NodeulProject.sheet;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class SheetPageInfo {

    private int pageNum;       // 현재 페이지 번호
    private int amount;        // 한 페이지당 게시물 수
    private int total;         // 총 게시물 수
    private int totalPage;     // 총 페이지 수
    private int startPage;     // 시작 페이지 번호
    private int endPage;       // 마지막 페이지 번호

    private int groupSize;      // 그룹당 페이지 수

    private int previousGroupStartPage;     // 이전 그룹의 페이지

    private int nextGroupStartPage;     // 다음 그룹의 페이지

    public SheetPageInfo(SheetCri cri, int total) {
        groupSize = 10;
        this.pageNum = cri.getPageNum() - 1;
        this.amount = cri.getAmount();
        this.total = total;
        int currentGroup = pageNum / groupSize;

        this.totalPage = (int) Math.ceil((double) total / amount);
//        this.endPage = (int) (Math.ceil((double) pageNum / groupSize)) * groupSize;
//        this.startPage = endPage - groupSize + 1;
        this.startPage = currentGroup * groupSize;
        log.info("============================== startPage : {}", startPage);
        this.endPage = Math.min(startPage + groupSize, totalPage);
        log.info("============================== endPage : {}", endPage);

        previousGroupStartPage = (currentGroup == 0) ? 0 : (currentGroup - 1) * groupSize;
        nextGroupStartPage = (currentGroup + 1) * groupSize;
        log.info("============================================== nextGroupStartPage 기본 : {}",nextGroupStartPage);
        if (totalPage % groupSize == 0 && startPage == totalPage - groupSize) {
            nextGroupStartPage = endPage + 1;
            log.info("============================================== nextGroupStartPage 첫번째 : {}",nextGroupStartPage);
        } else if (nextGroupStartPage > endPage) {
            nextGroupStartPage = endPage;
            log.info("============================================== nextGroupStartPage 두번째 : {}", nextGroupStartPage);
        } else {
            nextGroupStartPage += 1;
            log.info("============================================== nextGroupStartPage 일반적인 경우 : {}",nextGroupStartPage);
        }
        log.info("============================================== nextGroupStartPage 최종 : {}",nextGroupStartPage);
    }

    // Getter 메서드

    public int getPreviousGroupStartPage() { return previousGroupStartPage; }

    public int getNextGroupStartPage() { return nextGroupStartPage; }

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
        return pageNum > 0;
    }

    // 다음 페이지 여부를 확인하는 메서드
    public boolean hasNextPage() {
        return pageNum < totalPage - 1;
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
