package net.kunmc.lab.paperplugintemplate;

import dev.kotx.flylib.FlyLib;
import dev.kotx.flylib.command.Command;
import dev.kotx.flylib.command.CommandContext;
import net.kunmc.lab.paperplugintemplate.command.MainCommand;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public final class SpawnRatioModifier extends JavaPlugin {
    private BiomeBaseRegistryWrapper biomeBaseRegistryWrapper;
    public static SpawnRatioModifier instance;

    @Override
    public void onEnable() {
        instance = this;

        IRegistryCustom.Dimension customRegistry = ((CraftServer) getServer()).getServer().customRegistry;
        IRegistryWritable<BiomeBase> biomeIRegistry = customRegistry.b(IRegistry.ay);
        biomeBaseRegistryWrapper = new BiomeBaseRegistryWrapper(biomeIRegistry);

        saveDefaultConfig();
        applyConfig();

        FlyLib.create(this, builder -> {
            builder.command(new MainCommand("spm"));
            builder.command(new Command("test") {
                @Override
                public void execute(@NotNull CommandContext ctx) {
                    biomeBaseRegistryWrapper.toList().stream()
                            .map(BiomeBaseWrapper::mobsSettings)
                            .map(BiomeSettingsMobsWrapper::getAllSpawnerDataList)
                            .forEach(System.out::println);
                }
            });
        });
    }

    @Override
    public void onDisable() {
        for (EntityTypes<?> entityType : getConfigMap().keySet()) {
            updateWeight(entityType, 1.0);
        }
    }

    public void applyConfig() {
        for (Map.Entry<EntityTypes<?>, Double> entry : getConfigMap().entrySet()) {
            EntityTypes<?> entityType = entry.getKey();
            double coefficient = entry.getValue();
            updateWeight(entityType, coefficient);
        }
    }

    private Map<EntityTypes<?>, Double> getConfigMap() {
        FileConfiguration config = getConfig();

        return ((MemorySection) config.get("entitytypes")).getValues(false).entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<EntityTypes<?>, Double>(EntityTypes.getByName(entry.getKey()).get(), ((Double.parseDouble(entry.getValue().toString())))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void updateWeight(EntityTypes<?> entityType, double coefficient) {
        biomeBaseRegistryWrapper.toList().stream()
                .map(BiomeBaseWrapper::mobsSettings)
                .map(BiomeSettingsMobsWrapper::getAllSpawnerDataList)
                .flatMap(x -> x.values().stream())
                .flatMap(Collection::stream)
                .filter(x -> x.entityType == entityType)
                .forEach(x -> x.weight((int) (x.originalWeight * coefficient)));
    }
}