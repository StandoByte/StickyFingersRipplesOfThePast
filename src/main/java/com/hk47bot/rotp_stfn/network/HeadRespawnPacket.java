package com.hk47bot.rotp_stfn.network;

import com.github.standobyte.jojo.init.ModEntityTypes;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.github.standobyte.jojo.network.packets.fromclient.ClAngeloRockButtonPacket;
import com.github.standobyte.jojo.util.mod.IPlayerPossess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class HeadRespawnPacket {
    public HeadRespawnPacket() {
    }

    public static class Handler implements IModPacketHandler<HeadRespawnPacket> {

        @Override
        public void encode(HeadRespawnPacket msg, PacketBuffer buf) {
        }

        @Override
        public HeadRespawnPacket decode(PacketBuffer buf) {
            return new HeadRespawnPacket();
        }

        @Override
        public void handle(HeadRespawnPacket msg, Supplier<NetworkEvent.Context> ctx) {
            PlayerEntity player = ctx.get().getSender();
            player.invulnerableTime = 0;
            player.removeEffect(Effects.DAMAGE_RESISTANCE);
            player.hurt(new DamageSource("rockRespawn").bypassArmor().bypassInvul(), Float.MAX_VALUE);
        }

        @Override
        public Class<HeadRespawnPacket> getPacketClass() {
            return HeadRespawnPacket.class;
        }
    }
}
