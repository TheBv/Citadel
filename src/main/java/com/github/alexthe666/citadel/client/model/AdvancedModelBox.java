package com.github.alexthe666.citadel.client.model;

import com.github.alexthe666.citadel.client.model.TabulaModelRenderUtils.PositionTextureVertex;
import com.github.alexthe666.citadel.client.model.TabulaModelRenderUtils.TexturedQuad;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.matrix.MatrixStack.Entry;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.IVertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import jdk.nashorn.internal.codegen.CompilerConstants;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.spongepowered.tools.obfuscation.mirror.FieldHandle;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;
/**
 * An enhanced RendererModel
 *
 * @author gegy1000
 * @since 1.0.0
 */
@OnlyIn(Dist.CLIENT)
public class AdvancedModelBox extends ModelRenderer {
    public float defaultRotationX;
    public float defaultRotationY;
    public float defaultRotationZ;
    public float defaultOffsetX;
    public float defaultOffsetY;
    public float defaultOffsetZ;
    public float defaultPositionX;
    public float defaultPositionY;
    public float defaultPositionZ;
    public float scaleX;
    public float scaleY;
    public float scaleZ;
    public int textureOffsetX;
    public int textureOffsetY;
    public boolean scaleChildren;
    private AdvancedEntityModel model;
    private AdvancedModelBox parent;
    private int displayList;
    private boolean compiled;
    public ObjectList<com.github.alexthe666.citadel.client.model.TabulaModelRenderUtils.ModelBox> cubeList;
    public ObjectList<ModelRenderer> childModels;
    private float textureWidth;
    private float textureHeight;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    public String boxName;
    public AdvancedModelBox(AdvancedEntityModel model, String name) {
        super(model);
        this.scaleX = 1.0F;
        this.scaleY = 1.0F;
        this.scaleZ = 1.0F;
        this.boxName = "";
        this.textureWidth = (float)model.textureWidth;
        this.textureHeight = (float)model.textureHeight;
        this.model = model;
        this.cubeList = new ObjectArrayList();
        this.childModels = new ObjectArrayList();
        this.boxName = name;
    }

    static ObjIntConsumer setterLambda(final Lookup caller,
                                       final MethodHandle setterHandle) throws Throwable {

        final Class<?> functionKlaz = ObjIntConsumer.class;
        final String functionName = "accept";
        final Class<?> functionReturn = void.class;
        final Class<?>[] functionParams = new Class<?>[] { Object.class,
                int.class };

        final MethodType factoryMethodType = MethodType
                .methodType(functionKlaz);
        final MethodType functionMethodType = MethodType.methodType(
                functionReturn, functionParams);

        final CallSite setterFactory = LambdaMetafactory.metafactory( //
                caller, // Represents a lookup context.
                functionName, // The name of the method to implement.
                factoryMethodType, // Signature of the factory method.
                functionMethodType, // Signature of function implementation.
                setterHandle, // Function method implementation.
                setterHandle.type() // Function method type signature.
        );

        final MethodHandle setterInvoker = setterFactory.getTarget();

        final ObjIntConsumer setterLambda = (ObjIntConsumer) setterInvoker
                .invokeExact();

        return setterLambda;
    }

    public AdvancedModelBox(AdvancedEntityModel model) {
        this(model, (String)null);
        this.textureWidth = (float)model.textureWidth;
        this.textureHeight = (float)model.textureHeight;
        this.cubeList = new ObjectArrayList();
        this.childModels = new ObjectArrayList();
    }

    public AdvancedModelBox(AdvancedEntityModel model, int textureOffsetX, int textureOffsetY) {
        this(model);
        this.textureWidth = (float)model.textureWidth;
        this.textureHeight = (float)model.textureHeight;
        this.setTextureOffset(textureOffsetX, textureOffsetY);
        this.cubeList = new ObjectArrayList();
        this.childModels = new ObjectArrayList();
    }

    public ModelRenderer setTextureSize(int p_78787_1_, int p_78787_2_) {
        this.textureWidth = (float)p_78787_1_;
        this.textureHeight = (float)p_78787_2_;
        return this;
    }

    public ModelRenderer addBox(String p_217178_1_, float p_217178_2_, float p_217178_3_, float p_217178_4_, int p_217178_5_, int p_217178_6_, int p_217178_7_, float p_217178_8_, int p_217178_9_, int p_217178_10_) {
        this.setTextureOffset(p_217178_9_, p_217178_10_);
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_217178_2_, p_217178_3_, p_217178_4_, (float)p_217178_5_, (float)p_217178_6_, (float)p_217178_7_, p_217178_8_, p_217178_8_, p_217178_8_, this.mirror, false);
        return this;
    }

    public ModelRenderer addBox(float p_228300_1_, float p_228300_2_, float p_228300_3_, float p_228300_4_, float p_228300_5_, float p_228300_6_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228300_1_, p_228300_2_, p_228300_3_, p_228300_4_, p_228300_5_, p_228300_6_, 0.0F, 0.0F, 0.0F, this.mirror, false);
        return this;
    }

    public ModelRenderer addBox(float p_228304_1_, float p_228304_2_, float p_228304_3_, float p_228304_4_, float p_228304_5_, float p_228304_6_, boolean p_228304_7_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228304_1_, p_228304_2_, p_228304_3_, p_228304_4_, p_228304_5_, p_228304_6_, 0.0F, 0.0F, 0.0F, p_228304_7_, false);
        return this;
    }

    public void addBox(float p_228301_1_, float p_228301_2_, float p_228301_3_, float p_228301_4_, float p_228301_5_, float p_228301_6_, float p_228301_7_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228301_1_, p_228301_2_, p_228301_3_, p_228301_4_, p_228301_5_, p_228301_6_, p_228301_7_, p_228301_7_, p_228301_7_, this.mirror, false);
    }

    public void addBox(float p_228302_1_, float p_228302_2_, float p_228302_3_, float p_228302_4_, float p_228302_5_, float p_228302_6_, float p_228302_7_, float p_228302_8_, float p_228302_9_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228302_1_, p_228302_2_, p_228302_3_, p_228302_4_, p_228302_5_, p_228302_6_, p_228302_7_, p_228302_8_, p_228302_9_, this.mirror, false);
    }

    public void addBox(float p_228303_1_, float p_228303_2_, float p_228303_3_, float p_228303_4_, float p_228303_5_, float p_228303_6_, float p_228303_7_, boolean p_228303_8_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228303_1_, p_228303_2_, p_228303_3_, p_228303_4_, p_228303_5_, p_228303_6_, p_228303_7_, p_228303_7_, p_228303_7_, p_228303_8_, false);
    }

    private void addBox(int p_228305_1_, int p_228305_2_, float p_228305_3_, float p_228305_4_, float p_228305_5_, float p_228305_6_, float p_228305_7_, float p_228305_8_, float p_228305_9_, float p_228305_10_, float p_228305_11_, boolean p_228305_12_, boolean p_228305_13_) {
        this.cubeList.add(new TabulaModelRenderUtils.ModelBox(p_228305_1_, p_228305_2_, p_228305_3_, p_228305_4_, p_228305_5_, p_228305_6_, p_228305_7_, p_228305_8_, p_228305_9_, p_228305_10_, p_228305_11_, p_228305_12_, this.textureWidth, this.textureHeight));
    }


    /**
     * If true, when using setScale, the children of this model part will be scaled as well as just this part. If false, just this part will be scaled.
     *
     * @param scaleChildren true if this parent should scale the children
     * @since 1.1.0
     */
    public void setShouldScaleChildren(boolean scaleChildren) {
        this.scaleChildren = scaleChildren;
    }

    /**
     * Sets the scale for this AdvancedModelBox to be rendered at. (Performs a call to GLStateManager.scale()).
     *
     * @param scaleX the x scale
     * @param scaleY the y scale
     * @param scaleZ the z scale
     * @since 1.1.0
     */
    public void setScale(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
    }

    /**
     * Sets this RendererModel's default pose to the current pose.
     */
    public void updateDefaultPose() {
        this.defaultRotationX = this.rotateAngleX;
        this.defaultRotationY = this.rotateAngleY;
        this.defaultRotationZ = this.rotateAngleZ;

       // this.defaultOffsetX = this.offsetX;
       // this.defaultOffsetY = this.offsetY;
       // this.defaultOffsetZ = this.offsetZ;

        this.defaultPositionX = this.rotationPointX;
        this.defaultPositionY = this.rotationPointY;
        this.defaultPositionZ = this.rotationPointZ;
    }

    /**
     * Sets the current pose to the previously set default pose.
     */
    public void resetToDefaultPose() {
        this.rotateAngleX = this.defaultRotationX;
        this.rotateAngleY = this.defaultRotationY;
        this.rotateAngleZ = this.defaultRotationZ;

       // this.offsetX = this.defaultOffsetX;
       // this.offsetY = this.defaultOffsetY;
       // this.offsetZ = this.defaultOffsetZ;

        this.rotationPointX = this.defaultPositionX;
        this.rotationPointY = this.defaultPositionY;
        this.rotationPointZ = this.defaultPositionZ;
    }

    @Override
    public void addChild(ModelRenderer child) {
        super.addChild(child);
        this.childModels.add(child);
        if (child instanceof AdvancedModelBox) {
            AdvancedModelBox advancedChild = (AdvancedModelBox) child;
            advancedChild.setParent(this);
        }
    }

    /**
     * @return the parent of this box
     */
    public AdvancedModelBox getParent() {
        return this.parent;
    }

    /**
     * Sets the parent of this box
     *
     * @param parent the new parent
     */
    public void setParent(AdvancedModelBox parent) {
        this.parent = parent;
    }

    /**
     * Post renders this box with all its parents
     *
     * @param scale the render scale
     */
    public void parentedPostRender(float scale) {
        if (this.parent != null) {
            this.parent.parentedPostRender(scale);
        }
       // this.postRender(scale);
    }

    /**
     * Renders this box with all it's parents
     *
     * @param scale the render scale
     */
    public void renderWithParents(float scale) {
        if (this.parent != null) {
            this.parent.renderWithParents(scale);
        }
        //this.render(scale);
    }

    public void translateRotate(MatrixStack matrixStackIn) {
        matrixStackIn.translate((double)(this.rotationPointX / 16.0F), (double)(this.rotationPointY / 16.0F), (double)(this.rotationPointZ / 16.0F));
        if (this.rotateAngleZ != 0.0F) {
            matrixStackIn.rotate(Vector3f.ZP.rotation(this.rotateAngleZ));
        }

        if (this.rotateAngleY != 0.0F) {
            matrixStackIn.rotate(Vector3f.YP.rotation(this.rotateAngleY));
        }

        if (this.rotateAngleX != 0.0F) {
            matrixStackIn.rotate(Vector3f.XP.rotation(this.rotateAngleX));
        }

        matrixStackIn.scale(this.scaleX, this.scaleY, this.scaleZ);
    }

    @Override
    public void render(MatrixStack p_228309_1_, IVertexBuilder p_228309_2_, int p_228309_3_, int p_228309_4_, float p_228309_5_, float p_228309_6_, float p_228309_7_, float p_228309_8_) {
        if (this.showModel) {
            if (!this.cubeList.isEmpty() || !this.childModels.isEmpty()) {
                p_228309_1_.push();
                this.translateRotate(p_228309_1_);
                try {
                    this.doRender(p_228309_1_.getLast(), p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                ObjectListIterator var9 = this.childModels.iterator();
                if(!scaleChildren){
                    p_228309_1_.scale(1F / Math.max(this.scaleX, 0.0001F), 1F / Math.max(this.scaleY, 0.0001F) , 1F / Math.max(this.scaleZ, 0.0001F));
                }
                while (var9.hasNext()) {
                    ModelRenderer lvt_10_1_ = (ModelRenderer) var9.next();
                    lvt_10_1_.render(p_228309_1_, p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
                }

                p_228309_1_.pop();
            }
        }
    }
    public int getSize() {
        int other = 0;
        for (TabulaModelRenderUtils.ModelBox modelBox : this.cubeList){
            other += modelBox.quads.length *4;
        }
        return this.cubeList.size()* other;
    }
    private void doRender(Entry p_228306_1_, IVertexBuilder p_228306_2_, int lightmapUV, int overlayUV, float red, float green, float blue, float p_228306_8_) {
        BufferBuilder bufferBuilder = (BufferBuilder) p_228306_2_;
        bufferBuilder.growBuffer(getSize()*bufferBuilder.getVertexFormat().getSize());
        boolean fullFormat = bufferBuilder.fullFormat;
        Matrix4f lvt_9_1_ = p_228306_1_.getMatrix();
        Matrix3f lvt_10_1_ = p_228306_1_.getNormal();
        ObjectListIterator var11 = this.cubeList.iterator();
        while(var11.hasNext()) {
            TabulaModelRenderUtils.ModelBox lvt_12_1_ = (TabulaModelRenderUtils.ModelBox)var11.next();
            TexturedQuad[] var13 = lvt_12_1_.quads;
            int var14 = var13.length;

            for(int var15 = 0; var15 < var14; ++var15) {
                TexturedQuad lvt_16_1_ = var13[var15];
                Vector3f lvt_17_1_ = lvt_16_1_.normal.copy();
                lvt_17_1_.transform(lvt_10_1_);
                float lvt_18_1_ = lvt_17_1_.getX();
                float lvt_19_1_ = lvt_17_1_.getY();
                float lvt_20_1_ = lvt_17_1_.getZ();

                for(int lvt_21_1_ = 0; lvt_21_1_ < 4; ++lvt_21_1_) {
                    PositionTextureVertex lvt_22_1_ = lvt_16_1_.vertexPositions[lvt_21_1_];
                    float lvt_23_1_ = lvt_22_1_.position.getX() / 16.0F;
                    float lvt_24_1_ = lvt_22_1_.position.getY() / 16.0F;
                    float lvt_25_1_ = lvt_22_1_.position.getZ() / 16.0F;
                    Vector4f lvt_26_1_ = new Vector4f(lvt_23_1_, lvt_24_1_, lvt_25_1_, 1.0F);
                    lvt_26_1_.transform(lvt_9_1_);
                    bufferBuilder.putFloat(0, lvt_26_1_.getX());
                    bufferBuilder.putFloat(4, lvt_26_1_.getY());
                    bufferBuilder.putFloat(8, lvt_26_1_.getZ());
                    bufferBuilder.putByte(12, (byte) ((int) (red * 255.0F)));
                    bufferBuilder.putByte(13, (byte) ((int) (green * 255.0F)));
                    bufferBuilder.putByte(14, (byte) ((int) (blue * 255.0F)));
                    bufferBuilder.putByte(15, (byte) ((int) (p_228306_8_ * 255.0F)));
                    bufferBuilder.putFloat(16, lvt_22_1_.textureU);
                    bufferBuilder.putFloat(20, lvt_22_1_.textureV);
                    int i;
                    if (fullFormat) {
                        bufferBuilder.putShort(24, (short) (overlayUV & '\uffff'));
                        bufferBuilder.putShort(26, (short) (lightmapUV >> 16 & '\uffff'));
                        i = 28;
                    } else {
                        i = 24;
                    }

                    bufferBuilder.putShort(i + 0, (short) (lightmapUV & '\uffff'));
                    bufferBuilder.putShort(i + 2, (short) (lightmapUV >> 16 & '\uffff'));
                    bufferBuilder.putByte(i + 4, IVertexConsumer.normalInt(lvt_18_1_));
                    bufferBuilder.putByte(i + 5, IVertexConsumer.normalInt(lvt_19_1_));
                    bufferBuilder.putByte(i + 6, IVertexConsumer.normalInt(lvt_20_1_));
                    bufferBuilder.nextElementBytes += i+8;
                    bufferBuilder.vertexCount++;
                }
            }
        }

    }

    public AdvancedEntityModel getModel() {
        return this.model;
    }

    private float calculateRotation(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
        float movementScale = this.model.getMovementScale();
        float rotation = (MathHelper.cos(f * (speed * movementScale) + offset) * (degree * movementScale) * f1) + (weight * f1);
        return invert ? -rotation : rotation;
    }

    /**
     * Rotates this box back and forth (rotateAngleX). Useful for arms and legs.
     *
     * @param speed      is how fast the model runs
     * @param degree     is how far the box will rotate;
     * @param invert     will invert the rotation
     * @param offset     will offset the timing of the model
     * @param weight     will make the model favor one direction more based on how fast the mob is moving
     * @param walk       is the walked distance
     * @param walkAmount is the walk speed
     */
    public void walk(float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount) {
        this.rotateAngleX += this.calculateRotation(speed, degree, invert, offset, weight, walk, walkAmount);
    }

    /**
     * Rotates this box up and down (rotateAngleZ). Useful for wing and ears.
     *
     * @param speed      is how fast the model runs
     * @param degree     is how far the box will rotate;
     * @param invert     will invert the rotation
     * @param offset     will offset the timing of the model
     * @param weight     will make the model favor one direction more based on how fast the mob is moving
     * @param flap       is the flapped distance
     * @param flapAmount is the flap speed
     */
    public void flap(float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount) {
        this.rotateAngleZ += this.calculateRotation(speed, degree, invert, offset, weight, flap, flapAmount);
    }

    /**
     * Rotates this box side to side (rotateAngleY).
     *
     * @param speed       is how fast the model runs
     * @param degree      is how far the box will rotate;
     * @param invert      will invert the rotation
     * @param offset      will offset the timing of the model
     * @param weight      will make the model favor one direction more based on how fast the mob is moving
     * @param swing       is the swung distance
     * @param swingAmount is the swing speed
     */
    public void swing(float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount) {
        this.rotateAngleY += this.calculateRotation(speed, degree, invert, offset, weight, swing, swingAmount);
    }

    /**
     * Moves this box up and down (rotationPointY). Useful for bodies.
     *
     * @param speed  is how fast the model runs;
     * @param degree is how far the box will move;
     * @param bounce will make the box bounce;
     * @param f      is the walked distance;
     * @param f1     is the walk speed.
     */
    public void bob(float speed, float degree, boolean bounce, float f, float f1) {
        float movementScale = this.model.getMovementScale();
        degree *= movementScale;
        speed *= movementScale;
        float bob = (float) (Math.sin(f * speed) * f1 * degree - f1 * degree);
        if (bounce) {
            bob = (float) -Math.abs((Math.sin(f * speed) * f1 * degree));
        }
        this.rotationPointY += bob;
    }

    @Override
    public AdvancedModelBox setTextureOffset(int textureOffsetX, int textureOffsetY) {
        this.textureOffsetX = textureOffsetX;
        this.textureOffsetY = textureOffsetY;
        return this;
    }

    public void transitionTo(AdvancedModelBox to, float timer, float maxTime) {
        this.rotateAngleX += ((to.rotateAngleX - this.rotateAngleX) / maxTime) * timer;
        this.rotateAngleY += ((to.rotateAngleY - this.rotateAngleY) / maxTime) * timer;
        this.rotateAngleZ += ((to.rotateAngleZ - this.rotateAngleZ) / maxTime) * timer;

        this.rotationPointX += ((to.rotationPointX - this.rotationPointX) / maxTime) * timer;
        this.rotationPointY += ((to.rotationPointY - this.rotationPointY) / maxTime) * timer;
        this.rotationPointZ += ((to.rotationPointZ - this.rotationPointZ) / maxTime) * timer;

        this.offsetX += ((to.offsetX - this.offsetX) / maxTime) * timer;
        this.offsetY += ((to.offsetY - this.offsetY) / maxTime) * timer;
        this.offsetZ += ((to.offsetZ - this.offsetZ) / maxTime) * timer;
    }


}