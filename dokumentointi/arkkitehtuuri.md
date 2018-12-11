# BudgetBuddy arkkitehtuuri
## Rakenne
Ohjelman UI on yhteydessä suoraan databaseen sekä sovelluslogiikkaan joka on yhteydessä databaseen. Pakkaus budgetbuddy.ui toteuttaa käyttöliittymän, budgetbuddy.domain toteuttaa laskut ja budgetbuddy.dao tietokannan lukemisen ja kirjoittamisen.
### Käyttöliittymä
Käyttöliittymä sisältää kaksi erillistä näkymää
 - käyttäjän valinta
 - käyttäjän budgetti

Käyttäjän valinta myö avaa uusia ikkunoita
 - käyttäjän editointi
 - käyttäjän lisäys

Budgetti näkymäkin avaa uusia ikkunoita
 - Ostosten lisääminen

Jokainen näkymä on oma Scene-olio, joista ykseikerrallaan näytetää, eli sijoitetaan pääStageen. Käyttöliittymä on rakennettu budgetbuddy.ui.BudgetBudyUi

Käyttöliittymä lukee suoraan tietokannasta käyttäjät, mutta budgettiin liittyvät laskut on eristetty käyttöliittymästä. 

### Sovelluslogiikka
Käyttäjän valitsemiseen Ui listaa kaikki käyttäjät mistä voidaan valita se kenen tietokantaa luetaan. Ui pystyy myös luomaan uusia käyttäjiä mikäli näin halutaan. Käytäjiin Ui pääsee käsiksi käyttämällä UserDao.

Budgetin laskemisen tekee budgebuddy.domain.BudgetManager jolle on annettu käyttäjä ja sen database taulu. BudgetManager pääsee käsikis käyttäjän tauluun käyttämällä ItemCoa rajapintaa.

Ohjelman toimnnollisuus perustuu pääosin seuraavaan luokkakaavioon:
<img src="https://github.com/MTajakka/ot-harjoitustyo/blob/master/dokumentointi/kuvat/luokkakaavio.png" width="260">

### Tietojen Pysyväistallennus
Pakkauksen budgetbuddy.dao luokat DatabaseItemDao ja DatabaseUserDao huolehtii tietojen tallentamisen SQLite tietokantaan. Molemmat luokat perivät Dao luokan, mikä antaa niille perus ominaisuudet.

Sovellus ei käytä suoraan näitä luokkia vaan ne ovat eristetty rajapintojen ItemDao ja UserDao. Molemmat rajapinnat perivät rajapinnan DaoInterface mikä vastaa Dao luokan toimintaa.

### Tietokanta
Sovellus tallentaa tiedot SQLite tietkantaan joka sijaitsee ali hakemistossa database/. Tietokanta koostuu käyttäjä taulusta, missä jokaiselle käyttäjälle on määritelty oma taulu, sekä jokaisen käyttäjän taulu, missä on käyttäjän lisäämät ostokset.
