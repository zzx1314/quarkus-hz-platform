package org.huazhi.util;

import jakarta.ws.rs.QueryParam;

public class PageRequest {
    @QueryParam("page")
    private int page = 1;
    @QueryParam("size")
    private int size = 10;

    public PageRequest() {}

    public PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return Math.max(page, 1);
    }

    public int getSize() {
        return Math.max(size, 1);
    }

    public int getPageIndex() {
        return getPage() - 1;
    }
}

