# Käyttöohje

## Käynnistys

Ohjelma käynnistetään komennolla 
```
java -jar todoapp.jar
```
Käynnistäessään ohjelma luo kansion database, minne se tallentaa tietokantansa.

## Kirjautumis ikkuna

### Käyttäjä
Ruudulla näkyy kaikki käytössä olevat käyttäjät. Huom! käyttäjät eivät ole salasansuojattuja. Käyttäjälle voi kirjautua painamalla _login_ painiketta tai sen voi poistaa painamalla _delete_ painiketta. Käyttäjän nimen voi vaihtaa painamalla edit painiketta. Tämä avaa uuden ikkunan, johon voi syöttää uuden nimen. 

### Käyttäjän lisääminen
Käyttäjän voi lisätä painamalla _Add User_ painiketta, mikä avaa uuden ikkunna. Käyttäjä tarvitsee vain uniikin nimen.

## Käyttöliittymä

Vasemmassa yläkulmassa näkyy millä käyttäjällä on kirjaantunut ja käyttäjän voi vaihtaa painamalla nappia _Logout_. 

### Yhteenveto

Ikkunan keskellä näkyy yhteenveto lähes kaikista tallennetuista ostoksista.

### Ostoksen lisääminen

Napin _Add items_ painaminen avaa uuden ikkunan missä syötetään ostokset. Osotos koosttu sen nimestä, tyypistä, eli miten se tulisi luokitella, hinta yhteensä, määrä, ja yksittäis hinta. Yhteis hinta ja yksittäis hintä päivittävät toisiaan määrän mukaan, joten toinen täytyy vain lisätä. Määrä kuvastaa kappalemäärää ja täten ottaa vain kokonais lukuja. Hinta voi olla desimaaliluku kahden desimaalin tarkkuudella. Ostoksen voi lisätä välilistaan painamalla _Add_ nappia. Tämä ei vielä lisää sitä tiedostoon. Useita ostoksia voi säilyttää väli muistissa, ja niitä pystyy poistamaan painamalla _Delete_ nappia. Ostoksille pitää vielä määritää päivä ja se tapahtuu vasemmasta yläkulmasta syöttämällä päivä MM/dd/2018 muodossa tai valitsemalla kalenterista painamalla kalenteri nappia ja valitsemalla päivän. Kaikki välimuistin ostokoset voi lisätä tietokantaan painamalla _Finish_ nappia tai perua painamalla _Cancel_ nappia.
