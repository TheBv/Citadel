//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.alexthe666.citadel;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
        bus = Bus.MOD
)
public class ServerProxy {
    public ServerProxy() {
    }

    public void onPreInit() {
    }

    public void handleAnimationPacket(int entityId, int index) {
    }

    @SubscribeEvent
    public static void onItemsRegistry(Register<Item> registry) {
        registry.getRegistry().registerAll(new Item[]{Citadel.DEBUG_ITEM, Citadel.CITADEL_BOOK});
    }

    public void handlePropertiesPacket(String propertyID, CompoundNBT compound, int entityID) {
    }

    public void openBookGUI(ItemStack book) {
    }
}
