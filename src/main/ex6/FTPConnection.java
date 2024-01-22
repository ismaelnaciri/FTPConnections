package main.ex6;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.lang.management.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.*;

public class FTPConnection {

    private static final String SERVER = "ftpupload.net";
    private static final int PORT = 21;
    private static final String USERNAME = "if0_35747537";
    private static final String PASSWORD = "pZ7B4c2SZwD7X";
    private static final String SERVER_FILES_DIRECTORY = "/htdocs/";
    private static final Logger LOGGER = Logger.getLogger("MyLog.log");
    private static FileHandler fh = null;

    static {
        try {
            fh = new FileHandler("C:\\IdeaProjects\\2nDAM\\FTPConnections\\src\\main\\ex6\\MyLog.log", 256 * 256, 1, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            LOGGER.addHandler(fh);
        } catch (IOException | SecurityException e) {
            System.out.println("ERROR | " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        FTPClient ftpClient = new FTPClient();

        try {
            if (connectToServer(ftpClient, SERVER, PORT)) {
                logElement("Connection made correctly", Level.FINE);
            }

            if (loginToServer(ftpClient, USERNAME, PASSWORD)) {
                logElement("Login made correctly", Level.FINE);

                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                logElement("ENTERED BINARY TRANSFER MODE", Level.FINE);
            }

            ftpClient.enterLocalPassiveMode();
            logElement("ENTERED LOCAL PASSIVE MODE", Level.INFO);
            System.out.println("\n    prova2.txt TEXT \n ----------------------------------------");

            logElement("BEGGINING MEMEORY THREADS LOG ------------------------------------------", Level.INFO);
            logMemoryAndThreads();

//            logElement("Beggining ex9 log test", Level.INFO);
//            readFileData(ftpClient, "/htdocs/notValidFile.txt");
//
//            logElement("Beggining ex10 log test", Level.INFO);
//            FileInputStream fis = new FileInputStream("C:\\IdeaProjects\\2nDAM\\FTPConnections\\src\\main\\ex6\\data.txt");
//            ftpClient.changeWorkingDirectory(SERVER_FILES_DIRECTORY);
//            uploadFileToServer(ftpClient, fis, "isma_test1.txt");

//            ftpClient.retrieveFile()
//            System.out.println("\n            FILES FROM SERVER \n --------------------------------------");
//            listAllFromServer(ftpClient);
//            listFromPath(ftpClient, "/htdocs");
            System.out.println("------------------------------------------------- \n");

        } catch (Exception e) {
            logElement(e.getMessage(), Level.SEVERE);
            e.printStackTrace();
        } finally {
            if (logoutOfServer(ftpClient)) {
                logElement("CLIENT LOGGED OUT successfully", Level.INFO);
            }

            disconnectFromServer(ftpClient);
        }
    }

    public static void logMemoryAndThreads() {
        // Log memory usage
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        logElement("Heap Memory Usage: " + heapMemoryUsage.toString(), Level.INFO);
        logElement("Non-Heap Memory Usage: " + nonHeapMemoryUsage.toString(), Level.INFO);

        // Log thread information
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);

        logElement("Thread Count: " + threadMXBean.getThreadCount(), Level.INFO);

        for (ThreadInfo threadInfo : threadInfos) {
            logElement("Thread ID: " + threadInfo.getThreadId() +
                    " | Thread Name: " + threadInfo.getThreadName() +
                    " | Thread State: " + threadInfo.getThreadState(), Level.INFO);
        }
    }

    public static void logElement(String element, Level type) {
        try {
            if (type == Level.INFO)
                LOGGER.info(element);
            else if (type == Level.SEVERE)
                LOGGER.severe(element);
            else if (type == Level.WARNING)
                LOGGER.warning(element);
            else if (type == Level.CONFIG)
                LOGGER.config(element);
            else if (type == Level.FINE)
                LOGGER.fine(element);
            else if (type == Level.FINER)
                LOGGER.finer(element);
            else if (type == Level.FINEST)
                LOGGER.finest(element);

        } catch (Exception e) {
            System.out.println("ERROR | " + e.getMessage());
            LOGGER.severe(e.getMessage());
        }
    }

    public static void readFileData(FTPClient client, String path) {
        try {
            InputStream inputStream = client.retrieveFileStream(path);
            Scanner scanner = new Scanner(inputStream);
            StringBuffer sb = new StringBuffer();

            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
                sb.append("\n");
            }

            System.out.println(sb.toString());
        } catch (Exception e) {
            logElement(e.getMessage(), Level.SEVERE);
            e.printStackTrace();
        }
    }

    public static void uploadFileToServer(FTPClient client, InputStream data, String remoteFileName) {
        try {
            boolean done = client.storeFile(remoteFileName, data);

            if (done) {
                System.out.println("File Uploaded!!");
            }

        } catch (Exception e) {
            logElement(e.getMessage(), Level.SEVERE);
            e.printStackTrace();
        }
    }

    public static boolean connectToServer(FTPClient client, String server, int port) {
        if (client != null) {
            try {
                client.connect(server, port);

                return client.isConnected() ? true : false;
            } catch (Exception e) {
                return handleGeneralErrors(e);
            }
        }
        return false;
    }

    public static void disconnectFromServer(FTPClient client) {
        if (client != null) {
            try {
                if (client.isConnected()) {
                    client.disconnect();
                    logElement("CLIENT DISCONNECTED CORRECTLY", Level.INFO);
                }
            } catch (Exception e) {
                handleGeneralErrors(e);
            }
        }
    }

    public static boolean loginToServer(FTPClient client, String username, String password) {
        if (client != null) {
            if (client.isConnected()) {
                try {
                    return client.login(username, password) ? true : false;
                } catch (Exception e) {
                    return handleGeneralErrors(e);
                }
            }
            System.out.println("result: " + (2 == 0));
        }
        return false;
    }

    public static boolean logoutOfServer(FTPClient client) {
        if (client != null) {
            if (client.isConnected()) {
                try {
                    return client.logout() ? true : false;
                } catch (Exception e) {
                    return handleGeneralErrors(e);
                }
            }
            return false;
        }
        return false;
    }

    public static void listAllFromServer(FTPClient client) {
        if (client != null) {
            if (client.isConnected()) {
                try {
                    FTPFile[] serverFiles = client.listFiles();

                    for (FTPFile file : serverFiles) {
                        String details = file.getName();
                        if (file.isDirectory()) {
                            details = "[" + details + "]";
                        }
                        System.out.println(details);
                    }

                } catch (Exception e) {
                    logElement(e.getMessage(), Level.SEVERE);
                    e.printStackTrace();
                }
            }
        }
    }

    public static void listFromPath(FTPClient client, String path) {
        if (client != null) {
            if (client.isConnected()) {
                try {
                    FTPFile[] serverFiles = client.listFiles(path);

                    for (FTPFile file : serverFiles) {
                        String details = file.getName();
                        if (file.isDirectory()) {
                            details = "[" + details + "]";
                        }
                        System.out.println(details);
                    }

                } catch (Exception e) {
                    logElement(e.getMessage(), Level.SEVERE);
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean handleGeneralErrors(Exception e) {
        logElement(e.getMessage(), Level.SEVERE);
        e.printStackTrace();
        return false;
    }
}
