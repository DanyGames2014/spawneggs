package net.danygames2014.eggtest;

import net.danygames2014.spawneggs.api.event.SpawnEggColorizationEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class ColorizationListener {
    @EventListener
    public void colorize(SpawnEggColorizationEvent event) {
        System.out.println("COLORIZE!");
        event.registerSpawnEggColorHex("Pig", 0xFF0000, 0x00FF00, 0x0000FF);
    }
}
