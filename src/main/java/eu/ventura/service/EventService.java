package eu.ventura.service;

import eu.ventura.Pit;
import eu.ventura.events.major.MajorEvent;
import eu.ventura.events.major.impl.RagePit;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
public class EventService {
    private static EventService instance;
    private final Map<Long, MajorEvent> scheduled = new LinkedHashMap<>();
    private final Supplier<MajorEvent>[] events;
    private final Random random = new Random();
    private BukkitTask task;

    @SuppressWarnings("unchecked")
    private EventService() {
        this.events = new Supplier[] {
                RagePit::new
        };
    }

    public static EventService getInstance() {
        if (instance == null) {
            instance = new EventService();
        }
        return instance;
    }

    public void start() {
        long now = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
            now = scheduleNext(now);
            String timestamp = new SimpleDateFormat("MM/dd/yyyy h:mm a").format(new Date(now));
            Bukkit.getLogger().info("[" + timestamp + "] event scheduled");
        }
        task = Bukkit.getScheduler().runTaskTimer(Pit.instance, this::tick, 20L, 20L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        scheduled.clear();
    }

    private void tick() {
        if (Pit.event != null) {
            return;
        }

        long now = System.currentTimeMillis();
        Long nextTime = scheduled.keySet().stream().findFirst().orElse(null);

        if (nextTime != null && now >= nextTime) {
            MajorEvent event = scheduled.remove(nextTime);
            if (event != null) {
                event.schedule();
                scheduleNext(getLastScheduledTime());
            }
        }
    }

    private long scheduleNext(long after) {
        long delay = (45 + random.nextInt(46)) * 60 * 1000L;
        long time = after + delay;
        MajorEvent event = events[random.nextInt(events.length)].get();
        scheduled.put(time, event);
        return time;
    }

    private long getLastScheduledTime() {
        return scheduled.keySet().stream()
                .reduce((a, b) -> b)
                .orElse(System.currentTimeMillis());
    }
}
