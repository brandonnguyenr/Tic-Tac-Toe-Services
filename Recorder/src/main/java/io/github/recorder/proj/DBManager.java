package io.github.recorder.proj;

import io.github.coreutils.proj.messages.MoveData;
import io.github.coreutils.proj.messages.RoomData;
import io.github.recorder.proj.utils.PreparedStatementWrapper;

import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {
    private DBManager() {
        // empty
    }

    private static class InstanceHolder {
        private static final DBManager INSTANCE = new DBManager();
    }

    public static DBManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public boolean writeMove(MoveData data) {
        String sql = "INSERT INTO moves(gameid, playerid, x_coord, y_coord, time) VALUES(?, ?, ?, ?, ?);";
        boolean temp = false;
        try (
                Connection connection = DBSource.getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, data.getRoomID(), data.getPlayerID(), data.getX(), data.getY(), data.getTime()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]);
                        stat.setString(2, (String) params[1]);
                        stat.setString(3, (String) params[2]);
                        stat.setString(4, (String) params[3]);
                        stat.setString(5, (String) params[4]);
                    }
                };
        ) {
            if (stat.executeUpdate() != 0) temp = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public boolean writeRoom(RoomData data) {
        String sql = "INSERT INTO game(starttime, endtime, player1id, player2id, startingplayerid, winningplayerid) VALUES(?, ?, ?, ?, ?);";
        boolean temp = false;
        try (
                Connection connection = DBSource.getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql,
                        data.getStartTime(), data.getEndTime(), data.getPlayer1().getPlayerUserName(),
                        data.getPlayer2().getPlayerUserName(), data.getStartingPlayerID().getPlayerUserName(),data.getWinningPlayerID().getPlayerUserName()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]);
                        stat.setString(2, (String) params[1]);
                        stat.setString(3, (String) params[2]);
                        stat.setString(4, (String) params[3]);
                        stat.setString(5, (String) params[4]);
                        stat.setString(6, (String) params[5]);
                    }
                };
        ) {
            if (stat.executeUpdate() != 0) temp = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

//    public String getPlayerInfo(String userName) {
//        String sql = "SELECT * FROM users WHERE username = ?;";
////        JSONObject jsonObj = new JSONObject();
//        JSONArray array = new JSONArray();
//        try (
//                Connection connection = getDataSource().getConnection();
//                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, userName) {
//                    @Override
//                    protected void prepareStatement(Object... params) throws SQLException {
//                        stat.setString(1, (String) params[0]);
//                    }
//                };
//                ResultSet rs = stat.executeQuery();
//        ) {
//            while (rs.next()) {
//                JSONObject record = new JSONObject();
//                record.put("id", rs.getString("id"));
//                record.put("username", rs.getString("username"));
//                record.put("firstname", rs.getString("firstname"));
//                record.put("lastname", rs.getString("lastname"));
//                record.put("password", rs.getString("password"));
//                array.put(record);
//            }
////            jsonObj.put("users", array);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return array.toString();
//    }

}
