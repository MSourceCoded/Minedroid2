package sourcecoded.minedroid2.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.config.Property;
import org.lwjgl.input.Keyboard;
import org.omg.CORBA.CODESET_INCOMPATIBLE;
import sourcecoded.mdcomms.network.packets.Pkt0x00PingRequest;
import sourcecoded.mdcomms.socket.SourceCommsServer;
import sourcecoded.minedroid2.util.CacheUtils;
import sourcecoded.minedroid2.util.ConfigUtils;
import sun.security.krb5.Config;

import java.util.HashMap;

public class GuiScreenMinedroidPref extends GuiScreen {

    String title = "MineDroid Preferences";

    String errorMessage = null;

    Property propertyPort;

    /*
        BUTTON LISTS!
     */
    final int BUTTON_DONE = 0;
    final int BUTTON_SAVE = 1;
    final int BUTTON_OPEN = 10;
    final int BUTTON_KILL = 11;
    final int BUTTON_PING = 12;

    /*
        Text Fields
     */
    GuiTextField fieldPort;

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        propertyPort = ConfigUtils.PORT_PROPERTY;

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(BUTTON_DONE, this.width / 2 + 2, 200, 150, 20, "Done"));
        this.buttonList.add(new GuiButton(BUTTON_SAVE, this.width / 2 - 152, 200, 150, 20, "Save"));

        this.buttonList.add(new GuiButton(BUTTON_OPEN, this.width / 2 - 152, 175, 100, 20, "Open Connection"));
        this.buttonList.add(new GuiButton(BUTTON_KILL, this.width / 2 - 50, 175, 100, 20, "Kill Connection"));
        this.buttonList.add(new GuiButton(BUTTON_PING, this.width / 2 + 52, 175, 100, 20, "Ping Droid"));

        fieldPort = new GuiTextField(this.fontRendererObj, this.width / 2 - 50, 27, 200, 20);
        fieldPort.setFocused(true);
        fieldPort.setMaxStringLength(4);
        fieldPort.setText((String.valueOf(propertyPort.getInt())));
    }

        @Override
        public void onGuiClosed() {
            Keyboard.enableRepeatEvents(false);
        }

        @Override
        public void keyTyped(char typed, int par2) {
            this.fieldPort.textboxKeyTyped(typed, par2);
        }

        @Override
        public void mouseClicked(int par1, int par2, int par3) {
            super.mouseClicked(par1, par2, par3);
            this.fieldPort.mouseClicked(par1, par2, par3);
    }

    @Override
    public void actionPerformed(GuiButton theButton) {
        switch(theButton.id) {
            case BUTTON_DONE:
                this.mc.displayGuiScreen(null);
                break;
            case BUTTON_SAVE:
                try {
                    int portNumber = Integer.parseInt(fieldPort.getText());
                    errorMessage = null;

                    propertyPort.set(portNumber);
                    ConfigUtils.save();
                } catch (Exception e) {
                    errorMessage = fieldPort.getText() + " is not a number :(";
                }
                break;
            case BUTTON_OPEN:
                SourceCommsServer.instance().setData(propertyPort.getInt());
                SourceCommsServer.instance().open();
                SourceCommsServer.instance().setListeningState(true);
                break;
            case BUTTON_KILL:
                SourceCommsServer.instance().stop();
                break;
            case BUTTON_PING:
                SourceCommsServer.instance().sendToClient(new Pkt0x00PingRequest());
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 5, 16777215);

        if (errorMessage != null) {
            this.drawCenteredString(this.fontRendererObj, this.errorMessage, this.width / 2 + 50, 55, 16711680);
        }

        this.drawString(this.fontRendererObj, "Connected to Droid: ", this.width / 2 - 150, 70, 16755200);
        this.drawString(this.fontRendererObj, "Listening for Packets: ", this.width / 2 - 150, 90, 16755200);
        this.drawString(this.fontRendererObj, "Last Ping: ", this.width / 2 - 150, 110, 16755200);

        if (SourceCommsServer.instance().isConnected())
            this.drawString(this.fontRendererObj, "true", this.width / 2, 70, 34560);
        else
            this.drawString(this.fontRendererObj, "false", this.width / 2, 70, 16737792);

        if (SourceCommsServer.instance().isListening())
            this.drawString(this.fontRendererObj, "true", this.width / 2, 90, 34560);
        else
            this.drawString(this.fontRendererObj, "false", this.width / 2, 90, 16737792);

        this.drawString(this.fontRendererObj, String.valueOf(CacheUtils.pingCache) + " ms", this.width / 2, 110, 5636095);

        this.drawString(this.fontRendererObj, "Port Number", this.width / 2 - 150, 33, 16777215);
        fieldPort.drawTextBox();


        super.drawScreen(mouseX, mouseY, par3);
    }

}
