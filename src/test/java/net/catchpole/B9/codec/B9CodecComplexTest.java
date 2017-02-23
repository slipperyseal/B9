package net.catchpole.B9.codec;

import junit.framework.TestCase;
import net.catchpole.B9.codec.bean.ChildBean;
import net.catchpole.B9.codec.bean.GnarlyBean;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

public class B9CodecComplexTest {
    private B9Codec codec = new B9Codec();

    public B9CodecComplexTest() throws Exception {
        codec.addType('g', GnarlyBean.class);
        codec.addType('c', ChildBean.class);
    }

    @Test
    public void loopTest() throws Exception {
        // serialize a few thousand objects. if the codec changes in almost any way, this test should fail
        int expectedLength = 281576;
        long expectedCRC = 736540965L;
        int expectedCompressedLength = 83844;

        List<GnarlyBean> gnarlyBeanList = new ArrayList<>(2000);
        for (int x=0;x<1000;x++) {
            gnarlyBeanList.add(inventBean(x * 999, true));
            gnarlyBeanList.add(inventBean(x * 12345, false));
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(expectedLength);
        for (GnarlyBean gnarlyBean : gnarlyBeanList) {
            codec.encode(gnarlyBean, byteArrayOutputStream);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        for (GnarlyBean gnarlyBean : gnarlyBeanList) {
            if (!gnarlyBean.equals(codec.decode(byteArrayInputStream))) {
                throw new Exception();
            }
        }

        TestCase.assertEquals(expectedLength, data.length);
        TestCase.assertEquals(expectedCRC, getCRC(data));
        TestCase.assertEquals(expectedCompressedLength, compress(data).length);
    }

    @Test
    public void testCodec() throws Exception {
        testGnarlyBean(inventBean(10, false), 191);
        testGnarlyBean(inventBean(-10, true), 70);
        testGnarlyBean(inventBean(32766, true), 72);
        testGnarlyBean(inventBean(-32766, false), 199);
        testGnarlyBean(inventBean(-1000000, true), 74);
        testGnarlyBean(inventBean(1000000, false), 203);
    }

    private void testGnarlyBean(GnarlyBean gnarlyBean, int length) throws Exception {
        byte[] data = codec.encode(gnarlyBean);
        GnarlyBean gnarlyResult = (GnarlyBean)codec.decode(data);

        TestCase.assertEquals(gnarlyBean, gnarlyResult);
        // TreeMap is created by GnarlyBean so it should be preserved
        TestCase.assertEquals(TreeMap.class, gnarlyResult.map.getClass());
        // GnarlyBean doesn't create the set (even though LinkedHashSet was in the original bean) so B9 will use a HashSet
        TestCase.assertEquals(HashSet.class, gnarlyResult.set.getClass());
        TestCase.assertEquals(length, data.length);

        byte[] regular = regularSerialize(gnarlyBean);
        System.out.println("B9: " + data.length +
                " + compressed: " + compress(data).length +
                " java serialized: " + regular.length +
                " + compressed: " + compress(regular).length);
    }

    private byte[] regularSerialize(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    private GnarlyBean inventBean(long value, boolean useNull) {
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
        gnarlyBean.l1 = value++;
        gnarlyBean.l2 = useNull ? null : (long)value;
        gnarlyBean.f1 = ((float)value++) + 0.1f;
        gnarlyBean.f2 = useNull ? null : ((float)value) + 0.1f;
        gnarlyBean.d1 = ((double)value++) + 0.1d;
        gnarlyBean.d2 = useNull ? null : ((double)value) + 0.1d;
        gnarlyBean.string = useNull ? null : ("Test Value abcdefZ%(&#^(&4863q" + value);
        gnarlyBean.maya = useNull ? null : new ChildBean(1.1f);
        // use collection with predictable iteration order
        gnarlyBean.set = new LinkedHashSet<>();
        gnarlyBean.set.add("aaaa");
        gnarlyBean.set.add(new ChildBean(0.2f));
        // use collection with predictable iteration order
        gnarlyBean.map = new TreeMap();
        gnarlyBean.map.put("Key 1.2",new ChildBean(1.2f));
        gnarlyBean.map.put("Key 1.1",new ChildBean(1.1f));
        if (!useNull) gnarlyBean.maya.f = 1.1f;
        gnarlyBean.lucinda = useNull ? null : new ChildBean(1.2f);
        if (!useNull) gnarlyBean.lucinda.f = 1.2f;
        if (!useNull) gnarlyBean.objectArray = new Object[] { null, new ChildBean(2.1f), null,  new ChildBean(2.2f) };
        if (!useNull) gnarlyBean.list = Arrays.asList(new ChildBean(3.1f), new ChildBean(9.1f),
                null, 1000000, 2000000, 3.0F, new ChildBean(4.2f), new ChildBean(4.2f), new ChildBean(4.6f),
                new ChildBean(4.4f));
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

    private long getCRC(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }
}
