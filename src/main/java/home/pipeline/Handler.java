package home.pipeline;

import java.io.IOException;

@FunctionalInterface
public interface Handler<TInput, TOutput> {
    TOutput invoke(TInput input) throws IOException;
}
