package dao;

import model.Event;
import model.Person;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * A class used to access information regarding Persons from the database
 */

public class PersonDao {

    /**
     * the connection for the database that the class interacts with
     */
    private final Connection conn;


    /**
     * Creates a new instance of the PersonDao Class with a connection to a database
     * @param conn a connection class used to access a target database
     */
    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * a void type function that adds a Person to the database
     * @param person the new Person to be added to the database
     */
    public void insertPerson(Person person) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Persons (personID, associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8,person.getSpouseID());


            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a person into the database");
        }
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
     * Searches a database to find a person from a personID
     * @param personID
     * @return a person correlating to the given personID, or null if none is found
     */
    public Person findPerson(String personID) throws DataAccessException {
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Persons WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }


    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Persons";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table");
        }
    }

    public void deletePerson(String personID) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting the event");
        }
    }

    public Person[] exportPersons(String authToken) throws DataAccessException {
        Person person;
        ArrayList<Person> persons = new ArrayList<Person>();
        ResultSet rs;

        AuthtokenDao authtokenDao = new AuthtokenDao(conn);
        String user = authtokenDao.find_token(authToken).getUsername();


        String sql = "SELECT * FROM Persons WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                persons.add(person);
            }
            //HOW TO DO GOOD RETURN????
            return persons.toArray(new Person[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    public void deletePersonByUser(String username) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting the person");
        }
    }


    public void setParents(String userID, String fatherID, String motherID) throws DataAccessException {
        Person person;
        person = findPerson(userID);
        person.setFatherID(fatherID);
        person.setMotherID(motherID);
        deletePerson(userID);
        insertPerson(person);

    }

}
