package com.agnor99.crazygenerators.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

public class KeyboardHelper {
    private static final long WINDOW = Minecraft.getInstance().getMainWindow().getHandle();

    @OnlyIn(Dist.CLIENT)
    public static boolean isHoldingUp()
    {
        return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_UP) || InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_W);
    }
    @OnlyIn(Dist.CLIENT)
    public static boolean isHoldingDown()
    {
        return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_DOWN) || InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_S);
    }
    @OnlyIn(Dist.CLIENT)
    public static boolean isHoldingSpace()
    {
        return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_SPACE);
    }
}
