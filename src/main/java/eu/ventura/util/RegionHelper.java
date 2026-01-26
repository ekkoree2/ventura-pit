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

    public static boolean isInRadiusOfBlock(Player player, org.bukkit.Material block, int radius) {
        org.bukkit.block.Block center = player.getLocation().add(0.0, -1.0, 0.0).getBlock();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    org.bukkit.block.Block b = center.getRelative(x, y, z);
                    if (center.getLocation().distance(b.getLocation()) <= radius && b.getType() == block) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getPlayerQuadrant(Player player) {
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        if (x >= 0.0 && z >= 0.0) {
            return 1;
        }
        if (x <= 0.0 && z >= 0.0) {
            return 2;
        }
        if (x >= 0.0 && z <= 0.0) {
            return 3;
        }
        if (x <= 0.0 && z <= 0.0) {
            return 4;
        }
        return 0;
    }
}
