package com.agnor99.crazygenerators.objects.blocks;

import com.agnor99.crazygenerators.CrazyGenerators;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

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

    public Item createItemFromItemBlock(TranslationTextComponent textComponent) {
        return new GeneratorItem(this,textComponent);
    }
    public Item createItemFromItemBlock(String information) {
        return new GeneratorItem(this, new StringTextComponent(information));
    }

    private class GeneratorItem extends BlockItem {
        TextComponent information;
        public GeneratorItem(Block generatorBlock) {
            this(generatorBlock, new StringTextComponent(""));
        }
        public GeneratorItem(Block generatorBlock, TextComponent information) {
            super(generatorBlock, new Item.Properties().
                    group(CrazyGenerators.GeneratorItemGroup.instance));
            setRegistryName(generatorBlock.getRegistryName());
            this.information = information;
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag) {
            if(!information.equals("")) {
                textComponents.add(information);
            }
        }
    }
}
