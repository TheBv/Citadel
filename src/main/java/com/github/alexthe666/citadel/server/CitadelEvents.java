//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.alexthe666.citadel.server;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.config.ServerConfig;
import com.github.alexthe666.citadel.server.entity.EntityDataCapabilityImplementation;
import com.github.alexthe666.citadel.server.entity.EntityDataHandler;
import com.github.alexthe666.citadel.server.entity.EntityProperties;
import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.citadel.server.entity.IEntityData;
import com.github.alexthe666.citadel.server.entity.PropertiesTracker;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.entity.player.PlayerEvent.StopTracking;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CitadelEvents {
    private int updateTimer;

    public CitadelEvents() {
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public void onAttachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(new ResourceLocation("citadel", "extended_entity_data_citadel"), new ICapabilitySerializable() {
            private final LazyOptional<IEntityData> holder = LazyOptional.of(() -> {
                return new EntityDataCapabilityImplementation();
            });

            public INBT serializeNBT() {
                Capability<IEntityData> capability = Citadel.ENTITY_DATA_CAPABILITY;
                IEntityData instance = (IEntityData)capability.getDefaultInstance();
                instance.init((Entity)event.getObject(), ((Entity)event.getObject()).getEntityWorld(), false);
                return capability.getStorage().writeNBT(capability, instance, (Direction)null);
            }

            public void deserializeNBT(INBT nbt) {
                Capability<IEntityData> capability = Citadel.ENTITY_DATA_CAPABILITY;
                IEntityData instance = (IEntityData)capability.getDefaultInstance();
                instance.init((Entity)event.getObject(), ((Entity)event.getObject()).getEntityWorld(), true);
                capability.getStorage().readNBT(capability, instance, (Direction)null, nbt);
            }

            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
                return capability == Citadel.ENTITY_DATA_CAPABILITY ? Citadel.ENTITY_DATA_CAPABILITY.orEmpty(capability, this.holder).cast() : LazyOptional.empty();
            }
        });
    }

    @SubscribeEvent
    public void onEntityDestroyed(EntityLeaveWorldEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) {
            EntityDataHandler.INSTANCE.stopTracking(event.getEntity());
        }

    }

    @SubscribeEvent
    public void onEntityConstructing(EntityConstructing event) {
        if (ServerConfig.citadelEntityTrack) {
            boolean cached = EntityPropertiesHandler.INSTANCE.hasEntityInCache(event.getEntity().getClass());
            List<String> entityPropertiesIDCache = !cached ? new ArrayList() : null;
            EntityPropertiesHandler.INSTANCE.getRegisteredProperties().filter((propEntry) -> {
                return ((Class)propEntry.getKey()).isAssignableFrom(event.getEntity().getClass());
            }).forEach((propEntry) -> {
                Iterator var4 = ((List)propEntry.getValue()).iterator();

                while(var4.hasNext()) {
                    Class propClass = (Class)var4.next();

                    try {
                        Constructor<? extends EntityProperties> constructor = propClass.getConstructor();
                        EntityProperties prop = (EntityProperties)constructor.newInstance();
                        String propID = prop.getID();
                        EntityDataHandler.INSTANCE.registerExtendedEntityData(event.getEntity(), prop);
                        if (!cached) {
                            entityPropertiesIDCache.add(propID);
                        }
                    } catch (Exception var9) {
                        var9.printStackTrace();
                    }
                }

            });
            if (!cached) {
                EntityPropertiesHandler.INSTANCE.addEntityToCache(event.getEntity().getClass(), entityPropertiesIDCache);
            }
        }

    }

    @SubscribeEvent
    public void onEntityUpdate(LivingUpdateEvent event) {
        if (!event.getEntity().world.isRemote && event.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
            List<PropertiesTracker<?>> trackers = EntityPropertiesHandler.INSTANCE.getEntityTrackers(player);
            if (trackers != null && trackers.size() > 0) {
                boolean hasPlayer = false;
                Iterator var5 = trackers.iterator();

                PropertiesTracker tracker;
                while(var5.hasNext()) {
                    tracker = (PropertiesTracker)var5.next();
                    if (hasPlayer = tracker.getEntity() == player) {
                        break;
                    }
                }

                if (!hasPlayer) {
                    EntityPropertiesHandler.INSTANCE.addTracker(player, player);
                }

                var5 = trackers.iterator();

                while(var5.hasNext()) {
                    tracker = (PropertiesTracker)var5.next();
                    tracker.updateTracker();
                    if (tracker.isTrackerReady()) {
                        tracker.onSync();
                        PropertiesMessage message = new PropertiesMessage(tracker.getProperties(), tracker.getEntity());
                        Citadel.sendNonLocal(message, player);
                    }
                }
            }

        }
    }

    @SubscribeEvent
    public void onEntityUpdateDebug(LivingUpdateEvent event) {
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
            EntityPropertiesHandler.INSTANCE.addTracker(player, player);
        }

    }

    @SubscribeEvent
    public void onEntityStartTracking(StartTracking event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            EntityPropertiesHandler.INSTANCE.addTracker((ServerPlayerEntity)event.getPlayer(), event.getTarget());
        }

    }

    @SubscribeEvent
    public void onEntityStopTracking(StopTracking event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            EntityPropertiesHandler.INSTANCE.removeTracker((ServerPlayerEntity)event.getPlayer(), event.getTarget());
        }

    }

    @SubscribeEvent
    public void onServerTickEvent(ServerTickEvent event) {
        if (event.phase == Phase.END && ServerConfig.citadelEntityTrack) {
            ++this.updateTimer;
            if (this.updateTimer > 20) {
                this.updateTimer = 0;
                Iterator iterator = EntityPropertiesHandler.INSTANCE.getTrackerIterator();

                while(true) {
                    label30:
                    while(iterator.hasNext()) {
                        Entry<ServerPlayerEntity, List<PropertiesTracker<?>>> trackerEntry = (Entry)iterator.next();
                        ServerPlayerEntity player = (ServerPlayerEntity)trackerEntry.getKey();
                        ServerWorld playerWorld = (ServerWorld)player.world;
                        if (player != null && !player.removed && playerWorld != null) {
                            Iterator it = ((List)trackerEntry.getValue()).iterator();

                            while(true) {
                                PropertiesTracker tracker;
                                Entity entity;
                                ServerWorld entityWorld;
                                do {
                                    if (!it.hasNext()) {
                                        continue label30;
                                    }

                                    tracker = (PropertiesTracker)it.next();
                                    entity = tracker.getEntity();
                                    entityWorld = (ServerWorld)player.world;
                                } while(entity != null && !entity.removed && entityWorld != null);

                                it.remove();
                                tracker.removeTracker();
                            }
                        } else {
                            iterator.remove();
                            (trackerEntry.getValue()).forEach(PropertiesTracker::removeTracker);
                        }
                    }

                    return;
                }
            }
        }

    }
}
