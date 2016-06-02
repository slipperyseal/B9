package net.catchpole.B9.event;

import junit.framework.TestCase;
import org.junit.Test;

public class TriggerTest {
    @Test
    public void testEventTrigger() throws Exception {
        final int[] i = new int[1];
        Trigger trigger = new Trigger(new TriggerListener() {
            public void trigger() {
                i[0]++;
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        });
        trigger.trigger(); // hit
        Thread.sleep(100);
        trigger.trigger(); // miss
        Thread.sleep(100);
        trigger.trigger(); // miss
        Thread.sleep(100);
        trigger.trigger(); // miss

        Thread.sleep(1000);
        trigger.trigger(); // hit
        Thread.sleep(100);
        trigger.dispose();

        TestCase.assertEquals(2, i[0]);
    }
}