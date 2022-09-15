package me.sudura.armorkits;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class ArmorListener implements Listener {
    NamespacedKey ironKey;
    public ArmorListener(Plugin instance) {
        ironKey = new NamespacedKey(instance, "minerKit");
    }

    @EventHandler
    public void onChangeArmor (PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        int ironEquipped = 0;
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if(armor != null && (armor.getType() == Material.IRON_HELMET || armor.getType() == Material.IRON_CHESTPLATE || armor.getType() == Material.IRON_LEGGINGS || armor.getType() == Material.IRON_BOOTS)){
                ironEquipped++;
            }
        }
        if (ironEquipped >= 4) {
            player.sendMessage("Equipped Miner Kit");
            player.getPersistentDataContainer().set(ironKey, PersistentDataType.INTEGER, 1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        } else if (player.getPersistentDataContainer().get(ironKey, PersistentDataType.INTEGER) != null) {
            player.sendMessage("Unequipped Miner Kit");
            player.getPersistentDataContainer().remove(ironKey);
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        }
    }
}
