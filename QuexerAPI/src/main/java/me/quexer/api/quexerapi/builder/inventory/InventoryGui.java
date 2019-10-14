/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.quexer.api.quexerapi.builder.inventory;

import java.util.function.Consumer;
import me.quexer.api.quexerapi.QuexerAPI;
import me.quexer.api.quexerapi.event.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author Elija
 */
public class InventoryGui {
    
    private final QuexerAPI plugin;

    private final GuiItem[] guiItems;
    private final String name;
    private final Consumer<Player> closeConsumer;

    private Inventory inventory;
    private boolean destroy, destroyOnClose;

    private EventManager.EventListener<InventoryClickEvent> clickListener;
    private EventManager.EventListener<InventoryCloseEvent> closeListener;

    public InventoryGui(QuexerAPI plugin, GuiItem[] guiItems, String name, Inventory inventory, Consumer<Player> closeConsumer) {
        this.plugin = plugin;
        this.guiItems = guiItems;
        this.name = name;
        this.closeConsumer = closeConsumer;

        createInventory(inventory);
        initListener();
    }

    //<editor-fold defaultstate="collapsed" desc="initListener">
    private void initListener() {

        //<editor-fold defaultstate="collapsed" desc="InventoryClickEvent">
        clickListener = (InventoryClickEvent event) -> {
            if (event.getClick().isShiftClick() && event.getInventory().equals(this.inventory)) {
                event.setCancelled(true);
                return;
            }
            if (!event.getWhoClicked().getOpenInventory().getTopInventory().equals(this.inventory))
                return;
            if (event.getClickedInventory() == null || event.getWhoClicked() == null)
                return;
            if (event.getClickedInventory().equals(event.getWhoClicked().getInventory()))
                return;
            event.setCancelled(true);

            for (GuiItem item : this.guiItems) {
                if (item == null || (!item.compareItems(event.getCurrentItem())))
                    continue;
                item.click((Player) event.getWhoClicked());
                break;
            }
        };
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="InventoryCloseEvent">
        closeListener = (InventoryCloseEvent event) -> {
            Player player = (Player) event.getPlayer();

            if (destroy || !event.getInventory().equals(this.inventory))
                return;
            Bukkit.getScheduler().runTaskAsynchronously(plugin.getInstance(), () -> {
                if (closeConsumer != null)
                    closeConsumer.accept(player);
            });
            if (!destroyOnClose)
                return;
            destroy();
        };
        //</editor-fold>

        plugin.getEventManager().registerEvent(InventoryClickEvent.class, clickListener);
        plugin.getEventManager().registerEvent(InventoryCloseEvent.class, closeListener);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createInventory">
    private void createInventory(Inventory inventory) {
        this.inventory = (inventory == null ? Bukkit.createInventory(null, guiItems.length, name) : inventory);

        for (int i = 0; i < guiItems.length; i++) {
            GuiItem item = guiItems[i];
            if (item == null)
                continue;
            this.inventory.setItem(i, item.getItemStack().clone());
            item.setInventory(this);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setItem">
    public void setItem(int slot, GuiItem guiItem) {
        guiItems[slot] = guiItem;
        this.inventory.setItem(slot, guiItem.getItemStack().clone());
        guiItem.setInventory(this);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="removeItem">
    public void removeItem(GuiItem guiItem) {
        for(int i = 0; i < this.guiItems.length; i++) {
            if(this.guiItems[i] != guiItem)
                continue;
            this.guiItems[i] = null;
            this.inventory.remove(guiItem.getItemStack());
            guiItem.setInventory(null);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="destroy">
    public InventoryGui destroy() {
        destroy = true;
        plugin.getEventManager().registerEvent(InventoryClickEvent.class, clickListener);
        plugin.getEventManager().unregisterEvent(InventoryCloseEvent.class, closeListener);
        return this;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="update">
    public InventoryGui update(GuiItem guiItem) {
        for (int i = 0; i < this.guiItems.length; i++) {
            GuiItem item = this.guiItems[i];
            if (!guiItem.equals(item))
                continue;
            this.inventory.setItem(i, item.getItemStack().clone());
        }
        return this;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="open">
    public InventoryGui open(Player player) {
        player.openInventory(this.inventory);
        return this;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="isDestroyed">
    public boolean isDestroyed() {
        return destroy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setDestroyOnClose">
    public InventoryGui setDestroyOnClose(boolean destroyOnClose) {
        this.destroyOnClose = destroyOnClose;
        return this;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="isDestroyOnClose">
    public boolean isDestroyOnClose() {
        return destroyOnClose;
    }
    //</editor-fold>

    
}
