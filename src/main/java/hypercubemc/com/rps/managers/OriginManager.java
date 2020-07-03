package hypercubemc.com.rps.managers;

import hypercubemc.com.rps.origins.Origin;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class OriginManager {

    private ArrayList<Origin> origins;

    public OriginManager() {
        origins = new ArrayList<>();
    }

    public void addPlayer(Player player, Origin origin) {
        if (!(getOriginByPlayer(player) == null)) {
            return;
        }
        origins.add(origin);
    }

    public void removePlayer(Player player) {
        if (getOriginByPlayer(player) == null) {
            return;
        }
        for (Origin origin : origins) {
            if (origin.isPlayer(player)) {
                origins.remove(origin);
            }
        }
    }

    public Origin getOriginByPlayer(Player player) {
        for (Origin origin : origins) {
            if (origin.isPlayer(player)) {
                return origin;
            }
        }
        return null;
    }

    public void shutdown() {
        for (Origin origin : origins) {
            origin.shutdown();
        }
    }

}
