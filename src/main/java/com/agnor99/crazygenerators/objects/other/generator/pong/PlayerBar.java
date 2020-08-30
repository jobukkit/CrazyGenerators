package com.agnor99.crazygenerators.objects.other.generator.pong;

import com.agnor99.crazygenerators.objects.other.generator.pong.util.GameObject;
import com.agnor99.crazygenerators.objects.other.generator.pong.util.SubpixelPoint;
import net.minecraft.util.IntReferenceHolder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerBar extends GameObject {
    boolean moveUp, moveDown;
    int speed;
    boolean isPlayer;
    public int points;
    boolean isFreezing;
    PlayerBar(Board board, boolean isPlayer) {
        super(board);
        this.isPlayer = isPlayer;
        reset();
    }
    @Override
    public void tick() {
        if(!isPlayer) {
            doAI();
        }
        if(moveUp && !moveDown) {
            int relSpeed = Math.min(pos.y, speed); // if close to upper Edge just Edge
            for(int i = 0; i < board.balls.length; i++){
                if(board.balls[i].doesExist && board.balls[i].attachedBar == this) {
                    board.balls[i].pos.y -= relSpeed;
                }
            }
            pos.y -= relSpeed;
        }
        if(!moveUp && moveDown) {
            int relSpeed = Math.min(board.size.height-(pos.y + size.height),speed);
            for(int i = 0; i < board.balls.length; i++){
                if(board.balls[i].doesExist && board.balls[i].attachedBar == this) {
                    board.balls[i].pos.y += relSpeed;
                }
            }
            pos.y += relSpeed;
        }
        moveDown = false;
        moveUp = false;
    }

    @Override
    public void reset() {
        size = new Dimension(3,20);
        if(isPlayer) {
            pos = new SubpixelPoint(0, board.size.height/2-size.height/2);
        }else{
            pos = new SubpixelPoint(125-size.width, board.size.height/2-size.height/2);
        }
        speed = 3;
        isFreezing = false;
    }

    @Override
    public void hardReset() {
        reset();
        points = 0;
    }

    void doAI() {
        //calc for the most left ball
        Ball ball = null;
        for(int i = 0; i < board.balls.length;i++){
            if(ball == null){
                ball = board.balls[i];
            }else if(board.balls[i].pos.x > ball.pos.x){
                if(ball.doesExist && ball.attachedBar != this) {
                    ball = board.balls[i];
                }
            }
        }
        if(ball == null) {
            moveUp = false;
            moveDown = false;
            for(int i = 0; i < board.balls.length; i++){
                if(board.balls[i].doesExist && board.balls[i].attachedBar == this) {
                    if(Math.random() < 0.05d){
                        board.freeBalls();
                    }
                }
            }
            if(Math.random() < 0.5d){
                moveUp = true;
            }else {
                moveDown = true;
            }
            return;
        }

        if(ball.getCenterHeight() > getCenterHeight()+speed) {
            moveUp = false;
            moveDown = true;
        }else if(ball.getCenterHeight() < getCenterHeight()-speed) {
            moveUp = true;
            moveDown = false;
        }
    }

    @Override
    public List<IntReferenceHolder> getIntReferences() {
        List<IntReferenceHolder> references = super.getIntReferences();
        references.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return points;
            }

            @Override
            public void set(int i) {
                points = i;
            }
        });
        return references;
    }

    @Override
    public List<DrawObject> createDrawObjects() {
        List<DrawObject> drawObjects = new ArrayList<>();
        for(int i = 0; i < size.height;i++) {
            Point texturePosition;
            if(isPlayer){
                texturePosition = new Point(206,44);
            }else{
                texturePosition = new Point(209,44);
            }
            drawObjects.add(new DrawObject(new Point(pos.x,pos.y+i), texturePosition, new Dimension(3,1), new Point(1, pos.y + size.height/2)));
        }
        return drawObjects;
    }
}
