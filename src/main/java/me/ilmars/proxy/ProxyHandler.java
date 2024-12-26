package me.ilmars.proxy;

import io.netty.channel.Channel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.net.InetSocketAddress;

import static me.ilmars.MinecraftProxy.mc;

public class ProxyHandler {
    public static Proxy proxy = new Proxy();
    public static String lastProxyIp = "none";

    @SubscribeEvent
    public void postGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.gui instanceof GuiMultiplayer)) return;
        ScaledResolution sr = new ScaledResolution(mc);
        event.buttonList.add(new ProxyButton(1080, sr.getScaledWidth() - 125, 5, 120, 20, "Proxy: " + lastProxyIp));
    }

    public static void addProxyHandler(Channel channel) {
        if (proxy.isEnabled()) {
            lastProxyIp = proxy.getIp();
            InetSocketAddress address = new InetSocketAddress(proxy.getIp(), proxy.getPort());
            switch (proxy.getType()) {
                case SOCKS4:
                    channel.pipeline().addFirst(new Socks4ProxyHandler(address, proxy.getUserID()));
                    break;
                case SOCKS5:
                    channel.pipeline().addFirst(new Socks5ProxyHandler(address, proxy.getUsername(), proxy.getPassword()));
                    break;
            }
        } else {
            lastProxyIp = "none";
        }
    }
}
