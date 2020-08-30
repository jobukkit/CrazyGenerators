package com.agnor99.crazygenerators.util;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.client.gui.PongGeneratorScreen;
import com.agnor99.crazygenerators.client.gui.PositionGeneratorScreen;
import com.agnor99.crazygenerators.client.gui.QuestionGeneratorScreen;
import com.agnor99.crazygenerators.client.gui.TimingGeneratorScreen;
import com.agnor99.crazygenerators.init.ContainerInit;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CrazyGenerators.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {

        ScreenManager.registerFactory(ContainerInit.QUESTION_GENERATOR.get(), QuestionGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.TIMING_GENERATOR.get(), TimingGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.PONG_GENERATOR.get(), PongGeneratorScreen::new);
        ScreenManager.registerFactory(ContainerInit.POSITION_GENERATOR.get(), PositionGeneratorScreen::new);
    }
}
