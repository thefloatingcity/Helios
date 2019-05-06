package com.outlook.tehbrian.tfcplugin.utils;

import com.mongodb.client.model.UpdateOptions;
import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class DatabaseUtils {

    private static final Main main = Main.getInstance();

    private DatabaseUtils() {
    }

    public static void updatePlayer(Player player, long millisOntime) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            main.getDatabase().getCollection("players").updateOne(
                    eq("UUID", player.getUniqueId().toString()),
                    set("millisOntime", millisOntime),
                    new UpdateOptions().upsert(true));
        });
    }
}
