package application.metrics;

/**
 * Created by omryhay on 15/12/2015.
 */
//@Aspect
//public class LogExecutionTime
//{
//    private static StatsDClient statsD;
//
//    public static void Init(String prefix, String host, int port)
//    {
//        statsD = new NonBlockingStatsDClient(prefix, host, port);
//    }
//
//    @Around(value = "@annotation(application.metrics.GraphiteTimer)")
//    public Object LogExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable
//    {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//
//        System.out.println("Starting timed operation");
//        final Object retVal = joinPoint.proceed();
//        logExecutionTime(joinPoint, stopWatch);
//        return retVal;
//    }
//
//    private void logExecutionTime(ProceedingJoinPoint joinPoint, StopWatch stopWatch)
//    {
//        statsD.recordExecutionTime(String.format("%s.%s",
//                        joinPoint.getTarget().getClass().getName(),
//                        joinPoint.getSignature().getName()),
//                stopWatch.getTime());
//    }
//}