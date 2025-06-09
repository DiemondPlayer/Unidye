package net.diemond_player.unidye;

import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.entity.client.model.DyeableShulkerEntityModel;
import net.diemond_player.unidye.entity.client.renderer.*;
import net.diemond_player.unidye.payload.DatabasePayload;
import net.diemond_player.unidye.payload.SetColorAndRerenderBlockPayload;
import net.diemond_player.unidye.registry.*;
import net.diemond_player.unidye.util.DyeData;
import net.diemond_player.unidye.util.UnidyeBuiltinModelItemRenderer;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class UnidyeClient implements ClientModInitializer {

    public static HashMap<Integer, DyeData> database = new HashMap<>();

    public static final HashMap<Block, Integer> DYEABLE_BLOCKS_ADJUST = new HashMap<>() {{
            put(UnidyeBlocks.CUSTOM_CONCRETE_POWDER, 15);
    }};

    @Override
    public void onInitializeClient() {
        registerEntityRenderers();
        registerBlockEntityRenderers();
        registerEntityModelLayers();
        registerBlockRenderLayers();
        registerItemColors();
        registerBlockColors();
        registerBuiltinItemRenderer();
        registerNetworking();
        registerModelLoadingPlugin();
    }

    private void registerModelLoadingPlugin() {
        ModelLoadingPlugin.register(pluginContext -> {
            File resourcepackDir = MinecraftClient.getInstance().getResourcePackDir().toFile();
            File nativeSpecialDir = new File(resourcepackDir.getParentFile().getParentFile(), "src/main/resources/assets/unidye/models/item/custom_dye_special/");
            registerModels(pluginContext, nativeSpecialDir);

            ResourcePackManager resourcePackManager = MinecraftClient.getInstance().getResourcePackManager();
            List<ResourcePack> directoryResourcePacks = resourcePackManager.createResourcePacks()
                    .stream().filter(resourcePack -> resourcePack instanceof DirectoryResourcePack).toList();
            for(ResourcePack directoryResourcePack : directoryResourcePacks){
                File resourcepackSpecialDir = new File(resourcepackDir, directoryResourcePack.getInfo().title().getString()+"/assets/unidye/models/item/custom_dye_special/");
                registerModels(pluginContext, resourcepackSpecialDir);
            }

            for(String name : UnidyeUtils.DYE_COLOR_TO_NAME.values()){
                Unidye.LOGGER.info(name);
                pluginContext.addModels(Identifier.of("unidye", "item/custom_" + name + "_dye"));
            }
        });
    }

    private void registerModels(ModelLoadingPlugin.Context pluginContext, File dir) {
        if(dir.listFiles() != null){
            for(File fileChild : dir.listFiles()){
                String fileName = fileChild.getName();
                if(fileName.endsWith(".json")){
                    fileName = fileName.substring(0, fileName.indexOf(".json"));
                    Unidye.LOGGER.info(fileName);
                    pluginContext.addModels(Identifier.of("unidye", "item/custom_dye_special/" + fileName));
                }
            }
        }
    }

    private void registerNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(SetColorAndRerenderBlockPayload.ID, (payload, context) -> context.client().execute(() -> {
            ClientWorld world = context.client().world;
            BlockPos pos = payload.pos();
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DyeableBlockEntity dyeableBlockEntity) {
                dyeableBlockEntity.setColor(payload.color());
            }
            world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.REDRAW_ON_MAIN_THREAD);
        }));

        ClientPlayNetworking.registerGlobalReceiver(DatabasePayload.ID, (payload, context) -> context.client().execute(() -> {
            Unidye.LOGGER.info("received the database on client");
            database = payload.database();
            Unidye.LOGGER.info("successfully set the database on client");
        }));

//        ClientPlayNetworking.registerGlobalReceiver(UpdatePrefixPayload.ID, (payload, context) -> context.client().execute(() -> {
//            ClientWorld world = context.client().world;
//            ItemStack itemStack = payload.itemStack();
//        }));
    }

    private void registerBuiltinItemRenderer() {
        UnidyeBuiltinModelItemRenderer unidyeBuiltinModelItemRenderer = new UnidyeBuiltinModelItemRenderer();
        BuiltinItemRendererRegistry.INSTANCE.register(UnidyeBlocks.CUSTOM_BED, unidyeBuiltinModelItemRenderer::renderBlockEntities);
        BuiltinItemRendererRegistry.INSTANCE.register(UnidyeBlocks.CUSTOM_SHULKER_BOX, unidyeBuiltinModelItemRenderer::renderBlockEntities);
        BuiltinItemRendererRegistry.INSTANCE.register(UnidyeItems.CUSTOM_BANNER, unidyeBuiltinModelItemRenderer::renderBlockEntities);
        BuiltinItemRendererRegistry.INSTANCE.register(UnidyeItems.CUSTOM_DYE, unidyeBuiltinModelItemRenderer::renderCustomDye);
    }

    private void registerItemColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            String colorHex = String.format("%06X", (0xFFFFFF & UnidyeUtils.getColor(stack))).toLowerCase();
            Identifier patternModel = Identifier.of(Unidye.MOD_ID, "item/custom_dye_special/" + colorHex);
            BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(patternModel);
            return model != null || tintIndex > 0 ? -1 : ColorHelper.Argb.fullAlpha(UnidyeUtils.getColor(stack));
        }, UnidyeItems.CUSTOM_DYE);
        registerItemColor(UnidyeBlocks.CUSTOM_BANNER.asItem());
    }

    private void registerBlockColors() {
        registerBlockColor(UnidyeBlocks.CUSTOM_WOOL);
        registerBlockColor(UnidyeBlocks.CUSTOM_STAINED_GLASS);
        registerBlockColor(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE);
        registerBlockColor(UnidyeBlocks.CUSTOM_SHULKER_BOX);

        UnidyeBlockEntities.DYEABLE_BE_BLOCKS.forEach(block -> {
            if (DYEABLE_BLOCKS_ADJUST.containsKey(block)){
                registerBlockColor(block, DYEABLE_BLOCKS_ADJUST.get(block));
            } else {
                registerBlockColor(block);
            }
        });
    }

    private void registerBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(UnidyeBlocks.CUSTOM_STAINED_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(UnidyeBlocks.CUSTOM_CANDLE, RenderLayer.getTranslucent());
    }

    private void registerEntityModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(UnidyeModelLayers.CUSTOM_SHULKER, DyeableShulkerEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(UnidyeModelLayers.CUSTOM_BED_HEAD, DyeableBedBlockEntityRenderer::getHeadTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(UnidyeModelLayers.CUSTOM_BED_FOOT, DyeableBedBlockEntityRenderer::getFootTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(UnidyeModelLayers.CUSTOM_BANNER, DyeableBannerBlockEntityRenderer::getTexturedModelData);
    }

    private void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(UnidyeBlockEntities.DYEABLE_SHULKER_BOX_BE, DyeableShulkerBoxBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(UnidyeBlockEntities.DYEABLE_BED_BE, DyeableBedBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(UnidyeBlockEntities.DYEABLE_BANNER_BE, DyeableBannerBlockEntityRenderer::new);
    }

    private void registerEntityRenderers() {
        EntityRendererRegistry.register(UnidyeEntities.DYEABLE_FALLING_BLOCK_ENTITY, DyeableFallingBlockEntityRenderer::new);
    }

    public static void registerItemColor(Item item) {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ColorHelper.Argb.fullAlpha(UnidyeUtils.getColor(stack)), item);
    }

    public static void registerItemColor(Item item, int adjust) {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ColorHelper.Argb.fullAlpha(adjust(UnidyeUtils.getColor(stack), adjust)), item);
    }

    public static void registerBlockColor(Block block) {
        if(block.asItem() != Items.AIR) {
            registerItemColor(block.asItem());
        }
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> IDyeableBlockEntity.getColor(world, pos), block);
    }

    public static void registerBlockColor(Block block, int adjust) {
        if(block.asItem() != Items.AIR) {
            registerItemColor(block.asItem(), adjust);
        }
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> adjust(IDyeableBlockEntity.getColor(world, pos), adjust), block);
    }

    public static int adjust(int color, int i) {
        int j = Math.max(Math.min(((color & 0xFF0000) >> 16) + i, 255), 0);
        int k = Math.max(Math.min(((color & 0xFF00) >> 8) + i, 255), 0);
        int l = Math.max(Math.min(((color & 0xFF)) + i, 255), 0);
        int res = j;
        res = (res << 8) + k;
        res = (res << 8) + l;
        return res;
    }
}
