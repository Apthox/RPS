package hypercubemc.com.rps;

import hypercubemc.com.rps.listeners.Listeners;
import hypercubemc.com.rps.managers.OriginManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPS extends JavaPlugin implements Listener {
    private static RPS plugin_instance;
    private static OriginManager originManager;

    public static RPS getInstance() {
        return plugin_instance;
    }

    public static OriginManager getOriginManager() {
        return originManager;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        plugin_instance = this;
        originManager = new OriginManager();
        this.getCommand("RPS").setExecutor(new hypercubemc.com.rps.commands.RPS());
        this.getCommand("RPS").setTabCompleter(new hypercubemc.com.rps.commands.RPS());
    }

    @Override
    public void onDisable() {
        Bukkit.broadcastMessage("RPS - Shutting down message!");
        originManager.shutdown();
    }
}
