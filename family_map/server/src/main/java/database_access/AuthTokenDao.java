package database_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.AuthToken;

/** Responsible for interactions with the database AuthTokens table */
public class AuthTokenDao extends DAO {

    /** Constructs an AuthTokenDao object
     *
     * @param conn Database Connection
     */
    public AuthTokenDao(Connection conn) {
        super(conn);
    }

    @Override
    String getUserIDCol() {
        return "username";
    }

    @Override
    String getTblName() {
        return "AuthTokens";
    }

    @Override
    String getInsertStmt() {
        return "INSERT INTO AuthTokens (authToken, username) VALUES(?,?)";
    }

    @Override
    void setStrings(PreparedStatement stmt, Object o) throws SQLException {
        AuthToken token = (AuthToken)o;
        stmt.setString(1, token.getAuthToken());
        stmt.setString(2, token.getUsername());
    }

    @Override
    Object buildObjectFromResultString(ResultSet rs) throws SQLException {
        return new AuthToken(rs.getString("authToken"), rs.getString("username"));
    }
}