package eu.ventura.constants;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import eu.ventura.Pit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

    public static final SoundEffect MAJOR_START = new SoundEffect(Sound.ENTITY_ENDER_DRAGON_GROWL, 1.3, 1.1);
    public static final SoundEffect PRESTIGE = new SoundEffect(Sound.ENTITY_ENDER_DRAGON_GROWL, 1.2, 1.1);

    public static final SoundEffect BUG_REMOVE = new SoundEffect(Sound.UI_BUTTON_CLICK, 0.4f, 1.2);

    // gui
    public static final SoundEffect NO = new SoundEffect(Sound.ENTITY_VILLAGER_NO, 1.1, 1.0);
    public static final SoundEffect SUCCESS = new SoundEffect(Sound.BLOCK_NOTE_BLOCK_PLING, 1.1, 4.0555f);
    public static final SoundEffect ENDERMAN_NO = new SoundEffect(Sound.ENTITY_ENDERMAN_HURT, 1.1, 1.0);
    public static final SoundEffect YES = new SoundEffect(Sound.ENTITY_VILLAGER_YES, 1.1, 1.0);
    public static final SoundEffect ITEM_PURCHASE = new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, 1.0, 2.0);
    public static final SoundEffect GOLDEN_HEADS = new SoundEffect(Sound.ENTITY_GENERIC_EAT, 1.0, 0.85);
    public static final SoundEffect GOLDEN_HEADS_COOLDOWN = new SoundEffect(Sound.ENTITY_VILLAGER_NO, 1.0, 1.0);

    public static final SoundEffect MAJOR_END = new SoundEffect()
            .add(new SoundMoment(0).add(Sound.ENTITY_PLAYER_BURP, 1.0, 0.6984127))
            .add(new SoundMoment(5).add(Sound.ENTITY_PLAYER_BURP, 1.0, 0.5873016))
            .add(new SoundMoment(10).add(Sound.ENTITY_PLAYER_BURP, 1.0, 0.4920635));

    public static final SoundEffect MAJOR_EVENT_SCHEDULE = new SoundEffect()
            .add(new SoundMoment(0)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.571429)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.777778))
            .add(new SoundMoment(0)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.571429)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.777778))
            .add(new SoundMoment(2)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 0.777778)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 0.920635))
            .add(new SoundMoment(4)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 0.777778)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 0.920635))
            .add(new SoundMoment(6)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.920635)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 1.15873))
            .add(new SoundMoment(8)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.920635)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 1.15873))
            .add(new SoundMoment(10)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 0.571429)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 0.777778)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 0.920635)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 1.15873)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 1.301587)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 1.555556))
            .add(new SoundMoment(14)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 0.571429)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 0.777778)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 0.920635)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 1.15873)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 1.301587)
                    .add(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0, 1.555556))
            .add(new SoundMoment(16)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.571429)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.777778)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.920635)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 1.15873)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 1.301587)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 1.555556))
            .add(new SoundMoment(18)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.571429)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.777778)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 0.920635)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 1.15873)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 1.301587)
                    .add(Sound.BLOCK_NOTE_BLOCK_HARP, 1.0, 1.555556));

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

        public void play(LivingEntity checkPlayer) {
            play(checkPlayer, -1, -1);
        }

        public void play(LivingEntity checkPlayer, float volume, float pitch) {
            if (!(checkPlayer instanceof Player player)) return;
            if (!player.isOnline()) return;

            if (soundMoment != null) {
                if (volume >= 0 && pitch >= 0) {
                    soundMoment.play(player, volume, pitch);
                } else {
                    soundMoment.play(player);
                }
                return;
            }

            Map<Integer, List<SoundMoment.BukkitSound>> grouped = new TreeMap<>();
            for (SoundMoment moment : soundTimeList) {
                grouped.computeIfAbsent(moment.tick, k -> new ArrayList<>()).addAll(moment.bukkitSounds);
            }

            for (Map.Entry<Integer, List<SoundMoment.BukkitSound>> entry : grouped.entrySet()) {
                int delay = entry.getKey();
                List<SoundMoment.BukkitSound> soundsAtTick = entry.getValue();

                if (delay == 0) {
                    playSounds(player, soundsAtTick, volume, pitch);
                } else {
                    Bukkit.getScheduler().runTaskLater(Pit.instance, () -> {
                        if (!player.isOnline()) return;
                        playSounds(player, soundsAtTick, volume, pitch);
                    }, delay);
                }
            }
        }

        public void play(Location location) {
            if (soundMoment != null) {
                soundMoment.play(location);
                return;
            }

            Map<Integer, List<SoundMoment.BukkitSound>> grouped = new HashMap<>();
            for (SoundMoment moment : soundTimeList) {
                grouped.computeIfAbsent(moment.tick, k -> new ArrayList<>()).addAll(moment.bukkitSounds);
            }

            for (Map.Entry<Integer, List<SoundMoment.BukkitSound>> entry : grouped.entrySet()) {
                int delay = entry.getKey();
                List<SoundMoment.BukkitSound> soundsAtTick = entry.getValue();

                if (delay == 0) {
                    playSounds(location, soundsAtTick);
                } else {
                    Bukkit.getScheduler().runTaskLater(Pit.instance, () -> {
                        playSounds(location, soundsAtTick);
                    }, delay);
                }
            }
        }

        public void play(Location location, double radius) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getWorld() != location.getWorld() || onlinePlayer.getLocation().distance(location) > radius) {
                    continue;
                }
                play(onlinePlayer, -1, -1);
            }
        }

        public void play(Location location, double radius, float volume, float pitch) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getWorld() != location.getWorld() || onlinePlayer.getLocation().distance(location) > radius) {
                    continue;
                }
                play(onlinePlayer, volume, pitch);
            }
        }

        private void playSounds(Player player, List<SoundMoment.BukkitSound> sounds, float overrideVolume, float overridePitch) {
            Location location = player.getLocation();

            for (SoundMoment.BukkitSound bukkitSound : sounds) {
                float volume = overrideVolume >= 0 ? overrideVolume : bukkitSound.volume;
                float pitch = overridePitch >= 0 ? overridePitch : bukkitSound.pitch;

                if (bukkitSound.sound != null) {
                    sendSoundPacket(player, location, bukkitSound.sound, volume, pitch);
                } else if (bukkitSound.soundString != null) {
                    player.playSound(location, bukkitSound.soundString, volume, pitch);
                }
            }
        }

        private void playSounds(Location location, List<SoundMoment.BukkitSound> sounds) {
            for (Player player : location.getWorld().getPlayers()) {
                for (SoundMoment.BukkitSound bukkitSound : sounds) {
                    if (bukkitSound.sound != null) {
                        sendSoundPacket(player, location, bukkitSound.sound, bukkitSound.volume, bukkitSound.pitch);
                    }
                }
            }
        }

        private void sendSoundPacket(Player player, Location location, Sound sound, float volume, float pitch) {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.NAMED_SOUND_EFFECT);

            packet.getSoundEffects().write(0, sound);
            packet.getSoundCategories().write(0, EnumWrappers.SoundCategory.RECORDS);
            packet.getIntegers()
                    .write(0, (int) (location.getX() * 8.0))
                    .write(1, (int) (location.getY() * 8.0))
                    .write(2, (int) (location.getZ() * 8.0));
            packet.getFloat()
                    .write(0, volume)
                    .write(1, pitch);
            packet.getLongs().write(0, ThreadLocalRandom.current().nextLong());

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
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