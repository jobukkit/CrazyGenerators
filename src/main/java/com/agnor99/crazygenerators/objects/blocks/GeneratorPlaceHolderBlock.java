package com.agnor99.crazygenerators.objects.blocks;

import com.agnor99.crazygenerators.CrazyGenerators;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

public abstract class GeneratorPlaceHolderBlock extends Block {
    public static Block.Properties properties = Block.Properties.create(Material.ROCK)
            .hardnessAndResistance(2.2f, 15.0f)
            .harvestTool(ToolType.PICKAXE)
            .harvestLevel(0)
            .sound(SoundType.METAL)
            .notSolid()
            .variableOpacity();
    public GeneratorPlaceHolderBlock(String registryName) {
        super(properties);
        setRegistryName(registryName);
    }
    public Item createItemFromItemBlock() {
        return new BlockItem(
                this,
                new Item.Properties().
                        group(CrazyGenerators.GeneratorItemGroup.instance)
        ).setRegistryName(getRegistryName());
    }
}
