package main.ex6;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FTPConnection {

    private static final String SERVER = "ftpupload.net";
    private static final int PORT = 21;
    private static final String USERNAME = "if0_35747537";
    private static final String PASSWORD = "pZ7B4c2SZwD7X";
    private static final String SERVER_FILES_DIRECTORY = "/htdocs/";

    public static void main(String[] args) {

        FTPClient ftpClient = new FTPClient();

        try {
            if (connectToServer(ftpClient, SERVER, PORT)) {
                System.out.println("Connection made correctly");
            }

            if (loginToServer(ftpClient, USERNAME, PASSWORD)) {
                System.out.println("LOGIN successful");

                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            }

            ftpClient.enterLocalPassiveMode();
            System.out.println("\n    prova2.txt TEXT \n ----------------------------------------");
//            readFileData(ftpClient, "/htdocs/prova.txt");
//            ftpClient.retrieveFile()
//            System.out.println("\n            FILES FROM SERVER \n --------------------------------------");
//            listAllFromServer(ftpClient);
//            listFromPath(ftpClient, "/htdocs");
            System.out.println("------------------------------------------------- \n");

        } catch (Exception e) {
            System.out.println("ERROR. | " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (logoutOfServer(ftpClient)) {
                System.out.println("CLIENT LOGGED OUT");
            }

            disconnectFromServer(ftpClient);
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
                    System.out.println("CLIENT DISCONNECTED");
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
            System.out.println("result: " + (2 == 0) );
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

                    for (FTPFile file: serverFiles) {
                        String details = file.getName();
                        if (file.isDirectory()) {
                            details = "[" + details + "]";
                        }
                        System.out.println(details);
                    }

                } catch (Exception e) {
                    System.out.println("ERROR  |  " + e.getMessage());
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

                    for (FTPFile file: serverFiles) {
                        String details = file.getName();
                        if (file.isDirectory()) {
                            details = "[" + details + "]";
                        }
                        System.out.println(details);
                    }

                } catch (Exception e) {
                    System.out.println("ERROR  |  " + e.getMessage());
                    e.printStackTrace();
                }
            }
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
            System.out.println("ERROR | " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean handleGeneralErrors(Exception e) {
        System.out.println("ERROR | " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
