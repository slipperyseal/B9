package net.catchpole.B9.devices.gps;

import junit.framework.TestCase;
import org.junit.Test;

public class SentenceTest {
    @Test
    public void testChecksum() {
        Sentence sentence = new Sentence();
        TestCase.assertTrue(sentence.isChecksumValid("$PMTK251,38400*27"));
        TestCase.assertEquals("$PMTK251,38400*27\r\n", sentence.addChecksumAndCrlf("$PMTK251,38400"));

        System.out.println(sentence.addChecksumAndCrlf("$PMTK251,9600"));
        System.out.println(sentence.addChecksumAndCrlf("$PMTK314,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0"));
    }
}
