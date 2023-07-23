package net.minecraft.src;

public class ItemSpawnEgg<T extends Entity> extends Item {

    public int spawnedEntity;

    protected ItemSpawnEgg(int i1, int spawnedEntity) {
        super(i1);
        this.spawnedEntity = spawnedEntity;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack1, EntityPlayer entityPlayer2, World world3, int i4, int i5, int i6, int i7) {
        if(ModLoader.getMinecraftInstance().isMultiplayerWorld()){

        }else{
            try {
                // Create Entity
                Entity entity = EntityList.createEntity(spawnedEntity, world3);

                // Determine coordinates according to block side
                double x = i4; double y = i5; double z = i6;
                switch (i7){
                    case 0: y-=entity.height; break; // BOTTOM (Y--)
                    case 1: y++; x+=0.5; z+=0.5; break; // TOP (Y++)
                    case 2: z-=0.5; x+=0.5; break; // SIDE (Z--)
                    case 3: z+=1.5; x+=0.5; break; // SIDE (Z++)
                    case 4: x-=0.5; z+=0.5; break; // SIDE (X--)
                    case 5: x+=1.5; z+=0.5; break; // SIDE (X++)
                    default: y++; break;
                }

                // Set the Entity position
                entity.setPosition(x, y, z);

                // Spawn the Entity
                world3.entityJoinedWorld(entity);

                // If spawning was successful and consuming eggs is enabled, consume the Spawn Egg
                if(mod_spawnEggs.consumeSpawnEgg){
                    itemStack1.stackSize--;
                }

            } catch (Exception e) {
                System.out.println("Error when spawning Entity! \n" + e.getMessage());
            }
        }


        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
        if(mod_spawnEggs.allowSpawnInAir){
            onItemUse(itemStack1, entityPlayer3, world2, (int) entityPlayer3.posX, (int) entityPlayer3.posY, (int) entityPlayer3.posZ, 6);
        }

        return super.onItemRightClick(itemStack1, world2, entityPlayer3);
    }

    @Override
    public String getItemName() {
        return super.getItemName();
    }
}
