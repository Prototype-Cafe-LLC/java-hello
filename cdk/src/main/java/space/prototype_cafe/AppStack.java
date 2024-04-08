package prototype_cafe;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.events.targets.LambdaFunction;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.Schedule;
import software.amazon.awscdk.services.events.RuleProps;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import java.util.Arrays;
import java.util.Map;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.Effect;

public class AppStack extends Stack {
    public AppStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AppStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // CloudWatch の log groupの名前を指定
        LogGroup logGroup = LogGroup.Builder.create(this, "PeriodicLambdalogGroup")
            .logGroupName("PeriodicLambdalogGroup")
            .retention(RetentionDays.ONE_WEEK)
            .build();

        // 定期実行する periodicRule を指定
        Rule periodicRule = new Rule(this, "PeriodicLambdaRule", RuleProps.builder()
            .ruleName("PeriodicLambdaRule")
            .schedule(Schedule.expression("rate(1 minute)"))
            .build());

        // lambda に付与するロールを指定
        Role periodicRole = Role.Builder.create(this, "PeriodicLambdaRole")
            .roleName("PeriodicLambdaRole")
            .assumedBy(new ServicePrincipal("lambda.amazonaws.com"))
            .managedPolicies(Arrays.asList(
                ManagedPolicy.fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole")
            ))
            .build();

        // System.out.println("Lambdaの設定を行います");
        Function lambdaFunction = Function.Builder.create(this, "PeriodicLambdaFunction")
            .functionName("PeriodicLambdaFunction")
            .runtime(Runtime.JAVA_21)
            .code(Code.fromAsset("../runner/target/periodicRunLambda-0.2.jar"))
            .handler("prototype_cafe.PeriodicRun::handleRequest")
            .logGroup(logGroup)
            .role(periodicRole)
            .build();

        // System.out.println("Lambdaを作成します");
        LambdaFunction lambdaFunctionResource = new LambdaFunction(lambdaFunction);

        // System.out.println("labmda を定期実行させます");
        periodicRule.addTarget(lambdaFunctionResource);
    }
}
