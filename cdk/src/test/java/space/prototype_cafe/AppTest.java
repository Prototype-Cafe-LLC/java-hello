package prototype_cafe;

import software.amazon.awscdk.App;
import software.amazon.awscdk.assertions.Template;
import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class AppTest {
    @Test
    public void testStack() throws IOException {
        App app = new App();
        AppStack stack = new AppStack(app, "test");

        Template template = Template.fromStack(stack);
        template.hasResourceProperties("AWS::Lambda::Function", new HashMap<String, Object>(){{
            put("Handler", "prototype_cafe.PeriodicRun::handleRequest");
            put("Runtime", "java21");
        }});
    }
}
