package me.ilmars.proxy;

public class Proxy {
    private boolean enabled;
    private ProxyType type = ProxyType.SOCKS5;
    private String ip;
    private int port;
    private String userID;
    private String username;
    private String password;

    public Proxy() {
        this.enabled = false;
    }

    public Proxy(boolean isSocks4, String ip, int port, String userID, String username, String password) {
        this.type = isSocks4 ? ProxyType.SOCKS4 : ProxyType.SOCKS5;
        this.enabled = !ip.isEmpty();
        this.ip = ip;
        this.port = port;
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ProxyType getType() {
        return type;
    }

    public void setType(ProxyType type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
