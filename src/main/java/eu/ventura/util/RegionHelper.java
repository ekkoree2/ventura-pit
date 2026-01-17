package eu.ventura.util;

import eu.ventura.maps.Map;
import eu.ventura.service.MapService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class RegionHelper {
    public static boolean isInSpawn(Location location) {
        Map map = MapService.getCurrentMap();
        List<Vector> aabb = map.getSpawnAABB();
        if (aabb.isEmpty()) {
            return false;
        }

        Vector min = new Vector(
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                Double.MAX_VALUE
        );

        Vector max = new Vector(
                -Double.MAX_VALUE,
                -Double.MAX_VALUE,
                -Double.MAX_VALUE
        );

        for (Vector vec : aabb) {
            min.setX(Math.min(min.getX(), vec.getX()));
            min.setY(Math.min(min.getY(), vec.getY()));
            min.setZ(Math.min(min.getZ(), vec.getZ()));

            max.setX(Math.max(max.getX(), vec.getX()));
            max.setY(Math.max(max.getY(), vec.getY()));
            max.setZ(Math.max(max.getZ(), vec.getZ()));
        }

        Vector locVec = location.toVector();
        return locVec.isInAABB(min, max);
    }


    public static boolean isInSpawn(Player player) {
        return isInSpawn(player.getLocation());
    }
}
