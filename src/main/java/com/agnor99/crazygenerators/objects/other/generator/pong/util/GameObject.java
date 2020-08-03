package com.agnor99.crazygenerators.objects.other.generator.pong.util;

import com.agnor99.crazygenerators.objects.other.generator.pong.Board;
import com.agnor99.crazygenerators.objects.other.generator.pong.DrawObject;
import net.minecraft.util.IntReferenceHolder;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public abstract class GameObject implements Resetteable,Tickable {
    public final Board board;
    public Dimension size;
    public SubpixelPoint pos;
    public GameObject(Board board) {
        this.board = board;
        reset();
    }
    public int getCenterHeight() {
        return pos.y+size.height/2;
    }
    public List<IntReferenceHolder> getIntReferences() {
        List<IntReferenceHolder> referenceHolderList = new ArrayList();

        referenceHolderList.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return (int) pos.getX();
            }
            @Override
            public void set(int value) {
                pos.x = value;
            }
        });
        referenceHolderList.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return (int) pos.getY();
            }
            @Override
            public void set(int value) {
                pos.y = value;
            }
        });

        referenceHolderList.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return (int) size.getWidth();
            }
            @Override
            public void set(int value) {
                size.width = value;
            }
        });
        referenceHolderList.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return (int) size.getHeight();
            }
            @Override
            public void set(int value) {
                size.height = value;
            }
        });

        return referenceHolderList;
    }


    public boolean isColliding(GameObject gameObject){
        Rectangle r1 = new Rectangle(new Point(pos.x, pos.y), size);
        Rectangle r2 = new Rectangle(new Point(gameObject.pos.x, gameObject.pos.y), gameObject.size);
        return r1.intersects(r2);
    }

    public abstract List<DrawObject> createDrawObjects();
}
