
package pj.inventorybinds.ru.config.buttons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ButtonsList {

    @SerializedName("buttons")
    @Expose
    private List<ButtonJson> buttons;

    public List<ButtonJson> getButtons() {
        return buttons;
    }

    public void setButtons(List<ButtonJson> buttons) {
        this.buttons = buttons;
    }

}
