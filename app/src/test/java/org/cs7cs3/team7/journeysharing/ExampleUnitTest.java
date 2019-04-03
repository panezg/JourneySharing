package org.cs7cs3.team7.journeysharing;

import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testUnixTimestamp(){
        long unixTimestamp = Instant.now().getEpochSecond();
        System.out.println(unixTimestamp);
    }

    @Test
    public void testTimeTransfer(){
        String date = "2019/02/22";
        String time = "22:50";
        String res = date.replaceAll("/", "") + time.replaceAll(":", "");
        System.out.println(res);
    }
}