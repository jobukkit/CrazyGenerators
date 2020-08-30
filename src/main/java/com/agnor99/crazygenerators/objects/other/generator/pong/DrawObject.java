package com.agnor99.crazygenerators.objects.other.generator.pong;


import java.awt.*;

public class DrawObject {
    public Point drawPosition, texturePosition, relAnimationCenter;
    public Dimension size;
    public DrawObject(Point drawPosition, Point texturePosition, Dimension size){
        this(drawPosition, texturePosition, size, new Point(size.width/2, size.height/2));
    }
    public DrawObject(Point drawPosition, Point texturePosition, Dimension size, Point relAnimationCenter) {

        this.drawPosition = drawPosition;
        this.texturePosition = texturePosition;
        this.size = size;
        this.relAnimationCenter = relAnimationCenter;
    }
}
