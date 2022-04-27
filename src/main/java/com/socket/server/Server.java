package com.socket.server;

import com.socket.model.da.PacketDA;
import com.socket.model.to.Packet;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

public class Server implements Runnable {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Main.class.getName());

    Socket socket;
    long threadCount;
    PrintStream pout = null;
    DataInputStream dataInputStream;
    OutputStream outputStream = null;

    public Server(Socket socket, long threadCount) {
        this.socket = socket;
        this.threadCount = threadCount;
    }

    public void run() {
        try {
            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            logger.info(threadCount + "  " + "Client Address is: " + socket.getRemoteSocketAddress().toString().replace("/", ""));
            Date date = new Date();
            String timestamp = new Timestamp(date.getTime()).toString();
            logger.info(timestamp);
            byte[] data = new byte[1024];
            dataInputStream.read(data);
            processData(data);

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
            try {
                socket.close();
                logger.info(threadCount + "  " + "socket closed due to an exception.");
            } catch (Exception ex1) {
                logger.error(ex1);
            }
        }
    }

    private void processData(byte[] data) {
        //handling socket data and write to socket
        try {
            outputStream = socket.getOutputStream();
            pout = new PrintStream(outputStream);
        } catch (IOException ex) {
            logger.error(ex);
        }
        saveData(data);
    }


    private void saveData(byte[] data) {
        //TODO: extract related information from data and save it to your database
        Packet packet = new Packet();

        PacketDA packetDA = new PacketDA();
        packetDA.insertPacket(packet);
        packetDA.close();
    }


}
