package com.inf8480_tp2.shared.parser;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class OptionParser {

    private boolean isSafeMode = true;
    private float corruptRate;
    private String directoryAddress;
    private String serverAdress;
    private int directoryPort;
    private int serverPort;
    private int serverCapacity;

    public OptionParser(String[] args) {
        this.parseOptions(args);
    }

    public boolean isSafeMode() {
        return this.isSafeMode;
    }

    public void setUnsafe() {
        this.isSafeMode = false;
    }

    public float getCorruptRate() {
        return this.corruptRate;
    }

    public void setCorruptRate(float corruptRate) {
        this.corruptRate = corruptRate;
    }

    public String getDirectoryAddress() {
        return this.directoryAddress;
    }

    public void setDirectoryAddress(String directoryAddress) {
        this.directoryAddress = directoryAddress;
    }

    public String getServerAdress() {
        return this.serverAdress;
    }

    public void setServerAdress(String serverAdress) {
        this.serverAdress = serverAdress;
    }

    public int getDirectoryPort() {
        return this.directoryPort;
    }

    public void setDirectoryPort(int directoryPort) {
        this.directoryPort = directoryPort;
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerCapacity() {
        return this.serverCapacity;
    }

    public void setServerCapacity(int serverCapacity) {
        this.serverCapacity = serverCapacity;
    }

    public void parseOptions(String args[]) {
        String options = Arrays.stream(args).collect(Collectors.joining(" "));
        Scanner scanner = new Scanner(options);
        try {
            while (scanner.hasNext()) {
                String token = scanner.next();
                switch (token) {
                    case "--unsafe":
                        this.setUnsafe();
                        break;
                    case "--corrupt":
                        this.setCorruptRate(this.parseCorruptRate(scanner));
                        break;
                    case "--ipDir":
                        this.setDirectoryAddress(scanner.next());
                        break;
                    case "--ipServer":
                        this.setServerAdress(scanner.next());
                        break;
                    case "--portDir":
                        this.setDirectoryPort(this.parsePort(scanner));
                        break;
                    case "--portServer":
                        this.setServerPort(this.parsePort(scanner));
                        break;
                    case "--capacity":
                        this.setServerCapacity(this.parseCapacity(scanner));
                        break;
                }
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Invalid options");
        }
    }

    private float parseCorruptRate(Scanner scanner) {
        if (!scanner.hasNextFloat()) {
            throw new IllegalArgumentException("Invalid value for corruption option, should be a float between 0 and 100");
        }
        float rate = scanner.nextFloat();
        if (rate < 0 || rate > 100) {
            throw new IllegalArgumentException("Invalid value for corruption option, should be a float between 0 and 100");
        }
        return rate;
    }

    private int parsePort(Scanner scanner) {
        if (!scanner.hasNextInt()) {
            throw new IllegalArgumentException("Invalid value for port option, should be an integer between 5000 and 5050");
        }
        int port = scanner.nextInt();
        if (port < 5000 || port > 5050) {
            throw new IllegalArgumentException("Invalid value for port option, should be an integer between 5000 and 5050");
        }
        return port;
    }

    private int parseCapacity(Scanner scanner) {
        if (!scanner.hasNextInt()) {
            throw new IllegalArgumentException("Invalid value for capacity option, it should be an integer");
        }
        return scanner.nextInt();
    }
}
