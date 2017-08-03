package me.petersoj.listeners;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.petersoj.RecReportsPlugin;
import me.petersoj.listeners.events.SignUpdateEvent;
import me.petersoj.nms.players.RecordedPlayerv1_12;
import me.petersoj.report.ReportPlayer;
import net.minecraft.server.v1_12_R1.*;
import org.apache.commons.lang3.reflect.FieldUtils;
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
import org.bukkit.util.Vector;

import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Listeners implements Listener, SignUpdateEvent {

    private RecReportsPlugin plugin;


    public Listeners(RecReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public void listen() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onSignUpdate(Location location, String[] lines) {
        // Process 'other' report type
    }

    @SuppressWarnings("unchecked")
    public void setGlowing(Player glowingPlayer, Player sendPacketPlayer, boolean glow) {
        try {
            EntityPlayer entityPlayer = ((CraftPlayer) glowingPlayer).getHandle();

            DataWatcher toCloneDataWatcher = entityPlayer.getDataWatcher();
            DataWatcher newDataWatcher = new DataWatcher(entityPlayer);

            // The map that stores the DataWatcherItems is private within the DataWatcher Object.
            // We need to use Reflection to access it from Apache Commons and change it.
            Map<Integer, DataWatcher.Item<?>> currentMap = (Map<Integer, DataWatcher.Item<?>>) FieldUtils.readDeclaredField(toCloneDataWatcher, "d", true);
            Map<Integer, DataWatcher.Item<?>> newMap = Maps.newHashMap();

            // We need to clone the DataWatcher.Items because we don't want to point to those values anymore.
            for (Integer integer : currentMap.keySet()) {
                newMap.put(integer, currentMap.get(integer).d()); // Puts a copy of the DataWatcher.Item in newMap
            }

            // Get the 0th index for the BitMask value. http://wiki.vg/Entities#Entity
            DataWatcher.Item item = newMap.get(0);
            byte initialBitMask = (Byte) item.b(); // Gets the initial bitmask/byte value so we don't overwrite anything.
            byte bitMaskIndex = (byte) 6; // The index as specified in wiki.vg/Entities
            if (glow) {
                item.a((byte) (initialBitMask | 1 << bitMaskIndex));
            } else {
                item.a((byte) (initialBitMask & ~(1 << bitMaskIndex))); // Inverts the specified bit from the index.
            }

            // Set the newDataWatcher's (unlinked) map data
            FieldUtils.writeDeclaredField(newDataWatcher, "d", newMap, true);

            PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(glowingPlayer.getEntityId(), newDataWatcher, true);

            ((CraftPlayer) sendPacketPlayer).getHandle().playerConnection.sendPacket(metadataPacket);
        } catch (IllegalAccessException e) { // Catch statement necessary for FieldUtils.readDeclaredField()
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.networkManager.channel.pipeline().addBefore("packet_handler", "something", new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
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
                if (msg instanceof PacketPlayOutPosition) {
//                    PacketPlayOutPosition packetPlayOutPosition = (PacketPlayOutPosition) msg;
//                    Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> set = (Set<PacketPlayOutPosition.EnumPlayerTeleportFlags>) ReflectionUtils.getObject(packetPlayOutPosition, "f");
//                    for (PacketPlayOutPosition.EnumPlayerTeleportFlags flag : set) {
//                        System.out.println(flag.toString());
//                    }
//                    System.out.println(ReflectionUtils.getInt(packetPlayOutPosition, "g"));
                }
//                if (msg instanceof PacketPlayOutEntity.PacketPlayOutRelEntityMove) {
//                    System.out.println("PacketPlayOutRelEntityMove");
//                }
//                if (msg instanceof PacketPlayOutEntity.PacketPlayOutEntityLook) {
//                    System.out.println("PacketPlayOutEntityLook");
//                }
                super.write(ctx, msg, promise);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                super.channelRead(ctx, msg);
            }
        });
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

        if (e.getMessage().equals("/glow")) {
//            setGlowing(e.getPlayer(), Bukkit.getPlayer("Mateothebeast97"), !((CraftPlayer) e.getPlayer()).getHandle().glowing);
            Gson gson = new GsonBuilder().create();
            HashMap<UUID, HashMap<UUID, Vector>> map = new HashMap<>();

            HashMap<UUID, Vector> otherMap = new HashMap<>();
            otherMap.put(e.getPlayer().getUniqueId(), new Vector(2, 2, 300000));

            map.put(e.getPlayer().getUniqueId(), otherMap);
            map.put(UUID.fromString("675c8d83-fbe5-4824-832c-6cadb2ed448d"), otherMap);

            try {
                Files.write(Paths.get("/Users/Jacob/Desktop/text.txt"), gson.toJson(map).getBytes());
            } catch (Exception ee) {
                ee.printStackTrace();
            }


            try {
                byte[] bytes = Files.readAllBytes(Paths.get("/Users/Jacob/Desktop/text.txt"));
                HashMap<UUID, HashMap<UUID, Vector>> mapp = gson.fromJson(new String(bytes), new TypeToken<HashMap<UUID, HashMap<UUID, Vector>>>() {
                }.getType());
                System.out.println(mapp.get(e.getPlayer().getUniqueId()).get(e.getPlayer().getUniqueId()).getZ());
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }

        if (e.getMessage().equals("/spawnPlayer")) {
            String sig = "ItTvKVTrxs7j8HX4BY8ulIQ+y1Y4Aud22XM4TJhbzNEcBLR9gnnBzqs9RC5zZIKHbtQH9HnsFvVwCgQJeLpnyfZlewlkNJB8qrni0Ck3fEbQTgixzSifJSLMuxRJrvl6wxhCtCs3maptEeQMgsNTp9pLbjxlw/vZMKv0qkzcyTygvE4qO49GURW1Up8u3qCsAvvRwPpMqJdqio66yj7jM8sjiDY47F2h/wZfWDTYyuiV0b/DXxOEIXbkabnGiJNH2Y5qM94QYgEjpFXn5H7pLqUnvUkb3C5T83rfvVBrif4JawMccL/ZOAZKtSbK5NHTG9GAqIzB8kZdZ5qNsJINPuP58zEGVGA61qZxlB/uDmF3Wqj3oo93j1HQMQ9mJIMNsCf76RJiJCvPeoPQg16CtJvaJPNmzYdXAz4D7l2ha6z27Ktqx6mCiX6PniLm+32Rxk/fubHTMhSC1PIOT0Gs585Obim8QO3Cz93i7jLCRLZX6QIkxTfhhAl3SFj+4lt+xMPAEFFkFCQPgXFfV2pIBWkABRx4f/Ni24Wdxl8KpyVorxI2cNG4kXiJ5bKBfMGdV/EHLxweyDwwNq0g88EISSOapmgJbUrKRJ1ZA45QSMIgpgRBc4n0CdBqEo2JFv8jVNI6OKwo9zXS/GZJvLEztpK4JeLcGoV8aoiVFQmRESA=";
            String value = "eyJ0aW1lc3RhbXAiOjE1MDEyNzQ3NzYyMDgsInByb2ZpbGVJZCI6IjRkYmZlZmM4NjJkMDRhNDA4Mzk5YmQyODM3M" +
                    "jUyNjgyIiwicHJvZmlsZU5hbWUiOiJQZXRlcnNvaiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InV" +
                    "ybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTllMz" +
                    "M4ZWZlY2Q4NjlmNmVlMjIzMDVjNTk1MDc1YzFkNTI4NTZhODE3OGE5YjdkNDJmNTNlODk4NzI5NWQifX19";
            ReportPlayer reportPlayer = new ReportPlayer(0, e.getPlayer().getUniqueId(), "Petersoj", value, sig);
            RecordedPlayerv1_12 recordedPlayer = new RecordedPlayerv1_12(plugin, reportPlayer, e.getPlayer());
            recordedPlayer.spawn(e.getPlayer().getLocation());


            new BukkitRunnable() {
                int index = 0;

                @Override
                public void run() {
                    recordedPlayer.teleport(e.getPlayer().getLocation().add(1, 0, 0));

                    if (index % 20 == 0) {
                        recordedPlayer.teleport(e.getPlayer().getLocation().add(1, 0, 0));
                        recordedPlayer.setSneaking(true);


                    }
                    index++;
                }
            }.runTaskTimer(plugin, 0, 0);
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
