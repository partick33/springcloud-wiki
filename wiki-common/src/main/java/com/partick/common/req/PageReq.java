package com.partick.common.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class PageReq {
    @NotNull(message = "【页码不能为空】")
    private int page;
    @NotNull(message = "【每天条数不能为空】")
    @Max(value = 1000, message = "【每页条数】不能超过1000")
    private int Size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", Size=" + Size +
                '}';
    }
}