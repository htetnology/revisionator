public class TimeKeeper {
    private long startTimestamp;
    private long endTimestamp;
    private long timeTaken;

    public void start() {
        startTimestamp = System.currentTimeMillis();
    }

    public void stop() {
        endTimestamp = System.currentTimeMillis();
        timeTaken = endTimestamp - startTimestamp;
    }

    public long getElapsedMilliseconds() {
        return timeTaken;
    }
}
