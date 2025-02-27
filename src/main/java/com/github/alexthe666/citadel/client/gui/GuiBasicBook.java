//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.gui.data.EntityRenderData;
import com.github.alexthe666.citadel.client.gui.data.ImageData;
import com.github.alexthe666.citadel.client.gui.data.ItemRenderData;
import com.github.alexthe666.citadel.client.gui.data.LineData;
import com.github.alexthe666.citadel.client.gui.data.LinkData;
import com.github.alexthe666.citadel.client.gui.data.RecipeData;
import com.github.alexthe666.citadel.client.gui.data.TabulaRenderData;
import com.github.alexthe666.citadel.client.gui.data.Whitespace;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.TabulaModelHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.IRenderTypeBuffer.Impl;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.IOUtils;

@OnlyIn(Dist.CLIENT)
public abstract class GuiBasicBook extends Screen {
    private static final ResourceLocation BOOK_PAGE_TEXTURE = new ResourceLocation("citadel:textures/gui/book/book_pages.png");
    private static final ResourceLocation BOOK_BINDING_TEXTURE = new ResourceLocation("citadel:textures/gui/book/book_binding.png");
    private static final ResourceLocation BOOK_WIDGET_TEXTURE = new ResourceLocation("citadel:textures/gui/book/widgets.png");
    protected ItemStack bookStack;
    protected int xSize = 390;
    protected int ySize = 320;
    protected int currentPageCounter = 0;
    protected int maxPagesFromPrinting = 0;
    protected int linesFromJSON = 0;
    protected int linesFromPrinting = 0;
    protected ResourceLocation prevPageJSON;
    protected ResourceLocation currentPageJSON;
    protected ResourceLocation currentPageText = null;
    private BookPageButton buttonNextPage;
    private BookPageButton buttonPreviousPage;
    private BookPage internalPage = null;
    private List<LineData> lines = new ArrayList();
    private List<LinkData> links = new ArrayList();
    private List<ItemRenderData> itemRenders = new ArrayList();
    private List<RecipeData> recipes = new ArrayList();
    private List<TabulaRenderData> tabulaRenders = new ArrayList();
    private List<EntityRenderData> entityRenders = new ArrayList();
    private List<ImageData> images = new ArrayList();
    private List<Whitespace> yIndexesToSkip = new ArrayList();
    private Map<String, TabulaModel> renderedTabulaModels = new HashMap();
    private Map<String, Entity> renderedEntites = new HashMap();
    private Map<String, ResourceLocation> textureMap = new HashMap();
    private String writtenTitle = "";
    private int preservedPageIndex = 0;

    public GuiBasicBook(ItemStack bookStack, ITextComponent title) {
        super(title);
        this.bookStack = bookStack;
        this.currentPageJSON = this.getRootPage();
    }

    public static void drawEntityOnScreen(int posX, int posY, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY, Entity entity) {
        float f = (float)Math.atan((double)(mouseX / 40.0F));
        float f1 = (float)Math.atan((double)(mouseY / 40.0F));
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale(scale, scale, scale);
        entity.setOnGround(false);
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(20.0F);
        if (follow) {
            float yaw = f * 20.0F;
            entity.rotationYaw = yaw;
            entity.rotationPitch = -f1 * 20.0F;
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).renderYawOffset = yaw;
                ((LivingEntity)entity).prevRenderYawOffset = yaw;
                ((LivingEntity)entity).rotationYawHead = yaw;
                ((LivingEntity)entity).prevRotationYawHead = yaw;
            }

            quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
            quaternion.multiply(quaternion1);
        }

        matrixstack.rotate(quaternion);
        matrixstack.rotate(Vector3f.XP.rotationDegrees((float)(-xRot)));
        matrixstack.rotate(Vector3f.YP.rotationDegrees((float)yRot));
        matrixstack.rotate(Vector3f.ZP.rotationDegrees((float)zRot));
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.renderEntityStatic(entity, 0.0D, 0.0D, 0.0D, f, partialTicks, matrixstack, irendertypebuffer$impl, 15728880);
        });
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        entity.rotationYaw = 0.0F;
        entity.rotationPitch = 0.0F;
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).renderYawOffset = 0.0F;
            ((LivingEntity)entity).prevRotationYawHead = 0.0F;
            ((LivingEntity)entity).rotationYawHead = 0.0F;
        }

        RenderSystem.popMatrix();
    }

    public static void drawTabulaModelOnScreen(TabulaModel model, ResourceLocation tex, int posX, int posY, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY) {
        float f = (float)Math.atan((double)(mouseX / 40.0F));
        float f1 = (float)Math.atan((double)(mouseY / 40.0F));
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale(scale, scale, scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(0.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        if (follow) {
            quaternion.multiply(quaternion1);
        }

        matrixstack.rotate(quaternion);
        if (follow) {
            matrixstack.rotate(Vector3f.YP.rotationDegrees(180.0F + f * 40.0F));
        }

        matrixstack.rotate(Vector3f.XP.rotationDegrees((float)xRot));
        matrixstack.rotate(Vector3f.YP.rotationDegrees((float)yRot));
        matrixstack.rotate(Vector3f.ZP.rotationDegrees((float)zRot));
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        RenderSystem.runAsFancy(() -> {
            IVertexBuilder ivertexbuilder = irendertypebuffer$impl.getBuffer(RenderType.getEntityCutoutNoCull(tex));
            model.resetToDefaultPose();
            model.render(matrixstack, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        });
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        RenderSystem.popMatrix();
    }

    protected void init() {
        super.init();
        this.playBookOpeningSound();
        this.addNextPreviousButtons();
        this.addLinkButtons();
    }

    private void addNextPreviousButtons() {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize + 128) / 2;
        this.buttonPreviousPage = (BookPageButton)this.addButton(new BookPageButton(this, k + 10, l + 180, false, (p_214208_1_) -> {
            this.onSwitchPage(false);
        }, true));
        this.buttonNextPage = (BookPageButton)this.addButton(new BookPageButton(this, k + 365, l + 180, true, (p_214205_1_) -> {
            this.onSwitchPage(true);
        }, true));
    }

    private void addLinkButtons() {
        this.buttons.clear();
        this.children.clear();
        this.addNextPreviousButtons();
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize + 128) / 2;
        Iterator var3 = this.links.iterator();

        while(var3.hasNext()) {
            LinkData linkData = (LinkData)var3.next();
            if (linkData.getPage() == this.currentPageCounter) {
                int maxLength = Math.max(100, Minecraft.getInstance().fontRenderer.getStringWidth(linkData.getTitleText()) + 20);
                this.yIndexesToSkip.add(new Whitespace(linkData.getPage(), linkData.getX() - maxLength / 2, linkData.getY(), 100, 20));
                this.addButton(new Button(k + linkData.getX() - maxLength / 2, l + linkData.getY(), maxLength, 20, new StringTextComponent(linkData.getTitleText()), (p_213021_1_) -> {
                    this.prevPageJSON = this.currentPageJSON;
                    this.currentPageJSON = new ResourceLocation(this.getTextFileDirectory() + linkData.getLinkedPage());
                    this.preservedPageIndex = this.currentPageCounter;
                    this.currentPageCounter = 0;
                    this.addNextPreviousButtons();
                }));
            }
        }

    }

    private void onSwitchPage(boolean next) {
        if (next) {
            if (this.currentPageCounter < this.maxPagesFromPrinting) {
                ++this.currentPageCounter;
            }
        } else if (this.currentPageCounter > 0) {
            --this.currentPageCounter;
        } else if (this.internalPage != null && !this.internalPage.getParent().isEmpty()) {
            this.prevPageJSON = this.currentPageJSON;
            this.currentPageJSON = new ResourceLocation(this.getTextFileDirectory() + this.internalPage.getParent());
            this.currentPageCounter = this.preservedPageIndex;
            this.preservedPageIndex = 0;
        }

        this.refreshSpacing();
    }

    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        int color = this.getBindingColor();
        int r = (color & 16711680) >> 16;
        int g = (color & '\uff00') >> 8;
        int b = color & 255;
        this.renderBackground(matrixStack);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize + 128) / 2;
        this.getMinecraft().getTextureManager().bindTexture(this.getBookPageTexture());
        blit(matrixStack, k, l, 0.0F, 0.0F, this.xSize, this.ySize, this.xSize, this.ySize);
        this.getMinecraft().getTextureManager().bindTexture(this.getBookBindingTexture());
        BookBlit.setRGB(r, g, b, 255);
        BookBlit.func_238463_a_(matrixStack, k, l, 0.0F, 0.0F, this.xSize, this.ySize, this.xSize, this.ySize);
        if (this.internalPage == null || this.currentPageJSON != this.prevPageJSON || this.prevPageJSON == null) {
            this.internalPage = this.generatePage(this.currentPageJSON);
            if (this.internalPage != null) {
                this.refreshSpacing();
            }
        }

        if (this.internalPage != null) {
            matrixStack.push();
            this.renderOtherWidgets(matrixStack, x, y, this.internalPage);
            matrixStack.pop();
            this.writePageText(matrixStack, x, y);
        }

        this.prevPageJSON = this.currentPageJSON;
        super.render(matrixStack, x, y, partialTicks);
    }

    private void refreshSpacing() {
        if (this.internalPage != null) {
            String lang = Minecraft.getInstance().getLanguageManager().getCurrentLanguage().getCode().toLowerCase();
            this.currentPageText = new ResourceLocation(this.getTextFileDirectory() + lang + "/" + this.internalPage.getTextFileToReadFrom());
            boolean invalid = false;

            try {
                IResource var3 = Minecraft.getInstance().getResourceManager().getResource(this.currentPageText);
            } catch (Exception var4) {
                invalid = true;
                Citadel.LOGGER.warn("Could not find language file for translation, defaulting to english");
                this.currentPageText = new ResourceLocation(this.getTextFileDirectory() + "en_us/" + this.internalPage.getTextFileToReadFrom());
            }

            this.readInPageWidgets(this.internalPage);
            this.addWidgetSpacing();
            this.addLinkButtons();
            this.readInPageText(this.currentPageText);
        }

    }

    private Item getItemByRegistryName(String registryName) {
        return (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
    }

    private IRecipe getRecipeByName(String registryName) {
        try {
            RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
            if (manager.getRecipe(new ResourceLocation(registryName)).isPresent()) {
                return (IRecipe)manager.getRecipe(new ResourceLocation(registryName)).get();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return null;
    }

    private void addWidgetSpacing() {
        this.yIndexesToSkip.clear();
        Iterator var1 = this.itemRenders.iterator();

        while(var1.hasNext()) {
            ItemRenderData itemRenderData = (ItemRenderData)var1.next();
            Item item = this.getItemByRegistryName(itemRenderData.getItem());
            if (item != null) {
                this.yIndexesToSkip.add(new Whitespace(itemRenderData.getPage(), itemRenderData.getX(), itemRenderData.getY(), (int)(itemRenderData.getScale() * 17.0D), (int)(itemRenderData.getScale() * 15.0D)));
            }
        }

        var1 = this.recipes.iterator();

        while(var1.hasNext()) {
            RecipeData recipeData = (RecipeData)var1.next();
            IRecipe recipe = this.getRecipeByName(recipeData.getRecipe());
            if (recipe != null) {
                this.yIndexesToSkip.add(new Whitespace(recipeData.getPage(), recipeData.getX(), recipeData.getY() - (int)(recipeData.getScale() * 15.0D), (int)(recipeData.getScale() * 35.0D), (int)(recipeData.getScale() * 60.0D), true));
            }
        }

        var1 = this.images.iterator();

        while(var1.hasNext()) {
            ImageData imageData = (ImageData)var1.next();
            if (imageData != null) {
                this.yIndexesToSkip.add(new Whitespace(imageData.getPage(), imageData.getX(), imageData.getY(), (int)(imageData.getScale() * (double)imageData.getWidth()), (int)(imageData.getScale() * (double)imageData.getHeight() * 0.800000011920929D)));
            }
        }

        if (!this.writtenTitle.isEmpty()) {
            this.yIndexesToSkip.add(new Whitespace(0, 20, 5, 70, 15));
        }

    }

    private void renderOtherWidgets(MatrixStack matrixStack, int x, int y, BookPage page) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize + 128) / 2;
        Iterator var7 = this.itemRenders.iterator();

        float scale;
        while(var7.hasNext()) {
            ItemRenderData itemRenderData = (ItemRenderData)var7.next();
            if (itemRenderData.getPage() == this.currentPageCounter) {
                Item item = this.getItemByRegistryName(itemRenderData.getItem());
                if (item != null) {
                    RenderSystem.pushMatrix();
                    RenderSystem.translatef((float)k, (float)l, 32.0F);
                    scale = (float)itemRenderData.getScale();
                    RenderSystem.scalef(scale, scale, scale);
                    this.itemRenderer.renderItemAndEffectIntoGUI(new ItemStack(item), itemRenderData.getX(), itemRenderData.getY());
                    RenderSystem.popMatrix();
                }
            }
        }

        var7 = this.recipes.iterator();

        while(true) {
            float scale2;
            int i;
            RecipeData recipeData;
            IRecipe recipe;
            int playerTicks;
            do {
                do {
                    if (!var7.hasNext()) {
                        var7 = this.tabulaRenders.iterator();

                        while(var7.hasNext()) {
                            TabulaRenderData tabulaRenderData = (TabulaRenderData)var7.next();
                            if (tabulaRenderData.getPage() == this.currentPageCounter) {
                                TabulaModel model = null;
                                ResourceLocation texture;
                                if (this.textureMap.get(tabulaRenderData.getTexture()) != null) {
                                    texture = (ResourceLocation)this.textureMap.get(tabulaRenderData.getTexture());
                                } else {
                                    texture = (ResourceLocation)this.textureMap.put(tabulaRenderData.getTexture(), new ResourceLocation(tabulaRenderData.getTexture()));
                                }

                                if (this.renderedTabulaModels.get(tabulaRenderData.getModel()) != null) {
                                    model = (TabulaModel)this.renderedTabulaModels.get(tabulaRenderData.getModel());
                                } else {
                                    try {
                                        model = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/" + tabulaRenderData.getModel().split(":")[0] + "/" + tabulaRenderData.getModel().split(":")[1]));
                                    } catch (Exception var16) {
                                        Citadel.LOGGER.warn("Could not load in tabula model for book at " + tabulaRenderData.getModel());
                                    }

                                    this.renderedTabulaModels.put(tabulaRenderData.getModel(), model);
                                }

                                if (model != null && texture != null) {
                                    scale2 = (float)tabulaRenderData.getScale();
                                    drawTabulaModelOnScreen(model, texture, k + tabulaRenderData.getX(), l + tabulaRenderData.getY(), 30.0F * scale2, tabulaRenderData.isFollow_cursor(), tabulaRenderData.getRot_x(), tabulaRenderData.getRot_y(), tabulaRenderData.getRot_z(), (float)(k + tabulaRenderData.getX() - x), (float)(l + tabulaRenderData.getY() - y));
                                }
                            }
                        }

                        var7 = this.entityRenders.iterator();

                        while(var7.hasNext()) {
                            EntityRenderData entityRenderData = (EntityRenderData)var7.next();
                            if (entityRenderData.getPage() == this.currentPageCounter) {
                                Entity model = null;
                                EntityType type = (EntityType)ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityRenderData.getEntity()));
                                if (type != null) {
                                    model = (Entity)this.renderedEntites.putIfAbsent(entityRenderData.getEntity(), type.create(Minecraft.getInstance().world));
                                }

                                if (model != null) {
                                    scale2 = (float)entityRenderData.getScale();
                                    model.ticksExisted = Minecraft.getInstance().player.ticksExisted;
                                    i = k + entityRenderData.getX() - x;
                                    int mouseY = k + entityRenderData.getY() / 2 - y;
                                    drawEntityOnScreen(k + entityRenderData.getX(), l + entityRenderData.getY(), 30.0F * scale2, entityRenderData.isFollow_cursor(), entityRenderData.getRot_x(), entityRenderData.getRot_y(), entityRenderData.getRot_z(), (float)i, (float)mouseY, model);
                                }
                            }
                        }

                        var7 = this.images.iterator();

                        while(var7.hasNext()) {
                            ImageData imageData = (ImageData)var7.next();
                            if (imageData.getPage() == this.currentPageCounter && imageData != null) {
                                ResourceLocation tex = (ResourceLocation)this.textureMap.get(imageData.getTexture());
                                if (tex == null) {
                                    tex = new ResourceLocation(imageData.getTexture());
                                    this.textureMap.put(imageData.getTexture(), tex);
                                }

                                scale2 = (float)imageData.getScale();
                                this.getMinecraft().getTextureManager().bindTexture(tex);
                                matrixStack.push();
                                matrixStack.translate((double)(k + imageData.getX()), (double)(l + imageData.getY()), 0.0D);
                                matrixStack.scale(scale2, scale2, scale2);
                                this.blit(matrixStack, 0, 0, imageData.getU(), imageData.getV(), imageData.getWidth(), imageData.getHeight());
                                matrixStack.pop();
                            }
                        }

                        return;
                    }

                    recipeData = (RecipeData)var7.next();
                } while(recipeData.getPage() != this.currentPageCounter);

                recipe = this.getRecipeByName(recipeData.getRecipe());
                playerTicks = Minecraft.getInstance().player.ticksExisted;
            } while(recipe == null);

            scale2 = (float)recipeData.getScale();
            this.getMinecraft().getTextureManager().bindTexture(this.getBookWidgetTexture());
            matrixStack.push();
            matrixStack.translate((double)(k + recipeData.getX()), (double)(l + recipeData.getY()), 0.0D);
            matrixStack.scale(scale2, scale2, scale2);
            this.blit(matrixStack, 0, 0, 0, 88, 116, 53);
            matrixStack.pop();

            for(i = 0; i < recipe.getIngredients().size(); ++i) {
                Ingredient ing = (Ingredient)recipe.getIngredients().get(i);
                ItemStack stack = ItemStack.EMPTY;
                if (!ing.hasNoMatchingItems()) {
                    if (ing.getMatchingStacks().length > 1) {
                        int currentIndex = (int)((float)playerTicks / 20.0F % (float)ing.getMatchingStacks().length);
                        stack = ing.getMatchingStacks()[currentIndex];
                    } else {
                        stack = ing.getMatchingStacks()[0];
                    }
                }

                if (!stack.isEmpty()) {
                    RenderSystem.pushMatrix();
                    RenderSystem.translatef((float)k, (float)l, 32.0F);
                    RenderSystem.translatef((float)((int)((float)recipeData.getX() + (float)(i % 3 * 20) * scale2)), (float)((int)((float)recipeData.getY() + (float)(i / 3 * 20) * scale2)), 0.0F);
                    RenderSystem.scalef(scale2, scale2, scale2);
                    this.itemRenderer.renderItemAndEffectIntoGUI(stack, 0, 0);
                    RenderSystem.popMatrix();
                }
            }

            RenderSystem.pushMatrix();
            RenderSystem.translatef((float)k, (float)l, 32.0F);
            float finScale = scale2 * 1.5F;
            RenderSystem.translatef((float)recipeData.getX() + 70.0F * finScale, (float)recipeData.getY() + 10.0F * finScale, 0.0F);
            RenderSystem.scalef(finScale, finScale, finScale);
            this.itemRenderer.renderItemAndEffectIntoGUI(recipe.getRecipeOutput(), 0, 0);
            RenderSystem.popMatrix();
        }
    }

    private void writePageText(MatrixStack matrixStack, int x, int y) {
        FontRenderer font = this.font;
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize + 128) / 2;
        if (this.currentPageCounter == 0 && !this.writtenTitle.isEmpty()) {
            String actualTitle = I18n.format(this.writtenTitle, new Object[0]);
            matrixStack.push();
            float scale = 2.0F;
            if (font.getStringWidth(actualTitle) > 80) {
                scale = 2.0F - MathHelper.clamp((float)(font.getStringWidth(actualTitle) - 80) * 0.011F, 0.0F, 1.95F);
            }

            matrixStack.translate((double)(k + 10), (double)(l + 10), 0.0D);
            matrixStack.scale(scale, scale, 1.0F);
            font.drawString(matrixStack, actualTitle, 0.0F, 0.0F, this.getTitleColor());
            matrixStack.pop();
        }

        this.buttonNextPage.visible = this.currentPageCounter < this.maxPagesFromPrinting;
        boolean rootPage = this.currentPageJSON.equals(this.getRootPage());
        this.buttonPreviousPage.visible = this.currentPageCounter > 0 || !rootPage;
        Iterator var11 = this.lines.iterator();

        while(var11.hasNext()) {
            LineData line = (LineData)var11.next();
            if (line.getPage() == this.currentPageCounter) {
                font.drawString(matrixStack, line.getText(), (float)(k + 10 + line.getxIndex()), (float)(l + 10 + line.getyIndex() * 12), this.getTextColor());
            }
        }

    }

    public boolean isPauseScreen() {
        return false;
    }

    protected void playBookOpeningSound() {
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
    }

    protected void playBookClosingSound() {
    }

    protected abstract int getBindingColor();

    protected int getWidgetColor() {
        return this.getBindingColor();
    }

    protected int getTextColor() {
        return 3158064;
    }

    protected int getTitleColor() {
        return 12233880;
    }

    public abstract ResourceLocation getRootPage();

    public abstract String getTextFileDirectory();

    protected ResourceLocation getBookPageTexture() {
        return BOOK_PAGE_TEXTURE;
    }

    protected ResourceLocation getBookBindingTexture() {
        return BOOK_BINDING_TEXTURE;
    }

    protected ResourceLocation getBookWidgetTexture() {
        return BOOK_WIDGET_TEXTURE;
    }

    protected void playPageFlipSound() {
    }

    @Nullable
    protected BookPage generatePage(ResourceLocation res) {
        IResource resource = null;
        BookPage page = null;

        try {
            resource = Minecraft.getInstance().getResourceManager().getResource(res);

            try {
                resource = Minecraft.getInstance().getResourceManager().getResource(res);
                InputStream inputstream = resource.getInputStream();
                Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                page = BookPage.deserialize(reader);
            } catch (IOException var6) {
                var6.printStackTrace();
            }

            return page;
        } catch (IOException var7) {
            return null;
        }
    }

    protected void readInPageWidgets(BookPage page) {
        this.links.clear();
        this.itemRenders.clear();
        this.recipes.clear();
        this.tabulaRenders.clear();
        this.entityRenders.clear();
        this.images.clear();
        this.links.addAll(page.getLinkedButtons());
        this.itemRenders.addAll(page.getItemRenders());
        this.recipes.addAll(page.getRecipes());
        this.tabulaRenders.addAll(page.getTabulaRenders());
        this.entityRenders.addAll(page.getEntityRenders());
        this.images.addAll(page.getImages());
        this.writtenTitle = page.getTitle();
    }

    protected void readInPageText(ResourceLocation res) {
        IResource resource = null;
        int xIndex = 0;
        int actualTextX = 0;
        int yIndex = 0;

        try {
            resource = Minecraft.getInstance().getResourceManager().getResource(res);

            try {
                List<String> readStrings = IOUtils.readLines(resource.getInputStream(), StandardCharsets.UTF_8);
                this.linesFromJSON = readStrings.size();
                this.lines.clear();
                List<String> splitBySpaces = new ArrayList();
                Iterator var8 = readStrings.iterator();

                while(var8.hasNext()) {
                    String line = (String)var8.next();
                    splitBySpaces.addAll(Arrays.asList(line.split(" ")));
                }

                String lineToPrint = "";
                this.linesFromPrinting = 0;
                int page = 0;

                label130:
                for(int i = 0; i < splitBySpaces.size(); ++i) {
                    String word = (String)splitBySpaces.get(i);
                    int cutoffPoint = xIndex > 100 ? 30 : 35;
                    boolean newline = word.equals("<NEWLINE>");
                    Iterator var14 = this.yIndexesToSkip.iterator();

                    while(true) {
                        int buttonX;
                        int buttonY;
                        int height;
                        do {
                            do {
                                do {
                                    while(true) {
                                        Whitespace indexes;
                                        int indexPage;
                                        do {
                                            if (!var14.hasNext()) {
                                                boolean last = i == splitBySpaces.size() - 1;
                                                actualTextX += word.length() + 1;
                                                if (lineToPrint.length() + word.length() + 1 < cutoffPoint && !newline) {
                                                    lineToPrint = lineToPrint + " " + word;
                                                    if (last) {
                                                        ++this.linesFromPrinting;
                                                        this.lines.add(new LineData(xIndex, yIndex, lineToPrint, page));
                                                        ++yIndex;
                                                        actualTextX = 0;
                                                        if (newline) {
                                                            ++yIndex;
                                                        }
                                                    }
                                                } else {
                                                    ++this.linesFromPrinting;
                                                    if (yIndex > 13) {
                                                        if (xIndex > 0) {
                                                            ++page;
                                                            xIndex = 0;
                                                            yIndex = 0;
                                                        } else {
                                                            xIndex = 200;
                                                            yIndex = 0;
                                                        }
                                                    }

                                                    if (last) {
                                                        lineToPrint = lineToPrint + " " + word;
                                                    }

                                                    this.lines.add(new LineData(xIndex, yIndex, lineToPrint, page));
                                                    ++yIndex;
                                                    actualTextX = 0;
                                                    if (newline) {
                                                        ++yIndex;
                                                    }

                                                    lineToPrint = "" + (word.equals("<NEWLINE>") ? "" : word);
                                                }
                                                continue label130;
                                            }

                                            indexes = (Whitespace)var14.next();
                                            indexPage = indexes.getPage();
                                        } while(indexPage != page);

                                        buttonX = indexes.getX();
                                        buttonY = indexes.getY();
                                        int width = indexes.getWidth();
                                        height = indexes.getHeight();
                                        if (indexes.isDown()) {
                                            break;
                                        }

                                        if ((float)yIndex >= (float)(buttonY - height) / 12.0F && (float)yIndex <= (float)(buttonY + height) / 12.0F && (buttonX < 90 && xIndex < 90 || buttonX >= 90 && xIndex >= 90)) {
                                            ++yIndex;
                                        }
                                    }
                                } while((float)yIndex < (float)buttonY / 12.0F);
                            } while((float)yIndex > (float)(buttonY + height) / 12.0F);
                        } while((buttonX >= 90 || xIndex >= 90) && (buttonX < 90 || xIndex < 90));

                        yIndex += 2;
                    }
                }

                this.maxPagesFromPrinting = page;
            } catch (Exception var21) {
                var21.printStackTrace();
            }
        } catch (IOException var22) {
            Citadel.LOGGER.warn("Could not load in page .txt from json from page, page: " + res.toString());
        }

    }
}
