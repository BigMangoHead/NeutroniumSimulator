package net.bigmangohead.neutronsim.client;

import net.bigmangohead.neutronsim.NeutronSim;
import net.bigmangohead.neutronsim.neutronsim.Tags;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class NeutronSimGui extends GuiContainer {
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Tags.MOD_ID, "textures/gui/neutronium_simulator.png");

    private InventoryPlayer playerInv;

    private int posX;
    private int posY;

    public NeutronSimGui(Container container, InventoryPlayer playerInv) {
        super(container);
        this.playerInv = playerInv;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        drawTexturedModalRect(posX, posY, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.posX = (width - xSize) / 2;
        this.posY = (height - ySize) / 2;
    }
}
