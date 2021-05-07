package io.github.gameengine.proj;

import io.github.coreutils.proj.messages.LoginData;
import io.github.gameengine.proj.utils.PreparedStatementWrapper;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager extends DBSource{
    private DBManager() {
        // empty
    }

    private static class InstanceHolder {
        private static final DBManager INSTANCE = new DBManager();
    }

    public static DBManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    // TODO: see Recorder module for examples

    /**
     * This method will be called from the switch statement within {@code Main.java} that will query the database to
     * verify the password for the user. It will select a password from the database and check whether the entered password
     * and username was correct or not.
     * @param loginData: the data that contains all the details for login
     * @return a boolean value whether the password was true or not
     */
    public boolean verifyLogin (LoginData loginData) {
        String sql = "SELECT password FROM users WHERE username = ? AND password = ?;";
        boolean temp = false;
        try (
                Connection connection = getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, loginData.getUsername(),
                                                                            loginData.getPassword()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]);
                        stat.setString(2, (String) params[1]);
                    }
                };
                ResultSet rs2 = stat.executeQuery();
        ) {
            //we retrieve the password from the data we received back
            while(rs2.next())
            {
                if (rs2.getString("password").equals(loginData.getPassword()))
                    temp = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }


    public boolean createAccount(LoginData createAccountData) {
        String sql = "INSERT INTO users(username, firstname, lastname, password) VALUES(?, ?, ?, ?);";
        boolean result = false;

        try (
                Connection connection = getDataSource().getConnection();

                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, createAccountData.getUsername(), createAccountData.getFirstName(),
                        createAccountData.getLastName(), createAccountData.getPassword()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]);
                        stat.setString(2, (String) params[1]);
                        stat.setString(3, (String) params[2]);
                        stat.setString(4, (String) params[3]);
                    }
                };
        ) {
            if (stat.executeUpdate() != 0)
                result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
