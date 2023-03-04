package dao;

import model.Authtoken;
import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * A class used to access information regarding Authentication tokens from the database
 */

public class AuthtokenDao {


    private final Connection conn;

    /**
     * Creates a new instance of the Authtoken Class with a connection to a database
     * @param conn a connection class used to access a target database
     */
    public AuthtokenDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Validates the username and password of the user
     * @param username
     * @param password
     * @return if the username and password are a valid pair
     */
    public boolean validate(String username, String password) {
        return true;
    }

    /**
     * Tells whether an authentication token exists in the database
     * @param authtoken the token that may exist in the database
     * @return whether or not the token exists in the database
     */
    public boolean exists(Authtoken authtoken) throws DataAccessException {
        Authtoken token;
        ResultSet rs;
        String sql = "SELECT * FROM Authtokens WHERE authtoken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authtoken.getAuthtoken());
            rs = stmt.executeQuery();
            if (rs.next()) {
                token = new Authtoken(rs.getString("authtoken"), rs.getString("username"));
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an authtoken in the database");
        }
    }

    /**
     * a void type function that adds an authentication token to the database
     * @param authtoken the new authentication token to be added to the database
     */
    public void insert_token(Authtoken authtoken) throws DataAccessException {
        String sql = "INSERT INTO Authtokens (authtoken, username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, authtoken.getAuthtoken());
            stmt.setString(2, authtoken.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a person into the database");
        }
    }


    public Authtoken find_token(String authtoken) throws DataAccessException {
        Authtoken token;
        ResultSet rs;
        String sql = "SELECT * FROM Authtokens WHERE authtoken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authtoken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                token = new Authtoken(rs.getString("authtoken"), rs.getString("username"));
                return token;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an authtoken in the database");
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Authtokens";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the user table");
        }
    }


    public Authtoken find_token_by_user(String username) throws DataAccessException {
        Authtoken token;
        ResultSet rs;
        String sql = "SELECT * FROM Authtokens WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                token = new Authtoken(rs.getString("authtoken"), rs.getString("username"));
                return token;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a user in the database");
        }
    }

}
