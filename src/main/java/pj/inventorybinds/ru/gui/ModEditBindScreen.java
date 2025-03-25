package pj.inventorybinds.ru.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import pj.inventorybinds.ru.config.ButtonsConfig;
import pj.inventorybinds.ru.config.buttons.ButtonJson;

import static pj.inventorybinds.ru.config.ButtonsConfig.addButtonToButtonsList;
import static pj.inventorybinds.ru.config.ButtonsConfig.editButtonById;

public class ModEditBindScreen extends LightweightGuiDescription {

    static WTextField bindName;
    static WTextField bindCommand;
    static WTextField bindIcon;
    static WToggleButton serverBind;
    static ButtonJson buttonJson;

    public ModEditBindScreen(ButtonJson buttonJsonB) {
        buttonJson = buttonJsonB;
        WGridPanel root = new WGridPanel();
        this.setRootPanel(root);
        root.setSize(200, 200);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel screenName = new WLabel(Text.translatable("gui.inventorybinds.edit_bind"));
        root.add(screenName, 0, 0, 13, 1);

        WLabel bindNameText = new WLabel(Text.translatable("gui.inventorybinds.bind_name"));
        root.add(bindNameText, 0, 1, 7, 1);
        bindName = new WTextField();
        bindName.setText(buttonJson.getName());
        root.add(bindName, 0, 2, 10, 1);

        WLabel bindCommandText = new WLabel(Text.translatable("gui.inventorybinds.bind_cmd"));
        root.add(bindCommandText, 0, 4, 7, 1);
        bindCommand = new WTextField();
        bindCommand.setMaxLength(200);
        bindCommand.setText(buttonJson.getCommand());
        root.add(bindCommand, 0, 5, 10, 1);

        WLabel bindIconText = new WLabel(Text.translatable("gui.inventorybinds.item_id"));
        root.add(bindIconText, 0, 7, 7, 1);
        bindIcon = new WTextField();
        bindIcon.setMaxLength(200);
        bindIcon.setText(buttonJson.getItemId());
        root.add(bindIcon, 0, 8, 10, 1);



        serverBind = new WToggleButton(Text.translatable("gui.inventorybinds.server_only"));
        serverBind.setToggle(!buttonJson.getServer().equalsIgnoreCase("*"));
        root.add(serverBind, 0, 10, 7, 1);



        WButton save = new WButton(Text.translatable("gui.inventorybinds.save"));
        root.add(save, 0, 12, 5, 1);
        save.setOnClick(ModEditBindScreen::saveBind);
        WButton delete = new WButton(Text.translatable("gui.inventorybinds.delete"));
        delete.setOnClick(ModEditBindScreen::deleteBind);
        root.add(delete, 6, 12, 5, 1);



    }

    private static void deleteBind() {
        ButtonsConfig.removeButtonById(Integer.parseInt(buttonJson.getId()));
        exit();
    }



    private static void saveBind() {
        String bindNameStr = bindName.getText();
        String bindCommandStr = bindCommand.getText();
        String bindIconStr = bindIcon.getText();
        boolean bindServer = serverBind.getToggle();
        String bindServerStr = "*";

        if(bindNameStr.isEmpty()){
            bindNameStr = "newBind";
        }
        if(bindCommandStr.isEmpty()){
            bindCommandStr = "/spawn";
        }
        if(bindIconStr.isEmpty()){
            bindIconStr = "dirt";
        }
        if(bindServer){

            String server = "";

            try {
                server = MinecraftClient.getInstance().getNetworkHandler().getServerInfo().address;
            } catch (Exception e){
                server = "null";
            }

            bindServerStr = server;
        }


        editButtonById(Integer.parseInt(buttonJson.getId()), new ButtonJson(bindNameStr,bindCommandStr,bindIconStr,bindServerStr));

        exit();

    }

    private static void exit() {
        MinecraftClient.getInstance().player.closeScreen();
    }


    @Override
    public TriState isDarkMode() {
        // TRUE to force dark mode, FALSE to force light mode, DEFAULT to use the global setting
        return TriState.TRUE;
    }
}