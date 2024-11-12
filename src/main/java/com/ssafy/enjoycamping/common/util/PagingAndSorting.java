package com.ssafy.enjoycamping.common.util;

import com.ssafy.enjoycamping.common.exception.BadRequestException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingAndSorting {
    private int pageNo;
    private int pageCnt;
    private int offset;
    private Order order;
    private Sort sort;

    public PagingAndSorting(int pageNo, int pageCnt, Order order, Sort sort) {
        this.pageNo = pageNo;
        this.pageCnt = pageCnt;
        this.offset = (pageNo - 1) * pageCnt; // Offset 계산
        this.order = order;
        this.sort = sort;
    }

    public PagingAndSorting(int pageNo, int pageCnt) {
        this.pageNo = pageNo;
        this.pageCnt = pageCnt;
        this.offset = (pageNo - 1) * pageCnt; // Offset 계산
    }

    public interface Order {}
    public enum CampingOrder implements Order {
        name;
    }
    public enum AttractionOrder implements Order {
        title;
    }
    public enum ReviewOrder implements Order {
        created_at;
    }
    public enum DistanceOrder implements Order {
        distance;
    }

    public enum Sort {
        asc, desc;
    }

}
