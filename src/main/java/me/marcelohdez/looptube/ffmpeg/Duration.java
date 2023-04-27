package me.marcelohdez.looptube.ffmpeg;

public record Duration(long millis) {
    /** Will attempt to parse the given string into a duration object. (hh:mm:ss -> millis) */
    public static Duration from(String s) {
        var sec = 0L;
        var sections = s.split(":");
        var power = sections.length - 1; // power of duration, 0 = sec, 1 = min, 3 = hour

        for (var num : sections) {
            var value = Integer.parseInt(num);
            sec += value * Math.pow(60, power);
            power--;
        }

        return new Duration(sec * 1000);
    }

    public Duration minus(Duration other) {
        return new Duration(millis - other.millis);
    }

    @Override
    public String toString() {
        var sec = millis / 1000;
        var min = sec / 60;
        var hr = sec / 60 / 60;
        sec = sec % 60;

        var s = hr > 0 ? "%d:%2d:%2d".formatted(hr, min, sec) :
                    min > 0 ? "%d:%2d".formatted(min, sec) :
                            String.valueOf(sec);

        return s.replace(' ', '0');
    }
}
