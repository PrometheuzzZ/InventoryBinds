package pj.inventorybinds.ru.config;

import com.mojang.datafixers.util.Pair;

import static pj.inventorybinds.ru.InventoryBinds.MOD_ID;


public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static String BUTTONS;

    public static String defaultButtons = "{\n" +
            "  \"buttons\":[\n" +
            "    {\n" +
            "      \"name\":\"Template Command\",\n" +
            "      \"bind\":\"/spawn\",\n" +
            "      \"item_id\":\"grass_block\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\":\"Template Suggest Command\",\n" +
            "      \"bind\":\"!ban\",\n" +
            "      \"item_id\":\"grass_block\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\":\"Template Chat Message\",\n" +
            "      \"bind\":\"Hello World\",\n" +
            "      \"item_id\":\"grass_block\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    public static void registerConfigs() {
        configs = new ModConfigProvider();

        createConfigs();

        CONFIG = SimpleConfig.of(MOD_ID).provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {


        configs.addKeyValuePair(new Pair<>("BUTTONS", defaultButtons), "");

    }

    private static void assignConfigs() {
        BUTTONS = CONFIG.getOrDefault("BUTTONS", defaultButtons);


        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }
}