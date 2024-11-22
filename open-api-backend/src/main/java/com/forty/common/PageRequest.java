package com.forty.common;

import com.forty.constant.CommonConstant;
import lombok.Data;

@Data
public class PageRequest {

    /**
     * 当前页
     */
    private int currentPage = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序， 默认升序
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
