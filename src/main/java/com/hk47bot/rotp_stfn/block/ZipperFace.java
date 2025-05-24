package com.hk47bot.rotp_stfn.block;

import net.minecraft.util.Direction;

public class ZipperFace {
    private Direction direction;

    // 1 - 0 гр, 2 - 90 гр, 3 - 180 гр, 4 - 270 гр
    private int rotation;

    // 1 - прямо, 2 - г, 3 - т, 4 - х
    private int type;

    private boolean right_up;
    private boolean right_down;
    private boolean left_up;
    private boolean left_down;

    public ZipperFace(Direction direction, int rotation, int type, boolean right_up, boolean right_down, boolean left_up, boolean left_down){
        this.direction = direction;
        this.setRotation(rotation);
        this.setType(type);
        this.setRightUp(right_up);
        this.setRightDown(right_down);
        this.setLeftUp(left_up);
        this.setLeftDown(left_down);
    }
    public ZipperFace(Direction direction){
        this.direction = direction;
        this.setRotation(1);
        this.setType(1);
        this.setRightUp(false);
        this.setRightDown(false);
        this.setLeftUp(false);
        this.setLeftDown(false);
    }

    public Direction getDirection() {
        return direction;
    }
    public int getRotation() {
        return rotation;
    }
    public int getType() {
        return type;
    }
    public boolean isRightUp() {
        return right_up;
    }
    public boolean isRightDown() {
        return right_down;
    }
    public boolean isLeftUp() {
        return left_up;
    }
    public boolean isLeftDown() {
        return left_down;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setRightUp(boolean right_up) {
        this.right_up = right_up;
    }
    public void setRightDown(boolean right_down) {
        this.right_down = right_down;
    }
    public void setLeftUp(boolean left_up) {
        this.left_up = left_up;
    }
    public void setLeftDown(boolean left_down) {
        this.left_down = left_down;
    }
}
