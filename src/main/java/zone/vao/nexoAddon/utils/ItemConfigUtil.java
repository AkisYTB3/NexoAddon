package zone.vao.nexoAddon.utils;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.classes.Components;
import zone.vao.nexoAddon.classes.Mechanics;

import java.io.File;
import java.util.*;

public class ItemConfigUtil {

  private static final Set<File> itemFiles = new HashSet<>();

  public static Set<File> getItemFiles() {
    itemFiles.clear();
    itemFiles.addAll(NexoItems.itemMap().keySet());
    return itemFiles;
  }

  public static void loadComponents() {
    NexoAddon.getInstance().getComponents().clear();

    for (File itemFile : getItemFiles()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(itemFile);

      config.getKeys(false).forEach(itemId -> {
        ConfigurationSection itemSection = config.getConfigurationSection(itemId);
        if (itemSection == null || !itemSection.contains("Components")) return;

        Components component = NexoAddon.getInstance().getComponents()
            .computeIfAbsent(itemId, Components::new);

        loadEquippableComponent(itemSection, component);
        loadJukeboxPlayableComponent(itemSection, component);
        loadFertilizerComponent(itemSection, component);
      });
    }
  }

  private static void loadEquippableComponent(ConfigurationSection section, Components component) {
    if (section.contains("Components.equippable")) {
      try {
        EquipmentSlot slot = EquipmentSlot.valueOf(
            section.getString("Components.equippable.slot", "HEAD").toUpperCase()
        );
        component.setEquippable(slot);
      } catch (IllegalArgumentException ignored) {}
    }
  }

  private static void loadJukeboxPlayableComponent(ConfigurationSection section, Components component) {
    if (section.contains("Components.jukebox_playable.song_key")) {
      String songKey = section.getString("Components.jukebox_playable.song_key");
      component.setPlayable(songKey);
    }
  }

  private static void loadFertilizerComponent(ConfigurationSection section, Components component) {
    if (section.contains("Components.fertilizer.growth_speedup") && section.contains("Components.fertilizer.usable_on")) {
      int growthSpeedup = section.getInt("Components.fertilizer.growth_speedup", 1000);
      List<String> usableOn = section.getStringList("Components.fertilizer.usable_on");
      component.setFertilizer(growthSpeedup, usableOn);
    }
  }

  public static void loadMechanics() {
    NexoAddon.getInstance().getMechanics().clear();

    for (File itemFile : getItemFiles()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(itemFile);

      config.getKeys(false).forEach(itemId -> {
        ConfigurationSection itemSection = config.getConfigurationSection(itemId);
        if (itemSection == null || !itemSection.contains("Mechanics")) return;

        Mechanics mechanic = NexoAddon.getInstance().getMechanics()
            .computeIfAbsent(itemId, Mechanics::new);

        loadRepairMechanic(itemSection, mechanic);
        loadBigMiningMechanic(itemSection, mechanic);
        loadBedrockBreakMechanic(itemSection, mechanic);
        loadAuraMechanic(itemSection, mechanic);
      });
    }
  }

  private static void loadRepairMechanic(ConfigurationSection section, Mechanics mechanic) {
    if (section.contains("Mechanics.repair.ratio") || section.contains("Mechanics.repair.fixed_amount")) {
      double ratio = section.getDouble("Mechanics.repair.ratio");
      int fixedAmount = section.getInt("Mechanics.repair.fixed_amount");
      mechanic.setRepair(ratio, fixedAmount);
    }
  }

  private static void loadBigMiningMechanic(ConfigurationSection section, Mechanics mechanic) {
    if (section.contains("Mechanics.bigmining.radius") && section.contains("Mechanics.bigmining.depth")) {
      int radius = section.getInt("Mechanics.bigmining.radius", 1);
      int depth = section.getInt("Mechanics.bigmining.depth", 1);
      boolean switchable = section.getBoolean("Mechanics.bigmining.switchable", false);
      mechanic.setBigMining(radius, depth, switchable);
    }
  }

  private static void loadBedrockBreakMechanic(ConfigurationSection section, Mechanics mechanic) {
    if (section.contains("Mechanics.bedrockbreak.hardness") && section.contains("Mechanics.bedrockbreak.probability")) {
      int hardness = section.getInt("Mechanics.bedrockbreak.hardness");
      double probability = section.getDouble("Mechanics.bedrockbreak.probability");
      int durabilityCost = section.getInt("Mechanics.bedrockbreak.durability_cost", 1);
      boolean disableOnFirstLayer = section.getBoolean("Mechanics.bedrockbreak.disable_on_first_layer", true);
      mechanic.setBedrockBreak(hardness, probability, durabilityCost, disableOnFirstLayer);
    }
  }

  private static void loadAuraMechanic(ConfigurationSection section, Mechanics mechanic) {
    if (section.contains("Mechanics.aura.type") && section.contains("Mechanics.aura.particle")) {
      Particle particle = Particle.valueOf(section.getString("Mechanics.aura.particle", "FLAME").toUpperCase());
      String type = section.getString("Mechanics.aura.type");
      String customFormula = section.getString("Mechanics.aura.custom", null);
      mechanic.setAura(particle, type, customFormula);
    }
  }
}
