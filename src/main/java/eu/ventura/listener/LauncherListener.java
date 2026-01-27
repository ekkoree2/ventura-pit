package eu.ventura.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import eu.ventura.Pit;
import eu.ventura.maps.Map.PitLocation;
import eu.ventura.util.MathUtil;
import eu.ventura.util.RegionHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * author: ekkoree
 * created at: 1/25/2026
 */
public class LauncherListener implements Listener {
    private static final ProtocolManager protocol = ProtocolLibrary.getProtocolManager();
    private final Map<UUID, Long> cd = new HashMap<>();
    private final Map<UUID, Location> pos = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!RegionHelper.isInRadiusOfBlock(p, Material.SLIME_BLOCK, 3)) return;
        if (isOnCd(p)) return;

        Location from = p.getLocation();
        Location to = getTarget(p);
        setCd(p);

        ArmorStand stand = spawn(p);
        pos.put(stand.getUniqueId(), from);
        fx(from);

        new Flight(p, stand, from, to).runTaskTimer(Pit.instance, 0L, 1L);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity().isInsideVehicle()) {
            e.setCancelled(true);
        }
    }

    private Location getTarget(Player p) {
        int q = RegionHelper.getPlayerQuadrant(p);
        PitLocation[] targets = Pit.map.getInstance().getLauncherTargets(q);
        return targets[ThreadLocalRandom.current().nextInt(targets.length)].of();
    }

    private void fx(Location loc) {
        loc.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, loc, 1);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1.8f);
        loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.4f, 1.2f);
    }

    private class Flight extends BukkitRunnable {
        private final Player p;
        private final ArmorStand stand;
        private final Location from, to, c1, c2;
        private final float yaw0, yaw1;
        private Location last;
        private double t = 0;

        Flight(Player p, ArmorStand stand, Location from, Location to) {
            this.p = p;
            this.stand = stand;
            this.from = from;
            this.to = to;
            this.yaw0 = from.getYaw();
            this.last = from.clone();

            double dx = to.getX() - from.getX();
            double dz = to.getZ() - from.getZ();
            this.yaw1 = (float) Math.toDegrees(Math.atan2(-dx, dz));

            double dist = from.distance(to);
            double arc = Math.max(15, dist * 0.4);
            double mx = (from.getX() + to.getX()) / 2;
            double mz = (from.getZ() + to.getZ()) / 2;
            double peak = Math.max(from.getY(), to.getY()) + arc;

            this.c1 = new Location(from.getWorld(),
                    MathUtil.lerp(from.getX(), mx, 0.3), peak + arc * 0.2,
                    MathUtil.lerp(from.getZ(), mz, 0.3));
            this.c2 = new Location(from.getWorld(),
                    MathUtil.lerp(mx, to.getX(), 0.7), peak,
                    MathUtil.lerp(mz, to.getZ(), 0.7));
        }

        @Override
        public void run() {
            double cube = MathUtil.easeInOutCubic(t);
            Location location = MathUtil.cubicBezierLocationFromPoints(cube, from, c1, c2, to);

            if (!p.isInsideVehicle()) {
                Bukkit.getScheduler().runTask(Pit.instance, () -> p.teleport(location));
                this.cancel();
                return;
            }

            if (t <= 1.0) {
                location.setYaw(MathUtil.lerpAngle(yaw0, yaw1, (float) cube));

                double dx = location.getX() - last.getX();
                double dy = location.getY() - last.getY();
                double dz = location.getZ() - last.getZ();
                double hd = Math.sqrt(dx * dx + dz * dz);
                location.setPitch(Math.max(-60, Math.min(60, (float) -Math.toDegrees(Math.atan2(dy, hd)))));

                move(stand, location);
                last = location.clone();
                t += 0.02;
            } else {
                end();
                cancel();
            }
        }

        private void end() {
            pos.remove(stand.getUniqueId());

            Location fin = to.clone();
            fin.setYaw(yaw1);
            fin.setPitch(0);

            double dx = to.getX() - last.getX();
            double dz = to.getZ() - last.getZ();
            double hd = Math.sqrt(dx * dx + dz * dz);
            double vx = hd > 0 ? (dx / hd) * 0.3 : 0;
            double vz = hd > 0 ? (dz / hd) * 0.3 : 0;

            stand.eject();
            stand.remove();

            Bukkit.getScheduler().runTask(Pit.instance, () -> {
                p.teleport(fin);
                p.setVelocity(new org.bukkit.util.Vector(vx, -0.2, vz));
                to.getWorld().playSound(to, Sound.ENTITY_BAT_TAKEOFF, 0.2f, 0.8f);
            });
        }
    }

    private void move(Entity e, Location loc) {
        Location prev = pos.getOrDefault(e.getUniqueId(), e.getLocation());
        double dx = loc.getX() - prev.getX();
        double dy = loc.getY() - prev.getY();
        double dz = loc.getZ() - prev.getZ();

        PacketContainer pkt = protocol.createPacket(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);
        pkt.getIntegers().write(0, e.getEntityId());
        pkt.getShorts()
                .write(0, (short) (dx * 4096))
                .write(1, (short) (dy * 4096))
                .write(2, (short) (dz * 4096));
        pkt.getBytes()
                .write(0, (byte) (loc.getYaw() * 256 / 360))
                .write(1, (byte) (loc.getPitch() * 256 / 360));
        pkt.getBooleans().write(0, false);

        for (Player pl : e.getWorld().getPlayers()) {
            if (pl.getLocation().distance(e.getLocation()) < 128) {
                try { protocol.sendServerPacket(pl, pkt); } catch (Exception ignored) {}
            }
        }

        pos.put(e.getUniqueId(), loc);
    }

    private ArmorStand spawn(Player p) {
        ArmorStand s = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
        s.addPassenger(p);
        s.setVisible(false);
        s.setGravity(false);
        s.setInvulnerable(true);
        s.setMarker(true);
        return s;
    }

    private boolean isOnCd(Player p) {
        Long last = cd.get(p.getUniqueId());
        return last != null && System.currentTimeMillis() - last < 3000;
    }

    private void setCd(Player p) {
        cd.put(p.getUniqueId(), System.currentTimeMillis());
    }
}
