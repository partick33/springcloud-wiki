package com.partick.common.req;


public class DocQueryReq {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DocQueryReq{} " + super.toString();
    }
}