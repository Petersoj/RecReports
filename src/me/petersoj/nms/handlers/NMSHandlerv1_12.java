package me.petersoj.nms.handlers;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import me.petersoj.listeners.events.SignUpdateEvent;
import me.petersoj.nms.NMSHandler;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayInUpdateSign;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSHandlerv1_12 implements NMSHandler {

    @Override
    public void addSignUpdateListener(Player player, SignUpdateEvent signUpdateEvent) {
        EntityPlayer entityPlayer = getEntityPlayer(player);
        entityPlayer.playerConnection.networkManager.channel.pipeline().addBefore("packet_handler", player.getName(),
                new ChannelDuplexHandler() {
                    @Override
                    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
                        if (o instanceof PacketPlayInUpdateSign) {
                            PacketPlayInUpdateSign updateSign = (PacketPlayInUpdateSign) o;

                            BlockPosition position = updateSign.a(); // .a() is the update sign location
                            Location signLocation = new Location(player.getWorld(), position.getX(), position.getY(), position.getZ());

                            String[] lines = updateSign.b(); // .b() is the string[] lines of the sign.

                            signUpdateEvent.onSignUpdate(signLocation, lines);
                        }
                        super.channelRead(channelHandlerContext, o);
                    }
                });
    }

    private EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
