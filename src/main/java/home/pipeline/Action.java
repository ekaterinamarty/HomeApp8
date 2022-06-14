package home.pipeline;

import java.io.IOException;
import java.sql.SQLException;

@FunctionalInterface
public interface Action {
    void invoke() throws IOException, SQLException;
}
