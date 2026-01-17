package eu.ventura.model;

import eu.ventura.constants.Sounds;

public class TriggerModel {
    public final Sounds.SoundEffect sound;
    public final String message;
    public final Mode mode;

    public TriggerModel(Sounds.SoundEffect sound, String message, Mode mode) {
        this.sound = sound;
        this.message = message;
        this.mode = mode;
    }

    public enum Mode {
        CONFIRM_PANEL,
        PASS
    }
}
