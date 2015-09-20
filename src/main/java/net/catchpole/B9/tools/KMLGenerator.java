package net.catchpole.B9.tools;

import net.catchpole.B9.devices.gps.GpsParser;
import net.catchpole.B9.devices.gps.listener.LocationListener;
import net.catchpole.B9.spacial.Location;

import java.io.*;

/**
 * Parses raw GPS data and creates a KML file which can be loaded into Google Earth.
 */
public class KMLGenerator {
    private GpsParser gpsParser = new GpsParser();

    public KMLGenerator(String inFile) throws IOException {
        DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(inFile)));
        try {
            final PrintWriter writer = new PrintWriter(inFile + ".kml", "UTF-8");
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?><kml xmlns=\"http://www.opengis.net/kml/2.2\"><Document><Placemark><name>" +
                    inFile + "</name><description></description><LineString><extrude>1</extrude><tessellate>1</tessellate><coordinates>");
            gpsParser.addListener(new LocationListener() {
                public void listen(Location location) {
                    writer.println(String.format( "%.6f", location.getLongitude() ) + "," + String.format( "%.6f", location.getLatitude() )+ ",0");
                }
            });
            try {
                String line;
                while ((line = dis.readLine()) != null) {
                    gpsParser.parse(line);
                }
                writer.println("</coordinates></LineString></Placemark></Document></kml>");
            } finally {
                writer.close();
            }
        } finally {
            dis.close();
        }
    }

    public static void main(String[] args) throws IOException {
        new KMLGenerator(args[0]);
    }
}
