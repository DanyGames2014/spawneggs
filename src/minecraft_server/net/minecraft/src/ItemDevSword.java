package net.minecraft.src;

public class ItemDevSword extends ItemSword{
    public ItemDevSword(int i1, EnumToolMaterial enumToolMaterial2) {
        super(i1, EnumToolMaterial.EMERALD);
    }

    @Override
    public int getDamageVsEntity(Entity entity1) {
        return 9000;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
        for (int i = 0; i < world2.loadedEntityList.size(); i++) {
            Entity entity = (Entity)world2.loadedEntityList.get(i);
            if(!(entity instanceof EntityPlayer) && !(!entityPlayer3.isSneaking() && entity instanceof EntityItem)){
                entity.attackEntityFrom(entityPlayer3,9000);
            }
        }

        return super.onItemRightClick(itemStack1, world2, entityPlayer3);
    }
}
