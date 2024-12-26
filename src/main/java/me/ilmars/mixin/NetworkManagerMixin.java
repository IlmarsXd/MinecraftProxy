package me.ilmars.mixin;

import io.netty.channel.Channel;
import me.ilmars.proxy.ProxyHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.NetworkManager$5")
public class NetworkManagerMixin {
    @Inject(method = "initChannel", at = @At("RETURN"))
    public void initChannel(Channel p_initChannel_1_, CallbackInfo ci) {
        ProxyHandler.addProxyHandler(p_initChannel_1_);
    }
}