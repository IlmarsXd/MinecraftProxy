package me.ilmars.proxy;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.regex.Pattern;

final class GuiProxy extends GuiScreen {
    private final Color Color = new Color(160, 160, 160);
    private static final Pattern IP_PORT_PATTERN = Pattern.compile("^(?:\\d{1,3}\\.){3}\\d{1,3}:\\d{1,5}$");

    private static final int BUTTON_SOCKS4 = 1;
    private static final int BUTTON_SOCKS5 = 2;
    private static final int BUTTON_TEST = 4;
    private static final int BUTTON_APPLY = 6;
    private static final int BUTTON_CANCEL = 7;

    // Proxy type
    private GuiCheckBox socks4;
    private GuiCheckBox socks5;

    // IP:PORT
    private GuiTextField ipPort;

    // Socks4
    private GuiTextField userID;

    // Socks5
    private GuiTextField username;
    private GuiTextField password;

    private final GuiScreen parentScreen;

    private String msg = "";

    private Integer[] positionY;
    private int positionX;

    private Proxy oldProxy;

    private TestPing testPing = new TestPing();

    GuiProxy(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
    }

    private boolean setProxy() {
        String ipAndPort = ipPort.getText();
        if (ipAndPort.isEmpty() || ipAndPort.equalsIgnoreCase("none")) {
            ProxyHandler.proxy = new Proxy(socks4.isChecked(), "", 0, userID.getText(), username.getText(), password.getText());
            return true;
        }
        if (!validatePortAndIp(ipAndPort)) {
            msg = ChatFormatting.RED + "Invalid IP:PORT";
            ipPort.setFocused(true);
            return false;
        }

        String[] split = ipAndPort.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        ProxyHandler.proxy = new Proxy(socks4.isChecked(), ip, port, userID.getText(), username.getText(), password.getText());
        return true;
    }

    private static boolean validatePortAndIp(String ipAndPort) {
        return IP_PORT_PATTERN.matcher(ipAndPort).matches();
    }

    private void centerButtons(int amount, int buttonLength, int gap) {
        positionX = (this.width / 2) - (buttonLength / 2);
        positionY = new Integer[amount];
        int center = (this.height + amount * gap) / 2;
        int buttonStarts = center - (amount * gap);
        for (int i = 0; i != amount; i++) {
            positionY[i] = buttonStarts + (gap * i);
        }
    }

    @Override
    protected void actionPerformed(GuiButton b) {
        switch (b.id) {
            case BUTTON_SOCKS4:
                if (!socks5.isChecked()) {
                    socks4.setIsChecked(true);
                } else {
                    socks4.setIsChecked(true);
                    socks5.setIsChecked(false);
                }
                break;
            case BUTTON_SOCKS5:
                if (!socks4.isChecked()) {
                    socks5.setIsChecked(true);
                } else {
                    socks5.setIsChecked(true);
                    socks4.setIsChecked(false);
                }
                break;
            case BUTTON_TEST:
                if (setProxy()) {
                    testPing = new TestPing();
                    testPing.run("mc.hypixel.net", 25565, ProxyHandler.proxy.getIp());
                }
                break;
            case BUTTON_APPLY:
                if (setProxy()) {
                    Config.saveProxy(ProxyHandler.proxy);
                    this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                }
                break;
            case BUTTON_CANCEL:
                ProxyHandler.proxy = oldProxy;
                this.mc.displayGuiScreen(parentScreen);
                break;
        }
    }

    @Override
    protected void keyTyped(char c, int k) throws IOException {
        super.keyTyped(c, k);
        this.ipPort.textboxKeyTyped(c, k);
        this.username.textboxKeyTyped(c, k);
        this.password.textboxKeyTyped(c, k);
        msg = "";
        testPing.state = "";
    }

    @Override
    protected void mouseClicked(int x, int y, int b) throws IOException {
        super.mouseClicked(x, y, b);
        this.ipPort.mouseClicked(x, y, b);
        this.username.mouseClicked(x, y, b);
        this.password.mouseClicked(x, y, b);
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        this.drawDefaultBackground();

        this.drawString(this.fontRendererObj, "Proxy Type:", this.width / 2 - 149, positionY[1] + 5, 10526880);
        this.drawString(this.fontRendererObj, "IP:PORT", this.width / 2 - 125, positionY[2] + 5, 10526880);
        this.drawCenteredString(this.fontRendererObj, "Authentication (optional)", this.width / 2, positionY[3] + 8, Color.WHITE.getRGB());

        this.ipPort.drawTextBox();
        if (socks5.isChecked()) {
            this.drawString(this.fontRendererObj, "Username: ", this.width / 2 - 140, positionY[4] + 5, 10526880);
            this.drawString(this.fontRendererObj, "Password: ", this.width / 2 - 140, positionY[5] + 5, 10526880);
            this.username.drawTextBox();
            this.password.drawTextBox();
        } else {
            this.drawString(this.fontRendererObj, "User ID: ", this.width / 2 - 140, positionY[4] + 5, 10526880);
            this.userID.drawTextBox();
        }


        this.drawCenteredString(this.fontRendererObj, !msg.isEmpty() ? msg : testPing.state, this.width / 2, positionY[6] + 5, 10526880);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.ipPort.drawTextBox();
        this.username.drawTextBox();
        if (socks5.isChecked()) {
            this.password.drawTextBox();
        }

        testPing.pingPendingNetworks();
    }

    @Override
    public void initGui() {
        Proxy proxy = ProxyHandler.proxy;
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int buttonLength = 160;
        centerButtons(10, buttonLength, 26);

        // Text Fields
        this.ipPort = new GuiTextField(3, this.fontRendererObj, positionX, positionY[2], buttonLength, 20);
        this.ipPort.setText(proxy.isEnabled() ? proxy.getIp() + ":" + proxy.getPort() : "");
        this.ipPort.setFocused(true);
        this.ipPort.setMaxStringLength(255);

        this.username = new GuiTextField(4, this.fontRendererObj, positionX, positionY[4], buttonLength, 20);
        this.username.setText(proxy.getUsername());
        this.username.setMaxStringLength(255);

        this.userID = new GuiTextField(8, this.fontRendererObj, positionX, positionY[4], buttonLength, 20);
        this.userID.setText(proxy.getUserID());
        this.userID.setMaxStringLength(255);

        this.password = new GuiTextField(5, this.fontRendererObj, positionX, positionY[5], buttonLength, 20);
        this.password.setText(proxy.getPassword());
        this.password.setMaxStringLength(255);

        // Buttons
        this.socks4 = new GuiCheckBox(BUTTON_SOCKS4, positionX + 10, positionY[1] + 5, "Socks4", proxy.getType() == ProxyType.SOCKS4);
        this.socks5 = new GuiCheckBox(BUTTON_SOCKS5, positionX + 100, positionY[1] + 5, "Socks5", proxy.getType() == ProxyType.SOCKS5);

        GuiButton apply = new GuiButton(BUTTON_APPLY, positionX, positionY[7], buttonLength / 2 - 3, 20, "Apply");
        GuiButton test = new GuiButton(BUTTON_TEST, positionX + buttonLength / 2 + 3, positionY[7], buttonLength / 2 - 3, 20, "Test");
        GuiButton cancel = new GuiButton(BUTTON_CANCEL, positionX, positionY[8], buttonLength, 20, "Cancel");

        this.buttonList.add(this.socks4);
        this.buttonList.add(this.socks5);
        this.buttonList.add(apply);
        this.buttonList.add(test);
        this.buttonList.add(cancel);

        oldProxy = proxy;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        msg = "";
        Keyboard.enableRepeatEvents(false);
    }
}