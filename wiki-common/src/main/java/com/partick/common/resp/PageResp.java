package com.partick.common.resp;

import java.util.List;

public class PageResp<T> {
    private Long total;
    private List<T> list;

    public Long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "PageResp{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}