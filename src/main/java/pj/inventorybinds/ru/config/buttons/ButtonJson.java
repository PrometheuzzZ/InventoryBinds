
package pj.inventorybinds.ru.config.buttons;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ButtonJson {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("bind")
    @Expose
    private String command;
    @SerializedName("item_id")
    @Expose
    private String itemId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
