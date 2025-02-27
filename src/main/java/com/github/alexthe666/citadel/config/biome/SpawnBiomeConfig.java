//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.alexthe666.citadel.config.biome;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.config.biome.SpawnBiomeData.Deserializer;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;

public class SpawnBiomeConfig {
    public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(SpawnBiomeData.class, new Deserializer()).create();
    private ResourceLocation fileName;

    private SpawnBiomeConfig(ResourceLocation fileName) {
        if (!fileName.getNamespace().endsWith(".json")) {
            this.fileName = new ResourceLocation(fileName.getNamespace(), fileName.getPath() + ".json");
        } else {
            this.fileName = fileName;
        }

    }

    public static SpawnBiomeData create(ResourceLocation fileName, SpawnBiomeData dataDefault) {
        SpawnBiomeConfig config = new SpawnBiomeConfig(fileName);
        SpawnBiomeData data = config.getConfigData(dataDefault);
        return data;
    }

    public static <T> T getOrCreateConfigFile(File configDir, String configName, T defaults, Type type) {
        File configFile = new File(configDir, configName);
        if (!configFile.exists()) {
            try {
                FileUtils.write(configFile, GSON.toJson(defaults));
            } catch (IOException var7) {
                Citadel.LOGGER.error("Spawn Biome Config: Could not write " + configFile.toString(), var7);
            }
        }

        try {
            return GSON.fromJson(FileUtils.readFileToString(configFile), type);
        } catch (Exception var6) {
            Citadel.LOGGER.error("Spawn Biome Config: Could not load " + configFile.toString(), var6);
            return defaults;
        }
    }

    private File getConfigDirFile() {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path bopConfigPath = Paths.get(configPath.toAbsolutePath().toString(), this.fileName.getNamespace());
        return bopConfigPath.toFile();
    }

    private SpawnBiomeData getConfigData(SpawnBiomeData defaultConfigData) {
        SpawnBiomeData configData = (SpawnBiomeData)getOrCreateConfigFile(this.getConfigDirFile(), this.fileName.getPath(), defaultConfigData, (new TypeToken<SpawnBiomeData>() {
        }).getType());
        return configData;
    }
}
