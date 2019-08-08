package com.vgrazi.presentations.springcloudcontractsproducer;

import com.vgrazi.presentations.springcloudcontractsproducer.util.Utils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCases {
    @Test
    public void testAdjust() {
        double v;
        v = Utils.round(1_000, 10_000);     assertEquals(10_000,    v, .0001);
        v = Utils.round(10_000, 10_000);    assertEquals(10_000,    v, .0001);
        v = Utils.round(11_000, 10_000);    assertEquals(20_000,    v, .0001);
        v = Utils.round(15_000, 10_000);    assertEquals(20_000,    v, .0001);
        v = Utils.round(16_000, 10_000);    assertEquals(20_000,    v, .0001);
        v = Utils.round(100_000, 10_000);   assertEquals(100_000,   v, .0001);
        v = Utils.round(111_000, 10_000);   assertEquals(120_000,   v, .0001);
        v = Utils.round(4_000, 10_000);     assertEquals(10_000,    v, .0001);
        v = Utils.round(9_000, 10_000);     assertEquals(10_000,    v, .0001);
        v = Utils.round(40_000, 10_000);    assertEquals(40_000,    v, .0001);
        v = Utils.round(50_000, 10_000);    assertEquals(50_000,    v, .0001);
        v = Utils.round(90_000, 10_000);    assertEquals(90_000,    v, .0001);
        v = Utils.round(850_000, 10_000);   assertEquals(850_000,   v, .0001);
    }
}
