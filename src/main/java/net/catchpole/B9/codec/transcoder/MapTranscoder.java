package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class MapTranscoder implements TypeTranscoder<Map>, FieldInterceptor<Map>  {
    private ObjectArrayTranscoder objectArrayTranscoder;

    public MapTranscoder(ObjectArrayTranscoder objectArrayTranscoder) {
        this.objectArrayTranscoder = objectArrayTranscoder;
    }

    public Map read(BitInputStream in) throws IOException {
        Object[] all = objectArrayTranscoder.read(in);
        Map value = new HashMap();
        for (int x=0;x<all.length;x+=2) {
            value.put(all[x],all[x+1]);
        }
        return value;
    }

    public void write(BitOutputStream out, Map value) throws IOException {
        Object[] values = new Object[value.size()*2];
        int x=0;
        Set<Map.Entry> entrySet = value.entrySet();
        for (Map.Entry entry : entrySet) {
            values[x++] = entry.getKey();
            values[x++] = entry.getValue();
        }
        objectArrayTranscoder.write(out, values);
    }

    public Map intercept(Map currentValue, Map newValue) {
        if (currentValue != null && currentValue.isEmpty()) {
            currentValue.putAll(newValue);
            return currentValue;
        } else {
            return newValue;
        }
    }
}
