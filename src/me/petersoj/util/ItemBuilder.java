package me.petersoj.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Builder pattern for an {@link ItemStack}.
 *
 * @author Lukas Nehrke
 * @version 1.0
 */
public final class ItemBuilder {

    private Material material;
    private Integer amount;
    private MaterialData data;
    private Short durability;
    private String name;
    private String localizedName;
    private Boolean unbreakable = false;
    private ItemFlag[] flags;
    private String[] lore;

    public ItemBuilder() {
    }

    public ItemBuilder material(Material material) {
        this.material = checkNotNull(material);
        return this;
    }

    public ItemBuilder amount(int amount) {
        checkArgument(amount > 0);
        this.amount = amount;
        return this;
    }

    public ItemBuilder data(MaterialData data) {
        this.data = checkNotNull(data);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder data(byte data) {
        this.data = new MaterialData(data);
        return this;
    }

    public ItemBuilder durability(short durability) {
        checkArgument(durability > 0);
        this.durability = durability;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = checkNotNull(name);
        return this;
    }

    public ItemBuilder localizedName(String localizedName) {
        this.localizedName = checkNotNull(localizedName);
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        this.flags = checkNotNull(flags);
        return this;
    }

    public ItemBuilder flags(Collection<ItemFlag> flags) {
        this.flags = flags.toArray(new ItemFlag[flags.size()]);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        this.lore = checkNotNull(lore);
        return this;
    }

    public ItemBuilder lore(Collection<String> lore) {
        this.lore = lore.toArray(new String[lore.size()]);
        return this;
    }

    public ItemStack build() {
        final ItemStack item = new ItemStack(checkNotNull(material, "Material cannot be null"));
        final ItemMeta meta = item.getItemMeta();
        if (amount != null) {
            item.setAmount(amount);
        }
        if (data != null) {
            item.setData(data);
        }
        if (durability != null) {
            item.setDurability(durability);
        }
        if (name != null) {
            meta.setDisplayName(name);
        }
        if (localizedName != null) {
            meta.setLocalizedName(localizedName);
        }
        if (unbreakable != null) {
            meta.setUnbreakable(unbreakable);
        }
        if (flags != null) {
            meta.addItemFlags(flags);
        }
        if (lore != null) {
            meta.setLore(Arrays.asList(lore));
        }
        item.setItemMeta(meta);
        return item;
    }
}