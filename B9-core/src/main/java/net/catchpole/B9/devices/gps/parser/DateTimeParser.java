package net.catchpole.B9.devices.gps.parser;

public class DateTimeParser implements GpsMessageParser<String> {
    public String parse(String[] line) {
        String time = line[1];
        String date = line[9];
        if (time.length() > 6 && date.length() == 6) {
            String year = date.substring(4,6);
            boolean millennium = Integer.parseInt(year) < 50;
            return (millennium ? "20" : "19") + year +
                    '-' + date.substring(2,4) + '-' +
                    date.substring(0,2) +  'T' +
                    time.substring(0, 2) + ':' +
                    time.substring(2, 4) + ':' +
                    time.substring(4) + 'Z';
        }
        return null;
    }

    public String getKey() {
        return "$GPRMC";
    }
}
