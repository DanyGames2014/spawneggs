package net.danygames2014.spawneggs.api.event;

import net.danygames2014.spawneggs.ColorizationHandler;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;

@SuppressWarnings("UnstableApiUsage")
@EventPhases(StationAPI.INTERNAL_PHASE)
public class SpawnEggColorizationEvent extends Event {

    public SpawnEggColorizationEvent() {
    }

    /**
     * Registers custom color for a spawn egg using Java int color (Bit 0-7 is Blue, Bit 8-15 is Green, Bit 16-23 is Red)
     * These values can be 0 - 16777215
     * @param entity Registry name of the entity to register egg colors for
     * @param outerLayer Color of the Outer Layer (Border, Smiley Face)
     * @param innerLayer Color of the Inner Layer (The Base Layer in the Middle)
     * @param innerLayerOverlay Color of the Inner Overlay (Layer containing spots that are overlay ontop of the inner layer)
     * @return Whether the registration was succesfull
     */
    public boolean registerSpawnEggColorInt(String entity, int outerLayer, int innerLayer, int innerLayerOverlay){
        return ColorizationHandler.registerSpawnEggColorInt(entity, outerLayer, innerLayer, innerLayerOverlay);
    }

    /**
     * Registers custom color for a spawn egg using Hex Color representation
     * These values can be 0x000000 - 0xFFFFFF
     * @param entity Registry name of the entity to register egg colors for
     * @param outerLayer Color of the Outer Layer (Border, Smiley Face)
     * @param innerLayer Color of the Inner Layer (The Base Layer in the Middle)
     * @param innerLayerOverlay Color of the Inner Overlay (Layer containing spots that are overlay ontop of the inner layer)
     * @return Whether the registration was succesfull
     */
    public boolean registerSpawnEggColorHex(String entity, int outerLayer, int innerLayer, int innerLayerOverlay){
        return ColorizationHandler.registerSpawnEggColorHex(entity, outerLayer, innerLayer, innerLayerOverlay);
    }
}
