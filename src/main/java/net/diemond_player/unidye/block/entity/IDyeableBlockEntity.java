package net.diemond_player.unidye.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import static net.diemond_player.unidye.item.CustomDyeItem.DEFAULT_WHITE_COLOR;

public interface IDyeableBlockEntity {
    int getColor();
    void setColor(int color);
//    default void setColorAndRerender(int color, BlockPos blockPos, ServerWorld serverWorld){
//        this.setColor(color);
//        PacketByteBuf buf = PacketByteBufs.create();
//        buf.writeBlockPos(blockPos);
//
//        for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, blockPos)) {
//            ServerPlayNetworking.send(player, UnidyeClient.RERENDER_BLOCK_PACKET_ID, buf);
//        }
//    }
//    default void setColorAndRerender(int color, BlockPos blockPos, ServerPlayerEntity player){
//        this.setColor(color);
//        PacketByteBuf buf = PacketByteBufs.create();
//        buf.writeBlockPos(blockPos);
//        ServerPlayNetworking.send(player, UnidyeClient.RERENDER_BLOCK_PACKET_ID, buf);
//    }
    static int getColor(BlockView world, BlockPos pos) {
        if (world == null) {
            return DEFAULT_WHITE_COLOR;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof IDyeableBlockEntity dyeableBlockEntity) {
            return dyeableBlockEntity.getColor();
        } else {
            return DEFAULT_WHITE_COLOR;
        }
    }
}
