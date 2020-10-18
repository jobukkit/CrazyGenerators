package com.agnor99.crazygenerators.client.gui.util;

import com.agnor99.crazygenerators.objects.container.GeneratorContainer;
import com.agnor99.crazygenerators.network.NetworkUtil;
import com.agnor99.crazygenerators.network.packets.sync.PacketRequestSync;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public abstract class GeneratorScreen<SpecContainer extends GeneratorContainer> extends ContainerScreen<SpecContainer> {
    protected ResourceLocation BACKGROUND_TEXTURE;
    protected Point RELATIVE_SCREEN_POSITION;
    boolean startSynced = false;
    private String generatorName;



    public GeneratorScreen(SpecContainer screenContainer, PlayerInventory playerInventory, ITextComponent title, String generatorName) {
        super(screenContainer, playerInventory, title);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 184;
        this.generatorName = generatorName;

    }

    @Override
    protected void init() {
        super.init();
        RELATIVE_SCREEN_POSITION = new Point((this.width - this.xSize)/2, (this.height - this.ySize)/2);

        //TitlePos
        this.field_238742_p_ = 7;
        this.field_238743_q_ = 6;

        //PlayerInventoryTitlePos
        field_238744_r_ = 7;
        field_238745_s_ = 90;
    }

    @Override
    public SpecContainer getContainer() {
        return super.getContainer();
    }

    protected void setBackgroundTexture(ResourceLocation loc) {
        BACKGROUND_TEXTURE = loc;
    }
    
    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, final float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        func_230459_a_(stack, mouseX, mouseY);

    }
    @Override
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        if(!startSynced)requestSync();
        startSynced = true;
        super.func_230451_b_(stack, mouseX, mouseY);
        final int DEFAULT_COLOR = 4210752;
    }

    @Override
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        blit(stack, RELATIVE_SCREEN_POSITION.x, RELATIVE_SCREEN_POSITION.y, 0 , 0, xSize, ySize);

        drawEnergy(stack);

    }

    protected void drawButtonText(MatrixStack stack, String text, Button button) {
        if(!button.visible) return;
        final int WHITE = 16777215;
        font.drawString(stack, text,button.x+2-RELATIVE_SCREEN_POSITION.x, button.y+2-RELATIVE_SCREEN_POSITION.y, WHITE);
    }

    private void drawEnergy(MatrixStack stack) {
        Point energyContainerPoint = new Point(152,37);
        Point energyContainerTexturePoint = new Point(180,37);
        Dimension energyContainerSize = new Dimension(16,56);

        int currentEnergyContainerHeight = calcHeight(energyContainerSize.height,container.getEnergy(), container.getCapacity());
        int missingHeight = energyContainerSize.height-currentEnergyContainerHeight;

        energyContainerPoint.translate(0,missingHeight);
        energyContainerTexturePoint.translate(0,missingHeight);
        energyContainerSize.setSize(energyContainerSize.width, energyContainerSize.height-missingHeight);

        drawPartRelativeOnScreen(stack, energyContainerPoint, energyContainerTexturePoint, energyContainerSize);
    }
    protected int calcHeight(int maxHeight, float value, int maxValue) {
        return (int)(maxHeight*(value/((float)maxValue)));
    }


    protected void drawHoverMessages(MatrixStack stack, Point relativeMousePosition) {

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);


        drawEnergyHover(stack, relativeMousePosition);
        drawInfoHover(stack, relativeMousePosition);
    }

    private void drawInfoHover(MatrixStack stack, Point relativeMousePosition) {
        Point drawPosition = new Point(137,15);
        Dimension textureSize = new Dimension(12,12);

        if(isMouseOverHoverArea(relativeMousePosition, drawPosition, textureSize)) {
            TranslationTextComponent information = new TranslationTextComponent(generatorName);
            drawHoverMessage(stack, relativeMousePosition, information.getString());
        }
    }

    private void drawEnergyHover(MatrixStack stack, Point relativeMousePosition) {
        Point drawPosition = new Point(151,36);
        Dimension textureSize = new Dimension(18,58);

        if(isMouseOverHoverArea(relativeMousePosition, drawPosition, textureSize)) {
            drawHoverMessage(stack, relativeMousePosition, container.getEnergy() + "RF");
        }
    }

    protected void drawHoverMessage(MatrixStack stack, Point relativeMousePosition, String message) {
        List<String> lines = breakStringIntoLineList(message,92);
        List<StringTextComponent> comps = new ArrayList<>();

        lines.stream().forEach(text->comps.add(new StringTextComponent(text)));

        renderTooltip(stack, comps, relativeMousePosition.x, relativeMousePosition.y);
    }
    protected List<String> breakStringIntoLineList(String message, int lineWidth) {
        String[] words = message.split(" ");
        List<String> lines = new ArrayList<>();
        lines.add("");
        for(String word: words) {
            String currLine = lines.get(lines.size()-1);
            currLine = currLine + " " + word;
            currLine = currLine.trim();

            if(font.getStringWidth(currLine) < lineWidth) {
                lines.set(lines.size()-1, currLine);
            }else {
                lines.add(word);
            }
        }
        return lines;
    }

    protected boolean isMouseOverHoverArea(Point relativeMouseLocation, Point upperPosition, Dimension size) {
        return new Rectangle(upperPosition,size).contains(relativeMouseLocation);
    }

    protected void drawPartRelativeOnScreen(MatrixStack stack, Point positionRelativeToScreen, Point positionTexture, Dimension size) {
        blit(stack, RELATIVE_SCREEN_POSITION.x+positionRelativeToScreen.x, RELATIVE_SCREEN_POSITION.y+positionRelativeToScreen.y, positionTexture.x, positionTexture.y, size.width, size.height);
    }

    private void requestSync() {
        NetworkUtil.INSTANCE.sendToServer(new PacketRequestSync(container.getTileEntity().getPos()));
    }

}
