package eu.ventura.constants;

import eu.ventura.Pit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/14/2026
 */
public class Sounds {
    // enchants
    public static final SoundEffect CRATER = new SoundEffect(Sound.ENTITY_GENERIC_EXPLODE, 0.4, 1.7);
    public static final SoundEffect BOUNTY = new SoundEffect(Sound.ENTITY_WITHER_SPAWN, 0.75, 1.795f);
    public static final SoundEffect KILL = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.55, 1.79f);
    public static final SoundEffect LEVEL_UP = new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, 1.5, 1.3);
    public static final SoundEffect ASSIST = new SoundEffect(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.55, 1.72f);
    public static final SoundEffect GOLD_PICKUP = KILL;

    // gui
    public static final SoundEffect NO = new SoundEffect(Sound.ENTITY_VILLAGER_NO, 1.1, 1.0);
    public static final SoundEffect SUCCESS = new SoundEffect(Sound.BLOCK_NOTE_BLOCK_PLING, 1.1, 4.0555f);
    public static final SoundEffect ENDERMAN_NO = new SoundEffect(Sound.ENTITY_ENDERMAN_HURT, 1.1, 1.0);
    public static final SoundEffect YES = new SoundEffect(Sound.ENTITY_VILLAGER_YES, 1.1, 1.0);
    public static final SoundEffect ITEM_PURCHASE = new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, 1.0, 2.0);
    public static final SoundEffect GOLDEN_HEADS = new SoundEffect(Sound.ENTITY_GENERIC_EAT, 1.0, 0.85);
    public static final SoundEffect GOLDEN_HEADS_COOLDOWN = new SoundEffect(Sound.ENTITY_VILLAGER_NO, 1.0, 1.0);

    public static final SoundEffect DOUBLE_KILL = new SoundEffect()
            .add(new SoundMoment(0).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.8))
            .add(new SoundMoment(2).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.825));
    public static final SoundEffect TRIPLE_KILL = new SoundEffect()
            .add(new SoundMoment(0).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.8))
            .add(new SoundMoment(2).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.825))
            .add(new SoundMoment(4).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.875));
    public static final SoundEffect QUADRA_KILL = new SoundEffect()
            .add(new SoundMoment(0).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.8))
            .add(new SoundMoment(2).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.825))
            .add(new SoundMoment(4).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.875))
            .add(new SoundMoment(6).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.9));
    public static final SoundEffect PENTA_KILL = new SoundEffect()
            .add(new SoundMoment(0).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.8))
            .add(new SoundMoment(2).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.825))
            .add(new SoundMoment(4).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.875))
            .add(new SoundMoment(6).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.9))
            .add(new SoundMoment(8).add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5, 1.95));
    public static final SoundEffect DEATH = new SoundEffect()
            .add(new SoundMoment(0).add(Sound.ENTITY_GENERIC_HURT, 0.5, 0.89))
            .add(new SoundMoment(2).add(Sound.ENTITY_ZOMBIE_INFECT, 0.85, 1.69));

    public static class SoundEffect {
        private SoundMoment soundMoment;
        private final List<SoundMoment> soundTimeList = new ArrayList<>();

        public SoundEffect() {
            this.soundMoment = null;
        }

        public SoundEffect(Sound sound, double volume, double pitch) {
            this.soundMoment = new SoundMoment(new SoundMoment.BukkitSound(sound, volume, pitch));
        }

        public SoundEffect(String soundString, double volume, double pitch) {
            this.soundMoment = new SoundMoment(new SoundMoment.BukkitSound(soundString, volume, pitch));
        }

        public SoundEffect add(SoundMoment soundMoment) {
            soundTimeList.add(soundMoment);
            return this;
        }

        public void play(Location location, double radius) {
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer.getWorld() != location.getWorld() || onlinePlayer.getLocation().distance(location) > radius)
                    continue;
                play(onlinePlayer, -1, -1);
            }
        }

        public void play(Location location, double radius, float volume, float pitch) {
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer.getWorld() != location.getWorld() || onlinePlayer.getLocation().distance(location) > radius)
                    continue;
                play(onlinePlayer, volume, pitch);
            }
        }

        public void play(LivingEntity checkPlayer) {
            play(checkPlayer, -1, -1);
        }

        public void play(LivingEntity checkPlayer, float volume, float pitch) {
            if(!(checkPlayer instanceof Player player)) return;

            if(!player.isOnline()) return;
            if(soundMoment != null) {
                if(volume >= 0 && pitch >= 0) soundMoment.play(player, volume, pitch);
                else soundMoment.play(player);
                return;
            }
            List<SoundMoment> soundTimeList = new ArrayList<>(this.soundTimeList);
            new BukkitRunnable() {
                int count = 0;
                @Override
                public void run() {
                    for(SoundMoment soundMoment : new ArrayList<>(soundTimeList)) {
                        if(soundMoment.tick != count) continue;
                        soundTimeList.remove(soundMoment);
                        soundMoment.play(player);
                    }
                    if(soundTimeList.isEmpty()) cancel();
                    count++;
                }
            }.runTaskTimer(Pit.getInstance(), 0L, 1L);
        }

        public void play(Location location) {
            if(soundMoment != null) {
                soundMoment.play(location);
                return;
            }
            List<SoundMoment> soundTimeList = new ArrayList<>(this.soundTimeList);
            new BukkitRunnable() {
                @Override
                public void run() {
                    int count = 0;
                    List<SoundMoment> toRemove = new ArrayList<>();
                    for(SoundMoment soundMoment : soundTimeList) {
                        if(soundMoment.tick == count) {
                            toRemove.add(soundMoment);
                            soundMoment.play(location);
                        }
                        count++;
                    }
                    soundTimeList.removeAll(toRemove);
                    if(soundTimeList.isEmpty()) cancel();
                }
            }.runTaskTimer(Pit.getInstance(), 0L, 1L);
        }
    }

    public static class SoundMoment {
        private final List<BukkitSound> bukkitSounds = new ArrayList<>();
        private int tick;

        //			Time constructor
        public SoundMoment(int tick) {
            this.tick = tick;
        }

        //			Sound with no time
        public SoundMoment(BukkitSound bukkitSound) {
            this.bukkitSounds.add(bukkitSound);
        }

        //			Add sound to time constructed with time constructor
        public SoundMoment add(Sound sound, double volume, double pitch) {
            bukkitSounds.add(new BukkitSound(sound, volume, pitch));
            return this;
        }

        public SoundMoment add(String soundString, int volume, double pitch) {
            bukkitSounds.add(new BukkitSound(soundString, volume, pitch));
            return this;
        }

        public void play(Player player) {
            if(!player.isOnline()) return;
            for(BukkitSound bukkitSound : bukkitSounds) {
                if(bukkitSound.sound != null) {
                    player.playSound(player.getLocation(), bukkitSound.sound, bukkitSound.volume, bukkitSound.pitch);
                } else {
                    player.playSound(player.getLocation(), bukkitSound.soundString, bukkitSound.volume, bukkitSound.pitch);
                }
            }
        }

        public void play(Player player, float volume, float pitch) {
            if(!player.isOnline()) return;
            for(BukkitSound bukkitSound : bukkitSounds) {
                if(bukkitSound.sound != null) {
                    player.playSound(player.getLocation(), bukkitSound.sound, volume, pitch);
                } else {
                    player.playSound(player.getLocation(), bukkitSound.soundString, volume, pitch);
                }
            }
        }

        public void play(Location location) {
            for(BukkitSound bukkitSound : bukkitSounds) {
                if(bukkitSound.sound != null)
                    location.getWorld().playSound(location, bukkitSound.sound, bukkitSound.volume, bukkitSound.pitch);
            }
        }

        public static class BukkitSound {
            private Sound sound;
            private String soundString;
            private final float volume;
            private final float pitch;

            private BukkitSound(Sound sound, double volume, double pitch) {
                this.sound = sound;
                this.volume = (float) volume;
                this.pitch = (float) pitch;
            }

            private BukkitSound(String soundString, double volume, double pitch) {
                this.soundString = soundString;
                this.volume = (float) volume;
                this.pitch = (float) pitch;
            }
        }
    }
}