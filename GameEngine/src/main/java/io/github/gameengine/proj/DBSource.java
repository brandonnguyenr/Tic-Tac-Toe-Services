package io.github.gameengine.proj;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class DBSource {
    private static BasicDataSource dataSource;

    static {
        try {
            dataSource = new BasicDataSource();
            Properties properties = new Properties();
            // Loading properties file
            properties.load(DBSource.class.getResourceAsStream("db.properties"));
            dataSource.setDriverClassName(properties.getProperty("DRIVER_CLASS")); //loads the jdbc driver
            dataSource.setUrl(properties.getProperty("DB_CONNECTION_URL"));
            dataSource.setUsername(properties.getProperty("DB_USER"));
            dataSource.setPassword(properties.getProperty("DB_PWD"));
            // Parameters for connection pooling
//            basicDS.setInitialSize(10);
            dataSource.setMinIdle(1);
            dataSource.setMaxIdle(6);
            dataSource.setMaxTotal(10);
            dataSource.setValidationQuery("SELECT 1;");
            dataSource.setTestOnBorrow(true);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
