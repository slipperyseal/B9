package net.catchpole.B9.codec;

import junit.framework.TestCase;
import net.catchpole.B9.codec.bean.ChildBean;
import net.catchpole.B9.codec.bean.GnarlyBean;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class B9CodecComplexTest {
    private B9Codec codec = new B9Codec();

    public B9CodecComplexTest() throws Exception {
        codec.addType('g', GnarlyBean.class);
        codec.addType('c', ChildBean.class);
    }

    @Test
    public void testCodec() throws Exception {
        test(getBean(10, false), 191);
        test(getBean(-10, true), 70);
        test(getBean(32766, true), 72);
        test(getBean(-32766, false), 199);
        test(getBean(-1000000, true), 74);
        test(getBean(1000000, false), 203);
    }

    private void test(GnarlyBean gnarlyBean, int length) throws Exception {
        byte[] data = codec.encode(gnarlyBean);

        GnarlyBean gnarlyResult = (GnarlyBean)codec.decode(data);

        if (!gnarlyBean.equals(gnarlyResult)) {
            System.out.println(gnarlyBean);
            System.out.println(gnarlyResult);
        }

        TestCase.assertEquals(gnarlyBean, gnarlyResult);
        TestCase.assertEquals(length, data.length);
        byte[] regular = regularSerialize(gnarlyBean);
        System.out.println("B9: " + data.length +
                " B9 compressed: " + compress(data).length +
                " java serialized: " + regular.length +
                " java serialized compressed: " + compress(regular).length);
    }

    private byte[] regularSerialize(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    private GnarlyBean getBean(long value, boolean useNull) {
        GnarlyBean gnarlyBean = new GnarlyBean();
        gnarlyBean.b1 = false;
        gnarlyBean.b2 = useNull ? null : true;
        gnarlyBean.by1 = (byte)value++;
        gnarlyBean.by2 = useNull ? null : (byte)value;
        gnarlyBean.c1 = (char)value++;
        gnarlyBean.c2 = useNull ? null : (char)value;
        gnarlyBean.s1 = (short)value++;
        gnarlyBean.s2 = useNull ? null : (short)value;
        gnarlyBean.i1 = (int)value++;
        gnarlyBean.i2 = useNull ? null : (int)value;
        gnarlyBean.l1 = (long)value++;
        gnarlyBean.l2 = useNull ? null : (long)value;
        gnarlyBean.f1 = ((float)value++) + 0.1f;
        gnarlyBean.f2 = useNull ? null : ((float)value) + 0.1f;
        gnarlyBean.d1 = ((double)value++) + 0.1d;
        gnarlyBean.d2 = useNull ? null : ((double)value) + 0.1d;
        gnarlyBean.string = useNull ? null : ("Test Value abcdefZ%(&#^(&4863q" + value);
        gnarlyBean.maya = useNull ? null : new ChildBean(1.1f);
        gnarlyBean.set = new HashSet();
        gnarlyBean.set.add("aaaa");
        gnarlyBean.set.add(new ChildBean(0.2f));
        gnarlyBean.map = new HashMap();
        gnarlyBean.map.put("Key 1.2",new ChildBean(1.2f));
        gnarlyBean.map.put("Key 1.1",new ChildBean(1.1f));
        if (!useNull) gnarlyBean.maya.f = 1.1f;
        gnarlyBean.lucinda = useNull ? null : new ChildBean(1.2f);
        if (!useNull) gnarlyBean.lucinda.f = 1.2f;
        if (!useNull) gnarlyBean.objectArray = new Object[] { null, new ChildBean(2.1f), null,  new ChildBean(2.2f) };
        if (!useNull) gnarlyBean.list = Arrays.asList(new ChildBean(3.1f), new ChildBean(9.1f), null, 1000000, 2000000, 3.0F, new ChildBean(4.2f), new ChildBean(4.2f), new ChildBean(4.6f), new ChildBean(4.4f));
        return gnarlyBean;
    }

    public byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

    public byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }
}
