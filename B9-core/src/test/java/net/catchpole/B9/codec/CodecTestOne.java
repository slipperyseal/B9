package net.catchpole.B9.codec;

import junit.framework.TestCase;
import net.catchpole.B9.codec.bean.ChildBean;
import net.catchpole.B9.codec.bean.GnarlyBean;
import org.junit.Test;

import java.util.Arrays;

public class CodecTestOne {
    private Codec codec = new CodecOne();

    public CodecTestOne() throws Exception {
        codec.addType('g', GnarlyBean.class);
        codec.addType('c', ChildBean.class);
    }

    @Test
    public void testCodec() throws Exception {
        test(getBean(-10, true), 25);
        test(getBean(10, false), 104);
        test(getBean(-1000000, true), 33);
        test(getBean(1000000, false), 125);
    }

    private void test(GnarlyBean gnarlyBean, int length) throws Exception {
        byte[] data = codec.encode(gnarlyBean);
        TestCase.assertEquals(length, data.length);

        GnarlyBean gnarlyResult = (GnarlyBean)codec.decode(data);
        TestCase.assertEquals(gnarlyBean, gnarlyResult);
        System.out.println();
        System.out.println(gnarlyBean);
        System.out.println(gnarlyResult);
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
        gnarlyBean.string = useNull ? null : "Value " + value;
        gnarlyBean.maya = useNull ? null : new ChildBean(1.1f);
        if (!useNull) gnarlyBean.maya.f = 1.1f;
        gnarlyBean.lucinda = useNull ? null : new ChildBean(1.2f);
        if (!useNull) gnarlyBean.lucinda.f = 1.2f;
        if (!useNull) gnarlyBean.objectArray = new Object[] { null, new ChildBean(2.1f), null,  new ChildBean(2.2f) };
        if (!useNull) gnarlyBean.list = Arrays.asList( new ChildBean(3.1f), null,  new ChildBean(4.2f) );
        return gnarlyBean;
    }
}

