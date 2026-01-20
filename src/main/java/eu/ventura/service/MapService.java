package eu.ventura.service;

import eu.ventura.Pit;
import eu.ventura.constants.PitMap;
import eu.ventura.maps.Map;
import eu.ventura.maps.impl.KingsMap;

import java.util.Random;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class MapService {
    private static final Random random = new Random();

    public static Map getCurrentMap() {
        PitMap map = Pit.map;
        return switch (map) {
            case KINGS -> new KingsMap();
        };
    }

    public static Map.PitLocation getRandomSpawnLocation() {
        Map map = getCurrentMap();
        return map.getSpawnLocations().get(random.nextInt(map.getSpawnLocations().size()));
    }
}
