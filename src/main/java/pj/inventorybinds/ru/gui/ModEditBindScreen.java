package pj.inventorybinds.ru.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;
import pj.inventorybinds.ru.config.ButtonsConfig;
import pj.inventorybinds.ru.config.buttons.ButtonJson;

import java.util.Objects;

import static pj.inventorybinds.ru.config.ButtonsConfig.editButtonById;

public class ModEditBindScreen extends LightweightGuiDescription {

    static WTextField bindName;
    static WTextField bindCommand;
    static WTextField bindIcon;
    static WToggleButton serverBind;
    static WToggleButton hidden;
    static ButtonJson buttonJson;

    public ModEditBindScreen(ButtonJson buttonJsonB) {
        buttonJson = buttonJsonB;
        WPlainPanel root = new WPlainPanel();
        this.setRootPanel(root);
        root.setSize(200, 200);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel screenName = new WLabel(Text.translatable("gui.inventorybinds.edit_bind"));
        screenName.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(screenName, 0, 0, 200, 1);

        WLabel bindNameText = new WLabel(Text.translatable("gui.inventorybinds.bind_name"));
        root.add(bindNameText, 0, 19, 200, 1);
        bindName = new WTextField();
        bindName.setText(buttonJson.getName());
        root.add(bindName, 0, 32, 200, 1);

        WLabel bindCommandText = new WLabel(Text.translatable("gui.inventorybinds.bind_cmd"));
        root.add(bindCommandText, 0, 61, 200, 1);
        bindCommand = new WTextField();
        bindCommand.setMaxLength(200);
        bindCommand.setText(buttonJson.getCommand());
        root.add(bindCommand, 0, 74, 200, 1);

        WLabel bindIconText = new WLabel(Text.translatable("gui.inventorybinds.item_id"));
        root.add(bindIconText, 0, 103, 7, 1);
        bindIcon = new WTextField();
        bindIcon.setMaxLength(200);
        bindIcon.setText(buttonJson.getItemId());
        root.add(bindIcon, 0, 116, 200, 1);

        hidden = new WToggleButton(Text.translatable("gui.inventorybinds.hidden"));
        hidden.setToggle(buttonJson.getHide());
        root.add(hidden, 0, 148, 50, 16);

        serverBind = new WToggleButton(Text.translatable("gui.inventorybinds.server_only"));
        serverBind.setToggle(!buttonJson.getServer().equalsIgnoreCase("*"));
        root.add(serverBind, 0, 173, 50, 16);

        WButton save = new WButton(Text.translatable("gui.inventorybinds.save"));
        root.add(save, 0, 200, 98, 16);
        save.setOnClick(ModEditBindScreen::saveBind);
        WButton delete = new WButton(Text.translatable("gui.inventorybinds.delete"));
        delete.setOnClick(ModEditBindScreen::deleteBind);
        root.add(delete, 104, 200, 98, 16);



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
                server = Objects.requireNonNull(Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getServerInfo()).address;
            } catch (Exception e){
                server = "null";
            }

            bindServerStr = server;
        }

        Boolean bindHidden = hidden.getToggle();

        editButtonById(Integer.parseInt(buttonJson.getId()), new ButtonJson(bindNameStr,bindCommandStr,bindIconStr,bindServerStr, bindHidden));

        exit();

    }

    private static void exit() {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().setScreen(new InventoryScreen(MinecraftClient.getInstance().player));
    }


    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }
}