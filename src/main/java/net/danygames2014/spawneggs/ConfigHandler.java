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

        @ConfigEntry(name = "Blacklisted Entities (Registry names)")
        public String[] blacklistedEntities = new String[]{
                "Item",
                "Painting",
                "Mob",
                "FallingSand"
        };
    }
}
