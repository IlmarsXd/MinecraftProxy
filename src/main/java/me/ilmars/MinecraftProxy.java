package me.ilmars;

import me.ilmars.proxy.Config;
import me.ilmars.proxy.ProxyHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static me.ilmars.MinecraftProxy.MODID;

@Mod(modid = MODID)
public class MinecraftProxy {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final String MODID = "minecraftproxy";
    public static final String CONFIG_DIR = mc.mcDataDir + "/config/";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadCurrentProxy();
        MinecraftForge.EVENT_BUS.register(new ProxyHandler());
    }
}
