package cf.huzpsb.lt4j.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskRunnable implements Runnable {
    private static final AtomicInteger rdm = new AtomicInteger(114514);
    public String target;
    public int port;
    public AtomicInteger success;
    public AtomicInteger failed;
    public CountDownLatch latch;
    public String flag;

    public TaskRunnable(String target, int port, AtomicInteger success, AtomicInteger failed, CountDownLatch latch, String flag) {
        this.target = target;
        this.port = port;
        this.success = success;
        this.failed = failed;
        this.latch = latch;
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            latch.await();
            Socket socket = new Socket(target, port);

            // Send HTTP request
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.printf("GET /qwq/db/create?name=6&age=10&id=%d HTTP/1.1", rdm.incrementAndGet());
            out.println("Host: " + target);
            out.println("Connection: Close"); // Close the connection after the response is received
            out.println(); // End of request
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains(flag)) {
                    success.incrementAndGet();
                    return;
                }
            }
            socket.close();
        } catch (Exception ignored) {
        }
        failed.incrementAndGet();
    }
}
