package com.agnor99.crazygenerators.objects.other.generator.pong;

import com.agnor99.crazygenerators.objects.other.generator.pong.util.GameObject;
import com.agnor99.crazygenerators.objects.other.generator.pong.util.SubpixelPoint;
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
        pos = new SubpixelPoint(-10,-10);
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
        moveY();
        moveX();
        collidesWithItems();
    }

    private void moveY() {
        SubpixelPoint speededDirection = calculateSpeededDirection();

        pos.sy += speededDirection.sy;
        pos.y+=speededDirection.y;
        pos.revalidate();
        int dsy = pos.sy;
        pos.sy = 0;
        if(pos.getCompleteY() + size.height > board.size.height){
            pos.y -= (pos.y+size.height-board.size.height);
            pos.sy = -pos.sy;
            pos.revalidate();
            direction.y = -direction.y;
        }

        if(pos.y < 0){
            pos.y = -pos.y;
            pos.sy = -pos.sy;
            pos.revalidate();
            direction.y = -direction.y;
        }

        pos.sy = dsy;
    }
    private void moveX(){
        SubpixelPoint speededDirection = calculateSpeededDirection();
        pos.x += speededDirection.x;
        pos.sx += speededDirection.sx;
        pos.revalidate();
        if(speededDirection.getCompleteX() > 0) {
            if(pos.getCompleteX() + size.width >= board.computer.pos.x){
                if(pos.getCompleteY() + size.height > board.computer.pos.getCompleteY()
                        && pos.getCompleteY() < board.computer.pos.getCompleteY() + board.computer.size.height) {
                    board.doBounce();
                    updateDirectionAfterBounce();
                    pos.x = board.computer.pos.x-size.width;
                    pos.sx = board.computer.pos.sx;

                    if(board.computer.isFreezing) {
                        pos.y = board.computer.getCenterHeight()-size.height;
                        pos.sy = 0;
                        attachedBar = board.computer;
                    }
                }else{
                    board.doScore(board.player);
                }
            }
        }else{
            if(pos.getCompleteX() <= board.player.pos.x+board.player.size.width){
                if(pos.getCompleteY() + size.height > board.player.pos.getCompleteY()
                        && pos.getCompleteY() < board.player.pos.getCompleteY() + board.player.size.height) {
                    board.doBounce();
                    updateDirectionAfterBounce();
                    pos.x = board.player.pos.x + board.player.size.width;
                    pos.sx = board.player.pos.sx;

                    if(board.player.isFreezing) {
                        pos.y = board.player.getCenterHeight()-size.height;
                        pos.sy = 0;
                        attachedBar = board.player;
                    }
                }else{
                    board.doScore(board.computer);
                }
            }
        }
    }

    public void collidesWithItems(){
        if(isColliding(board.item)){
            if(board.item.type != ItemType.NONE
                && board.item.numTicksSinceHit == 0) {
                board.item.isActive = true;
                board.item.hitBallDirection = new Point(direction);
            }
        }
    }

    SubpixelPoint calculateSpeededDirection() {
        return SubpixelPoint.create(speed, direction);

    }
    public void updateDirectionAfterBounce(){

        int x = new Random().nextInt(3)+1;
        if(direction.x > 0){
            direction.x = -x;
        }else{
            direction.x = x;
        }
        direction.y = new Random().nextInt(5)-2;
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
        drawObjects.add(new DrawObject(new Point(pos.x, pos.y), new Point(212,44), new Dimension(size)));
        return drawObjects;
    }
}
