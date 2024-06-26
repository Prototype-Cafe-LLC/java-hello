package prototype_cafe;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class PeriodicRun implements RequestHandler<EmptyRecord, Context>{

  @Override
  /*
   * No input
   * No return
   */
  public Context handleRequest(EmptyRecord event, Context context)
  {
    // LambdaLogger logger = context.getLogger();
    // logger.log("##handleRequest" + event + context);
    return context;
  }
}

record EmptyRecord() {
}