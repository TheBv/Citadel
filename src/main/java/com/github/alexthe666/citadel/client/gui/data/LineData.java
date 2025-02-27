//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.alexthe666.citadel.client.gui.data;

public class LineData {
    private int xIndex;
    private int yIndex;
    private String text;
    private int page;

    public LineData(int xIndex, int yIndex, String text, int page) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.text = text;
        this.page = page;
    }

    public int getxIndex() {
        return this.xIndex;
    }

    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public int getyIndex() {
        return this.yIndex;
    }

    public void setyIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
