package database_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Person;

/** Handles interaction with the Persons database table */
public class PersonDao extends DAO {

    public PersonDao(Connection conn) {
        super(conn);
    }

    @Override
    String getUserIDCol() {
        return  "descendant";
    }

    @Override
    String getTblName() {
        return  "Persons";
    }

    @Override
    String getInsertStmt() {
        return "INSERT INTO Persons (personID, descendant, firstName, lastName, gender, " +
                "father, mother, spouse) VALUES(?,?,?,?,?,?,?,?)";
    }

    @Override
    void setStrings(PreparedStatement stmt, Object o) throws SQLException {
        Person person = (Person)o;
        stmt.setString(1, person.getPersonID());
        stmt.setString(2, person.getDescendant());
        stmt.setString(3, person.getFirstName());
        stmt.setString(4, person.getLastName());
        stmt.setString(5, person.getGender());
        stmt.setString(6, person.getFather());
        stmt.setString(7, person.getMother());
        stmt.setString(8, person.getSpouse());
    }

    @Override
    Object buildObjectFromResultString(ResultSet rs) throws SQLException {
        return new Person(rs.getString("personID"), rs.getString("descendant"),
                rs.getString("firstName"), rs.getString("lastName"),
                rs.getString("gender"), rs.getString("father"),
                rs.getString("mother"), rs.getString("spouse"));
    }
}
