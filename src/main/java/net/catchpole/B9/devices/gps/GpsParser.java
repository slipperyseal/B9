package net.catchpole.B9.devices.gps;

import net.catchpole.B9.devices.gps.listener.*;
import net.catchpole.B9.devices.gps.parser.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GpsParser {
    private Map<String,GpsMessageParser> messageParserMap = new HashMap<String, GpsMessageParser>();
    private Map<Class,String> listenerClasses = new HashMap<Class, String>();
    private Map<String,List<MessageListener>> listenerMap = new HashMap<String, List<MessageListener>>();

    public GpsParser() {
        addMessageParser(new DilutionOfPrecisionParser(), DilutionOfPrecisionListener.class);
        addMessageParser(new HeadingParser(), HeadingListener.class);
        addMessageParser(new LocationParser(), LocationListener.class);
        addMessageParser(new RecommendedMinimumParser(), RecommendedMinimumListener.class);
    }

    private void addMessageParser(GpsMessageParser gpsMessageParser, Class listenerClass) {
        messageParserMap.put(gpsMessageParser.getKey(), gpsMessageParser);
        if (listenerClass != null) {
            listenerClasses.put(listenerClass, gpsMessageParser.getKey());
        }
    }

    public void addListener(MessageListener messageListener) {
        for (Map.Entry<Class,String> entry : listenerClasses.entrySet()) {
            if (entry.getKey().isAssignableFrom(messageListener.getClass())) {
                List<MessageListener> messageListeners = listenerMap.get(entry.getValue());
                if (messageListeners == null) {
                    messageListeners = new ArrayList<MessageListener>();
                    listenerMap.put(entry.getValue(), messageListeners);
                }
                messageListeners.add(messageListener);
                return;
            }
        }
    }

    public void parse(String line) {
        String key = line.substring(0,6);
        GpsMessageParser gpsMessageParser = messageParserMap.get(key);
        List<MessageListener> messageListenerList = listenerMap.get(key);
        if (gpsMessageParser != null && messageListenerList != null && checkSum(line)) {
            Object result = gpsMessageParser.parse(line.split(","));
            if (result != null) {
                for (MessageListener messageListener : messageListenerList) {
                    messageListener.listen(result);
                }
            }
        }
    }

    public boolean checkSum(String line) {
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
