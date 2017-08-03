package me.petersoj.nms.players;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.petersoj.RecReportsPlugin;
import me.petersoj.nms.RecordedPlayer;
import me.petersoj.report.ReportPlayer;
import net.minecraft.server.v1_12_R1.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class RecordedPlayerv1_12 extends RecordedPlayer {

    private EntityPlayer entityPlayer;
    private EntityPlayer sendPacketsPlayer;

    private Map<Integer, DataWatcher.Item<?>> dataWatcherMap;

    public RecordedPlayerv1_12(RecReportsPlugin plugin, ReportPlayer reportPlayer, Player sendPacketsPlayer) {
        super(plugin, reportPlayer, sendPacketsPlayer);

        this.sendPacketsPlayer = ((CraftPlayer) sendPacketsPlayer).getHandle();
    }

    @Override
    public void spawn(Location location) {
        if (location == null) {
            throw new NullPointerException("Location cannot be null!");
        } else if (entityPlayer != null) {
            throw new IllegalStateException("RecordedPlayer is already spawned!");
        } else {
            this.createEntityPlayer(location);

            // Add the player to tablist (necessary for client in order to receive skin data)
            PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
            this.sendPacket(playerInfo);

            // Spawn the actual player
            PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
            this.sendPacket(namedEntitySpawn);

            // Remove the player from the clients tablist
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Removes from tablist, but the entity still exists on the client.
                    PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(
                            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
                    sendPacket(playerInfo);
                }
            }.runTaskLater(super.getPlugin(), 20); // 1 second later
        }
    }

    @SuppressWarnings("unchecked")
    private void createEntityPlayer(Location location) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        PlayerInteractManager interactManager = new PlayerInteractManager(worldServer);

        // Create the GameProfile and add the skin texture to it.
        GameProfile gameProfile = new GameProfile(getReportPlayer().getUUID(), getReportPlayer().getPlayerName());
        Property texture = new Property("textures", getReportPlayer().getSkinProfileValue(), getReportPlayer().getSkinProfileSignature());
        gameProfile.getProperties().put("textures", texture);

        this.entityPlayer = new EntityPlayer(server, worldServer, gameProfile, interactManager);
        this.entityPlayer.locX = location.getX();
        this.entityPlayer.locY = location.getY();
        this.entityPlayer.locZ = location.getZ();
        this.entityPlayer.yaw = location.getYaw();
        this.entityPlayer.pitch = location.getPitch();

        // Setup Data Watcher
        try {
            dataWatcherMap = (Map<Integer, DataWatcher.Item<?>>) FieldUtils.readDeclaredField(entityPlayer.getDataWatcher(), "d", true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void despawn() {
        if (entityPlayer == null) {
            throw new IllegalStateException("RecordedPlayer has not been spawned!");
        }

        PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy(entityPlayer.getId());
        this.sendPacket(entityDestroy);
    }

    @Override
    public void look(float yaw, float pitch) {
        // Convert floats to bytes
        byte byteYaw = (byte) ((int) (yaw * 256.0F / 360.0F));
        byte bytePitch = (byte) ((int) (pitch * 256.0F / 360.0F));

        // Set these in EntityPlayer for later use
        this.entityPlayer.yaw = yaw;
        this.entityPlayer.pitch = pitch;

        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket =
                new PacketPlayOutEntity.PacketPlayOutEntityLook(entityPlayer.getId(), byteYaw, bytePitch, isOnGround());
        this.sendPacket(lookPacket);
        this.sendHeadLook(byteYaw);
    }

    @Override
    public void moveTo(Location location) {
        if (location == null) {
            throw new NullPointerException("Location cannot be null!");
        }

        // Get previously set location from entityPlayer
        Location prevLocation = new Location(location.getWorld(), entityPlayer.locX, entityPlayer.locY, entityPlayer.locZ);

        if (location.distanceSquared(prevLocation) >= 16) { // Don't send RelMove if location is greater than 4 blocks.
            this.teleport(location); // Teleport instead of RelEntityMove
        } else {

            int deltaX = (int) (location.getX() * 32 - prevLocation.getX() * 32) * 128;
            int deltaY = (int) (location.getY() * 32 - prevLocation.getY() * 32) * 128;
            int deltaZ = (int) (location.getZ() * 32 - prevLocation.getZ() * 32) * 128;

            // Set new Location in EntityPlayer now that we have our values stored
            this.entityPlayer.locX = location.getX();
            this.entityPlayer.locY = location.getY();
            this.entityPlayer.locZ = location.getZ();

            if (entityPlayer.yaw != location.getYaw() || entityPlayer.pitch != location.getPitch()) {

                byte yaw = (byte) ((int) (location.getYaw() * 256.0F / 360.0F));
                byte pitch = (byte) ((int) (location.getPitch() * 256.0F / 360.0F));

                PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook moveLookPacket =
                        new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(entityPlayer.getId(), deltaX, deltaY, deltaZ, yaw, pitch, isOnGround());

                this.sendPacket(moveLookPacket);
                this.sendHeadLook(yaw);

                // Set new direction in EntityPlayer
                this.entityPlayer.yaw = location.getYaw();
                this.entityPlayer.pitch = location.getPitch();
            } else {
                PacketPlayOutEntity.PacketPlayOutRelEntityMove movePacket =
                        new PacketPlayOutEntity.PacketPlayOutRelEntityMove(entityPlayer.getId(), deltaX, deltaY, deltaZ, isOnGround());
                this.sendPacket(movePacket);
            }
        }
    }

    @Override
    public void teleport(Location location) {
        if (location == null) {
            throw new NullPointerException("Location cannot be null!");
        }

        this.entityPlayer.locX = location.getX();
        this.entityPlayer.locY = location.getY();
        this.entityPlayer.locZ = location.getZ();
        this.entityPlayer.yaw = location.getYaw();
        this.entityPlayer.pitch = location.getPitch();
        this.entityPlayer.onGround = isOnGround();

        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(entityPlayer);
        this.sendPacket(teleportPacket);
        this.look(location.getYaw(), location.getPitch());
    }

    @Override
    public void changeWorld(Location newWorld) {
        if (newWorld == null) {
            throw new NullPointerException("New World Location cannot be null!");
        }

        this.despawn();
        this.spawn(newWorld);
    }

    @Override
    public void setSneaking(boolean sneak) {
        this.setByteFlag(1, sneak);
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), true);
        sendPacket(metadataPacket);
    }

    @Override
    public void setOnFire(boolean onFire) {
        this.setByteFlag(0, onFire);
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), true);
        sendPacket(metadataPacket);
    }

    @Override
    public void swingArm() {
        PacketPlayOutAnimation animationPacket = new PacketPlayOutAnimation(entityPlayer, 0);
        sendPacket(animationPacket);
    }

    @Override
    public void doDamageAnimation() {
        PacketPlayOutAnimation animationPacket = new PacketPlayOutAnimation(entityPlayer, 1);
        sendPacket(animationPacket);
    }

    @Override
    public void setEquipment(int recordedSlotNumber, ItemStack item) {
        if (item == null) {
            throw new NullPointerException("The Item cannot be null!");
        }

        PacketPlayOutEntityEquipment equipmentPacket = null;

        // .c() is the number used in 1.8 I believe
        if (recordedSlotNumber == EnumItemSlot.MAINHAND.c()) {
            equipmentPacket = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(item));
        } else if (recordedSlotNumber == EnumItemSlot.OFFHAND.c()) {
            equipmentPacket = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(item));
        } else if (recordedSlotNumber == EnumItemSlot.FEET.c()) {
            equipmentPacket = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(item));
        } else if (recordedSlotNumber == EnumItemSlot.LEGS.c()) {
            equipmentPacket = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(item));
        } else if (recordedSlotNumber == EnumItemSlot.CHEST.c()) {
            equipmentPacket = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(item));
        } else if (recordedSlotNumber == EnumItemSlot.HEAD.c()) {
            equipmentPacket = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(item));
        }

        sendPacket(equipmentPacket);
    }

    private void sendHeadLook(byte yaw) {
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(entityPlayer, yaw);
        sendPacket(headRotationPacket);
    }

    private boolean isOnGround() {
        return entityPlayer.locY % 1 == 0;
    }

    @SuppressWarnings("unchecked")
    private void setByteFlag(int bitMaskIndex, boolean value) {
        DataWatcher.Item item = dataWatcherMap.get(0);
        byte initialBitMask = (Byte) item.b(); // Gets the initial bitmask/byte value so we don't overwrite anything.

        if (value) {
            item.a((byte) (initialBitMask | 1 << bitMaskIndex));
        } else {
            item.a((byte) (initialBitMask & ~(1 << bitMaskIndex))); // Inverts the specified bit from the index.
        }
    }

    private void sendPacket(Packet packet) {
        if (packet != null) {
            sendPacketsPlayer.playerConnection.sendPacket(packet);
        }
    }
}
