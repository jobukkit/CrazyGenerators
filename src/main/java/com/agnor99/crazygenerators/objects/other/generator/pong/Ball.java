package com.agnor99.crazygenerators.objects.other.generator.pong;

import net.minecraft.util.IntReferenceHolder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ball extends GameObject {
    Point direction;
    float speed;
    PlayerBar attachedBar;
    boolean doesExist;
    public Ball(Board board) {
        super(board);
    }


    @Override
    public void reset() {
        size = new Dimension(3,3);
        pos = new Point(-10,-10);
        direction = new Point(1,1);
        speed = 1.5f;
        attachedBar = null;
        doesExist = false;
    }

    @Override
    public void hardReset() {
        reset();
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public float getSpeed() {
        return speed;
    }

    @Override
    public void tick() {
        if(!doesExist) return;
        if(attachedBar != null) return;
        Point speededDirection = calculateSpeededDirection();
        pos.translate(0,speededDirection.y);
        if(pos.y+size.height > board.size.height){
            pos.y -= (pos.y+size.height-board.size.height);
            direction.y = -direction.y;
        }

        if(pos.y < 0){
            pos.y = -pos.y;
            direction.y = -direction.y;
        }

        for(int i = 0; i < Math.abs(speededDirection.x); i++){
            if(speededDirection.x > 0) {
                if(pos.x+size.width == board.computer.pos.x
                        && board.computer.pos.y - size.height < pos.y
                        && board.computer.pos.y + board.computer.size.height + size.height > pos.y){
                    board.doBounce();
                    updateDirectionAfterBounce(board.computer.moveUp, board.computer.moveDown);
                    break;
                }
                pos.x++;
                if(pos.x >= board.size.width){
                    board.doScore(board.player);
                }

            }else{
                if(pos.x == board.player.pos.x+board.player.size.width
                        && board.player.pos.y - size.height < pos.y
                        && board.player.pos.y + board.player.size.height + size.height > pos.y){
                    board.doBounce();
                    updateDirectionAfterBounce(board.player.moveUp, board.player.moveDown);
                    break;
                }else{
                    pos.x--;

                    if(pos.x <= 0){
                        board.doScore(board.computer);
                    }
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
    private void updateDirectionAfterBounce(boolean up, boolean down){
        int rangeOffsett;
        if(up && !down){
            rangeOffsett = -2;
        }else if(!up && down){
            rangeOffsett = 0;
        }else{
            rangeOffsett = -1;
        }

        int yModifier = new Random().nextInt(3)-rangeOffsett;
        direction.y += yModifier;
        direction.y =  Math.min(direction.y, 5);
        direction.y = Math.max(direction.y, -5);
        direction.x = -direction.x;
    }
    @Override
    public List<IntReferenceHolder> getIntReferences() {
        List<IntReferenceHolder> references = super.getIntReferences();
        references.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return (int)(getSpeed()*100000);
            }

            @Override
            public void set(int i) {
                setSpeed(i/100000.0f);
            }
        });
        return references;
    }

    @Override
    public List<DrawObject> createDrawObjects() {
        List<DrawObject> drawObjects = new ArrayList<>(1);
        drawObjects.add(new DrawObject(new Point(pos), new Point(212,44), new Dimension(size)));
        return drawObjects;
    }
}
