package com.github.alexthe666.citadel.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Floats;
import com.mojang.blaze3d.vertex.DefaultColorVertexBuilder;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.IVertexConsumer;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.IntArrays;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.BitSet;
import java.util.List;

public class BufferBuilderImproved extends DefaultColorVertexBuilder implements IVertexConsumer  {
    private static final Logger LOGGER = LogManager.getLogger();
    public ByteBuffer byteBuffer;
    private final List<BufferBuilderImproved.DrawState> drawStates = Lists.newArrayList();
    private int drawStateIndex = 0;
    private int renderedBytes = 0;
    private int nextElementBytes = 0;
    private int uploadedBytes = 0;
    private int vertexCount;
    @Nullable
    private VertexFormatElement vertexFormatElement;
    private int vertexFormatIndex;
    private int drawMode;
    private VertexFormat vertexFormat;
    private boolean fastFormat;
    private boolean fullFormat;
    private boolean isDrawing;

    public BufferBuilderImproved(int bufferSizeIn) {
        this.byteBuffer = GLAllocation.createDirectByteBuffer(bufferSizeIn * 4);
    }

    protected void growBuffer() {
        this.growBuffer(this.vertexFormat.getSize());
    }

    private void growBuffer(int increaseAmount) {
        if (this.nextElementBytes + increaseAmount > this.byteBuffer.capacity()) {
            int i = this.byteBuffer.capacity();
            int j = i + roundUpPositive(increaseAmount);
            LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", i, j);
            ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer(j);
            ((Buffer)this.byteBuffer).position(0);
            bytebuffer.put(this.byteBuffer);
            ((Buffer)bytebuffer).rewind();
            this.byteBuffer = bytebuffer;
        }
    }

    private static int roundUpPositive(int xIn) {
        int i = 2097152;
        if (xIn == 0) {
            return i;
        } else {
            if (xIn < 0) {
                i *= -1;
            }

            int j = xIn % i;
            return j == 0 ? xIn : xIn + i - j;
        }
    }

    public void sortVertexData(float cameraX, float cameraY, float cameraZ) {
        ((Buffer)this.byteBuffer).clear();
        FloatBuffer floatbuffer = this.byteBuffer.asFloatBuffer();
        int i = this.vertexCount / 4;
        float[] afloat = new float[i];

        for(int j = 0; j < i; ++j) {
            afloat[j] = getDistanceSq(floatbuffer, cameraX, cameraY, cameraZ, this.vertexFormat.getIntegerSize(), this.renderedBytes / 4 + j * this.vertexFormat.getSize());
        }

        int[] aint = new int[i];

        for(int k = 0; k < aint.length; aint[k] = k++) {
        }

        IntArrays.mergeSort(aint, (p_227830_1_, p_227830_2_) -> {
            return Floats.compare(afloat[p_227830_2_], afloat[p_227830_1_]);
        });
        BitSet bitset = new BitSet();
        FloatBuffer floatbuffer1 = GLAllocation.createDirectFloatBuffer(this.vertexFormat.getIntegerSize() * 4);

        for(int l = bitset.nextClearBit(0); l < aint.length; l = bitset.nextClearBit(l + 1)) {
            int i1 = aint[l];
            if (i1 != l) {
                this.limitToVertex(floatbuffer, i1);
                ((Buffer)floatbuffer1).clear();
                floatbuffer1.put(floatbuffer);
                int j1 = i1;

                for(int k1 = aint[i1]; j1 != l; k1 = aint[k1]) {
                    this.limitToVertex(floatbuffer, k1);
                    FloatBuffer floatbuffer2 = floatbuffer.slice();
                    this.limitToVertex(floatbuffer, j1);
                    floatbuffer.put(floatbuffer2);
                    bitset.set(j1);
                    j1 = k1;
                }

                this.limitToVertex(floatbuffer, l);
                ((Buffer)floatbuffer1).flip();
                floatbuffer.put(floatbuffer1);
            }

            bitset.set(l);
        }

    }

    private void limitToVertex(FloatBuffer floatBufferIn, int indexIn) {
        int i = this.vertexFormat.getIntegerSize() * 4;
        ((Buffer)floatBufferIn).limit(this.renderedBytes / 4 + (indexIn + 1) * i);
        ((Buffer)floatBufferIn).position(this.renderedBytes / 4 + indexIn * i);
    }

    public BufferBuilderImproved.State getVertexState() {
        ((Buffer)this.byteBuffer).limit(this.nextElementBytes);
        ((Buffer)this.byteBuffer).position(this.renderedBytes);
        ByteBuffer bytebuffer = ByteBuffer.allocate(this.vertexCount * this.vertexFormat.getSize());
        bytebuffer.put(this.byteBuffer);
        ((Buffer)this.byteBuffer).clear();
        return new BufferBuilderImproved.State(bytebuffer, this.vertexFormat);
    }

    private static float getDistanceSq(FloatBuffer floatBufferIn, float x, float y, float z, int integerSize, int offset) {
        float f = floatBufferIn.get(offset + integerSize * 0 + 0);
        float f1 = floatBufferIn.get(offset + integerSize * 0 + 1);
        float f2 = floatBufferIn.get(offset + integerSize * 0 + 2);
        float f3 = floatBufferIn.get(offset + integerSize * 1 + 0);
        float f4 = floatBufferIn.get(offset + integerSize * 1 + 1);
        float f5 = floatBufferIn.get(offset + integerSize * 1 + 2);
        float f6 = floatBufferIn.get(offset + integerSize * 2 + 0);
        float f7 = floatBufferIn.get(offset + integerSize * 2 + 1);
        float f8 = floatBufferIn.get(offset + integerSize * 2 + 2);
        float f9 = floatBufferIn.get(offset + integerSize * 3 + 0);
        float f10 = floatBufferIn.get(offset + integerSize * 3 + 1);
        float f11 = floatBufferIn.get(offset + integerSize * 3 + 2);
        float f12 = (f + f3 + f6 + f9) * 0.25F - x;
        float f13 = (f1 + f4 + f7 + f10) * 0.25F - y;
        float f14 = (f2 + f5 + f8 + f11) * 0.25F - z;
        return f12 * f12 + f13 * f13 + f14 * f14;
    }

    public void setVertexState(BufferBuilderImproved.State state) {
        ((Buffer)state.stateByteBuffer).clear();
        int i = state.stateByteBuffer.capacity();
        this.growBuffer(i);
        ((Buffer)this.byteBuffer).limit(this.byteBuffer.capacity());
        ((Buffer)this.byteBuffer).position(this.renderedBytes);
        this.byteBuffer.put(state.stateByteBuffer);
        ((Buffer)this.byteBuffer).clear();
        VertexFormat vertexformat = state.stateVertexFormat;
        this.setVertexFormat(vertexformat);
        this.vertexCount = i / vertexformat.getSize();
        this.nextElementBytes = this.renderedBytes + this.vertexCount * vertexformat.getSize();
    }

    public void begin(int glMode, VertexFormat format) {
        if (this.isDrawing) {
            throw new IllegalStateException("Already building!");
        } else {
            this.isDrawing = true;
            this.drawMode = glMode;
            this.setVertexFormat(format);
            this.vertexFormatElement = format.getElements().get(0);
            this.vertexFormatIndex = 0;
            ((Buffer)this.byteBuffer).clear();
        }
    }

    public void setVertexFormat(VertexFormat vertexFormatIn) {
        if (this.vertexFormat != vertexFormatIn) {
            this.vertexFormat = vertexFormatIn;
            boolean flag = vertexFormatIn == DefaultVertexFormats.ENTITY;
            boolean flag1 = vertexFormatIn == DefaultVertexFormats.BLOCK;
            this.fastFormat = flag || flag1;
            this.fullFormat = flag;
        }
    }

    public void finishDrawing() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not building!");
        } else {
            this.isDrawing = false;
            this.drawStates.add(new BufferBuilderImproved.DrawState(this.vertexFormat, this.vertexCount, this.drawMode));
            this.renderedBytes += this.vertexCount * this.vertexFormat.getSize();
            this.vertexCount = 0;
            this.vertexFormatElement = null;
            this.vertexFormatIndex = 0;
        }
    }

    public void putByte(int indexIn, byte byteIn) {
        this.byteBuffer.put(this.nextElementBytes + indexIn, byteIn);
    }

    public void putShort(int indexIn, short shortIn) {
        this.byteBuffer.putShort(this.nextElementBytes + indexIn, shortIn);
    }

    public void putFloat(int indexIn, float floatIn) {
        this.byteBuffer.putFloat(this.nextElementBytes + indexIn, floatIn);
    }

    public void endVertex() {
        if (this.vertexFormatIndex != 0) {
            throw new IllegalStateException("Not filled all elements of the vertex");
        } else {
            ++this.vertexCount;
            this.growBuffer();
        }
    }

    public void nextVertexFormatIndex() {
        ImmutableList<VertexFormatElement> immutablelist = this.vertexFormat.getElements();
        this.vertexFormatIndex = (this.vertexFormatIndex + 1) % immutablelist.size();
        this.nextElementBytes += this.vertexFormatElement.getSize();
        VertexFormatElement vertexformatelement = immutablelist.get(this.vertexFormatIndex);
        this.vertexFormatElement = vertexformatelement;
        if (vertexformatelement.getUsage() == VertexFormatElement.Usage.PADDING) {
            this.nextVertexFormatIndex();
        }

        if (this.defaultColor && this.vertexFormatElement.getUsage() == VertexFormatElement.Usage.COLOR) {
            IVertexConsumer.super.color(this.defaultRed, this.defaultGreen, this.defaultBlue, this.defaultAlpha);
        }

    }

    public IVertexBuilder color(int red, int green, int blue, int alpha) {
        if (this.defaultColor) {
            throw new IllegalStateException();
        } else {
            return IVertexConsumer.super.color(red, green, blue, alpha);
        }
    }

    public void addVertex(Vertex vertex){
        addVertex(vertex.x, vertex.y, vertex.z, vertex.red, vertex.green, vertex.blue, vertex.alpha, vertex.texU, vertex.texV, vertex.overlayUV, vertex.lightmapUV, vertex.normalX, vertex.normalY, vertex.normalZ);
    }
    /*public void addVertex(float x, float y, float z, float red, float green, float blue, float alpha, float texU, float texV, int overlayUV, int lightmapUV, float normalX, float normalY, float normalZ) {
        if (this.defaultColor) {
            throw new IllegalStateException();
        } else if (this.fastFormat) {
            this.putFloat(0, x);
            this.putFloat(4, y);
            this.putFloat(8, z);
            this.putByte(12, (byte)((int)(red * 255.0F)));
            this.putByte(13, (byte)((int)(green * 255.0F)));
            this.putByte(14, (byte)((int)(blue * 255.0F)));
            this.putByte(15, (byte)((int)(alpha * 255.0F)));
            this.putFloat(16, texU);
            this.putFloat(20, texV);
            int i;
            if (this.fullFormat) {
                this.putShort(24, (short)(overlayUV & '\uffff'));
                this.putShort(26, (short)(overlayUV >> 16 & '\uffff'));
                i = 28;
            } else {
                i = 24;
            }

            this.putShort(i + 0, (short)(lightmapUV & '\uffff'));
            this.putShort(i + 2, (short)(lightmapUV >> 16 & '\uffff'));
            this.putByte(i + 4, IVertexConsumer.normalInt(normalX));
            this.putByte(i + 5, IVertexConsumer.normalInt(normalY));
            this.putByte(i + 6, IVertexConsumer.normalInt(normalZ));
            this.nextElementBytes += i + 8;
            this.endVertex();
        } else {
            super.addVertex(x, y, z, red, green, blue, alpha, texU, texV, overlayUV, lightmapUV, normalX, normalY, normalZ);
        }
    }*/

    public Pair<BufferBuilderImproved.DrawState, ByteBuffer> getNextBuffer() {
        BufferBuilderImproved.DrawState bufferbuilder$drawstate = this.drawStates.get(this.drawStateIndex++);
        ((Buffer)this.byteBuffer).position(this.uploadedBytes);
        this.uploadedBytes += bufferbuilder$drawstate.getVertexCount() * bufferbuilder$drawstate.getFormat().getSize();
        ((Buffer)this.byteBuffer).limit(this.uploadedBytes);
        if (this.drawStateIndex == this.drawStates.size() && this.vertexCount == 0) {
            this.reset();
        }

        ByteBuffer bytebuffer = this.byteBuffer.slice();
        bytebuffer.order(this.byteBuffer.order()); // FORGE: Fix incorrect byte order
        ((Buffer)this.byteBuffer).clear();
        return Pair.of(bufferbuilder$drawstate, bytebuffer);
    }

    public void reset() {
        if (this.renderedBytes != this.uploadedBytes) {
            LOGGER.warn("Bytes mismatch " + this.renderedBytes + " " + this.uploadedBytes);
        }

        this.discard();
    }

    public void discard() {
        this.renderedBytes = 0;
        this.uploadedBytes = 0;
        this.nextElementBytes = 0;
        this.drawStates.clear();
        this.drawStateIndex = 0;
        this.vertexCount =0;
    }

    public VertexFormatElement getCurrentElement() {
        if (this.vertexFormatElement == null) {
            throw new IllegalStateException("BufferBuilder not started");
        } else {
            return this.vertexFormatElement;
        }
    }

    public boolean isDrawing() {
        return this.isDrawing;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class DrawState {
        private final VertexFormat format;
        private final int vertexCount;
        private final int drawMode;

        private DrawState(VertexFormat formatIn, int vertexCountIn, int drawModeIn) {
            this.format = formatIn;
            this.vertexCount = vertexCountIn;
            this.drawMode = drawModeIn;
        }

        public VertexFormat getFormat() {
            return this.format;
        }

        public int getVertexCount() {
            return this.vertexCount;
        }

        public int getDrawMode() {
            return this.drawMode;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class State {
        public final ByteBuffer stateByteBuffer;
        private final VertexFormat stateVertexFormat;

        private State(ByteBuffer byteBufferIn, VertexFormat vertexFormatIn) {
            this.stateByteBuffer = byteBufferIn;
            this.stateVertexFormat = vertexFormatIn;
        }
    }

    // Forge start
    public void putBulkData(ByteBuffer buffer) {
        growBuffer(buffer.limit() + this.vertexFormat.getSize());
        ((Buffer)this.byteBuffer).position(this.vertexCount * this.vertexFormat.getSize());
        this.byteBuffer.put(buffer);
        this.vertexCount += buffer.limit() / this.vertexFormat.getSize();
        this.nextElementBytes += buffer.limit();
    }

    public VertexFormat getVertexFormat() { return this.vertexFormat; }

    public void addVertexList(List<Vertex> vertexList){
        for (Vertex vertex : vertexList){
            addVertex(vertex.x, vertex.y, vertex.z, vertex.red, vertex.green, vertex.blue, vertex.alpha, vertex.texU, vertex.texV, vertex.overlayUV, vertex.lightmapUV, vertex.normalX, vertex.normalY, vertex.normalZ);
        }

    }
    @Override
    public void addVertex(float x, float y, float z, float red, float green, float blue, float alpha, float texU, float texV, int overlayUV, int lightmapUV, float normalX, float normalY, float normalZ){
        try {
            if (this.defaultColor) {
                throw new IllegalStateException();
            } else if (fastFormat) {
                this.putFloat(0, x);
                this.putFloat(4, y);
                this.putFloat(8, z);
                this.putByte(12, (byte) ((int) (red * 255.0F)));
                this.putByte(13, (byte) ((int) (green * 255.0F)));
                this.putByte(14, (byte) ((int) (blue * 255.0F)));
                this.putByte(15, (byte) ((int) (alpha * 255.0F)));
                this.putFloat(16, texU);
                this.putFloat(20, texV);
                int i;
                if (fastFormat) {
                    this.putShort(24, (short) (overlayUV & '\uffff'));
                    this.putShort(26, (short) (overlayUV >> 16 & '\uffff'));
                    i = 28;
                } else {
                    i = 24;
                }

                this.putShort(i + 0, (short) (lightmapUV & '\uffff'));
                this.putShort(i + 2, (short) (lightmapUV >> 16 & '\uffff'));
                this.putByte(i + 4, IVertexConsumer.normalInt(normalX));
                this.putByte(i + 5, IVertexConsumer.normalInt(normalY));
                this.putByte(i + 6, IVertexConsumer.normalInt(normalZ));
                nextElementBytes += i + 8;
                ++this.vertexCount;
                //this.endVertex();
            } else {
                System.out.print("NotGood");
                //super.addVertex(x, y, z, red, green, blue, alpha, texU, texV, overlayUV, lightmapUV, normalX, normalY, normalZ);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void endVertex(int amount) {
        try {
            Field vertexCountField =this.getClass().getDeclaredField("vertexCount");
            vertexCountField.setAccessible(true);
            int vertexCount = vertexCountField.getInt(this);
            vertexCount += amount;
            vertexCountField.setInt(this,vertexCount);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public static class Vertex{
        float x;
        float y;
        float z;
        float red;
        float green;
        float blue;
        float alpha;
        float texU;
        float texV;
        int overlayUV;
        int lightmapUV;
        float normalX;
        float normalY;
        float normalZ;
        Vertex(float x, float y, float z, float red, float green, float blue, float alpha, float texU, float texV, int overlayUV, int lightmapUV, float normalX, float normalY, float normalZ) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
            this.texU = texU;
            this.texV = texV;
            this.overlayUV = overlayUV;
            this.lightmapUV = lightmapUV;
            this.normalX = normalX;
            this.normalY = normalY;
            this.normalZ = normalZ;
        }
    }
}
