package net.catchpole.B9.codec;

import net.catchpole.B9.codec.bean.PersonBean;
import org.junit.Test;

public class B9CodecPersonTest {
    private B9Codec codec = new B9Codec();

    @Test
    public void codecPersonTest() throws Exception {
        codec.addType('p', PersonBean.class);

        PersonBean personBean = new PersonBean();
        personBean.setName("Spike");
        personBean.setAge(10);
        personBean.setAlive(true);
        personBean.setCats(null);

        byte[] bytes = codec.encode(personBean);
        System.out.println(bytes.length);
        System.out.println(codec.decode(bytes));
    }
}

/*

        PersonBean.class

             8 bits 'p' = PersonBean.class

        Integer age;
             1 bit  - true - is not null?
             1 bit  - true - is not zero?
             1 bit  - true - fits in 16 bits?
            16 bits - value 10

        boolean isAlive;

             1 bit -  value true

        Integer cats;

             1 bit - false - is not null?

        String name;

            1 bit  - true - is not null?
            1 bit  - true - is not zero length?
            1 bit  - true - fits in 16 bits?
           16 bits - value 5 (length)

           3 bits - bits required = 5
           8 bits - base value = 83
           5 bits -  0 +83 = S
           5 bits - 29 +83 = p
           5 bits - 22 +83 = i
           5 bits - 24 +83 = k
           5 bits - 18 +83 = e

        11 bytes total

 */
