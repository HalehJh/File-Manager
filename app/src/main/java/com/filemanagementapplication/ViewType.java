package com.filemanagementapplication;

public enum ViewType {

    Row(0),Grid(1);
    private int value;

    ViewType(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}
