package me.ilmars.proxy;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static me.ilmars.MinecraftProxy.CONFIG_DIR;

public class Config {
    private static Configuration config = null;
    private final static String CATEGORY = "currentProxy";

    public static void loadCurrentProxy() {
        try {
            config = new Configuration(new File(CONFIG_DIR + "ProxyServer.cfg"));
            config.load();

            String typeString = config.get(CATEGORY, "type", "SOCKS5", "Type").getString();
            boolean isSocks4 = typeString.equals("SOCKS4");

            String ip = config.get(CATEGORY, "ip", "", "IP").getString();
            int port = config.get(CATEGORY, "port", 0, "Port").getInt();

            String userID = config.get(CATEGORY, "userid", "", "User ID for SOCKS4").getString();
            String username = config.get(CATEGORY, "username", "", "Username for SOCKS5").getString();
            String password = config.get(CATEGORY, "password", "", "Password for SOCKS5").getString();

            ProxyHandler.proxy = new Proxy(isSocks4, ip, port, userID, username, password);
        } catch (Exception e) {
            System.out.println("Error loading config, returning to default variables.");
        } finally {
            if (config != null) config.save();
        }
    }

    public static void saveProxy(Proxy proxy) {
        config.get(CATEGORY, "type", "SOCKS5", "Type").set(proxy.getType().name());

        config.get(CATEGORY, "ip", "", "IP").set(proxy.getIp());
        config.get(CATEGORY, "port", 0, "Port").set(proxy.getPort());

        config.get(CATEGORY, "userid", "", "User ID for SOCKS4").set(proxy.getUserID());
        config.get(CATEGORY, "username", "", "Username for SOCKS5").set(proxy.getUsername());
        config.get(CATEGORY, "password", "", "Password for SOCKS5").set(proxy.getPassword());
        config.save();
    }
}
