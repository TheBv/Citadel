//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.alexthe666.citadel.server.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.Entity;

public enum EntityDataHandler {
    INSTANCE;

    private Map<Entity, List<IEntityData<?>>> registeredEntityData = new WeakIdentityHashMap();

    private EntityDataHandler() {
    }

    public <T extends Entity> void registerExtendedEntityData(T entity, IEntityData<T> entityData) {
        List<IEntityData<?>> registered = (List)this.registeredEntityData.get(entity);
        if (registered == null) {
            registered = new ArrayList();
        }

        if (!((List)registered).contains(entityData)) {
            ((List)registered).add(entityData);
        }

        this.registeredEntityData.put(entity, registered);
    }

    public <T extends Entity> void stopTracking(T entity) {
        this.registeredEntityData.remove(entity);
    }

    public <T extends Entity> IEntityData<T> getEntityData(T entity, String identifier) {
        List<IEntityData<T>> managers = this.getEntityData(entity);
        if (managers != null) {
            Iterator var4 = managers.iterator();

            while(var4.hasNext()) {
                IEntityData manager = (IEntityData)var4.next();
                if (manager.getID().equals(identifier)) {
                    return manager;
                }
            }
        }

        return null;
    }

    public <T extends Entity> List<IEntityData<T>> getEntityData(T entity) {
        return (List)(this.registeredEntityData.containsKey(entity) ? (List)this.registeredEntityData.get(entity) : new ArrayList());
    }
}
