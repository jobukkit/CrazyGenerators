package com.agnor99.crazygenerators.objects.other.generator.pong;

import com.agnor99.crazygenerators.objects.other.generator.pong.util.GameObject;
import com.agnor99.crazygenerators.objects.other.generator.pong.util.Resetteable;
import com.agnor99.crazygenerators.objects.other.generator.pong.util.Tickable;
import com.agnor99.crazygenerators.objects.tile.PongGeneratorTileEntity;
import net.minecraft.util.IntReferenceHolder;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board implements Resetteable, Tickable {
    PongGeneratorTileEntity te;

    final Dimension size = new Dimension(125, 62);
    List<GameObject> gameObjects = new ArrayList<>();
    public Ball[] balls = new Ball[3];
    public PlayerBar player;
    public PlayerBar computer;
    public Item item;
    boolean start = false;
    int numBounces;
    public Board(PongGeneratorTileEntity te) {
        this.te = te;


        for(int i = 0; i < balls.length;i++) {
            balls[i] = new Ball(this);
            gameObjects.add(balls[i]);
        }
        player = new PlayerBar(this, true);
        gameObjects.add(player);
        computer = new PlayerBar(this, false);
        gameObjects.add(computer);
        item = new Item(this);
        gameObjects.add(item);
        reset();
    }


    @Override
    public void reset() {
        for (GameObject g : gameObjects) {
            g.reset();
        }
        balls[0].doesExist = true;
        balls[0].attachedBar = player;
        balls[0].pos.setLocation(player.pos.x+player.size.width, player.getCenterHeight()-balls[0].size.height/2);
        numBounces = 0;
    }

    @Override
    public void hardReset() {
        for(GameObject g: gameObjects){
            g.hardReset();
        }
        balls[0].doesExist = true;
        balls[0].attachedBar = player;
        balls[0].pos.setLocation(player.pos.x+player.size.width, player.getCenterHeight()-balls[0].size.height/2);
        numBounces = 0;
    }

    @Override
    public void tick() {
        if(!start)return;
        for (GameObject g : gameObjects) {
            g.tick();
        }
    }
    public void setPlayerMoving(boolean up, boolean down){
        player.moveUp = up;
        player.moveDown = down;
    }
    public void start() {
        start = true;
    }
    public void stop() {
        start = false;
    }
    public List<IntReferenceHolder> getIntReferenceHolder() {
        List<IntReferenceHolder> references = new ArrayList<>();
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
    public List<DrawObject> createDrawObjects() {
        List<DrawObject> drawObjects = new ArrayList<>();
        for(GameObject g: gameObjects){
            drawObjects.addAll(g.createDrawObjects());
        }
        return drawObjects;
    }


    public void freeBalls() {
        if(!start) return;
        for(Ball ball: balls) {
            ball.attachedBar = null;
        }
    }
    void doScore(PlayerBar bar) {
        bar.points++;
        bar.size.height = bar.size.height-bar.points;
        if(bar.isPlayer){
            te.addPointEnergy(balls[0].speed);
        }
        if(bar.points > 9) {
            if(bar.isPlayer){
                te.addWinEnergy();
            }
            hardReset();
        }
        reset();
    }
    void doBounce() {
        numBounces++;
        for(Ball b: balls){
            b.speed = (float)Math.min(b.speed+0.3, 5.0f);
        }
        if(numBounces%2== 0) {
            te.addBounceEnergy(balls[0].speed);
        }
        if(numBounces%5 == 0){
            item.spawn();
        }
    }
}