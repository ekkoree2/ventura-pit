package eu.ventura.events.major.impl;

import eu.ventura.Pit;
import eu.ventura.constants.NumberFormat;
import eu.ventura.constants.Strings;
import eu.ventura.event.PitDamageEvent;
import eu.ventura.event.PitKillEvent;
import eu.ventura.events.major.MajorEvent;
import eu.ventura.java.NewString;
import eu.ventura.maps.Map;
import eu.ventura.service.PitBlockService;
import eu.ventura.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.Map.Entry;

/**
 * author: ekkoree
 * created at: 1/22/2026
 */
public class RagePit extends MajorEvent {
    private final java.util.Map<UUID, Double> dmg = new HashMap<>();
    private int kills = 0;

    private List<Entry<UUID, Double>> sorted() {
        List<Entry<UUID, Double>> list = new ArrayList<>(dmg.entrySet());
        list.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        return list;
    }

    public int getPlace(Player p) {
        UUID uuid = p.getUniqueId();
        if (!dmg.containsKey(uuid)) return dmg.size() + 1;

        List<Entry<UUID, Double>> list = sorted();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKey().equals(uuid)) return i + 1;
        }
        return list.size() + 1;
    }

    protected List<Player> getTop() {
        List<Player> top = new ArrayList<>();
        for (Entry<UUID, Double> e : sorted()) {
            if (top.size() >= 3) break;
            Player p = Bukkit.getPlayer(e.getKey());
            if (p != null) top.add(p);
        }
        return top;
    }

    @Override
    protected List<String> getEndingMessage(Player p) {
        List<Player> top = getTop();
        double dealt = dmg.getOrDefault(p.getUniqueId(), 0d);

        List<String> msg = new ArrayList<>(Strings.Lore.EVENT_END.get(p).compile(
                getDisplayName(),
                0, 0, "",
                kills >= 600 ?
                        Strings.Formatted.RAGE_PIT_SUCCESS.format(p, NumberFormat.DEF.of(kills)) :
                        Strings.Formatted.RAGE_PIT_FAIL.format(p, NumberFormat.DEF.of(kills)),
                dealt > 0 ?
                        Strings.Formatted.RAGE_PIT_PLACE.format(p, NumberFormat.DEF.of(dealt), getPlace(p)) :
                        Strings.Simple.RAGE_PIT_DIDNT_PARTICIPATE.get(p)
        ));

        if (!top.isEmpty()) {
            msg.add(Strings.Simple.EVENT_END_TOP_HEADER.get(p));
            for (int i = 0; i < top.size(); i++) {
                Player t = top.get(i);
                msg.add(Strings.Formatted.RAGE_PIT_TOP_ENTRY.format(p,
                        i + 1,
                        PlayerUtil.getDisplayName(t),
                        NumberFormat.DEF.of(dmg.getOrDefault(t.getUniqueId(), 0d))
                ));
            }
        }

        msg.add(Strings.Simple.EVENT_END_FOOTER.get(p));
        return msg;
    }

    @Override
    protected String getBossBar(Player p, int phase) {
        int c = phase % 88;
        String m = "§5§lMAJOR EVENT!";
        String r = " §c§lRAGE PIT!";

        if (c < 12) {
            m = animWave("MAJOR EVENT!", "§d§l", "§f§l", "§5§l", c);
        } else if (c >= 16 && c < 28) {
            m = animWave("MAJOR EVENT!", "§d§l", "§f§l", "§5§l", c - 16);
        } else if (c >= 48 && c < 68) {
            r = (c - 48) % 10 < 5 ? " §d§lRAGE PIT!" : " §c§lRAGE PIT!";
        }

        return m + r + " §7Ending in §a" + formatTime();
    }

    @Override
    public String getDisplayName() {
        return NewString.of("&c&lRAGE PIT");
    }

    @Override
    public List<String> getScoreboard(Player p) {
        double dealt = dmg.getOrDefault(p.getUniqueId(), 0d);
        return Strings.Lore.RAGE_PIT_SCOREBOARD.get(p).compile(
                formatTime(),
                NumberFormat.DEF.of(dealt),
                dealt > 0 ? " (#" + getPlace(p) + ")" : "",
                kills >= 600 ? "&a" : "&c", kills
        );
    }

    @Override
    public void onKill(PitKillEvent e) {
        kills++;
    }

    @Override
    public void onDamage(PitDamageEvent e) {
        UUID uuid = e.data.trueAttacker.getUniqueId();
        dmg.merge(uuid, e.getFinalDamage() / 2, Double::sum);
    }

    @Override
    protected int getDuration() {
        return 240;
    }

    @Override
    public void start() {
        dmg.clear();
        kills = 0;

        eu.ventura.maps.Map map = Pit.map.getInstance();
        List<Location> locs = new ArrayList<>();

        for (Map.PitLocation loc : map.getRagePitGlass()) {
            Location base = loc.of();
            int x = base.getBlockX(), z = base.getBlockZ();
            int maxY = base.getWorld().getMaxHeight();

            for (int y = base.getBlockY() + 1; y < maxY; y++) {
                Block b = base.getWorld().getBlockAt(x, y, z);
                if (b.getType() != Material.AIR) break;
                locs.add(b.getLocation());
            }
        }

        int[] idx = {0};
        Bukkit.getScheduler().runTaskTimer(Pit.instance, task -> {
            for (int i = 0; i < 50 && idx[0] < locs.size(); i++, idx[0]++) {
                Location loc = locs.get(idx[0]);
                loc.getBlock().setType(Material.GLASS, false);
                PitBlockService.saveBlock(loc, Long.MAX_VALUE);
            }
            if (idx[0] >= locs.size()) {
                task.cancel();
                super.start();
            }
        }, 1L, 1L);
    }

    @Override
    protected void onDisable() {
        PitBlockService.removeAllBlocks(Material.GLASS);
        dmg.clear();
        kills = 0;
    }
}
