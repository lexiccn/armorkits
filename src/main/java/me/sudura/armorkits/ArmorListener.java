package me.sudura.armorkits;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;


public class ArmorListener implements Listener {
    private final NamespacedKey currentKit;
    static final HashMap<String, Material[]> kitMaterials = new HashMap<>();
    static final HashMap<String, PotionEffect[]> kitEffects = new HashMap<>();

    public ArmorListener(ArmorKits instance) {
        currentKit = new NamespacedKey(instance, "currentKit");

        kitMaterials.put("explorer", new Material[]{Material.getMaterial("LEATHER_BOOTS"), Material.getMaterial("LEATHER_LEGGINGS"), Material.getMaterial("LEATHER_CHESTPLATE"), Material.getMaterial("LEATHER_HELMET")});
        kitEffects.put("explorer", new PotionEffect[]{new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1)});

        kitMaterials.put("miner", new Material[]{Material.getMaterial("IRON_BOOTS"), Material.getMaterial("IRON_LEGGINGS"), Material.getMaterial("IRON_CHESTPLATE"), Material.getMaterial("IRON_HELMET")});
        kitEffects.put("miner", new PotionEffect[]{new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1), new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0)});
    }

    public static String figureOutKit(Player p){
        Material[] armor = new Material[4];
        int n = 0;
        for (ItemStack i : p.getInventory().getArmorContents()) {
            armor[n] = i != null ? i.getType() : Material.AIR;
            n++;
        }

        for(Map.Entry<String, Material[]> kit : kitMaterials.entrySet()) {
            if (armor[0].equals(kit.getValue()[0]) && armor[1].equals(kit.getValue()[1]) && armor[2].equals(kit.getValue()[2]) && armor[3].equals(kit.getValue()[3])) return kit.getKey();
        }
        return null;
    }

    @EventHandler
    public void onChangeArmor (PlayerArmorChangeEvent event) {
        if (event.getOldItem() != null && event.getNewItem() != null && event.getOldItem().getType() == event.getNewItem().getType()) {
            //They're the same; no need to handle anything
            return;
        }

        Player p = event.getPlayer();
        //Get the PDC value; remove the effects
        String kit = p.getPersistentDataContainer().get(currentKit, PersistentDataType.STRING);
        if (kit != null) {
            p.sendMessage("Unequipped "+kit+" kit!");
            p.getPersistentDataContainer().remove(currentKit);
            for (PotionEffect eff : kitEffects.get(kit)) {
                p.removePotionEffect(eff.getType());
            }
        }
        //Set the PDC value; add the effects
        kit = figureOutKit(p);
        if (kit != null) {
            p.sendMessage("Equipped "+kit+" kit!");
            p.getPersistentDataContainer().set(currentKit, PersistentDataType.STRING, kit);
            for (PotionEffect eff : kitEffects.get(kit)) {
                p.addPotionEffect(eff);
            }
        }
    }
}
