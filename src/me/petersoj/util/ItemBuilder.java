package me.petersoj.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * Builder pattern for an {@link ItemStack}.
 *
 * @author Lukas Nehrke (edited by Jacob Peterson)
 * @version 1.0
 */
public final class ItemBuilder {

    private Material material = Material.AIR;
    private int amount = 1;
    private MaterialData data;
    private short durability = -1;
    private String name;
    private String localizedName;
    private boolean unbreakable = false;
    private ItemFlag[] flags;
    private List<String> lore;

    // Default empty constructor choice
    public ItemBuilder() {
    }

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder data(MaterialData data) {
        this.data = data;
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder data(int data) {
        this.data = new MaterialData(material);
        this.data.setData((byte) data);
        return this;
    }

    public ItemBuilder durability(short durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder localizedName(String localizedName) {
        this.localizedName = localizedName;
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        return this;
    }

    public ItemBuilder flags(Collection<ItemFlag> flags) {
        this.flags = flags.toArray(new ItemFlag[flags.size()]);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemStack build() {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        item.setAmount(amount);
        if (data != null) {
            item.setData(data);
        }
        if (durability > 0) {
            item.setDurability(durability);
        }
        if (name != null) {
            meta.setDisplayName(name);
        }
//        Below Removed for 1.8 compatibility
//        if (localizedName != null) {
//            meta.setLocalizedName(localizedName);
//        }
//        meta.setUnbreakable(unbreakable);
        if (flags != null) {
            meta.addItemFlags(flags);
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }
}