package hypercubemc.com.rps.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Item {

    private HashMap<String, ItemStack> phases;

    public Item(Player player) {

    }

    public void addPhase(String phase, ItemStack item) {
        if (hasPhase(phase)) {
            return;
        }
        phases.put(phase, item);
    }

    public boolean hasPhase(String phase) {
        return phases.containsKey(phase);
    }

    public ItemStack getGetPhase(String phase){
        if (!hasPhase(phase)) {
            return null;
        }
        return phases.get(phase);
    }

}
