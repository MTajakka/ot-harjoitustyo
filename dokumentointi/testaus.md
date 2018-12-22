# Tausdokumentti

Ohjelmaa on testattu JUnit testejä käyttämällä sekä manuaalisesti käyttöjärjestelmän toiminnallisuuksia.

## JUnit testit

### sovelluslogiikka

Sovelluksen logiikan testaa JUnit testit BudgetManagerTest, ItemTest ja TypePriceTest, jotka simuloivat käyttöjarjestelmää.

### Dao

Daotkin on testattu käyttämällä JUnitestejä käyttämällä ja ne soveltavat CRUD (Create Read Update Delete) menetelmää. Testit luovat databasen, minkä ne sitten poistavat testien jälkeen.

## Testikattavuus

Käyttöliittymää lukuun ottamatta, ohjelman testikattavuus on 89% komentoa ja 71% haaroja. Suurin osa testaamattomista ominaisuuksita on BudgetManagerin komennot mitkä se suoraan lähettää ItemDaoon sekä Itemin setterit ja equalsin eri haarat.

## Käyttöliittymä 

Käyttöliittymä on testattu manuaalisesti, mutta syventävästi. 

### Asennus ja kofigurointi

Ohjelmaa on testattu vain Linux-ympäristössä käyttöohjeen kuvaamalla tavalla. Ohjelma on testattu tilanteessa, että käyttäjän on täytynyt vain käynnistää ohjelma ja se on alkanut toimia.
