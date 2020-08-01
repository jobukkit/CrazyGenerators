package com.agnor99.crazygenerators.objects.other.generator.pong;


import net.minecraft.util.IntReferenceHolder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Item extends GameObject {
    int id;
    public Item(Board board) {
        super(board);
    }
    @Override
    public List<DrawObject> createDrawObjects() {
            List<DrawObject> drawObjects = new ArrayList<>();
            drawObjects.add(new DrawObject(new Point(), new Point(), new Dimension()));
            return drawObjects;
    }

    @Override
    public List<IntReferenceHolder> getIntReferences() {
        List<IntReferenceHolder> references =  super.getIntReferences();
        references.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return id;
            }

            @Override
            public void set(int i) {
                id = i;
            }
        });
        return references;
    }

    @Override
    public void reset() {
        pos = new Point(-10,-10);
    }

    @Override
    public void hardReset() {
        reset();
    }

    @Override
    public void tick() {

    }
}
