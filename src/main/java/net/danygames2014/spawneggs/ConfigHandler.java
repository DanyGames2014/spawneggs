package net.danygames2014.spawneggs;


import net.glasslauncher.mods.api.gcapi.api.*;

public class ConfigHandler {
    @GConfig(value = "config", visibleName = "Spawn Eggs Config")
    public static final Config config = new Config();

    public static class Config {
        @ConfigName("Consume Spawn Egg when used")
        @MultiplayerSynced
        @LongDescription("If true the spawn egg will be consumed when used")
        public Boolean consumeSpawnEgg = true;
        @ConfigName("Allow Spawning by Right-clicking Air")
        @MultiplayerSynced
        @LongDescription("If true and the player right-clicks in the air the mob will be spawned above the player")
        public Boolean allowSpawnInAir = false;
        @ConfigName("Removed Invalid Spawn Eggs")
        @MultiplayerSynced
        @LongDescription("If true and an exception is raised upon spawning the entity, the spawn egg will be removed from the player's inventory")
        public Boolean removeInvalidSpawnEggs = true;
        @ConfigName("Register Dev Sword (Milos)")
        @LongDescription("If true the Dev Sword will be registered. It has a damage of over 9000 and when right-clicked ALL loaded entities will be killed, if sneaking that will include items")
        public Boolean allowDevSword = true;
        @ConfigName("Attempt to localize unlocalized entity names")
        @LongDescription("If true and no localization for the entity is present in the lang file, the registry name will be used. If false it will remain unlocalized")
        public Boolean attemptLocalization = true;
        @ConfigName("Blacklisted Entities (Registry names)")
        public String[] blacklistedEntities = new String[]{
                "Item",
                "Painting",
                "Mob",
                "FallingSand"
        };

    }
}
