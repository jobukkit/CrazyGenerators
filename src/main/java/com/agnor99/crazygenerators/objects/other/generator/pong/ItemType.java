package com.agnor99.crazygenerators.objects.other.generator.pong;

public enum ItemType {
    NONE(0),
    SIZE_UP(1),
    SIZE_DOWN(2),
    TRIPLE_BALLS(3),
    FREEZING_ICE(4);

    public int id;

    ItemType(int id){
        this.id = id;
    }
}
