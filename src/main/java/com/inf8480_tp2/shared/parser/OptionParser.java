package com.inf8480_tp2.shared.parser;

import java.io.File;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Utility class used to parse command line arguments passed to the different programs
 * of the system
 *
 * @author Baptiste Rigondaud and Loïc Poncet
 */
public class OptionParser {

    private boolean isSafeMode = true;
    private float corruptRate;
    private String directoryAddress;
    private int directoryPort;
    private int serverPort;
    private int serverCapacity = 4;
    private String operationFilePath;

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
    
    public String getOperationFilePath() {
        return this.operationFilePath;
    }
    
    private void setOperationsFile(Scanner scanner) {
        if(!scanner.hasNext())
            throw new IllegalArgumentException("Invalid value for operations file option.");
        this.operationFilePath = System.getProperty("user.dir") + File.separator + scanner.next();
    }

    /**
     * Do the parsing of the command line arguments
     *
     * @param args The arguments passed by command line to the program
     */
    public void parseOptions(String args[]) {
        if (args.length == 0) {
            this.helpTriggered();
        }
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
                        this.setUnsafe();
                        this.setCorruptRate(this.parseCorruptRate(scanner));
                        break;
                    case "--ipDir":
                        this.setDirectoryAddress(scanner.next());
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
                    case "--operations":
                        this.setOperationsFile(scanner);
                        break;
                    default:
                        this.helpTriggered();
                }
            }
        } catch (NoSuchElementException e) {
            this.helpTriggered();
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
        if (port <= 5000 || port >= 5050) {
            throw new IllegalArgumentException("Invalid value for port option, should be an integer between 5000 and 5050 (both excluded)");
        }
        return port;
    }

    private int parseCapacity(Scanner scanner) {
        if (!scanner.hasNextInt()) {
            throw new IllegalArgumentException("Invalid value for capacity option, it should be an integer > 0");
        } else {
            int res = scanner.nextInt();
            if (res <= 0)
                throw new IllegalArgumentException("Capacity should be more than 0");
            return res;
        }
    }

    /**
     * Display an help message and shut down the execution
     */
    private void helpTriggered() {
        System.out.println("HELP: Different options can be passed via the command line arguments.\n" +
                "OPTIONS\n" +
                "--unsafe (server, specify that the server is corrupted)\n" +
                "--corrupt rate (server, specify the corrupt rate of the server, must be between 0 and 100)\n" +
                "--ipDir ip ([server, repartitor], specify the IP address of the name directory)\n" +
                "--portDir port ([server, directory], specify on which port the name directory should be/is running)\n" +
                "--portServer port (server, specify on which port the server should be running)\n" +
                "--capacity C (server, specify the capacity of the compute server)\n" +
                "--operations file_path (repartitor, Specify the RELATIVE file path of the file containing the operations)");
        System.exit(0);
    }
}
