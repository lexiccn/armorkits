package me.sudura.armorkits;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ArmorListener implements Listener {
    private final NamespacedKey currentKit;
    static final HashMap<String, ArrayList<Material>> kitMaterials = new HashMap<>();
    static final HashMap<String, ArrayList<PotionEffect>> kitEffects = new HashMap<>();

    public ArmorListener(ArmorKits instance) {
        currentKit = new NamespacedKey(instance, "currentKit");
        for (String kit : instance.getConfig().getConfigurationSection("kits").getKeys(false)) {
            kitMaterials.put(kit, new ArrayList<>());
            for (String mat : instance.getConfig().getStringList("kits."+kit+".materials")) {
                kitMaterials.get(kit).add(Material.getMaterial(mat));
            }
            kitEffects.put(kit, new ArrayList<>());
            for (Map.Entry<String, Object> effect : instance.getConfig().getConfigurationSection("kits."+kit+".effects").getValues(false).entrySet()) {
                kitEffects.get(kit).add(new PotionEffect(PotionEffectType.getByName(effect.getKey()), Integer.MAX_VALUE, (Integer)effect.getValue()));
            }
        }
    }

    public static String figureOutKit(Player p){
        Material[] armor = new Material[4];
        int n = 0;
        for (ItemStack i : p.getInventory().getArmorContents()) {
            armor[n] = i != null ? i.getType() : Material.AIR;
            n++;
        }

        for(Map.Entry<String, ArrayList<Material>> kit : kitMaterials.entrySet()) {
            //Feel like this can be done better with a loop
            if (armor[0].equals(kit.getValue().get(3)) && armor[1].equals(kit.getValue().get(2)) && armor[2].equals(kit.getValue().get(1)) && armor[3].equals(kit.getValue().get(0))) return kit.getKey();
        }
        return null;
    }

    @EventHandler
    public void onChangeArmor (PlayerArmorChangeEvent event) {
        if (event.getOldItem() != null && event.getNewItem() != null && event.getOldItem().getType().equals(event.getNewItem().getType())) {
            //They're the same; no need to handle anything
            return;
        }

        Player p = event.getPlayer();
        String kit = p.getPersistentDataContainer().get(currentKit, PersistentDataType.STRING);
        String newKit = figureOutKit(p);

        if (!Objects.equals(kit, newKit)) {
            if (kit != null) {
                p.sendMessage("Unequipped "+ChatColor.RED+kit+ChatColor.RESET+" kit!");
                p.getPersistentDataContainer().remove(currentKit);
                for (PotionEffect eff : kitEffects.get(kit)) {
                    p.removePotionEffect(eff.getType());
                }
            }
            if (newKit != null) {
                p.sendMessage("Equipped "+ChatColor.GREEN+newKit+ChatColor.RESET+" kit!");
                p.getPersistentDataContainer().set(currentKit, PersistentDataType.STRING, newKit);
                for (PotionEffect eff : kitEffects.get(newKit)) {
                    p.addPotionEffect(eff);
                }
            }
        }
    }
}
