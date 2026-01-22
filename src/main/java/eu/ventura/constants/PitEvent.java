package eu.ventura.constants;

import eu.ventura.events.major.MajorEvent;
import eu.ventura.events.major.impl.RagePit;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public enum PitEvent {
    RAGE_PIT(new RagePit());

    public final MajorEvent instance;

    PitEvent(MajorEvent instance) {
        this.instance = instance;
    }
}
