package home.requests;

import java.util.HashMap;
import java.util.Map;

public class RequestContext extends HashMap<String, Object> {
    public RequestContext() {
        super();
    }

    public RequestContext(Map<String, Object> m) {
        super(m);
    }
}
