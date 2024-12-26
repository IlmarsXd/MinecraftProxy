package me.ilmars.mixin;

import me.ilmars.MinecraftProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(FMLHandshakeMessage.ModList.class)
public class MixinModList {

    @Shadow
    private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    private void onInit(List<ModContainer> modIds, CallbackInfo ci) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) return;
        System.out.println(modTags.remove(MinecraftProxy.MODID));
    }
}
