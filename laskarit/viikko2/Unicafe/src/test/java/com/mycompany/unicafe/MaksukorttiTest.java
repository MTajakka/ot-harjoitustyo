package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoOikein() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void sapldonKasvatus() {
        kortti.lataaRahaa(10);
        assertEquals(20, kortti.saldo());
    }    
    
    @Test
    public void saldonVaheneminen() {
        assertTrue(kortti.otaRahaa(5));
        assertEquals(5, kortti.saldo());
    }
    
    @Test
    public void saldonEiMeneNegatiiviseksi() {
        assertFalse(kortti.otaRahaa(20));
        assertEquals(10, kortti.saldo());
    }
    
    
}
