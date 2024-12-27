package me.ilmars.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.apache.commons.lang3.StringUtils;

import static me.ilmars.MinecraftProxy.mc;

public class ProxyButton extends GuiButton {
    public ProxyButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        displayString = StringUtils.abbreviate("Proxy: " + ProxyHandler.lastProxyIp, 22);
        super.drawButton(mc, mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        mc.displayGuiScreen(new GuiProxy(mc.currentScreen));
    }
}
