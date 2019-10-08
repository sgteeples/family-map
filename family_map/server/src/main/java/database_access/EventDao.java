package database_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Event;

/** Handles interaction with the Events database table */
public class EventDao extends DAO {

    public EventDao(Connection conn) {
        super(conn);
    }

    @Override
    String getUserIDCol() {
        return "descendant";
    }

    @Override
    String getTblName() {
        return  "Events";
    }

    @Override
    String getInsertStmt() {
        return "INSERT INTO Events (EventID, Descendant, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
    }

    @Override
    void setStrings(PreparedStatement stmt, Object o) throws SQLException {
        Event event = (Event)o;

        if (event.getLatitude() == null || event.getLongitude() == null |
            event.getYear() == null) {
            throw new SQLException("The event being inserted contains null values" +
                                   " where there should be real values");
        }

        stmt.setString(1, event.getEventID());
        stmt.setString(2, event.getDescendant());
        stmt.setString(3, event.getPersonID());
        stmt.setDouble(4, event.getLatitude());
        stmt.setDouble(5, event.getLongitude());
        stmt.setString(6, event.getCountry());
        stmt.setString(7, event.getCity());
        stmt.setString(8, event.getEventType());
        stmt.setInt(9, event.getYear());
    }

    @Override
    Object buildObjectFromResultString(ResultSet rs) throws SQLException {
        return new Event(rs.getString("EventID"), rs.getString("Descendant"),
                rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                rs.getInt("Year"));
    }
}
