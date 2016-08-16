/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playlist;

/**
 *
 * @author Serioja
 */
public class Duration {

    private final Integer seconds;
    private String prettySeconds;

    public Duration(Integer sec) {
        prettySeconds = toPrettyString(sec);
        seconds = sec;
    }

    public Integer getSecondsCount() {
        return seconds;
    }

    @Override
    public String toString() {
        return prettySeconds;
    }

    private String toPrettyString(Integer seconds) {
        int totalSeconds = seconds;

        int d = totalSeconds / 86400;
        totalSeconds = totalSeconds - (d * 86400);
        int h = totalSeconds / 3600;
        totalSeconds = totalSeconds - (h * 3600);
        int m = totalSeconds / 60;
        totalSeconds = totalSeconds - (m * 60);
        int s = totalSeconds;

        if (d > 0) {
            return String.format("(%d)%02d:%02d:%02d", d, h, m, s);
        } else {
            return String.format("%02d:%02d:%02d", h, m, s);
        }
    }
}
