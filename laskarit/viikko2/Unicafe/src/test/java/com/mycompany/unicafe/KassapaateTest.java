/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author markus
 */
public class KassapaateTest {
    
    Kassapaate kassa;
    
    public KassapaateTest() {
    }
    
    @Before
    public void setUp() {
         kassa = new Kassapaate();
    }
    
    @Test
    public void kassapaateLuotuOikein() {
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kateisellaMaksettuEdullisestiYlihinnan() {
        assertEquals(60, kassa.syoEdullisesti(300));
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
        assertEquals(100000+240, kassa.kassassaRahaa());
    }
    
    @Test
    public void kateisellaMaksettuEdullisestiAlihinnan() {
        assertEquals(200, kassa.syoEdullisesti(200));
        assertEquals(0, kassa.edullisiaLounaitaMyyty());

    }
    
    @Test
    public void kateisellaMaksettuMaukkaastiYlihinnan() {
        assertEquals(30, kassa.syoMaukkaasti(430));
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
        assertEquals(100000+400, kassa.kassassaRahaa());
    }
    
    @Test
    public void kateisellaMaksettuMaukkaastiAlihinnan() {
        assertEquals(330, kassa.syoMaukkaasti(330));
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());

    }
    
    @Test
    public void kortillaEdullisestiTarpeeksi() {
        Maksukortti kortti = new Maksukortti(500);
        assertTrue(kassa.syoEdullisesti(kortti));
        assertEquals(260, kortti.saldo());
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void kortillaEdullisestiEiTarpeeksi() {
        Maksukortti kortti = new Maksukortti(200);
        assertFalse(kassa.syoEdullisesti(kortti));
        assertEquals(200, kortti.saldo());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void kortillaMaukkaastiTarpeeksi() {
        Maksukortti kortti = new Maksukortti(500);
        assertTrue(kassa.syoMaukkaasti(kortti));
        assertEquals(100, kortti.saldo());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void kortillaMaukkaastiEiTarpeeksi() {
        Maksukortti kortti = new Maksukortti(200);
        assertFalse(kassa.syoMaukkaasti(kortti));
        assertEquals(200, kortti.saldo());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void kortinLataus() {
        Maksukortti kortti = new Maksukortti(200);
        kassa.lataaRahaaKortille(kortti, 100);
        assertEquals(100100, kassa.kassassaRahaa());
        assertEquals(300, kortti.saldo());
    }
    
    @Test
    public void kortinNegatiivinenLataus() {
        Maksukortti kortti = new Maksukortti(200);
        kassa.lataaRahaaKortille(kortti, -100);
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(200, kortti.saldo());
    }
}
