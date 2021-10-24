package net.kunmc.lab.paperplugintemplate;

import net.minecraft.server.v1_16_R3.BiomeBase;

public class BiomeBaseWrapper {
    public final BiomeBase biomeBase;
    private final BiomeSettingsMobsWrapper mobsSettings;

    public BiomeBaseWrapper(BiomeBase biomeBase) {
        this.biomeBase = biomeBase;
        this.mobsSettings = new BiomeSettingsMobsWrapper(biomeBase.b());
    }

    public BiomeSettingsMobsWrapper mobsSettings() {
        return mobsSettings;
    }
}
