
package pj.inventorybinds.ru.config.buttons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ButtonsList {

    @SerializedName("buttons")
    @Expose
    private List<ButtonJson> buttons;

    @SerializedName("bind_editor_enabled")
    @Expose
    private boolean bindEditorEnabled;

    @SerializedName("new_bind_button_enabled")
    @Expose
    private boolean newBindButtonEnabled;

    public boolean getBindEditorEnabled() {
        return bindEditorEnabled;
    }

    public boolean getNewBindButtonEnabled() {
        return newBindButtonEnabled;
    }

    public List<ButtonJson> getButtons() {
        return buttons;
    }

    public void setButtons(List<ButtonJson> buttons) {
        this.buttons = buttons;
    }

}
