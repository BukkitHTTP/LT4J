package cf.huzpsb.lt4j;

import cf.huzpsb.lt4j.tasks.Task;

public class Main {
    public static final String VERSION = "1.0 alpha";

    public static void main(String[] args) {
        System.out.println("Load Test 4 Java v" + VERSION);
        new Task("127.0.0.1", 80, 1000000, "name").run(); // 1M
        System.exit(0);
    }
}
