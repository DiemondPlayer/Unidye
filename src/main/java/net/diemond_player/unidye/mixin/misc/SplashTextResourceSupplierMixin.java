package net.diemond_player.unidye.mixin.misc;

//Taken with permission from Artyrian's Frontiers
//Source: currently unreleased

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.Unidye;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public abstract class SplashTextResourceSupplierMixin {
    @Unique
    private final List<String> unidyeTexts = Lists.newArrayList();
    @Unique
    private static final Identifier UNIDYE_SPLASHES = Identifier.of(Unidye.MOD_ID,"texts/splashes.txt");

    @ModifyReturnValue(method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Ljava/util/List;", at = @At(value = "RETURN", ordinal = 0))
    protected List<String> unidye$prepare(List<String> original, @Local(argsOnly = true) ResourceManager resourceManager, @Local(argsOnly = true) Profiler profiler) {
        try {
            BufferedReader bufferedReader = MinecraftClient.getInstance().getResourceManager().openAsReader(UNIDYE_SPLASHES);

            List<String> splashTexts;
            try {
                splashTexts = bufferedReader.lines().map(String::trim).filter(splashText -> splashText.hashCode() != 125780783).toList();
            } catch (Throwable var7) {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }

                throw var7;
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            List<String> complete = Lists.newArrayList();
            boolean worked = complete.addAll(splashTexts);

            if (worked)
            {
                Unidye.LOGGER.info("Successfully mixed splash texts.");
                return complete;
            }
            else
            {
                Unidye.LOGGER.error("Unable to mix splash texts.");
                return original;
            }
        } catch (IOException var8) {
            return original;
        }
    }

    @Inject(method = "apply(Ljava/util/List;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("TAIL"))
    protected void unidye$apply(List<String> list, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci)
    {
        this.unidyeTexts.addAll(list);
    }
}
