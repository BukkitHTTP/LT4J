package cf.huzpsb.lt4j.tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Executor {
    public static final ExecutorService es = new ThreadPoolExecutor(2, 20, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
}
