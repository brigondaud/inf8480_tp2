package com.inf8480_tp2.shared.server;

/**
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class ServerInfo {

    private String ipAddress;
    private int capacity;

    public ServerInfo(String ip, int capacity) {
        this.ipAddress = ip;
        this.capacity = capacity;
    }

    public String getIpAdress() {
        return ipAddress;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setIpAdress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServerInfo)) {
            return false;
        }
        ServerInfo other = (ServerInfo) obj;
        return this.ipAddress.equals(other.ipAddress);
    }

    @Override
    public int hashCode() {
        return this.ipAddress.hashCode();
    }
}
