package net.danygames2014.spawneggs.events.init;

import net.danygames2014.spawneggs.SpawnEggs;
import net.danygames2014.spawneggs.item.SpawnEggItem;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemInstance;
import paulevs.bhcreative.api.CreativeTab;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

public class CreativeListener {
    public static CreativeTab spawnEggsTab;

    @EventListener
    public void onTabInit(TabRegistryEvent event){
        spawnEggsTab = new SimpleTab(SpawnEggs.MOD_ID.id("spawneggs"), ItemListener.spawnEggs.get(0));
        event.register(spawnEggsTab);
        for (SpawnEggItem item : ItemListener.spawnEggs){
            spawnEggsTab.addItem(new ItemInstance(item, 1));
        }
    }
}