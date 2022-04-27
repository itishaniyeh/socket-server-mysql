package com.socket.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    public static Properties getConfig() {
        Properties prop = new Properties();
        try (InputStream input = ConfigLoader.class
                .getClassLoader().getResourceAsStream("config.properties")) {
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }
}
