package com.agnor99.crazygenerators.client.gui;

import com.agnor99.crazygenerators.CrazyGenerators;
import com.agnor99.crazygenerators.client.gui.util.GeneratorScreen;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.item_generator.RequestItemPacket;
import com.agnor99.crazygenerators.objects.container.ItemGeneratorContainer;
import com.agnor99.crazygenerators.objects.other.generator.timing.ButtonData;
import com.agnor99.crazygenerators.objects.tile.ItemGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;


@OnlyIn(Dist.CLIENT)
public class ItemGeneratorScreen extends GeneratorScreen<ItemGeneratorContainer> {
    ItemGeneratorTileEntity igte;

    RequestItemButton button;
    public ItemGeneratorScreen(ItemGeneratorContainer screenContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(screenContainer, playerInventory, title, "information.item_generator");
        setBackgroundTexture(new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/item_generator.png"));
        igte = (ItemGeneratorTileEntity) screenContainer.getTileEntity();
    }

    @Override
    protected void init() {
        super.init();
        button = new RequestItemButton();
        addButton(button);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int WHITE = 16777215;
        final int DEFAULT_COLOR = 4210752;


        Point relativeMousePosition = new Point(mouseX, mouseY);
        relativeMousePosition.translate(-RELATIVE_SCREEN_POSITION.x, -RELATIVE_SCREEN_POSITION.y);
        if(igte.toFind != null) {
            font.drawString(igte.toFind.getRegistryName().toString().replace("minecraft:", ""), 9, 17, WHITE);
            itemRenderer.renderItemIntoGUI(new ItemStack(igte.toFind), 80,47);
        }

        drawButtonText(new TranslationTextComponent("button.item_generator.new").getFormattedText(), button);

        drawHoverMessages(relativeMousePosition);
    }

    @Override
    protected void drawHoverMessages(Point mousePosition) {
        super.drawHoverMessages(mousePosition);
    }
    private class RequestItemButton extends ImageButton {
        public RequestItemButton() {
            super(RELATIVE_SCREEN_POSITION.x+59, RELATIVE_SCREEN_POSITION.y+74,
                    58, 11,
                    0, 0, 11,
                    new ResourceLocation(CrazyGenerators.MOD_ID, "textures/gui/item/new_item.png"),
                    new ItemGeneratorScreen.RequestItemButtonHandler());
        }
    }
    private class RequestItemButtonHandler implements Button.IPressable {

        @Override
        public void onPress(Button button) {
            NetworkUtil.INSTANCE.sendToServer(new RequestItemPacket(igte.getPos(), minecraft.player.dimension));
        }
    }
}
