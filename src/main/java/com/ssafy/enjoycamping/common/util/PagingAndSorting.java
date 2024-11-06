package com.ssafy.enjoycamping.common.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingAndSorting {
    private int pageNo;
    private int pageCnt;
    private String order;
    private String sort;
    private int offset;

    public PagingAndSorting(int pageNo, int pageCnt, String order, String sort) {
        this.pageNo = pageNo;
        this.pageCnt = pageCnt;
        this.order = order;
        this.sort = sort;
        this.offset = pageNo * pageCnt; // Offset 계산
    }
}
