package net.kunmc.lab.paperplugintemplate;

import net.minecraft.server.v1_16_R3.BiomeBase;
import net.minecraft.server.v1_16_R3.IRegistry;

import java.util.List;
import java.util.stream.Collectors;

public class BiomeBaseRegistryWrapper {
    public final IRegistry<BiomeBase> biomeBaseRegistry;
    private final List<BiomeBaseWrapper> biomeBaseWrapperList;

    public BiomeBaseRegistryWrapper(IRegistry<BiomeBase> biomeBaseRegistry) {
        this.biomeBaseRegistry = biomeBaseRegistry;
        this.biomeBaseWrapperList = biomeBaseRegistry.g().map(BiomeBaseWrapper::new).collect(Collectors.toList());
    }

    public List<BiomeBaseWrapper> toList() {
        return biomeBaseWrapperList;
    }
}
