package net.danygames2014.spawneggs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

    /**
     * Some Mixin magic by mine_diver to access all registered mobs
     * @author mine_diver
     */
    @Mixin(EntityRegistry.class)
    public interface EntityRegistryAccessor {
        @Accessor("classToId")
        static Map<Class<? extends Entity>, String> getEntities() {
            return Util.assertMixin();
        }

        @Accessor("idToClass")
        static Map<String, Class<? extends Entity>> getStringToIdMap() {
            return Util.assertMixin();
        }
    }

