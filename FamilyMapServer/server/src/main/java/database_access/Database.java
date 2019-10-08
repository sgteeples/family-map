package database_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Handles opening, closing, and distributing connections to the database */
public class Database {

    private Connection conn;
    private EventDao eventDao;
    private PersonDao personDao;
    private UserDao userDao;
    private AuthTokenDao authTokenDao;

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Opens a connection to the database. Also creates DAOs for the connection
     *
     * @throws DatabaseException Thrown if something goes wrong related to the database
     * @return A connection to the database
     */
    public Connection openConnection() throws DatabaseException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Unable to open connection to database");
        }

        eventDao = new EventDao(conn);
        personDao = new PersonDao(conn);
        userDao = new UserDao(conn);
        authTokenDao = new AuthTokenDao(conn);

        return conn;
    }

    /** Closes the connection to the database. Also makes the DAO connections null
     *
     * @param commit A boolean that is true if changes to the database should be committed
     */
    public void closeConnection(boolean commit) {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }
            conn.close();
            conn = null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        eventDao = null;
        personDao = null;
        userDao = null;
        authTokenDao = null;

    }

    /** Creates the EventDao, AuthTokenDao, PersonDao, and UserDao database tables */
    public void createTables() {
        try {
            openConnection();

            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Events" +
                    "(" +
                    "eventID TEXT UNIQUE NOT NULL, " +
                    "descendant TEXT NOT NULL, " +
                    "personID TEXT NOT NULL, " +
                    "latitude REAL NOT NULL, " +
                    "longitude REAL NOT NULL, " +
                    "country TEXT NOT NULL, " +
                    "city TEXT NOT NULL, " +
                    "eventType TEXT NOT NULL, " +
                    "year INTEGER NOT NULL, " +
                    "primary key (eventID), " +
                    "foreign key (descendant) references Users(username), " +
                    "foreign key (personID) references Persons(personID)" +
                    ")");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Users" +
                    "(" +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "firstName TEXT NOT NULL, " +
                    "lastName TEXT NOT NULL, " +
                    "gender TEXT NOT NULL CHECK(gender IN ('f', 'm')), " +
                    "personID TEXT UNIQUE NOT NULL, " +
                    "primary key (username), " +
                    "foreign key (personID) references Persons(PersonID)" +
                    ")");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AuthTokens " +
                    "(" +
                    "authToken TEXT UNIQUE NOT NULL, " +
                    "username TEXT NOT NULL, " +
                    "primary key (authToken), " +
                    "foreign key (username) references Users(username)" +
                    ")");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Persons " +
                    "(" +
                    "personID TEXT UNIQUE NOT NULL, " +
                    "descendant TEXT NOT NULL, " +
                    "firstName TEXT NOT NULL, " +
                    "lastName TEXT NOT NULL, " +
                    "gender TEXT NOT NULL CHECK(gender IN ('f', 'm')), " +
                    "father TEXT, " +
                    "mother TEXT, " +
                    "spouse TEXT, " +
                    "primary key (personID), " +
                    "foreign key (descendant) references Users(username)" +
                    ")");
            closeConnection(true);
        } catch (Exception e) {
            closeConnection(false);
            System.out.println(e.getMessage());
        }
    }

    /** Clears all tables in the database
     *
     * @throws DatabaseException Thrown if something goes wrong related to the database
     */
    public void clearTables() throws DatabaseException {
        // Create each of the individual tables
        eventDao.clearTable();
        authTokenDao.clearTable();
        personDao.clearTable();
        userDao.clearTable();
    }

    public EventDao getEventDao() {
        return eventDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public AuthTokenDao getAuthTokenDao() {
        return authTokenDao;
    }
}