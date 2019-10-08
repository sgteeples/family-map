package database_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

/** Handles interaction with the Users database table */
public class UserDao extends DAO {

    public UserDao(Connection conn) {
        super(conn);
    }

    @Override
    String getUserIDCol() {
        return "username";
    }

    @Override
    String getTblName() {
        return "Users";
    }

    @Override
    String getInsertStmt() {
        return "INSERT INTO Users (username, password, email, firstName, lastName, " +
                "gender, personID) VALUES(?,?,?,?,?,?,?)";
    }

    @Override
    void setStrings(PreparedStatement stmt, Object o) throws SQLException {
        User user = (User)o;
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getFirstName());
        stmt.setString(5, user.getLastName());
        stmt.setString(6, user.getGender());
        stmt.setString(7, user.getPersonID());
    }

    @Override
    Object buildObjectFromResultString(ResultSet rs) throws SQLException {
        return new User(rs.getString("username"), rs.getString("password"),
                rs.getString("email"), rs.getString("firstName"),
                rs.getString("lastName"), rs.getString("gender"),
                rs.getString("personID"));
    }

}