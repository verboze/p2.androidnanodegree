package com.vbz.spotifystreamer.utils;

public class PlayerUtils {
    public String timeToString(long millisecs) {
        // convert millisecs to seconds and minutes
        int minutes = (int)(millisecs % (1000*60*60)) / (1000*60);
        int seconds = (int) ((millisecs % (1000*60*60)) % (1000*60) / 1000);

        return String.format("%02d:%02d", minutes, seconds);
    }

    public int getCurrentPercentage(long elapsedTime, long totalDuration) {
        // convert millisecs to seconds, then take the percentage of that
        long currentSecs = (int) (elapsedTime / 1000);
        long totalSecs = (int) (totalDuration / 1000);
        Double percentage = (((double)currentSecs) / totalSecs) * 100;

        return percentage.intValue();
    }

    public long getElapsedFromPercentage(int percent, long totalDuration) {
        return (long) (((double) percent) / 100 * totalDuration);
    }
}
