package pj.inventorybinds.ru.gui.buttons;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.logging.Logger;

public class DynamicTextureManager {

    private static ArrayList<DynamicTextureObject> dynamicTextureObjectArrayList = new ArrayList<>();

    private static class DynamicTextureObject{

        private String hash;
        private Identifier identifier;

        DynamicTextureObject(String hash, Identifier identifier){
            this.hash = hash;
            this.identifier = identifier;
        }

        public Identifier getIdentifier() {
            return identifier;
        }

        public String getHash() {
            return hash;
        }

    }

    public static void addDynamicTexture(String hash, Identifier identifier){

        if(!checkHash(hash)) {
            dynamicTextureObjectArrayList.add(new DynamicTextureObject(hash, identifier));
        }

    }

    public static boolean checkHash(String hash){
        for(DynamicTextureObject dynamicTextureObject : dynamicTextureObjectArrayList){
            if(dynamicTextureObject.getHash().contains(hash)){
                return true;
            }
        }

        return false;
    }

    public static Identifier getTextureByHash(String hash){
        for(DynamicTextureObject dynamicTextureObject : dynamicTextureObjectArrayList){
            if(dynamicTextureObject.getHash().contains(hash)){
                return dynamicTextureObject.getIdentifier();
            }
        }

        return null;
    }


}
