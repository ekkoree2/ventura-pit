package eu.ventura;

import co.aikar.commands.PaperCommandManager;
import dev.kyro.arcticapi.ArcticAPI;
import eu.ventura.commands.*;
import eu.ventura.constants.PitMap;
import eu.ventura.listener.*;
import eu.ventura.service.CombatService;
import eu.ventura.service.PlayerService;
import eu.ventura.util.MongoUtil;
import hvh.ventura.VenturaCore;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Properties;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class Pit extends JavaPlugin {
    public static Pit instance;
    public static final PitMap map = PitMap.KINGS;
    private BukkitTask autoSaveTask;

    private void purgeDirectory(Path path) {
        try {
            if (Files.exists(path)) {
                Files.walk(path)
                        .sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (Exception ignored) {}
                        });
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onEnable() {
        instance = this;
        Path worldPath = Path.of("world");
        purgeDirectory(worldPath.resolve("advancements"));
        purgeDirectory(worldPath.resolve("playerdata"));
        purgeDirectory(worldPath.resolve("stats"));

        Properties env = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(".env")) {
            if (is != null) env.load(is);
        } catch (Exception ignored) {}

        String mongoUri = env.getProperty("MONGO_URI", "mongodb://localhost:27017");
        String mongoDatabase = env.getProperty("MONGO_DATABASE", "ventura_pit");
        MongoUtil.initialize(mongoUri, mongoDatabase);

        VenturaCore.init(this);

        CombatService.getInstance().start();
        ArcticAPI.init(this, "", "");

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new SpawnCommand());
        commandManager.registerCommand(new EnchantCommand());
        commandManager.registerCommand(new FreshCommand());
        commandManager.registerCommand(new StatsCommand());

        getServer().getPluginManager().registerEvents(new PlayerListener(
                VenturaCore.getNpcManager(),
                VenturaCore.getHologramApi()
        ), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new AssistListener(), this);
        getServer().getPluginManager().registerEvents(new BountyListener(), this);
        getServer().getPluginManager().registerEvents(new ServerListener(), this);
        getServer().getPluginManager().registerEvents(new ScoreboardListener(), this);
        getServer().getPluginManager().registerEvents(new ResourceListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        autoSaveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, PlayerService::saveAll, 1200L, 1200L);
    }

    @Override
    public void onDisable() {
        if (autoSaveTask != null) {
            autoSaveTask.cancel();
        }
        PlayerService.saveAll();
        MongoUtil.shutdown();

        VenturaCore.shutdown();

        instance = null;

        CombatService.getInstance().stop();

        HandlerList.unregisterAll(this);
    }
}
