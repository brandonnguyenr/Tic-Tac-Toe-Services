package io.github.recorder.proj;

import io.github.coreutils.proj.messages.*;
import io.github.recorder.proj.utils.PreparedStatementWrapper;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DBManager class for the Recorder microservice.
 */
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

    /**
     * Writes a move to the recorder database in the moves table.
     * <p>
     *     A move message is expected to contain information for the following colummns:
     *     <ul>
     *         <li>roomid: id of the room the move was made in</li>
     *         <li>playerid: id of the player that made the move</li>
     *         <li>x_coord: the x coordinate of the move on the board</li>
     *         <li>y_coord: the y coordinate of the move on the board</li>
     *         <li>time: the server system time in milliseconds of the move</li>
     *     </ul>
     *     This information is packaged into the {@code MoveData} argument.
     * </p>
     * @param data the data of the move
     * @return true if the write attempt was successful
     * @author Grant Goldsworth/Kord Boniadi
     * @see MoveData
     */
    public boolean writeMove(MoveData data) {
        String sql = "INSERT INTO moves(roomid, playerid, x_coord, y_coord, time) VALUES(?, ?, ?, ?, ?);";
        boolean temp = false;

        int playerID = getPlayerID(data.getPlayerUserName());
        try (
                Connection connection = DBSource.getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, data.getRoomID(), playerID, data.getX(), data.getY(), data.getTime()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setInt(1, (int) params[0]);    // room ID
                        stat.setInt(2, (int) params[1]);    // player recorder DB id
                        stat.setInt(3, (int) params[2]);    // x
                        stat.setInt(4, (int) params[3]);    // y
                        stat.setLong(5, (long) params[4]);  // time
                    }
                };
        ) {
            if (stat.executeUpdate() != 0) temp = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * Writes the history of a single room to the recorder database in the rooms table.
     * <p>
     *     A room message is expected to contain information for the following colummns:
     *     <ul>
     *         <li>player1id: id of player 1 (arbitrary)</li>
     *         <li>player2id: id of player 2 (arbitrary)</li>
     *         <li>starttime: system time in miliseconds</li>
     *         <li>endtime: system time in miliseconds</li>
     *         <li>startingid: player1id or player2id</li>
     *         <li>winningid: player1id or player2id</li>
     *         <li>istie: true if a tie (full board), false otherwise</li>
     *     </ul>
     *     This information is packaged into the {@code RoomData} argument.
     * </p>
     * @param data the data of the room.
     * @return the recorder database scope id of the room after it is written, -1 if unsuccessful
     * @author Grant Goldsworth, Kord Boniadi
     * @see RoomData
     */
    public int writeRoom(RoomData data) {
        int temp = -1;
        int winner;
        boolean isTie = false;

        // check that both players are not null.
        // recorder DB only writes if both players participated until the game ended (win/tie)
        if (data.getPlayer1() == null || data.getPlayer2() == null) {return -1;}

        int player1id = getPlayerID(data.getPlayer1().getPlayerUserName());
        int player2id = getPlayerID(data.getPlayer2().getPlayerUserName());
        int starter = getPlayerID(data.getStartingPlayerID().getPlayerUserName());

        // if there is a tie (winner is not set), use the magic value of the NULL user in the recorder players table
        if (data.getWinningPlayerID() == null) {
            winner = getPlayerID("NULL");
            isTie = true;
        }
        else {
            // get the winning player's id
            winner = getPlayerID(data.getWinningPlayerID().getPlayerUserName());
        }

        // check if room is open, don't want to be calling data on null players
        String sql = "INSERT INTO rooms(player1id, player2id, starttime, endtime, startingid, winningid, istie) VALUES(?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        try (
                Connection connection = DBSource.getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql,
                            player1id, player2id, data.getStartTime(), data.getEndTime(), starter, winner, isTie
                        ) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setInt(1, (int) params[0]);        // player1id
                        stat.setInt(2, (int) params[1]);        // player2id
                        stat.setLong(3, (long) params[2]);      // startTime
                        stat.setLong(4, (long) params[3]);      // endTime
                        stat.setInt(5, (int) params[4]);        // startingId
                        stat.setInt(6, (int) params[5]);        // winningId
                        stat.setBoolean(7, (boolean) params[6]);// isTie
                    }
                };
                ResultSet response = stat.executeQuery();
        ) {
            // if the execution returns anything other than 0, success. false otherwise, print exception
            while(response.next())
                temp = response.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * Writes a new player to the recorder database in the players table.
     * <p>
     *     A player represents a simplified account, where only the account's
     *     username is stored. This table is used to cross reference the moves and rooms tables
     *     in the recorder database.
     * </p>
     * <p>
     *     This method will not add a player to the database if the username is already logged.
     * </p>
     * @param data the data of the new player
     * @return true if the write attempt was successful, false otherwise.
     * @author Grant Goldsworth
     * @see LoginData
     */
    public boolean writeNewPlayer(LoginData data) {
        // TODO figure out how to check if there's already an entry with this username
        String sql = "INSERT INTO players(username) VALUES(?);";
        boolean temp = false;
        try (
                Connection connection = DBSource.getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, data.getUsername()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]);
                    }
                };
        ) {
            if (stat.executeUpdate() != 0) {temp = true;}
        }
        catch (PSQLException e) {System.out.println("Duplicate name received, ignoring");}
        catch (SQLException e) {e.printStackTrace();}
        return temp;
    }

    /**
     * This method will update the username of an existing player in the recorder database. It listens for
     * an account update message, and checks if the username is in the recorder player db. If it is,
     * the username is updated to the new one to match the foreign databases.
     * @return true if name update was successful, false otherwise
     * @author Grant Goldsworth
     */
    public boolean updatePlayerUsername(UpdateData data) {
        System.out.println("DEBUG: old username = " + data.getUsername());
        System.out.println("DEBUG: new username = " + data.getNewUsername());
        String sql = "UPDATE players SET username = ? WHERE username = ?";
        boolean result = false;
        try (
                Connection connection = DBSource.getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, data.getNewUsername(), data.getUsername()) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]); // first arg is the new username
                        stat.setString(2, (String) params[1]); // second arg is the old username
                    }
                }
            )
        {
            if (stat.executeUpdate() != 0) {result = true;}
        }
        catch (Exception ex) {ex.printStackTrace();}
        return result;
    }

    /**
     * This method takes the unique username of a player (username from the account in the game engine DB),
     * and returns the ID that is associated with player in the recorder DB. The id is primary and serial.
     * @param username username of player, must be unique
     * @return recorder db scope id, -1 if username does not exist
     * @author Grant Goldsworth
     */
    private int getPlayerID(String username) {
        int result = -1; // -1 represents this username does not exist in the table
        String sql = "SELECT id FROM players WHERE username = ?;";
        try (
                Connection connection = DBSource.getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, username) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setString(1, (String) params[0]);
                    }
                };
                ResultSet response = stat.executeQuery();
        )
        {
            // this will only run if the response has something
            // if the database does not have a player with specified username, result is
            // left as -1
            // DA LOOP THRU DA ROWS BRADDA
            while (response.next()) {
                result = response.getInt("id");
            }
        }
        catch (Exception ex) {ex.printStackTrace();}
        System.out.println("getPlayerID(" + username + ") returns " + result);
        return result;
    }

    /**
     * Gets the player name associated with the ID parameter from the recorder
     * database.
     * @param id
     * @return the playername associated with the id parameter in the recorder database.
     * @author Grant Goldsworth
     */
    private String getPlayerName(int id) {
        String result = ""; // empty
        String sql = "SELECT username FROM players WHERE id = ?;";
        try (
                Connection connection = DBSource.getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, id) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setInt(1, (int) params[0]);
                    }
                };
                ResultSet response = stat.executeQuery();
        )
        {
            // this will only run if the response has something
            while (response.next()) {
                result = response.getString("username");
            }
        }
        catch (Exception ex) {ex.printStackTrace();}
        System.out.println("getPlayerUsername(" + id + ") returns " + result);
        return result;
    }

    /**
     * Gets a list of all the rooms from the recorder with the specified player.
     * Rooms in the recorder database are only complete games, i.e. win/loss/tie.
     * @param data the player to search for, only needs to have playername field defined
     * @return ArrayList of RoomData objects. Will be empty if no rooms are found.
     * @see RoomData
     * @author Grant Goldsworth
     */
    public ArrayList<RoomResponse> getRoomsWithPlayer(PlayerData data) {
        // getting rooms with a player in it
        // I receive a player's username,
        //  use this username to get recorder DB scope ID
        //  find all rooms that have this player in it
        //      for each room, get the moves for each room (using room ID)
        String sql = "select * from rooms where player1id = ? or player2id = ?;";
        ArrayList<RoomResponse> resultList = new ArrayList<>();
        int playerId = -1;
        if((playerId = getPlayerID(data.getPlayerUserName())) == -1) {
            return resultList;
        }

        try(
            Connection connection = DBSource.getDataSource().getConnection();
            PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, playerId) {
                @Override
                protected void prepareStatement(Object... params) throws SQLException {
                    stat.setInt(1, (int) params[0]);
                    stat.setInt(2, (int) params[0]);
                }
            };
            ResultSet result = stat.executeQuery();
        ) {

            // parse result, build room data objects
            while (result.next()) {
                // extract the row and build a room data
                RoomData room = new RoomData();
                PlayerData p1 = new PlayerData(getPlayerName(result.getInt("player1id")), null);
                PlayerData p2 = new PlayerData(getPlayerName(result.getInt("player2id")), null);
                PlayerData starter = new PlayerData(getPlayerName(result.getInt("startingid")), null);
                // logic for determining who is the winner
                if (result.getInt("winningid") != getPlayerID("NULL")) {
                    room.setWinningPlayerID(new PlayerData(getPlayerName(result.getInt("winningid")), null));
                }
                room.setStartingPlayerID(starter);
                room.setRoomID(result.getInt("id"));
                room.setPlayer1(p1);
                room.setPlayer2(p2);
                room.setStartTime(result.getLong("starttime"));
                room.setEndTime(result.getLong("endtime"));
                RoomResponse response = new RoomResponse(data.getPlayerUserName(), room);
                // TODO testing stuff
//                System.out.println("Room: " + room);
//                System.out.printf("Response: persp=%s, other=%s, id=%d, start/end=%s/%s, WLT=%s\n\n",
//                        response.getPerspectivePlayer(),
//                        response.getOtherPlayer(),
//                        response.getRoomID(),
//                        response.getFormattedStartTime(),
//                        response.getFormattedEndTime(),
//                        response.getWinLossTie()
//                );
                resultList.add(response);
            }
        }
        catch (SQLException sqlEx) {
            System.out.println("Something went wrong in getRoomsWithPlayer()");
            sqlEx.printStackTrace();
        }
        catch (Exception ex) {ex.printStackTrace();}
        return resultList;
    }

    /**
     * Gets all of the moves associated with a room and returns it as a list
     * of MoveData objects. If no room is found, the list is empty.
     * @param room the RoomData object to get all of the moves from
     * @return array list of MoveData objects
     * @see MoveData
     * @author Grant Goldsworth
     */
    public ArrayList<MoveData> getMovesFromRoom(RoomData room) {
        String sql = "SELECT * FROM moves WHERE roomid = ?;";
        ArrayList<MoveData> result = new ArrayList<>(); // list of moves in this room
        int roomID = room.getRoomID();
        try (
                Connection connection = DBSource.getDataSource().getConnection();
                PreparedStatementWrapper stat = new PreparedStatementWrapper(connection, sql, roomID) {
                    @Override
                    protected void prepareStatement(Object... params) throws SQLException {
                        stat.setInt(1, (int) params[0]);
                    }
                };
                ResultSet response = stat.executeQuery();
        )
        {
            // this will only run if the response has something
            while (response.next()) {
                // get player name from id
                String player = getPlayerName(response.getInt("playerid"));
                // add new move to list
                result.add(new MoveData(roomID, player, response.getInt("x_coord"),
                        response.getInt("y_coord"), response.getLong("time")));
            }
        }
        catch (SQLException sqlEx) {
            System.out.println("Something went wrong in getMovesFromRoom()");
            sqlEx.printStackTrace();
        }
        catch (Exception ex) {ex.printStackTrace();}

        return result;
    }
}
