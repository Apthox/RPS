package hypercubemc.com.rps.origins;

import hypercubemc.com.rps.RPS;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Arrays;

public class Orion extends Origin {

    public Orion(Player player) {
        super(player);
        type = OriginEnum.ORION;
    }

    @Override
    public void setKit() {
        PlayerInventory inv = this.player.getInventory();
        inv.clear();

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "CUSTOM BOW NAME");
        meta.setLore(Arrays.asList(new String[]{"LORE 1", "LORE 2", "LORE 3"}));
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        bow.setItemMeta(meta);
        bow = addGlow(bow);
        inv.setItem(0, bow);

        ItemStack arrows = new ItemStack(Material.ARROW);
        arrows.setAmount(64);
        inv.setItem(8, arrows);

        ItemStack ThunderStrike = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta TS_meta = (PotionMeta) ThunderStrike.getItemMeta();
        TS_meta.setBasePotionData(new PotionData(PotionType.WATER));
        TS_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        TS_meta.setDisplayName(ChatColor.BLUE + "Enable Thunder Strike");
        ThunderStrike.setItemMeta(TS_meta);
        inv.setItem(7, ThunderStrike);

        ItemStack OrionBolt = new ItemStack(Material.SPECTRAL_ARROW);
        meta = OrionBolt.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Orion Bolt");
        OrionBolt.setItemMeta(meta);
        inv.setItem(6, OrionBolt);

        ItemStack woodenSword = new ItemStack(Material.WOODEN_SWORD);
        meta = woodenSword.getItemMeta();
        meta.addEnchant(Enchantment.KNOCKBACK, 2, false);
        meta.setUnbreakable(true);
        woodenSword.setItemMeta(meta);
        inv.setItem(1, woodenSword);

        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setDisplayName(ChatColor.AQUA + "Lightning Steps");
        potionMeta.setColor(Color.AQUA);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20,2), true);
        potion.setItemMeta(potionMeta);
        inv.setItem(2, potion);

    }

    @Override
    public void onShootBowEvent(EntityShootBowEvent e) {
        player.sendMessage("Bow Event Triggered!");

        Entity entity = e.getProjectile();
        Arrow arrow = entity.getWorld().spawnArrow(entity.getLocation(), entity.getVelocity(), (float) entity.getVelocity().length(), 1);
        e.setCancelled(true);
        player.updateInventory();

        if (hasAttribute("thunderstrike")) {
            thunderStrike(arrow);
        }

        if (hasAttribute("bolt")) {
            bolt(arrow);
        }

    }

    public void bolt(Arrow arrow) {
        removeAttribute("bolt");
        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "The bolt has been fired!");

        arrow.setVelocity(arrow.getVelocity().multiply(.3));
        arrow.setGravity(false);
        arrow.setColor(Color.GREEN);

        new BukkitRunnable(){
            @Override
            public void run() {
                if (arrow.isOnGround() || arrow.isDead() || arrow.getVelocity().length() < .01) {

                    arrow.getLocation().getWorld().strikeLightning(arrow.getLocation());
                    arrow.getLocation().getWorld().createExplosion(arrow.getLocation(), 3, true, false);

                    new BukkitRunnable(){
                        public int count = 10;

                        @Override
                        public void run() {
                            int r_x = (int) (Math.random() * 10) - 5;
                            int r_z = (int) (Math.random() * 10) - 5;
                            Location loc = arrow.getLocation().add(r_x, 0, r_z);
                            loc.getWorld().strikeLightning(loc);
                            count = count - 1;
                            if (count < 1) {
                                cancel();
                            }
                        }
                    }.runTaskTimer(RPS.getInstance(), 0, 10);

                    arrow.remove();
                    cancel();
                }
            }
        }.runTaskTimer(RPS.getInstance(), 0, 3);
    }

    public void thunderStrike(Arrow arrow) {
        boolean charged = arrow.getVelocity().length() > 2.7;
        arrow.setVelocity(arrow.getVelocity().multiply(2));
        arrow.setColor(Color.BLUE);

        new BukkitRunnable() {
            @Override
            public void run() {

                if (arrow.isOnGround() || arrow.isDead() || arrow.getVelocity().length() < .005) {
                    if (charged) {
                        arrow.getLocation().getWorld().strikeLightning(arrow.getLocation());
                        arrow.getLocation().getWorld().createExplosion(arrow.getLocation(), 1, false, false);
                    }
                    arrow.remove();
                    cancel();
                }
            }
        }.runTaskTimer(RPS.getInstance(), 0, 3);
    }

    @Override
    public void onInteractEvent(PlayerInteractEvent e) {
        e.getPlayer().getLocation().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
        ItemStack item = e.getItem();

        if (item == null) {
            return;
        }

        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        if (item.getType().equals(Material.TIPPED_ARROW) ) {

            if (hasAttribute("thunderstrike")) {
                player.sendMessage(ChatColor.RED + "Already has Thunder Strike");
                return;
            }

            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 5, 5);


            player.sendMessage(ChatColor.AQUA + "Enabled Thunder Strike");
            addAttribute("thunderstrike");
            removeAttribute("bolt");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(ChatColor.AQUA + "Thunder Strike has ended!");
                    removeAttribute("thunderstrike");
                }
            }.runTaskLater(RPS.getInstance(), 20 * 8);

            return;
        }

        if (item.getType().equals(Material.SPECTRAL_ARROW)) {
            if (hasAttribute("bolt")) {
                player.sendMessage(ChatColor.RED + "Already has Orion's Bolt");
                return;
            }

            addAttribute("bolt");
            removeAttribute("thunderstrike");

            player.sendMessage(ChatColor.AQUA + "Orion's Bolt has been equipped!");
        }

        if (item.getType().equals(Material.POTION)) {
            addAttribute("steps");

            lightingSteps();
        }
    }

    public void lightingSteps() {
        int interval = 5;
        int duration = 20 * 10;
        int cooldown = 20 * 10;

        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 5);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 2));
        PlayerInventory inv = player.getInventory();
        ItemStack emptyPotion = new ItemStack(Material.GLASS_BOTTLE);
        inv.setItem(2, emptyPotion);

        new BukkitRunnable() {
            public int time_count;

            @Override
            public void run() {
                player.setLevel(time_count / 20);
                player.setExp((float) (time_count / (duration + cooldown + 0.0)));
                if (time_count >= (duration + cooldown)) {
                    ItemStack potion = new ItemStack(Material.POTION);
                    PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                    potionMeta.setDisplayName(ChatColor.AQUA + "Lightning Steps");
                    potionMeta.setColor(Color.AQUA);
                    potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20,2), true);
                    potion.setItemMeta(potionMeta);
                    inv.setItem(2, potion);
                    cancel();
                }
                time_count += interval;
            }
        }.runTaskTimer(RPS.getInstance(), 0, 5);
    }

    @Override
    public void onDropEvent(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onInventoryEvent(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof PlayerInventory) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onPickupEvent(EntityPickupItemEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onHungerEvent(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
}
