package hypercubemc.com.rps.listeners;

import hypercubemc.com.rps.RPS;
import hypercubemc.com.rps.managers.OriginManager;
import hypercubemc.com.rps.origins.Origin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public class Listeners implements Listener {

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent e) {
        Entity entity = e.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;

        OriginManager manager = RPS.getOriginManager();
        Origin origin = manager.getOriginByPlayer(player);

        if (origin == null)
            return;

        origin.onShootBowEvent(e);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        OriginManager manager = RPS.getOriginManager();
        Origin origin = manager.getOriginByPlayer(player);

        if (origin == null)
            return;

        origin.onInteractEvent(e);
    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent e) {

        Player player = e.getPlayer();

        OriginManager manager = RPS.getOriginManager();
        Origin origin = manager.getOriginByPlayer(player);

        if (origin == null)
            return;

        origin.onDropEvent(e);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        e.getWhoClicked().sendMessage("triggered!");
        Player player = (Player) e.getWhoClicked();

        OriginManager manager = RPS.getOriginManager();
        Origin origin = manager.getOriginByPlayer(player);

        if (origin == null)
            return;

        origin.onInventoryEvent(e);
    }

    @EventHandler
    public void onPickupEvent(EntityPickupItemEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;

            OriginManager manager = RPS.getOriginManager();
            Origin origin = manager.getOriginByPlayer(player);

            if (origin == null)
                return;

            origin.onPickupEvent(e);
        }
    }

    @EventHandler
    public void onHungerEvent(FoodLevelChangeEvent e) {
        Entity entity = e.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;

        OriginManager manager = RPS.getOriginManager();
        Origin origin = manager.getOriginByPlayer(player);

        if (origin == null)
            return;

        origin.onHungerEvent(e);
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e) {

    }

}
