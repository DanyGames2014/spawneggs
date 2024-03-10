package net.danygames2014.spawneggs.events.init;

import net.danygames2014.spawneggs.SpawnEggs;
import net.danygames2014.spawneggs.item.SpawnEggItem;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import paulevs.bhcreative.api.CreativeTab;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

public class CreativeListener {
    public static CreativeTab spawnEggsTab;

    @EventListener
    public void onTabInit(TabRegistryEvent event){
        spawnEggsTab = new SimpleTab(SpawnEggs.MOD_ID.id("spawneggs"), SpawnEggs.spawnEggs.get(0));
        event.register(spawnEggsTab);
        for (SpawnEggItem item : SpawnEggs.spawnEggs){
            spawnEggsTab.addItem(new ItemStack(item, 1));
        }
    }
}
