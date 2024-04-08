package prototype_cafe;

import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.mockito.Mockito;

public class AppTest {
    @Test
    public void testStack() throws IOException {
        Context context = Mockito.mock(Context.class);
        EmptyRecord event = new EmptyRecord();
        PeriodicRun handler = new PeriodicRun();
        handler.handleRequest(event, context);
    }
}

