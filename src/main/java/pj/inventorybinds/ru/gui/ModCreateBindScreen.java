package pj.inventorybinds.ru.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;


import static pj.inventorybinds.ru.config.ButtonsConfig.addButtonToButtonsList;


public class ModCreateBindScreen extends LightweightGuiDescription {

    static WTextField bindName;
    static WTextField bindCommand;
    static WTextField bindIcon;
    static WToggleButton serverBind;

    public ModCreateBindScreen() {
        WGridPanel root = new WGridPanel();
        this.setRootPanel(root);
        root.setSize(200, 200);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel screenName = new WLabel(Text.translatable("gui.inventorybinds.new_bind"));
        root.add(screenName, 0, 0, 10, 1);

        WLabel bindNameText = new WLabel(Text.translatable("gui.inventorybinds.bind_name"));
        root.add(bindNameText, 0, 1, 7, 1);
        bindName = new WTextField();
        root.add(bindName, 0, 2, 10, 1);

        WLabel bindCommandText = new WLabel(Text.translatable("gui.inventorybinds.bind_cmd"));
        root.add(bindCommandText, 0, 4, 7, 1);
        bindCommand = new WTextField();
        bindCommand.setMaxLength(200);
        root.add(bindCommand, 0, 5, 10, 1);

        WLabel bindIconText = new WLabel(Text.translatable("gui.inventorybinds.item_id"));
        root.add(bindIconText, 0, 7, 7, 1);
        bindIcon = new WTextField();
        bindIcon.setMaxLength(50);
        root.add(bindIcon, 0, 8, 10, 1);

        serverBind = new WToggleButton(Text.translatable("gui.inventorybinds.server_only"));
        root.add(serverBind, 0, 10, 7, 1);



        WButton save = new WButton(Text.translatable("gui.inventorybinds.save"));
        root.add(save, 0, 12, 5, 1);
        save.setOnClick(ModCreateBindScreen::saveBind);
        WButton cansel = new WButton(Text.translatable("gui.inventorybinds.cancel"));
        cansel.setOnClick(ModCreateBindScreen::exit);
        root.add(cansel, 6, 12, 5, 1);



    }

    private static void exit() {
        MinecraftClient.getInstance().player.closeScreen();
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


        addButtonToButtonsList(bindNameStr,bindCommandStr,bindIconStr,bindServerStr);

        exit();
    }


    @Override
    public TriState isDarkMode() {
        // TRUE to force dark mode, FALSE to force light mode, DEFAULT to use the global setting
        return TriState.TRUE;
    }
}
