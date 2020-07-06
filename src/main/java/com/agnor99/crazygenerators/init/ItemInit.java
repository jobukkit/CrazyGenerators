package com.agnor99.crazygenerators.init;

import com.agnor99.crazygenerators.CrazyGenerators;

import com.agnor99.crazygenerators.CrazyGenerators.GeneratorItemGroup;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = CrazyGenerators.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(CrazyGenerators.MOD_ID)
public class ItemInit {
    public static final Item generator_core = new Item(
            new Item.Properties().group(GeneratorItemGroup.instance)
    ).setRegistryName("generator_core");

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(generator_core);
    }
}
