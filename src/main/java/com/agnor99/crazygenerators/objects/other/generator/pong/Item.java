package com.agnor99.crazygenerators.objects.other.generator.pong;


import com.agnor99.crazygenerators.objects.other.generator.pong.util.GameObject;
import com.agnor99.crazygenerators.objects.other.generator.pong.util.SubpixelPoint;
import net.minecraft.util.IntReferenceHolder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item extends GameObject {
    ItemType type;
    boolean isActive;

    SubpixelPoint hitBallPos;
    Point hitBallDirection;
    int numTicksSinceHit;

    public Item(Board board) {
        super(board);
        size = new Dimension(10,10);
    }
    @Override
    public List<DrawObject> createDrawObjects() {
            List<DrawObject> drawObjects = new ArrayList<>();
            drawObjects.add(new DrawObject(new Point(pos.x, pos.y), new Point(23 + (type.id - 1)*10,190), new Dimension(size)));
            return drawObjects;
    }

    @Override
    public List<IntReferenceHolder> getIntReferences() {
        List<IntReferenceHolder> references =  super.getIntReferences();
        references.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return type.id;
            }

            @Override
            public void set(int i) { type = ItemType.values()[i];
            }
        });
        references.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return numTicksSinceHit;
            }

            @Override
            public void set(int i) { numTicksSinceHit = i;
            }
        });
        return references;
    }

    @Override
    public void reset() {
        pos = new SubpixelPoint(-10,-10);
        type = ItemType.NONE;
        isActive = false;
        hitBallDirection = null;
        numTicksSinceHit = 0;
    }

    @Override
    public void hardReset() {
        reset();
    }

    @Override
    public void tick() {
        if(isActive) {
            numTicksSinceHit++;
            PlayerBar affectedBar;
            switch (type) {
                case SIZE_UP:
                    if(hitBallDirection.x > 0) {
                        affectedBar = board.player;
                    }else{
                        affectedBar = board.computer;
                    }
                    affectedBar.size.height += 10;
                    if(affectedBar.pos.getCompleteY()+affectedBar.size.height > board.size.height) {
                        affectedBar.pos.y = board.size.height-affectedBar.size.height;
                    }
                    isActive = false;
                    break;

                case SIZE_DOWN:
                    if(hitBallDirection.x > 0) {
                        affectedBar = board.computer;
                    }else{
                        affectedBar = board.player;
                    }
                    affectedBar.size.height -= 5;
                    affectedBar.pos.x += 2;

                    isActive = false;
                    break;

                case TRIPLE_BALLS:
                    if(numTicksSinceHit == 10) {
                        board.balls[1].pos = new SubpixelPoint(pos.x, pos.y);
                        board.balls[1].doesExist = true;
                        board.balls[1].speed = board.balls[0].speed;
                        board.balls[1].direction = new Point(hitBallDirection);
                    }else if(numTicksSinceHit == 20) {
                        board.balls[2].pos =  new SubpixelPoint(pos.x, pos.y);
                        board.balls[2].doesExist = true;
                        board.balls[2].speed = board.balls[0].speed;
                        board.balls[2].direction = new Point(hitBallDirection);
                    }

                    break;

                case FREEZING_ICE:
                    if(hitBallDirection.x > 0) {
                        affectedBar = board.player;
                    }else{
                        affectedBar = board.computer;
                    }
                    affectedBar.isFreezing = true;

                    isActive = false;
                    break;

                case NONE:
                    break;
            }
        }
    }
    public void spawn(){
        pos.x = new Random().nextInt(board.size.width-size.width);
        pos.y = new Random().nextInt(board.size.height-size.height);
        type = ItemType.values()[new Random().nextInt(ItemType.values().length-1)+1];
    }
}
