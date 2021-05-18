package io.github.gameengine.proj;

import io.github.coreutils.proj.messages.LoginData;
import io.github.coreutils.proj.messages.UpdateData;
import io.github.gameengine.proj.utils.PreparedStatementWrapper;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

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

    /**
     * This method will be called from the {@code AuthorizationCallback.java} class that will query the database to
     * verify the password for the user. It will select a password from the database and check whether the entered password
     * and username was correct or not.
     * @param loginData: the data that contains all the details for login
     * @return a boolean value whether the password was true or not
     * @author Utsav Parajuli
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
 //           we retrieve the password from the data we received back
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


    /**
     * This method will be called from the {@code AuthorizationCallback.java} class that will query the database to
     * create account for the client. It will update the database and store their username, firstname, lastname and password.
     * @param createAccountData: the data that contains all the details for creating a new account
     * @return a boolean value whether the account was successfully created or not
     * @author Utsav Parajuli
     */
    public boolean createAccount(LoginData createAccountData) {
        String sql = "INSERT INTO users(username, firstname, lastname, password) VALUES(?, ?, ?, ?);";
        boolean result = false;

        //trying to get the database connection
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
            //Throws a PSQLException for duplicate username
            try {
                if (stat.executeUpdate() != 0)
                    result = true;
            } catch (PSQLException e) {
                //empty result = false
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This method will update the password of the particular user specified with the username.
     * @param updatePasswordData: The class that contains all the data to update
     * @return a boolean representation of the request whether it was successful or not
     * @author Utsav Parajuli
     */
    public boolean updatePassword (UpdateData updatePasswordData) {
        String sql = "UPDATE users SET password = ? WHERE username = ? AND password = ?;";
        boolean temp = false;
        try (
                Connection connection = getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql,
                        updatePasswordData.getNewPassword(), updatePasswordData.getUsername(),
                        updatePasswordData.getPassword()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]);
                        stat.setString(2, (String) params[1]);
                        stat.setString(3, (String) params[2]);
                    }
                };
        ) {
            if (stat.executeUpdate() != 0)
                temp = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }


    /**
     * This method will update the personal info of the particular user specified with the username.
     * @param updatePersonalData: The class that contains all the data to update
     * @return a boolean representation of the request whether it was successful or not
     * @author Utsav Parajuli
     */
    public boolean updatePersonalInfo (UpdateData updatePersonalData) {
        String sql = "UPDATE users SET firstname = ?, lastname = ? WHERE username = ?;";
        boolean temp = false;
        try (
                Connection connection = getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql,
                        updatePersonalData.getFirstName(), updatePersonalData.getLastName(),
                        updatePersonalData.getUsername()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]);
                        stat.setString(2, (String) params[1]);
                        stat.setString(3, (String) params[2]);
                    }
                };
        ) {
            if (stat.executeUpdate() != 0)
                temp = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }


    /**
     * This method will update the username of the particular user specified with the username.
     * @param updateUsername: The class that contains all the data to update
     * @return a boolean representation of the request whether it was successful or not
     * @author Utsav Parajuli
     */
    public boolean updateUsername (UpdateData updateUsername) {
        String sql = "UPDATE users SET username = ? WHERE username = ?;";
        boolean temp = false;
        try (
                Connection connection = getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql,
                        updateUsername.getNewUsername(), updateUsername.getUsername()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]);
                        stat.setString(2, (String) params[1]);
                    }
                };
        ) {
            //Throws a PSQLException for duplicate username
            try {
                if (stat.executeUpdate() != 0)
                    temp = true;
            } catch (PSQLException e) {
                //temp is false
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * This method will update the account deletion status of the particular user specified with the username.
     * @param updateDeleted: The class that contains all the data to update
     * @return a boolean representation of the request whether it was successful or not
     * @author Utsav Parajuli
     */
    public boolean updateIsDeleted (UpdateData updateDeleted) {
        String sql = "UPDATE users SET isDeleted = ? WHERE username = ?;";
        boolean temp = false;
        try (
                Connection connection = getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql,
                        updateDeleted.isDeleted(), updateDeleted.getUsername()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]);
                        stat.setString(2, (String) params[1]);
                    }
                };
        ) {
            if (stat.executeUpdate() != 0)
                temp = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}
