package net.kunmc.lab.paperplugintemplate;

import net.minecraft.server.v1_16_R3.BiomeSettingsMobs;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumCreatureType;
import net.minecraft.server.v1_16_R3.WeightedRandom;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BiomeSettingsMobsWrapper {
    public final BiomeSettingsMobs biomeSettingsMobs;
    private final Map<EnumCreatureType, List<SpawnerDataWrapper>> creatureTypeSpawnerDatasMap = new HashMap<>();

    public BiomeSettingsMobsWrapper(BiomeSettingsMobs biomeSettingsMobs) {
        this.biomeSettingsMobs = biomeSettingsMobs;

        try {
            Field field = BiomeSettingsMobs.class.getDeclaredField("e");
            field.setAccessible(true);
            Map<EnumCreatureType, List<BiomeSettingsMobs.c>> map = (Map<EnumCreatureType, List<BiomeSettingsMobs.c>>) field.get(biomeSettingsMobs);
            for (Map.Entry<EnumCreatureType, List<BiomeSettingsMobs.c>> entry : map.entrySet()) {
                creatureTypeSpawnerDatasMap.put(entry.getKey(),
                        entry.getValue().stream()
                                .map(SpawnerDataWrapper::new)
                                .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SpawnerDataWrapper> getSpawnerDataList(EnumCreatureType creatureType) {
        return creatureTypeSpawnerDatasMap.get(creatureType);
    }

    public Map<EnumCreatureType, List<SpawnerDataWrapper>> getAllSpawnerDataList() {
        return creatureTypeSpawnerDatasMap;
    }

    public static class SpawnerDataWrapper {
        public final BiomeSettingsMobs.c spawnerData;
        public final EntityTypes<?> entityType;
        public final int minCount;
        public final int maxCount;
        public final int originalWeight;
        private Field weightField;

        public SpawnerDataWrapper(BiomeSettingsMobs.c spawnerData) {
            this.spawnerData = spawnerData;
            entityType = spawnerData.c;
            minCount = spawnerData.d;
            maxCount = spawnerData.e;

            try {
                weightField = WeightedRandom.WeightedRandomChoice.class.getDeclaredField("a");
                weightField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
           
            originalWeight = weight();
        }

        public int weight() {
            try {
                return weightField.getInt(spawnerData);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return 0;
            }
        }

        public void weight(int weight) {
            try {
                if (weight < 1) {
                    weight = 1;
                }

                weightField.set(spawnerData, weight);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        public String toString() {
            return EntityTypes.getName(entityType) + "*(" + minCount + "-" + maxCount + "):" + weight();
        }

    }
}
