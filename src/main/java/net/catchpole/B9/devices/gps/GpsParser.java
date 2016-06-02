package net.catchpole.B9.devices.gps;

import net.catchpole.B9.devices.gps.listener.*;
import net.catchpole.B9.devices.gps.parser.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GpsParser {
    private Sentence sentence = new Sentence();
    private Map<String,List<GpsMessageParser>> messageParserMap = new HashMap<String, List<GpsMessageParser>>();
    private Map<Class,Class> listenerClasses = new HashMap<Class, Class>();
    private Map<Class,List<MessageListener>> listenerMap = new HashMap<Class, List<MessageListener>>();

    public GpsParser() {
        addMessageParser(new DateTimeParser(), DateTimeListener.class);
        addMessageParser(new DilutionOfPrecisionParser(), DilutionOfPrecisionListener.class);
        addMessageParser(new VectorParser(), VectorListener.class);
        addMessageParser(new LocationParser(), LocationListener.class);
        addMessageParser(new RecommendedMinimumParser(), RecommendedMinimumListener.class);
    }

    private void addMessageParser(GpsMessageParser gpsMessageParser, Class listenerClass) {
        List<GpsMessageParser> gpsMessageParserList = messageParserMap.get(gpsMessageParser.getKey());
        if (gpsMessageParserList == null) {
            gpsMessageParserList = new ArrayList<GpsMessageParser>();
            messageParserMap.put(gpsMessageParser.getKey(), gpsMessageParserList);
        }
        gpsMessageParserList.add(gpsMessageParser);
        if (listenerClass != null) {
            listenerClasses.put(listenerClass, gpsMessageParser.getClass());
        }
    }

    public synchronized void addListener(MessageListener messageListener) {
        for (Map.Entry<Class,Class> entry : listenerClasses.entrySet()) {
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

    public synchronized void parse(String line) {
        String[] split = null;
        if (line.length() > 6) {
            String messageType = line.substring(0, 6);
            List<GpsMessageParser> gpsMessageParserList = messageParserMap.get(messageType);
            if (gpsMessageParserList != null) {
                for (GpsMessageParser gpsMessageParser : gpsMessageParserList) {
                    List<MessageListener> messageListenerList = listenerMap.get(gpsMessageParser.getClass());
                    if (messageListenerList != null && sentence.isChecksumValid(line)) {
                        if (split == null) {
                            split = line.split(",");
                        }
                        notifyListeners(messageListenerList, gpsMessageParser.parse(split));
                    }
                }
            }
        }
    }

    private void notifyListeners(List<MessageListener> messageListenerList, Object result) {
        if (result != null) {
            for (MessageListener messageListener : messageListenerList) {
                messageListener.listen(result);
            }
        }
    }
}
