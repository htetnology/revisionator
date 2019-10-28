public class TimeFormatter {
    public static String millisecondsToMinutesAndSeconds(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;

        return minutes + " minutes and " + seconds + " seconds";
    }
}
