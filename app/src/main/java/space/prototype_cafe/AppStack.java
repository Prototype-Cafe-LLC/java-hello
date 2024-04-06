package space.prototype_cafe;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Code;
// import software.amazon.awscdk.services.events.targets.LambdaFunction;
// import software.amazon.awscdk.services.events.targets.LambdaFunctionProps;

public class AppStack extends Stack {
    public AppStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AppStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Function lambdaFunction = Function.Builder.create(this, "MyLambdaFunction")
            .runtime(Runtime.JAVA_21)
            .code(Code.fromAsset("lambda"))
            .handler("space.prototype_cafe.main.handler")
            .build();

        // Create a Lambda function as a stack resource
        // LambdaFunction lambdaFunctionResource = new LambdaFunction(this, "MyLambdaFunctionResource", LambdaFunctionProps.builder()
        //     .runtime(Runtime.JAVA_21)
        //     .code(Code.fromAsset("MyLambdaHandler.java"))
        //     .handler("space.prototype_cafe.MyLambdaHandler")
        //     .build());

        // output "hello" as log
        // System.out.println("hello");
    }
}
