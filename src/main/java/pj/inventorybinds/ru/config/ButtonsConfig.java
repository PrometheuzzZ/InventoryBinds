package pj.inventorybinds.ru.config;

import com.google.gson.Gson;
import pj.inventorybinds.ru.config.buttons.ButtonsList;


public class ButtonsConfig {

    private static ButtonsList buttonsList;

    public static void loadButtonsFromConfig(){

        Gson gson  = new Gson();
        ButtonsList buttonsConfig1;

        buttonsConfig1 = gson.fromJson(ModConfigs.BUTTONS, ButtonsList.class);


        buttonsList = buttonsConfig1;
    }

    public static ButtonsList getButtonsList(){
        return buttonsList;
    }

}
