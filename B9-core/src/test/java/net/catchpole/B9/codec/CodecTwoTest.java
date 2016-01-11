package net.catchpole.B9.codec;

import junit.framework.TestCase;
import net.catchpole.B9.codec.bean.ChildBean;
import net.catchpole.B9.codec.bean.GnarlyBean;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CodecTwoTest {
    private CodecTwo codec = new CodecTwo();

    public CodecTwoTest() throws Exception {
        codec.addType('g', GnarlyBean.class);
        codec.addType('c', ChildBean.class);
    }

    @Test
    public void testCodec() throws Exception {
        test(getBean(10, false), 112);
        test(getBean(-10, true), 104);
        test(getBean(32766, true), 33);
        test(getBean(-32766, false), 125);
        test(getBean(-1000000, true), 33);
        test(getBean(1000000, false), 125);
    }

    private void test(GnarlyBean gnarlyBean, int length) throws Exception {
        byte[] data = codec.encode(gnarlyBean);

        GnarlyBean gnarlyResult = (GnarlyBean)codec.decode(data);

        System.out.println();
        System.out.println(gnarlyBean);
        System.out.println(gnarlyResult);

        TestCase.assertEquals(gnarlyBean, gnarlyResult);
//        TestCase.assertEquals(length, data.length);
        System.out.println("Len: " + data.length + " compressed: " + compress(data).length);
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
        gnarlyBean.string = useNull ? null : ("Test Value " + value);
        gnarlyBean.maya = useNull ? null : new ChildBean(1.1f);
        if (!useNull) gnarlyBean.maya.f = 1.1f;
        gnarlyBean.lucinda = useNull ? null : new ChildBean(1.2f);
        if (!useNull) gnarlyBean.lucinda.f = 1.2f;
        if (!useNull) gnarlyBean.objectArray = new Object[] { null, new ChildBean(2.1f), null,  new ChildBean(2.2f) };
        if (!useNull) gnarlyBean.list = Arrays.asList(new ChildBean(3.1f), null, new ChildBean(4.2f), new ChildBean(4.2f), new ChildBean(4.6f), new ChildBean(4.4f));
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
