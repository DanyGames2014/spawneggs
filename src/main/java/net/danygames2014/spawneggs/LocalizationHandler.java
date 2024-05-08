package net.danygames2014.spawneggs;

import net.danygames2014.spawneggs.mixin.TranslationStorageAccessor;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.Properties;

/**
 * This class handles the localization and registering of localized names
 */
public class LocalizationHandler {

    // Translations
    public static TranslationStorage translationStorage = TranslationStorage.getInstance();
    public static Properties translations;

    /**
     * Tries to use reflection to access the translation list from Translation Storage, if an exception is thrown, makes a new empty list
     */
    static {
        translations = ((TranslationStorageAccessor) translationStorage).getTranslations();
    }

    /**
     * Generates a localization for a spawn egg using the lang file for the specified entity and register the localization into the TranslationStorage
     *
     * @param spawnedEntity Registry name of the entity to localize a spawnegg for
     */
    public static void registerSpawnEggLocalization(String spawnedEntity) {
        // By default the localization for spawn egg is "%s Spawn Egg" and each entity can be localized separately,
        // this function forms the localization by combining the spawn egg localization with the individual mob
        // localization as they're registered, if it cannot find a localization it will check whetever it should
        // attempt to localize it, in which case it will take the mob's registry name

        /// Check if the mod lang file contains a localization for the mob
        // If localization is present, it will be used
        if (translations.containsKey("entity.spawneggs." + spawnedEntity.toLowerCase() + ".name")) {
            translations.put(
                    // [Key] Translation Key
                    "item.spawneggs." + spawnedEntity.toLowerCase() + "_spawn_egg.name",

                    // [Value] Translated Name
                    // Replaces the %s in spawn egg localization with the localized entity name
                    String.format(
                            // Generic Spawn Egg Name ( Example : %s Spawn Egg )
                            translations.getProperty("item.spawneggs.spawn_egg.name"),
                            // Entity Name ( Example: Zombie )
                            translations.getProperty("entity.spawneggs." + spawnedEntity.toLowerCase() + ".name")
                    )
            );
            // If localization is not present, the config will be checked to determine whetever localization should be attempted from the registry name
        } else {
            // Check if localization should be attempted
            if (ConfigHandler.config.attemptLocalization) {
                try {
                    translations.put(
                            // [Key] Translation Key
                            "item.spawneggs." + spawnedEntity.toLowerCase() + "_spawn_egg.name",

                            // [Value] Translated Name
                            // Replaces the %s in spawn egg localization with the localized entity name
                            String.format(
                                    // Generic Spawn Egg Name ( Example : %s Spawn Egg )
                                    translations.getProperty("item.spawneggs.spawn_egg.name", "%s Spawn Egg"),
                                    // Entity Name ( Example: Zombie )
                                    spawnedEntity
                            )
                    );
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Generates a localization for a spawn egg using supplied localization for the specified entity and register the localization into the TranslationStorage
     *
     * @param spawnedEntity             Registry name of the entity to localize a spawnegg for
     * @param spawnedEntityLocalization Localized name to show inthe spawn egg name
     * @return If the action was succesfull
     */
    public static boolean registerSpawnEggLocalization(String spawnedEntity, String spawnedEntityLocalization) {
        // Check if translation doesnt already exist
        if (translations.containsKey("item.spawneggs." + spawnedEntity.toLowerCase() + "_spawn_egg.name")) {
            return false;
        }

        translations.put(
                // [Key] Translation Key
                "item.spawneggs." + spawnedEntity.toLowerCase() + "_spawn_egg.name",

                // [Value] Translated Name
                // Replaces the %s in spawn egg localization with the localized entity name
                String.format(
                        // Generic Spawn Egg Name ( Example : %s Spawn Egg )
                        translations.getProperty("item.spawneggs.spawn_egg.name"),
                        // Entity Name ( Example: Zombie )
                        spawnedEntityLocalization
                )
        );

        return true;
    }
}
