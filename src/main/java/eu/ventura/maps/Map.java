package eu.ventura.maps;

import eu.ventura.constants.PitHologram;
import eu.ventura.constants.PitMap;
import eu.ventura.constants.PitNPC;
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

    List<Vector> getMiddleAABB();

    PitMap getMap();

    Hologram[] getHolograms();

    Npc[] getNpcs();

    List<PitLocation> getRagePitGlass();

    PitLocation getLeaderboardLocation();

    PitLocation[] getLauncherTargets(int quadrant);

    @RequiredArgsConstructor
    @Getter
    public static class Hologram {
        private final PitHologram pitHologram;
        private final PitLocation location;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Npc {
        private final PitNPC pitNpc;
        private final PitLocation location;
    }

    public static class PitLocation {
        private final double x;
        private final double y;
        private final double z;
        private final float yaw;
        private final float pitch;

        public PitLocation(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = 0;
            this.pitch = 0;
        }

        public PitLocation(double x, double y, double z, float yaw, float pitch) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }

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
