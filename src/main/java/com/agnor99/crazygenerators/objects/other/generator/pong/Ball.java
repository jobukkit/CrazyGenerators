package com.agnor99.crazygenerators.objects.other.generator.pong;

import net.minecraft.util.IntReferenceHolder;

import java.awt.*;
import java.util.List;

public class Ball extends GameObject {
    Point direction;
    int speed;
    public Ball(Board board) {
        super(board);
    }


    @Override
    public void reset() {
        size = new Dimension(3,3);
        pos = new Point(10,10);
        direction = new Point(1,1);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getSpeed() {
        return speed;
    }

    @Override
    public void tick() {
        Point speededDirection = calculateSpeededDirection();
        pos.translate(0,speededDirection.y);
        if(pos.y+size.height > board.size.height){
            pos.y -= (pos.y+size.height-board.size.height);
        }

        if(pos.y < 0){
            pos.y = -pos.y;
        }

        for(int i = 0; i < Math.abs(speededDirection.x); i++){
            if(speededDirection.x > 0) {
                if(pos.x+size.width == board.computer.pos.x){
                    speededDirection.x*=-1;
                    direction.x*=-1;

                    pos.x--;
                }else{
                    pos.x++;
                }
            }else{
                if(pos.x == board.computer.pos.x+board.computer.size.width){
                    speededDirection.x*=-1;
                    direction.x*=-1;

                    pos.x++;
                }else{
                    pos.x--;
                }
            }
        }
    }
    Point calculateSpeededDirection() {
        Point speededDirection = new Point();
        while(speededDirection.distance(0,0) < speed) {
            speededDirection.translate(direction.x, direction.y);
        }
        return speededDirection;
    }

    @Override
    public List<IntReferenceHolder> getIntReferences() {
        List<IntReferenceHolder> references = super.getIntReferences();
        references.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return getSpeed();
            }

            @Override
            public void set(int i) {
                setSpeed(i);
            }
        });
        return references;
    }
}
