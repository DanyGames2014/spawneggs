package net.danygames2014.spawneggs.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateSwordItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class DevSwordItem extends TemplateSwordItem {
    public DevSwordItem(Identifier identifier) {
        super(identifier, ToolMaterial.DIAMOND);
    }

    @Override
    public boolean postHit(ItemStack arg, LivingEntity arg2, LivingEntity arg3) {
        return true;
    }

    @Override
    public boolean postMine(ItemStack arg, int i, int j, int k, int l, LivingEntity arg2) {
        return true;
    }

    @Override
    public int getAttackDamage(Entity attackedEntity) {
        return 9999;
    }

    @Override
    public ItemStack use(ItemStack item, World world, PlayerEntity player) {
        if(world.isRemote){
            return item;
        }

        for (int i = 0; i < world.entities.size(); i++){
            Entity entity = (Entity) world.entities.get(i);
            if(!(entity instanceof PlayerEntity) && !(!player.isSneaking() && entity instanceof ItemEntity)){
                entity.damage(player,9000);
            }
        }
        return super.use(item,world,player);
    }
}
