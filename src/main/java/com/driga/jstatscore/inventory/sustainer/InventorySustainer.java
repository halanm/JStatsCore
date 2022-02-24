package com.driga.jstatscore.inventory.sustainer;

import com.driga.jstatscore.inventory.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InventorySustainer {
    private String nameInventory;
    private MenuSize menuSize;
    private InventoryWrapper inventoryWrapper;
    private InventoryHandler inventoryHandler;

    public static void register(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)new Listener() {
            private boolean isWrapper(Inventory inventory) {
                return inventory.getHolder() instanceof InventoryWrapper;
            }

            private InventoryWrapper getWrapper(Inventory inventory) {
                return (InventoryWrapper)inventory.getHolder();
            }

            @EventHandler(priority = EventPriority.HIGH)
            private void onClick(InventoryClickEvent e) {
                if (!this.isWrapper(e.getInventory())) {
                    return;
                }
                InventoryWrapper inventoryWrapper = this.getWrapper(e.getInventory());
                int slot = e.getSlot();
                e.setCancelled(true);
                try {
                    if (inventoryWrapper.getClickServices()[slot] == null) {
                        return;
                    }
                }catch (ArrayIndexOutOfBoundsException ex){
                    return;
                }
                if(!e.getSlotType().equals(InventoryType.SlotType.CONTAINER)){
                    return;
                }
                inventoryWrapper.getClickServices()[slot].applyEvent(e);
            }

            @EventHandler(priority = EventPriority.HIGH)
            private void onOpen(InventoryOpenEvent e) {
                if (!this.isWrapper(e.getInventory())) {
                    return;
                }
                InventoryWrapper wrapper = this.getWrapper(e.getInventory());
                if (wrapper.getOpenService() == null) {
                    return;
                }
                wrapper.getOpenService().applyEvent(e);
            }

            @EventHandler(priority = EventPriority.HIGH)
            private void onClose(InventoryCloseEvent e) {
                if (!this.isWrapper(e.getInventory())) {
                    return;
                }
                InventoryWrapper wrapper = this.getWrapper(e.getInventory());
                if (wrapper.getCloseService() == null) {
                    return;
                }
                wrapper.getCloseService().applyEvent(e);
            }
        }, plugin);
    }

    public void open(HumanEntity entity) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)this.inventoryWrapper, this.menuSize.getSlot(), this.nameInventory);
        this.inventoryWrapper.setInventory(inventory);
        ItemBuilder[] builders = this.inventoryWrapper.getBuilders();
        for (int i = 0; i != builders.length; ++i) {
            if (builders[i] != null) {
                inventory.setItem(i, builders[i].build());
            }
        }
        entity.openInventory(inventory);
    }

    public InventoryHandler getHandler() {
        return this.inventoryHandler;
    }

    public InventorySustainer(String nameInventory, MenuSize size) {
        this.nameInventory = nameInventory;
        this.menuSize = size;
        this.inventoryHandler = new InventoryHandler();
        this.inventoryWrapper = new InventoryWrapper(size);
    }

    public InventorySustainer(MenuSize size) {
        this("Chest", size);
    }

    public InventoryWrapper getWrapper() {
        return this.inventoryWrapper;
    }

    public void setItem(int slot, ItemBuilder builder, ClickService service) {
        this.inventoryWrapper.applyClick(slot, builder, service);
    }

    public void setItem(int slot, ItemStack itemStack) {
        this.getWrapper().getInventory().setItem(slot, itemStack);
    }

    public void setItem(int slot, ItemBuilder itemBuilder) {
        this.setItem(slot, itemBuilder, null);
    }

    public enum MenuSize
    {
        ONE_LINE(9),
        TWO_LINES(18),
        THREE_LINES(27),
        FOUR_LINES(36),
        FIVE_LINES(45),
        SIX_LINES(54);

        private int slot;

        private MenuSize(int size) {
            this.slot = size;
        }

        public int getSlot() {
            return this.slot;
        }

        public static MenuSize fromSize(int slot) {
            for (MenuSize size : values()) {
                if (size.getSlot() == slot) {
                    return size;
                }
            }
            return null;
        }
    }

    public static class InventoryWrapper implements InventoryHolder
    {
        private Inventory inventory;
        private ClickService[] clickServices;
        private ItemBuilder[] builders;
        private CloseService closeService;
        private OpenService openService;

        protected CloseService getCloseService() {
            return this.closeService;
        }

        protected OpenService getOpenService() {
            return this.openService;
        }

        public void applyClose(CloseService closeService) {
            this.closeService = closeService;
        }

        public void applyOpen(OpenService openService) {
            this.openService = openService;
        }

        protected void applyClick(int slot, ItemBuilder itemBuilder, ClickService service) {
            this.builders[slot] = itemBuilder;
            this.clickServices[slot] = service;
        }

        public InventoryWrapper(MenuSize menuSize) {
            this.clickServices = new ClickService[menuSize.getSlot()];
            this.builders = new ItemBuilder[menuSize.getSlot()];
        }

        public ItemBuilder[] getBuilders() {
            return this.builders;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        public ClickService[] getClickServices() {
            return this.clickServices;
        }

        public Inventory getInventory() {
            return this.inventory;
        }
    }

    public static class InventoryHandler
    {
        private Object[] objects;

        public Object[] getObjects() {
            return this.objects;
        }

        public void setObjects(Object... obj) {
            this.objects = obj;
        }
    }

    protected interface OpenService
    {
        void applyEvent(InventoryOpenEvent p0);
    }

    protected interface CloseService
    {
        void applyEvent(InventoryCloseEvent p0);
    }

    protected interface ClickService
    {
        void applyEvent(InventoryClickEvent p0);
    }
}

