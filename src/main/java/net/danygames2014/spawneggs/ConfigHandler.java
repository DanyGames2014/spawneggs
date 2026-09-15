package net.danygames2014.spawneggs;


import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;

public class ConfigHandler {
    @ConfigRoot(value = "config", visibleName = "Spawn Eggs Config")
    public static final Config config = new Config();

    public static class Config {
        @ConfigEntry(name = "Consume Spawn Egg when used", longDescription = "If true the spawn egg will be consumed when used", multiplayerSynced = true)
        public Boolean consumeSpawnEgg = true;

        @ConfigEntry(name = "Allow Spawning by Right-clicking Air", longDescription = "If true and the player right-clicks in the air the mob will be spawned above the player", multiplayerSynced = true)
        public Boolean allowSpawnInAir = false;

        @ConfigEntry(name = "Removed Invalid Spawn Eggs", longDescription = "If true and an exception is raised upon spawning the entity, the spawn egg will be removed from the player's inventory", multiplayerSynced = true)
        public Boolean removeInvalidSpawnEggs = true;

        @ConfigEntry(name = "Register Dev Sword (Milos)", longDescription = "If true the Dev Sword will be registered. It has a damage of over 9000 and when right-clicked ALL loaded entities will be killed, if sneaking that will include items")
        public Boolean allowDevSword = true;

        @ConfigEntry(name = "Attempt to localize unlocalized entity names", longDescription = "If true and no localization for the entity is present in the lang file, the registry name will be used. If false it will remain unlocalized")
        public Boolean attemptLocalization = true;

        @ConfigEntry(name = "Automatic Spawn Egg Colorization", longDescription = "Allows the automatic calculation of spawn egg colors from the entity's texture, if its available")
        public Boolean automaticEggColorization = true;

        @ConfigEntry(name = "Blacklisted Entities (Registry names)")
        public String[] blacklistedEntities = new String[]{
                "Item", // Crashes
                "Painting", // Crashes
                "Mob", // Does Nothing
                "FallingSand", // Crashes
                "aether:AetherLightning", // Unnecessary
                "aether:CloudParachute", // Does nothing
                "aether:EnchantedDart", // Unnecessary
                "aether:FlamingArrow", // Unnecessary
                "aether:FloatingBlock", // Crashes
                "aether:GoldenDart", // Unnecessary
                "aether:HomeShot", // Crashes
                "aether:LightningKnife", // Unnecessary
                "aether:Minicloud", // Unnecessary
                "aether:NotchWave", // Bugged entity
                "aether:PoisonDart", // Unnecessary
                "aether:PoisonNeedle", // Unnecessary
                "aether:Slider", // Bugged entity
                "aether:ZephyrSnowball", // Bugged entity
                "battletower:GolemFireball", // Bugged entity
                "battletower:TowerGolem", // Bugged entity
                "battletower:TowerGolem_old", // Bugged entity
                "buildcraft:travelling_item", // Crashes
                "buildcraft:robot", // Does nothing
                "buildcraft:mechanical_arm", // Does nothing
                "buildcraft:block", // Does nothing
                "buildcraft:block_with_parent", // Does nothing
                "logisticspipes:routed_item", // Crashes
                "elementalarrows:egg_arrow", // Unnecessary
                "elementalarrows:explosive_arrow", // Unnecessary
                "elementalarrows:fire_arrow", // Unnecessary
                "elementalarrows:ice_arrow", // Unnecessary
                "elementalarrows:lighting_arrow", // Unnecessary
                "elementalarrows:torch_arrow", // Unnecessary
                "tropicraft:poison_blot", // Unnecessary
                "wolves:BroadheadArrow", // Unnecessary
                "wolves:BlockLiftedByPlatform", // Crashes
                "wolves:FallingAnvil", // Does nothing
                "wolves:MovingPlatform", // Unnecessary
                "wolves:MovingAnchor", // Unnecessary
                "wolves:StapiTEST", // Unnecessary
                "wolves:WaterWheel", // Unnecessary
                "wolves:WindMill", // Unnecessary
        };
    }
}
