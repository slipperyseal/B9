package net.catchpole.B9.devices.gps.parser;

import net.catchpole.B9.devices.gps.message.DilutionOfPrecision;

public class DilutionOfPrecisionParser implements GpsMessageParser<DilutionOfPrecision> {
    public DilutionOfPrecision parse(String[] line) {
        if (line[2].length() == 1) {
            DilutionOfPrecision dilutionOfPrecision = new DilutionOfPrecision();
            dilutionOfPrecision.setFix(parseCode(line[2]));
            for (int x=3;x<3+12;x++) {
                Integer prn = parseInt(line[x]);
                if (prn != null) {
                    dilutionOfPrecision.addPseudorandomNoiseCode(prn);
                }
            }
            dilutionOfPrecision.setPrecisionDilution(parseDouble(line[14]));
            dilutionOfPrecision.setHorizontalDilution(parseDouble(line[15]));
            dilutionOfPrecision.setVerticalDilution(parseDouble(line[16]));
            return dilutionOfPrecision;
        }
        return null;
    }

    public String getKey() {
        return "$GPGSA";
    }

    private Double parseDouble(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(string);
        } catch (Throwable t) {
            return null;
        }
    }

    private Integer parseInt(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(string);
        } catch (Throwable t) {
            return null;
        }
    }

    private char parseCode(String string) {
        return string == null || string.length() != 1 ? 0 : string.charAt(0);
    }
}
