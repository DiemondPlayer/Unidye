package net.diemond_player.unidye.util;

//Mostly taken from MIT-licensed project Adorn by Juuz
//Source: https://github.com/Juuxel/Adorn/blob/bd70a2955640897bc68ff1f4f201fe5e6c10bc32/fabric/src/main/java/juuxel/adorn/AdornMixinPlugin.java

import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class UnidyeMixinPlugin implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "net.diemond_player.unidye.mixin.compat.emi.EmiApiMixin", () -> FabricLoader.getInstance().isModLoaded("emi"),
            "net.diemond_player.unidye.mixin.compat.emi.EmiArmorDyeRecipeMixin", () -> FabricLoader.getInstance().isModLoaded("emi"),
            "net.diemond_player.unidye.mixin.compat.rei.ClientHelperImplMixin", () -> FabricLoader.getInstance().isModLoaded("roughlyenoughitems"),
            "net.diemond_player.unidye.mixin.compat.rei.ArmorDyeRecipeFillerMixin", () -> FabricLoader.getInstance().isModLoaded("roughlyenoughitems"),
            "net.diemond_player.unidye.mixin.render.BlockDustParticleMixin", () -> !FabricLoader.getInstance().isModLoaded("bountifulfares")
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}