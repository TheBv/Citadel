//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.alexthe666.citadel.client.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiCitadelBook extends com.github.alexthe666.citadel.client.gui.GuiBasicBook {
    public GuiCitadelBook(ItemStack bookStack) {
        super(bookStack, new TranslationTextComponent("citadel_guide_book.title"));
    }

    protected int getBindingColor() {
        return 6595195;
    }

    public ResourceLocation getRootPage() {
        return new ResourceLocation("citadel:book/citadel_book/root.json");
    }

    public String getTextFileDirectory() {
        return "citadel:book/citadel_book/";
    }
}
