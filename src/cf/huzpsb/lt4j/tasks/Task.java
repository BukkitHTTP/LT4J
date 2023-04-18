package cf.huzpsb.lt4j.tasks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    public AtomicInteger success = new AtomicInteger(0);
    public AtomicInteger failed = new AtomicInteger(0);
    public String target;
    public int port;
    public int tskNum;
    public String flag;

    public Task(String target, int port, int tskNum, String flag) {
        this.target = target;
        this.port = port;
        this.tskNum = tskNum;
        this.flag = flag;
    }

    public void run() {
        System.out.println("Initializing test...");
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < tskNum; i++) {
            Executor.es.execute(new TaskRunnable(target, port, success, failed, latch, flag));
        }
        long startTime = System.currentTimeMillis();
        int lastSuccess = 0;
        int lastFailed = 0;
        int maxQPS = 0;
        latch.countDown();
        while (true) {
            try {
                //noinspection BusyWait
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int currentSuccess = success.get();
            int currentFailed = failed.get();
            int deltaSuccess = currentSuccess - lastSuccess;
            int deltaFailed = currentFailed - lastFailed;
            System.out.print("Running! SuccessQPS: " + deltaSuccess + " ErrorQPS: " + deltaFailed);
            if (deltaSuccess > maxQPS) {
                maxQPS = deltaSuccess;
            }
            lastSuccess = currentSuccess;
            lastFailed = currentFailed;
            int finished = currentSuccess + currentFailed;
            System.out.printf("(%.2f%%)\n", ((double) finished / tskNum) * 100);
            if (finished == tskNum) {
                break;
            }
        }
        long endTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Test finished! Total time: " + endTime + "s");
        System.out.println("Total success: " + lastSuccess + " Total failed: " + lastFailed);
        System.out.printf("Max QPS: %d (%.2f msPQ)\n", maxQPS, (double) 1000 / maxQPS);
        System.out.printf("Average QPS: %d (%.2f msPQ)\n", lastSuccess / endTime, (double) 1000 / (lastSuccess / endTime));
        System.out.printf("Uprate %.2f%%\n", ((double) lastSuccess / tskNum) * 100);
    }
}
