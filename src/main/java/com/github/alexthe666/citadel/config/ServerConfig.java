//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.alexthe666.citadel.config;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class ServerConfig {
    public final BooleanValue citadelEntityTracker;
    public static boolean citadelEntityTrack;

    public ServerConfig(Builder builder) {
        builder.push("general");
        this.citadelEntityTracker = buildBoolean(builder, "Track Entities", "all", true, "True if citadel tracks entity properties(freezing, stone mobs, etc) on server. Turn this to false to solve some server lag, may break some stuff.");
    }

    private static BooleanValue buildBoolean(Builder builder, String name, String catagory, boolean defaultValue, String comment) {
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }
}
