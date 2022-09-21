package me.sudura.armorkits;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ArmorKits extends JavaPlugin {
    FileConfiguration config = getConfig();
    public void onEnable() {
        this.saveDefaultConfig();
        List kits = config.getMapList("kits");
        this.getServer().getPluginManager().registerEvents(new ArmorListener(this), this);
    }
}
