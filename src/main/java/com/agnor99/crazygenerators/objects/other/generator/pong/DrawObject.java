package com.agnor99.crazygenerators.objects.other.generator.pong;


import java.awt.*;

public class DrawObject {
    public Point drawPosition,texturePosition;
    public Dimension size;
    public DrawObject(Point drawPosition, Point texturePosition, Dimension size){
        this.drawPosition = drawPosition;
        this.texturePosition = texturePosition;
        this.size = size;
    }
}
