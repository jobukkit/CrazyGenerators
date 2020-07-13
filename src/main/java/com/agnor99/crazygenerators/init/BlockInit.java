package com.agnor99.crazygenerators.init;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.objects.blocks.GeneratorFrameBlock;
import com.agnor99.crazygenerators.objects.blocks.QuestionGeneratorBlock;
import com.agnor99.crazygenerators.objects.blocks.TimingGeneratorBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CrazyGenerators.MOD_ID)
@Mod.EventBusSubscriber(modid = CrazyGenerators.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    public static final QuestionGeneratorBlock question_generator = new QuestionGeneratorBlock();
    public static final GeneratorFrameBlock generator_frame = new GeneratorFrameBlock();
    public static final TimingGeneratorBlock timing_generator = new TimingGeneratorBlock();

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(generator_frame);
        event.getRegistry().register(question_generator);
        event.getRegistry().register(timing_generator);
    }

    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(generator_frame.createItemFromItemBlock());
        event.getRegistry().register(question_generator.createItemFromItemBlock("\u00A73Who want to be a millionaire?"));
        event.getRegistry().register(timing_generator.createItemFromItemBlock());
    }
}
