
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
    @SerializedName("server")
    @Expose
    private String server;
    @SerializedName("id")
    @Expose
    private String id;


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

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ButtonJson(String name, String command, String itemId, String server) {
        this.name = name;
        this.command = command;
        this.itemId = itemId;
        this.server = server;
    }

    public ButtonJson() {

    }

}
