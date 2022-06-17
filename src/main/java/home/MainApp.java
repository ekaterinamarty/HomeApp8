package home;

import java.sql.SQLException;

public class MainApp {
    public static void main(String[] args) throws SQLException {
        UserInterface userInterface = new UserInterface();
        userInterface.runApplication();
    }
}