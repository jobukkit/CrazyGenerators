package com.agnor99.crazygenerators.objects.other.generator.pong.util;

import java.awt.*;
import java.awt.geom.Point2D;

public class SubpixelPoint {

    public static int SUBPIXEL_PER_PIXEL = 8;

    public int x = 0;
    public int y = 0;
    public int sx = 0;
    public int sy = 0;
    public SubpixelPoint() {}
    public SubpixelPoint(int x, int y){
        this.x = x;
        this.y = y;
    }
    public SubpixelPoint(int x, int y, int sx, int sy){
        this.x = x;
        this.y = y;
        this.sx = sx;
        this.sy = sy;
    }
    public void revalidate() {
        while(sx < 0){
            sx+=SUBPIXEL_PER_PIXEL;
            x--;
        }
        while(sy < 0){
            sy+=SUBPIXEL_PER_PIXEL;
            y--;
        }
        while(sx >= SUBPIXEL_PER_PIXEL){
            sx-=SUBPIXEL_PER_PIXEL;
            x++;
        }
        while(sy >= SUBPIXEL_PER_PIXEL){
            sy-=SUBPIXEL_PER_PIXEL;
            y++;
        }
    }
    public double distanceToZero() {
        revalidate();
        double dx = getCompleteX();
        double dy = getCompleteY();
        return Point2D.distance(dx,dy,0,0);
    }
    public static SubpixelPoint create(double distance, Point direction){
        SubpixelPoint retValue = new SubpixelPoint();
        while(retValue.distanceToZero() < distance){
            retValue.addToSubpixel(direction);
        }
        return retValue;
    }
    public void addToSubpixel(Point value){
        sx+=value.x;
        sy+=value.y;
        revalidate();
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public double getCompleteX() {
        return x + sx/(float)SUBPIXEL_PER_PIXEL;
    }
    public double getCompleteY() {
        return y + sy/(float)SUBPIXEL_PER_PIXEL;
    }
    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
}
