//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.alexthe666.citadel.client.gui.data;

public class TabulaRenderData {
    private String model;
    private String texture;
    private int x;
    private int y;
    private double scale;
    private int page;
    private double rot_x;
    private double rot_y;
    private double rot_z;
    private boolean follow_cursor;

    public TabulaRenderData(String model, String texture, int x, int y, double scale, int page, double rot_x, double rot_y, double rot_z, boolean follow_cursor) {
        this.model = model;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.page = page;
        this.rot_x = rot_x;
        this.rot_y = rot_y;
        this.rot_z = rot_z;
        this.follow_cursor = follow_cursor;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTexture() {
        return this.texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getScale() {
        return this.scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public double getRot_x() {
        return this.rot_x;
    }

    public void setRot_x(double rot_x) {
        this.rot_x = rot_x;
    }

    public double getRot_y() {
        return this.rot_y;
    }

    public void setRot_y(double rot_y) {
        this.rot_y = rot_y;
    }

    public double getRot_z() {
        return this.rot_z;
    }

    public void setRot_z(double rot_z) {
        this.rot_z = rot_z;
    }

    public boolean isFollow_cursor() {
        return this.follow_cursor;
    }

    public void setFollow_cursor(boolean follow_cursor) {
        this.follow_cursor = follow_cursor;
    }
}
