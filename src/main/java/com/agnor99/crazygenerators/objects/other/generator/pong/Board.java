package com.agnor99.crazygenerators.objects.other.generator.pong;

import net.minecraft.util.IntReferenceHolder;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board implements Resetteable, Tickable {
    final Dimension size = new Dimension(125, 62);
    List<GameObject> gameObjects = new ArrayList<>();

    Ball ball;
    PlayerBar player;
    PlayerBar computer;

    public Board() {
        reset();
    }


    @Override
    public void reset() {
        for (GameObject g : gameObjects) {
            g.reset();
        }
        ball = new Ball(this);
        gameObjects.add(ball);
        player = new PlayerBar(this, true);
        gameObjects.add(player);
        computer = new PlayerBar(this, false);
        gameObjects.add(computer);
    }

    @Override
    public void tick() {
        for (GameObject g : gameObjects) {
            g.tick();
        }
    }

    public List<IntReferenceHolder> getIntReferenceHolder() {
        List<IntReferenceHolder> references = new ArrayList<IntReferenceHolder>();
        for (GameObject g : gameObjects) {
            references.addAll(g.getIntReferences());
        }

        references.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return (int)size.getWidth();
            }

            @Override
            public void set(int i) {
                size.width = i;
            }
        });
        references.add(new IntReferenceHolder() {
            @Override
            public int get() {
                return (int)size.getHeight();
            }

            @Override
            public void set(int i) {
                size.height = i;
            }
        });


        return references;
    }
}