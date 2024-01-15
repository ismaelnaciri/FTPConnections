package main.ex4;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPConnection {

    private static final String SERVER = "ftpupload.net";
    private static final int PORT = 21;
    private static final String USERNAME = "if0_35747537";
    private static final String PASSWORD = "pZ7B4c2SZwD7X";

    public static void main(String[] args) {

        FTPClient ftpClient = new FTPClient();

        try {
            connectToServer(ftpClient, SERVER, PORT);

            boolean loginSuccess = ftpClient.login(USERNAME, PASSWORD);

            if (loginSuccess) {
                System.out.println("LOGIN SUCCESSFULL!");

                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


            } else {
                System.out.println("LOGIN FAILED. ");
            }

        } catch (Exception e) {
            System.out.println("ERROR. | " + e.getMessage());
            e.printStackTrace();
        } finally {
            disconnectFromServer(ftpClient, SERVER, PORT);
        }
    }

    public static void connectToServer(FTPClient client, String server, int port) {
        if (client != null) {
            try {
                client.connect(server, port);

                System.out.println("Connection made correctly");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void disconnectFromServer(FTPClient client, String server, int port) {
        if (client != null) {
            try {
                if (client.isConnected()) {
                    client.logout();
                    client.disconnect();
                    System.out.println("CLIENT DISCONNECTED");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
