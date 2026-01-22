package eu.ventura.maps.impl;

import eu.ventura.constants.PitHologram;
import eu.ventura.constants.PitMap;
import eu.ventura.constants.PitNPC;
import eu.ventura.maps.Map;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/14/2026
 */
public class KingsMap implements Map {
    @Override
    public List<Vector> getSpawnAABB() {
        return List.of(
            new Vector(24.5, 93, -24.5),
            new Vector(0.5, 93, -23.5),
            new Vector(-24.5, 93, -24.5),
            new Vector(-23.5, 93, 0.5),
            new Vector(-24.5, 93, 24.5),
            new Vector(0.5, 93, 23.5),
            new Vector(24.5, 93, 24.5),
            new Vector(23.5, 93, 0.5),
            new Vector(0.5, 200, 0.5)
        );
    }

    @Override
    public PitMap getMap() {
        return PitMap.KINGS;
    }

    @Override
    public Hologram[] getHolograms() {
        return new Hologram[]{
                new Hologram(PitHologram.ENDER_CHEST, new PitLocation(-10.5, 96, 6.5, -90, 0))
        };
    }

    @Override
    public Npc[] getNpcs() {
        return new Npc[]{
                new Npc(PitNPC.PERKS_NPC, new PitLocation(-1.5, 95, 12.5, 180, 0)),
                new Npc(PitNPC.SHOP_NPC, new PitLocation(2.5, 95, 12.5, 180, 0)),
                new Npc(PitNPC.PRESTIGE, new PitLocation(0.5, 96, -12.5, 0, 0)),
        };
    }

    @Override
    public List<PitLocation> getSpawnLocations() {
        return List.of(
                new PitLocation(0.0, 95.0, 9.0, 180.0f, -0.0f),
                new PitLocation(9.0, 95.0, -9.0, 45.0f, -0.0f),
                new PitLocation(9.0, 95.0, 9.0, 0.0f, 0.0f),
                new PitLocation(-9.0, 95.0, 9.0, 0.0f, 0.0f),
                new PitLocation(-9.0, 95.0, -9.0, 0.0f, 0.0f),
                new PitLocation(9.0, 95.0, 0.0, 0.0f, 0.0f),
                new PitLocation(0.0, 95.0, 9.0, 0.0f, 0.0f),
                new PitLocation(0.0, 95.0, -9.0, 0.0f, 0.0f),
                new PitLocation(-9.0, 95.0, 0.0, 270.0f, -0.0f)
        );
    }

    @Override
    public PitLocation getLeaderboardLocation() {
        return new PitLocation(14.5, 99, 0.5);
    }

    @Override
    public List<PitLocation> getRagePitGlass() {
        return List.of(
                new PitLocation(-13.5, 71, 20.5),
                new PitLocation(-12.5, 71, 20.5),
                new PitLocation(-11.5, 71, 20.5),

                new PitLocation(3.5, 71, 21.5),
                new PitLocation(2.5, 71, 21.5),
                new PitLocation(1.5, 71, 21.5),
                new PitLocation(0.5, 71, 21.5),
                new PitLocation(-1.5, 71, 21.5),
                new PitLocation(-2.5, 71, 21.5),

                new PitLocation(14.5, 71, 20.5),
                new PitLocation(13.5, 71, 20.5),
                new PitLocation(12.5, 71, 20.5),

                new PitLocation(20.5, 71, 12.5),
                new PitLocation(20.5, 71, 13.5),
                new PitLocation(20.5, 71, 14.5),

                new PitLocation(21.5, 71, 3.5),
                new PitLocation(21.5, 71, 2.5),
                new PitLocation(21.5, 71, 1.5),
                new PitLocation(21.5, 71, 0.5),
                new PitLocation(21.5, 71, -1.5),
                new PitLocation(21.5, 71, -2.5),

                new PitLocation(20.5, 71, -11.5),
                new PitLocation(20.5, 71, -12.5),
                new PitLocation(20.5, 71, -13.5),

                new PitLocation(12.5, 71, -19.5),
                new PitLocation(13.5, 71, -19.5),
                new PitLocation(14.5, 71, -19.5),

                new PitLocation(3.5, 71, -20.5),
                new PitLocation(2.5, 71, -20.5),
                new PitLocation(1.5, 71, -20.5),
                new PitLocation(0.5, 71, -20.5),
                new PitLocation(-1.5, 71, -20.5),
                new PitLocation(-2.5, 71, -20.5),

                new PitLocation(-13.5, 71, -20.5),
                new PitLocation(-12.5, 71, -20.5),
                new PitLocation(-11.5, 71, -20.5),

                new PitLocation(-19.5, 71, -11.5),
                new PitLocation(-19.5, 71, -12.5),
                new PitLocation(-19.5, 71, -13.5),

                new PitLocation(-20.5, 71, 3.5),
                new PitLocation(-20.5, 71, 2.5),
                new PitLocation(-20.5, 71, 1.5),
                new PitLocation(-20.5, 71, 0.5),
                new PitLocation(-20.5, 71, -1.5),
                new PitLocation(-20.5, 71, -2.5),

                new PitLocation(-19.5, 71, 14.5),
                new PitLocation(-19.5, 71, 13.5),
                new PitLocation(-19.5, 71, 12.5)
        );
    }
}
