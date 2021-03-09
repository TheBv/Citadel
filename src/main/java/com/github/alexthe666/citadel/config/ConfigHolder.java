//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.alexthe666.citadel.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHolder {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ServerConfig SERVER;

    public ConfigHolder() {
    }

    static {
        Pair<ServerConfig, ForgeConfigSpec> specPair = (new Builder()).configure(ServerConfig::new);
        SERVER = (ServerConfig)specPair.getLeft();
        SERVER_SPEC = (ForgeConfigSpec)specPair.getRight();
    }
}
