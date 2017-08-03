package me.petersoj.nms.handlers;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import me.petersoj.RecReportsPlugin;
import me.petersoj.listeners.events.SignUpdateEvent;
import me.petersoj.nms.NMSHandler;
import me.petersoj.nms.RecordedPlayer;
import me.petersoj.nms.players.RecordedPlayerv1_12;
import me.petersoj.report.ReportPlayer;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayInUpdateSign;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSHandlerv1_12 implements NMSHandler {

    @Override
    public RecordedPlayer getNewRecordedPlayer(RecReportsPlugin plugin, ReportPlayer reportPlayer, Player sendPacketsPlayer) {
        return new RecordedPlayerv1_12(plugin, reportPlayer, sendPacketsPlayer);
    }

    @Override
    public void openSignInterface(Player player, String initialText, String finalText, int delayToFinal) {

    }

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
