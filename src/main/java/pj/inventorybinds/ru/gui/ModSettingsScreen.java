package pj.inventorybinds.ru.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Util;


import java.io.File;

import static pj.inventorybinds.ru.InventoryBinds.MOD_ID;


public class ModSettingsScreen
        extends LightweightGuiDescription {
    public ModSettingsScreen() {
        WGridPanel root = new WGridPanel();
        this.setRootPanel(root);
        root.setSize(100, 100);
        root.setInsets(Insets.ROOT_PANEL);
        WButton openConfigFolder = new WButton(Text.translatable("gui.inventorybinds.config_open_button"));
        WLabel screenName = new WLabel(Text.translatable("gui.inventorybinds.settings"));
        root.add(screenName, 0, 0, 7, 1);
        root.add(openConfigFolder, 0, 4, 7, 1);
        openConfigFolder.setOnClick(ModSettingsScreen::openConfigFolderAction);
    }

    private static void openConfigFolderAction(){
        Util.getOperatingSystem().open(new File(FabricLoader.getInstance().getGameDir().toFile(), "config/"+MOD_ID));
    }

    @Override
    public TriState isDarkMode() {
        // TRUE to force dark mode, FALSE to force light mode, DEFAULT to use the global setting
        return TriState.TRUE;
    }
}
