package pj.inventorybinds.ru.gui.buttons;
import net.minecraft.util.Identifier;
import java.util.HashMap;

import static pj.inventorybinds.ru.InventoryBinds.MOD_ID;

public class DynamicTextureManager {

    private static HashMap<String, Identifier> stringIdentifierHashMap = new HashMap<>();



    public static void addDynamicTexture(String hash, Identifier identifier) {

        if (!checkHash(hash)) {
            stringIdentifierHashMap.put(hash, identifier);
        }

    }

    public static void updateDynamicTexture(String hash, Identifier identifier) {

        if (checkHash(hash)) {

            stringIdentifierHashMap.replace(hash, identifier);

        } else {

            addDynamicTexture(hash, identifier);

        }

    }

    public static boolean checkHash(String hash) {
        return stringIdentifierHashMap.containsKey(hash);
    }

    public static Identifier getTextureByHash(String hash) {

        if (checkHash(hash)) {
            return stringIdentifierHashMap.get(hash);
        } else {
            return Identifier.of(MOD_ID, "textures/gui/missing_item.png");
        }


    }


}
