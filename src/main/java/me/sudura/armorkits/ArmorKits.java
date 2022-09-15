package me.sudura.armorkits;

import org.bukkit.plugin.java.JavaPlugin;

public class ArmorKits extends JavaPlugin {
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new ArmorListener(this), this);
    }
}
