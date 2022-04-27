/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socket.server;

import com.socket.database.ConfigLoader;
import com.socket.database.HikariCPDataSource;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPool {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ThreadPool.class.getName());

    ServerSocket myServerSocket;
    boolean ServerOn = true;
    Properties prop = ConfigLoader.getConfig();
    int port = Integer.parseInt(prop.getProperty("server.port"));
    long threadCount = 1;

    public ThreadPool() {
        try {
            myServerSocket = new ServerSocket(port);
            logger.info("server initialized on port: " + port);
            HikariCPDataSource.getConnection();
            logger.info("initialized database connection successfully");
        } catch (IOException ioe) {
            logger.info("Could not create server socket on port " + port + ". Quitting...");
            System.exit(-1);
        } catch (SQLException ex) {
            Logger.getLogger(ThreadPool.class.getName()).log(Level.SEVERE, null, ex);
            logger.info("Could not connect to datasource");
        }
        while (ServerOn) {
            try {
                Socket clientSocket = myServerSocket.accept();
                clientSocket.setSoTimeout(Integer.parseInt(prop.getProperty("client.timeout")));
                logger.info("Accepted connection; Client Address is: " + clientSocket.getRemoteSocketAddress().toString().replace("/", ""));
                new Thread(new Server(clientSocket, threadCount++)).start();
            } catch (IOException ioe) {
                logger.info("Exception found on accept. Ignoring. Stack Trace :");
                ioe.printStackTrace();
            }
        }
        try {
            myServerSocket.close();
            logger.info("Server Stopped");
        } catch (Exception ioe) {
            logger.info("Error Found stopping server socket");
            System.exit(-1);
        }
    }
}
