package me.petersoj.nms.players;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.petersoj.RecReportsPlugin;
import me.petersoj.nms.RecordedPlayer;
import me.petersoj.report.ReportPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

// http://web.archive.org/web/20140910111231/http://wiki.vg/Protocol
public class RecordedPlayerv1_8_8 extends RecordedPlayer {

    private EntityPlayer entityPlayer;
    private EntityPlayer sendPacketsPlayer;

    public RecordedPlayerv1_8_8(RecReportsPlugin plugin, ReportPlayer reportPlayer, Player sendPacketsPlayer) {
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

    private void createEntityPlayer(Location location) {
        MinecraftServer server = MinecraftServer.getServer();
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

            byte deltaX = (byte) ((int) MathHelper.floor((location.getX() - prevLocation.getX()) * 32));
            byte deltaY = (byte) ((int) MathHelper.floor((location.getY() - prevLocation.getY()) * 32));
            byte deltaZ = (byte) ((int) MathHelper.floor((location.getZ() - prevLocation.getZ()) * 32));

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
    }

    @Override
    public void setSneaking(boolean sneak) {
        entityPlayer.setSneaking(sneak);
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), true);
        sendPacket(metadataPacket);
    }

    @Override
    public void swingArm() {
        PacketPlayOutAnimation animationPacket = new PacketPlayOutAnimation(entityPlayer, 0);
        sendPacket(animationPacket);
    }

    @Override
    public void setOnFire(boolean onFire) {
        // Stolen from Entity.class :)
        byte b0 = entityPlayer.getDataWatcher().getByte(0);
        if (onFire) {
            entityPlayer.getDataWatcher().watch(0, (byte) (b0 | 1 << 0));
        } else {
            entityPlayer.getDataWatcher().watch(0, (byte) (b0 & ~(1 << 0)));
        }

        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), true);
        sendPacket(metadataPacket);
    }

    @Override
    public void setMainHandItem(ItemStack item) {
        if (item == null) {
            throw new NullPointerException("The Item cannot be null!");
        }

        PacketPlayOutEntityEquipment equipmentPacket =
                new PacketPlayOutEntityEquipment(entityPlayer.getId(), 0, CraftItemStack.asNMSCopy(item));
        sendPacket(equipmentPacket);
    }

    @Override
    public void setOffHandItemv1_12(ItemStack item) {
        throw new UnsupportedClassVersionError("Bad version call! 1.8 doesn't have offhands!");
    }

    @Override
    public void setHelmet(ItemStack item) {
        if (item == null) {
            throw new NullPointerException("The Item cannot be null!");
        }

        PacketPlayOutEntityEquipment equipmentPacket =
                new PacketPlayOutEntityEquipment(entityPlayer.getId(), 4, CraftItemStack.asNMSCopy(item));
        sendPacket(equipmentPacket);
    }

    @Override
    public void setChestplate(ItemStack item) {
        if (item == null) {
            throw new NullPointerException("The Item cannot be null!");
        }

        PacketPlayOutEntityEquipment equipmentPacket =
                new PacketPlayOutEntityEquipment(entityPlayer.getId(), 3, CraftItemStack.asNMSCopy(item));
        sendPacket(equipmentPacket);
    }

    @Override
    public void setLeggings(ItemStack item) {
        if (item == null) {
            throw new NullPointerException("The Item cannot be null!");
        }

        PacketPlayOutEntityEquipment equipmentPacket =
                new PacketPlayOutEntityEquipment(entityPlayer.getId(), 2, CraftItemStack.asNMSCopy(item));
        sendPacket(equipmentPacket);
    }

    @Override
    public void setBoots(ItemStack item) {
        if (item == null) {
            throw new NullPointerException("The Item cannot be null!");
        }

        PacketPlayOutEntityEquipment equipmentPacket =
                new PacketPlayOutEntityEquipment(entityPlayer.getId(), 1, CraftItemStack.asNMSCopy(item));
        sendPacket(equipmentPacket);
    }

    private void sendHeadLook(byte yaw) {
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(entityPlayer, yaw);
        sendPacket(headRotationPacket);
    }

    private boolean isOnGround() {
        return entityPlayer.locY % 1 == 0;
    }

    private void sendPacket(Packet packet) {
        sendPacketsPlayer.playerConnection.sendPacket(packet);
    }
}
