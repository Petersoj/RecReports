package me.petersoj.view;

import me.petersoj.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * This is a singleton view class that holds the view for reports
 * and can open the view.
 */

public class ReportsView {

    private Inventory reportsViewInventory;
    private ItemStack borderItem;
    private ItemStack nextPageItem;
    private ItemStack previousPageItem;

    public ReportsView() {
        this.createItems();
        this.setupInventory();
    }

    private void createItems() {
        this.borderItem = new ItemBuilder(Material.STAINED_GLASS_PANE).name("").lore("").data(14).build(); // blank Red glass pane

        this.nextPageItem = new ItemBuilder(Material.SKULL_ITEM).name(ChatColor.BOLD + "Next Page")
                .lore("").data(3).build(); // Player Skull
        SkullMeta nextPageMeta = (SkullMeta) nextPageItem.getItemMeta();
        nextPageMeta.setOwner("MHF_ArrowRight");
        this.nextPageItem.setItemMeta(nextPageMeta);

        this.previousPageItem = new ItemBuilder(Material.SKULL_ITEM).name(ChatColor.BOLD + "Previous Page")
                .lore("").data(3).build(); // Player Skull
        SkullMeta previousPageMeta = (SkullMeta) previousPageItem.getItemMeta();
        previousPageMeta.setOwner("MHF_ArrowLeft");
        this.previousPageItem.setItemMeta(previousPageMeta);
    }

    private void setupInventory() {
        if (reportsViewInventory == null) {
            this.reportsViewInventory = Bukkit.createInventory(null, 54, ChatColor.RED + "Reports View");
        }

        // set Top, Bottom, Left, and Right Side
        for (int i = 0; i <= 54; i++) {
            if ((i <= 8) || (i >= 45)) {
                this.reportsViewInventory.setItem(i, borderItem);
            } else if (i % 9 == 0) {
                this.reportsViewInventory.setItem(i, borderItem);
                this.reportsViewInventory.setItem(i + 8, borderItem);
            }
        }
    }

    public void updateReportsView() {

    }

    public Inventory getReportsViewInventory() {
        return reportsViewInventory;
    }
}
