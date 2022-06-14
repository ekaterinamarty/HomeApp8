package home;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface {
    private final Controller controller = new Controller();

    public void runApplication() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        //noinspection InfiniteLoopStatement
        while (true) {
            String sb = "Выберите действие:\n" +
                    "1 - Получить текущую погоду\n" +
                    "2 - Получить погоду на следующий день\n" +
                    "3 - Получить погоду на следующие 5 дней\n" +
                    "4 - Вывести таблицу прогнозов из базы данных\n" +
                    "выход (exit) - завершить работу";
            System.out.println(sb);
            String result = scanner.nextLine();
            checkIsExit(result);

            if (!result.equals("4")) {
                System.out.println("Введите название города на английском языке");
                String city = scanner.nextLine();
                setGlobalCity(city);
            }

            try {
                validateUserInput(result);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            try {
                notifyController(result);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkIsExit(String result) throws SQLException {
        if (result.equalsIgnoreCase("выход") || result.equalsIgnoreCase("exit")) {
            System.out.println("Завершаю работу");
            controller.onCloseApp();
            System.exit(0);
        }
    }

    private void setGlobalCity(String city) {
        ApplicationGlobalState.getInstance().setSelectedCity(city);
    }

    private void validateUserInput(String userInput) throws IOException {
        if (userInput == null || userInput.length() != 1) {
            throw new IOException("Incorrect user input: expected one digit as answer, but actually get " + userInput);
        }
        try {
            Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            throw new IOException("Incorrect user input: character is not numeric!");
        }
    }

    private void notifyController(String input) throws IOException, SQLException {
        controller.onUserInput(input);
    }
}
