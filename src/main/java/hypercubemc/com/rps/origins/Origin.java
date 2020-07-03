package hypercubemc.com.rps.origins;


import hypercubemc.com.rps.RPS;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Origin {
    protected OriginEnum type;
    protected Player player;
    protected ArrayList<String> attributes;
    protected ArrayList<String> cooldowns;
    protected HashMap<String, BossBar> bars;

    public Origin(Player player) {
        this.type = OriginEnum.NONE;
        this.player = player;
        this.attributes = new ArrayList<>();
        this.cooldowns = new ArrayList<>();
        this.bars = new HashMap<>();

        setKit();
        player.setFoodLevel(20);
        player.setHealth(20);
        BossBar bossBar = Bukkit.createBossBar("Lighting Steps - 15s", BarColor.BLUE, BarStyle.SOLID);
        bossBar.setProgress(.75);
        bossBar.addPlayer(player);

        BossBar bossBar2 = Bukkit.createBossBar("Thunder Strikes - 30s", BarColor.YELLOW, BarStyle.SOLID);
        bossBar2.setProgress(.5);
        bossBar2.addPlayer(player);
    }

    public ItemStack addGlow(ItemStack item){
        if(item == null) return null;
        item.addUnsafeEnchantment(Enchantment.LUCK, 1);
        ItemMeta  meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public void setKit() {
        this.player.getInventory().clear();
    }

    public OriginEnum getType() {
        return type;
    }

    public boolean isPlayer(Player player) {
        if (this.player.getUniqueId().equals(player.getUniqueId())) {
            return true;
        }
        return false;
    }

    public void addAttribute(String attr) {
        this.attributes.add(attr.toUpperCase());
    }

    public boolean hasAttribute(String attr) {
        return this.attributes.contains(attr.toUpperCase());
    }

    public void removeAttribute(String attr) {
        if (hasAttribute(attr)) {
            attributes.remove(attr.toUpperCase());
        }
    }

    public void addCooldown(String attr, int duration) {
        if (isCoolingDown(attr)) {
            return;
        }
        this.cooldowns.add(attr.toUpperCase());
        if (duration < 1) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isCoolingDown(attr)) {
                    removeCooldown(attr);
                }
            }
        }.runTaskLater(RPS.getInstance(), duration);
    }

    public void shutdown() {
        for (String key : this.bars.keySet()) {
            this.bars.get(key).removeAll();
        }
    }

    public boolean isCoolingDown(String attr) {
        return this.cooldowns.contains(attr.toUpperCase());
    }
    
    public void removeCooldown(String attr) {
        this.cooldowns.remove(attr.toUpperCase());
    }

    public void onShootBowEvent(EntityShootBowEvent e) {}
    public void onInteractEvent(PlayerInteractEvent e) {}
    public void onDropEvent(PlayerDropItemEvent e) {}
    public void onInventoryEvent(InventoryClickEvent e) {}
    public void onPickupEvent(EntityPickupItemEvent e) {}
    public void onHungerEvent(FoodLevelChangeEvent e) {}

}
