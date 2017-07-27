package me.petersoj.listeners;

import io.netty.buffer.Unpooled;
import me.petersoj.RecReportsPlugin;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.UnsupportedEncodingException;


public class Listeners implements Listener {

    private RecReportsPlugin plugin;

    public Listeners(RecReportsPlugin plugin) {
        this.plugin = plugin;
    }


    public void listen() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
//        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.networkManager.channel.pipeline().addBefore("packet_handler", "something", new ChannelDuplexHandler() {
//
//            @Override
//            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//                if (msg instanceof PacketPlayOutTileEntityData) {
//                    try {
//
//                        PacketPlayOutTileEntityData data = (PacketPlayOutTileEntityData) msg;
//                        Field field = data.getClass().getDeclaredField("c");
//                        field.setAccessible(true);
//
//                        NBTTagCompound compound = (NBTTagCompound) field.get(data);
//                        Field map = compound.getClass().getDeclaredField("map");
//                        map.setAccessible(true);
//
//                        Map<String, NBTBase> map1 = (Map<String, NBTBase>) map.get(compound);
//                        for (String str : map1.keySet()) {
//                            Bukkit.broadcastMessage(str + " " + map1.get(str));
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                super.write(ctx, msg, promise);
//            }
//
//            @Override
//            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                if (msg instanceof Packet) {
//                    System.out.println(msg.getClass().getSimpleName());
//                }
//                super.channelRead(ctx, msg);
//            }
//        });
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equals("/poop")) {

            Player player = e.getPlayer();
            Location location = player.getLocation();

            e.getPlayer().sendBlockChange(location, Material.SIGN_POST, (byte) 1);


            NBTTagCompound compound = new NBTTagCompound();
            compound.set("Text1", new NBTTagString("{\"text\":\"\"}"));
            compound.set("Text2", new NBTTagString("{\"text\":\"\"}"));
            compound.set("Text3", new NBTTagString("{\"text\":\"\"}"));
            compound.set("Text4", new NBTTagString("{\"text\":\"\"}"));

            compound.set("x", new NBTTagInt(location.getBlockX()));
            compound.set("y", new NBTTagInt(location.getBlockY()));
            compound.set("z", new NBTTagInt(location.getBlockZ()));
            compound.set("id", new NBTTagString("minecraft:sign"));
            PacketPlayOutTileEntityData entityData = new PacketPlayOutTileEntityData(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), 9, compound);

            ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(entityData);

            new BukkitRunnable() {
                @Override
                public void run() {
                    NBTTagCompound compound = new NBTTagCompound();
                    compound.set("Text2", new NBTTagString("{\"text\":\"" + convertUnicodeString("\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E\u203E") + "\"}"));
                    compound.set("Text1", new NBTTagString("{\"text\":\"\"}"));
                    compound.set("Text3", new NBTTagString("{\"text\":\"asfddfsafdsasfdasdfasfadfsadfsadfsdasdfa\"}"));
                    compound.set("Text4", new NBTTagString("{\"text\":\"asdfasdfadfsdfsafdsfdsa\"}"));

                    compound.set("x", new NBTTagInt(location.getBlockX()));
                    compound.set("y", new NBTTagInt(location.getBlockY()));
                    compound.set("z", new NBTTagInt(location.getBlockZ()));
                    compound.set("id", new NBTTagString("minecraft:sign"));
                    PacketPlayOutTileEntityData entityData = new PacketPlayOutTileEntityData(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), 9, compound);

                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(entityData);
//                    PacketPlayOutOpenSignEditor signPacket = new PacketPlayOutOpenSignEditor(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
//                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(signPacket);
                }
            }.runTaskLater(plugin, 20);

            PacketPlayOutOpenSignEditor signPacket = new PacketPlayOutOpenSignEditor(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(signPacket);
        }

        if (e.getMessage().equals("/book")) {
            openBook(new ItemStack(Material.BOOK), e.getPlayer());
        }
    }

    public void openBook(ItemStack book, Player p) {
//        int slot = p.getInventory().getHeldItemSlot();
//        ItemStack old = p.getInventory().getItem(slot);
//        p.getInventory().setItem(slot, book);

        PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());
        packetdataserializer.a(EnumHand.MAIN_HAND);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|BOpen", packetdataserializer));

//        ByteBuf buf = Unpooled.buffer(256);
//        buf.setByte(0, (byte) 0);
//        buf.writerIndex(1);
//
//        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
//        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
//        p.getInventory().setItem(slot, old);
    }

    public ItemStack book(String title, String author, String... pages) {
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK, 1);
        net.minecraft.server.v1_12_R1.ItemStack nmsis = CraftItemStack.asNMSCopy(is);
        NBTTagCompound bd = new NBTTagCompound();
        bd.setString("title", title);
        bd.setString("author", author);
        NBTTagList bp = new NBTTagList();
        for (String text : pages) {
            bp.add(new NBTTagString(text));
        }
        bd.set("pages", bp);
        nmsis.setTag(bd);
        is = CraftItemStack.asBukkitCopy(nmsis);
        return is;
    }

    private String convertUnicodeString(String unicode) {
        try {
            byte[] bytes = unicode.getBytes("UTF-8");
            String text = new String(bytes, "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
