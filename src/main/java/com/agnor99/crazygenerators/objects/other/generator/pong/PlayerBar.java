package com.agnor99.crazygenerators.objects.other.generator.pong;

import net.minecraft.util.IntReferenceHolder;

import java.awt.*;
import java.util.List;

public class PlayerBar extends GameObject {
    boolean moveUp, moveDown;
    int speed;
    boolean isPlayer;
    int points;
    PlayerBar(Board board, boolean isPlayer) {
        super(board);
        this.isPlayer = isPlayer;
    }
    @Override
    public void tick() {
        if(!isPlayer) {
            doAI();
        }
        if(moveUp && !moveDown) {
            pos.y = Math.max(pos.y-speed, 0);
        }
        if(moveUp && !moveDown) {
            pos.y = Math.min(pos.y+speed, board.size.height);
        }
        moveDown = false;
        moveUp = false;
    }

    @Override
    public void reset() {
        size = new Dimension(3,20);
        pos = new Point(10,10);
        speed = 3;
    }

    void doAI() {
        if(board.ball.getCenterHeight() > getCenterHeight()+speed) {
            moveUp = false;
            moveDown = true;
        }else if(board.ball.getCenterHeight() < getCenterHeight()-speed) {
            moveUp = true;
            moveDown = false;
        }else{
            moveDown = false;
            moveUp = false;
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
}
