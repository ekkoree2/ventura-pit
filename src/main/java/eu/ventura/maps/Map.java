package eu.ventura.maps;

import eu.ventura.constants.PitHologram;
import eu.ventura.constants.PitMap;
import eu.ventura.constants.PitNPCs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/14/2026
 */
@SuppressWarnings("all")
public interface Map {
    List<Vector> getSpawnAABB();

    List<PitLocation> getSpawnLocations();

    PitMap getMap();

    Hologram[] getHolograms();

    Npc[] getNpcs();

    @RequiredArgsConstructor
    @Getter
    public static class Hologram {
        private final PitHologram pitHologram;
        private final PitLocation location;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Npc {
        private final PitNPCs pitNpc;
        private final PitLocation location;
    }

    @RequiredArgsConstructor
    public static class PitLocation {
        private final double x;
        private final double y;
        private final double z;
        private final float yaw;
        private final float pitch;

        public Location of() {
            World world = Bukkit.getWorlds().getFirst();
            return new Location(
                    world,
                    this.x,
                    this.y,
                    this.z,
                    this.yaw,
                    this.pitch
            );
        }
    }
}
