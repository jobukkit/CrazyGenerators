package com.agnor99.crazygenerators.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

public class MouseHelper {
    private static final long WINDOW = Minecraft.getInstance().getMainWindow().getHandle();

    @OnlyIn(Dist.CLIENT)
    public static boolean isMouseDown()
    {
        return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_MOUSE_BUTTON_LEFT);
    }
}
