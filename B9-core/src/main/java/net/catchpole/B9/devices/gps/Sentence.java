package net.catchpole.B9.devices.gps;

public class Sentence {
    public boolean isChecksumValid(String line) {
        int len = line.length();
        if (len > 5 && line.charAt(0) == '$') {
            int value = 0;
            for (int x=1;x<len;x++) {
                int c = line.charAt(x);
                // line may have trailing special characters
                if (c == '*' && len >= x+2) {
                    return value == (getHexValue(line.charAt(x+1)) << 4) + getHexValue(line.charAt(x+2));
                }
                value ^= c;
            }
        }
        return false;
    }

    public String addChecksumAndCrlf(String line) {
        int value = 0;
        int len = line.length();
        for (int x=1;x<len;x++) {
            int c = line.charAt(x);
            value ^= c;
        }
        return line + "*" + String.format("%02X", value) + "\r\n";
    }

    private int getHexValue(char c) {
        if (c >= '0' && c <= '9') {
            return c-'0';
        }
        if (c >= 'a' && c <= 'f') {
            return c-'a'+0xa;
        }
        if (c >= 'A' && c <= 'F') {
            return c-'A'+0xa;
        }
        return 0;
    }
}
