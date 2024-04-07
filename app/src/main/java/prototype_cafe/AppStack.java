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
            .logGroupName(id)
            .retention(RetentionDays.ONE_WEEK)
            .build();

        // 定期実行する periodicRule を指定
        Rule periodicRule = new Rule(this, "PeriodicLambdaRule", RuleProps.builder()
            .ruleName(id)
            .schedule(Schedule.expression("rate(1 minute)"))
            .build());

        // lambda に付与するロールを指定
        Role role = Role.Builder.create(this, "PeriodicLambdaRole")
            .roleName(id)
            .assumedBy(new ServicePrincipal("lambda.amazonaws.com"))
            .managedPolicies(Arrays.asList(
                ManagedPolicy.fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole")
            ))
            .inlinePolicies(Map.of(
                // rule リソースへのアクセス権限
                "PeriodicLambdaPolicy", PolicyDocument.Builder.create()
                    .statements(Arrays.asList(
                        PolicyStatement.Builder.create()
                            .actions(Arrays.asList("events:PutTargets", "events:PutRule", "events:DescribeRule"))
                            .resources(Arrays.asList(periodicRule.getRuleArn()))
                            .effect(Effect.ALLOW)
                            .build()
                    ))
                    .build()
            ))
            .build();

        System.out.println("Lambdaの設定を行います");
        Function lambdaFunction = Function.Builder.create(this, "PeriodicLambdaFunction")
            .functionName(id)
            .runtime(Runtime.JAVA_21)
            .code(Code.fromAsset("../app/build/libs/app-1.0-all.jar"))
            .handler("index.MyLambdaHandler.handleRequest")
            .logGroup(logGroup)
            .role(role)
            .build();

        System.out.println("Lambdaを作成します");
        LambdaFunction lambdaFunctionResource = new LambdaFunction(lambdaFunction);

        System.out.println("labmda を定期実行させます");
        periodicRule.addTarget(lambdaFunctionResource);
    }
}
