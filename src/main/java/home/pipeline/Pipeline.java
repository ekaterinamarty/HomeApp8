package home.pipeline;

import java.io.IOException;

public class Pipeline<TInput, TOutput> {
    private final Handler<TInput, TOutput> handler;

    public Pipeline(Handler<TInput, TOutput> handler) {
        this.handler = handler;
    }

    public TOutput execute(TInput input) throws IOException {
        return handler.invoke(input);
    }

    public <K> Pipeline<TInput, K> add(Handler<TOutput, K> next) {
        return new Pipeline<>(input -> next.invoke(handler.invoke(input)));
    }
}
