package sourcecoded.minedroid2.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMainMenuHandler {

    public static final int MINEDROID_BUTTON_ID = 7378;

    public static void initGui(GuiScreen theGui, List<GuiButton> buttonList) {

        for (GuiButton button : buttonList) {
            if (button instanceof GuiButtonMinedroidPref) return;
        }

        GuiButtonMinedroidPref prefButton = new GuiButtonMinedroidPref(MINEDROID_BUTTON_ID, theGui.width / 2 - 152, theGui.height / 6 + 48 - 6);
        buttonList.add(prefButton);
        }

    public static void onActionPerformed(GuiButton guiButton) {
        switch (guiButton.id) {
            case MINEDROID_BUTTON_ID:
            Minecraft.getMinecraft().displayGuiScreen(new GuiScreenMinedroidPref());
        }
    }
}
