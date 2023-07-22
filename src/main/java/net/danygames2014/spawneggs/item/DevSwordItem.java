package net.danygames2014.spawneggs.item;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Item;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplateSword;

public class DevSwordItem extends TemplateSword {
    public DevSwordItem(Identifier identifier) {
        super(identifier, ToolMaterial.field_1691);
    }

    @Override
    public boolean postHit(ItemInstance arg, Living arg2, Living arg3) {
        return true;
    }

    @Override
    public boolean postMine(ItemInstance arg, int i, int j, int k, int l, Living arg2) {
        return true;
    }

    @Override
    public int getAttack(EntityBase arg) {
        return 9999;
    }

    @Override
    public ItemInstance use(ItemInstance item, Level world, PlayerBase player) {
        if(world.isServerSide){
            return item;
        }

        for (int i = 0; i < world.entities.size(); i++){
            EntityBase entity = (EntityBase) world.entities.get(i);
            if(!(entity instanceof PlayerBase) && !(!player.method_1373() && entity instanceof Item)){
                entity.damage(player,9000);
            }
        }
        return super.use(item,world,player);
    }
}
