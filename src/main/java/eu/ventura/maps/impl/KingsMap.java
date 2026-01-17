package eu.ventura.maps.impl;

import eu.ventura.constants.PitHologram;
import eu.ventura.constants.PitMap;
import eu.ventura.constants.PitNPCs;
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
                new Npc(PitNPCs.PERKS_NPC, new PitLocation(-1.5, 95, 12.5, 180, 0)),
                new Npc(PitNPCs.SHOP_NPC, new PitLocation(2.5, 95, 12.5, 180, 0)),
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
}
