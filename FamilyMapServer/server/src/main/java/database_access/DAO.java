package database_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Abstraction of a DAO - a Database Access Object */
public abstract class DAO {

    private final Connection conn;

    DAO(Connection conn) {
        this.conn = conn;
    }

    /** Clears database table associated with the DAO
     *
     * @throws DatabaseException Thrown if something goes wrong with the database
     */
    public void clearTable() throws DatabaseException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM " + getTblName();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }
    }

    /** Inserts an object into the database table associated with the DAO
     *
     * @param o Object to insert
     * @throws DatabaseException Thrown if something goes wrong with the database
     */
    public void insert(Object o) throws DatabaseException {
        String sql = getInsertStmt();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setStrings(stmt, o);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }
    }

    /** Removes all data associated with a given user from database table associated
     * with the DAO.
     *
     * @param username Username of the user to remove data for
     * @throws DatabaseException Thrown if something goes wrong with the database
     */
    public void removeUserData(String username) throws DatabaseException {
        String sql = "DELETE FROM " + getTblName() + " WHERE " + getUserIDCol() + " = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }
    }

    /** Finds all objects that have a certain value in a certain column of the DAO
     *
     * @param col The name of the column to search in
     * @param val The value to search for
     * @return A List of Objects that represent the items found in the database
     * @throws DatabaseException Thrown if something goes wrong with the database
     */
    public List<Object> find(String col, String val) throws DatabaseException {
        ResultSet rs;
        Object obj;
        List<Object> foundObjects = new ArrayList<>();

        String sql = "SELECT * FROM " + getTblName() + " WHERE " + col + " = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, val);
            rs = stmt.executeQuery();

            while (rs.next()) {
                obj = buildObjectFromResultString(rs);
                foundObjects.add(obj);
            }

            return foundObjects;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage());
        }
    }

    // Overridden by the individual children DAOs
    abstract String getTblName();
    abstract String getInsertStmt();
    abstract String getUserIDCol();
    abstract void setStrings(PreparedStatement stmt, Object o) throws SQLException;
    abstract Object buildObjectFromResultString(ResultSet rs) throws SQLException;
}