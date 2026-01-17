package eu.ventura.constants;

import eu.ventura.maps.Map;
import eu.ventura.maps.impl.KingsMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * author: ekkoree
 * created at: 1/14/2026
 */
@RequiredArgsConstructor
@Getter
public enum PitMap {
    KINGS(new KingsMap());

    private final Map instance;
}
