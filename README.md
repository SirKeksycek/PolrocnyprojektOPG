🧭 Adventúrka: Ravenrock

Textová adventúrna hra odohrávajúca sa v meste **Ravenrock**, ktorému hrozí katastrofa. Energetické jadro mesta sa prehrieva a len správne rozhodnutia môžu zabrániť zničeniu celého mesta.

Tvojou úlohou je preskúmavať lokácie, zbierať predmety, používať ich na správnych miestach a rozhodnúť o osude Ravenrocku.

Pozor – nie všetky cesty vedú k úspechu. Niektoré rozhodnutia môžu skončiť smrťou alebo zničením mesta.

---

🎮 O čom hra je

Hra je textová adventúra, v ktorej hráč:

* pohybuje sa medzi miestnosťami,
* zbiera užitočné predmety,
* používa predmety na odomykanie nových možností,
* objavuje tajné lokácie,
* rozhoduje o tom, ako príbeh skončí.

---

🗺️ Lokácie

Počas hry môžeš navštíviť:

* Okraj mesta
* Opustený sklad
* Strážna veža
* Podzemná chodba
* Brána do centra
* Energetické jadro
* Centrum mesta
* Zachránené mesto
* Tajná miestnosť
* Zamknutá brána

Nie všetky lokácie sú dostupné od začiatku hry.

---

🎒 Predmety

Počas hrania môžeš nájsť:

* karta
* naradie
* mapa
* baterka
* páka

Každý predmet má svoj význam a niektoré lokácie alebo udalosti sú dostupné len po jeho použití.

---

🕹️ Ovládanie hry

Hra využíva textové príkazy.

Príklady:

```text
go sklad
go veza
take karta
take baterka
use karta
use mapa
use naradie
```

Dostupné príkazy závisia od aktuálnej miestnosti a predmetov v inventári.

---

🏁 Možné konce hry

✅ Dobrý ending

Podarí sa ti stabilizovať energetické jadro pomocou:

* naradia alebo
* páky

Mesto Ravenrock bude zachránené.

---

❌ Zlý ending

Ak vstúpiš do centra bez potrebného vybavenia, môžeš zomrieť v tme.

Rovnako môžeš skončiť zle pri nesprávnom postupe cez zabezpečené oblasti mesta.

---

⚠️ Tajný ending

Pomocou mapy môžeš objaviť skrytú cestu vedúcu do tajnej miestnosti s pokladom.

Nie každý hráč ju nájde.

---

▶️ Spustenie hry

1. Otvor projekt v Java IDE (IntelliJ IDEA, Eclipse alebo NetBeans).
2. Uisti sa, že máš nainštalované:

   * Java JDK 8 alebo novšie.
3. Spusť hlavnú triedu projektu (`main`).
4. Hra sa ovláda cez konzolu.

---

📦 Použité knižnice

Projekt používa knižnicu na spracovanie JSON súborov:

```xml
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20240303</version>
</dependency>
```

Ak nepoužívaš Maven, stačí pridať knižnicu medzi externé závislosti projektu.

---

🗃️ Herný svet

Mapa hry je definovaná v JSON súbore, ktorý obsahuje:

* miestnosti,
* predmety,
* prechody medzi miestnosťami,
* použitia predmetov,
* podmienky pre konce hry,
* zmeny mapy počas hrania.

Vďaka tomu je možné hru jednoducho rozširovať bez úpravy zdrojového kódu.

---

✨ Zaujímavosti

* Niektoré cesty sa sprístupnia až po použití predmetov.
* Mapa dokáže meniť svoje prepojenia počas hry.
* Existujú skryté lokácie.
* Viacero predmetov môže viesť k rovnakému cieľu.
* Hra obsahuje viac než jeden možný koniec.

---

👤 Autor

Projekt bol vytvorený ako školská Java adventúrna hra.

Cieľ projektu:

* precvičiť objektovo orientované programovanie,
* prácu s JSON súbormi,
* návrh hernej logiky,
* správu projektu pomocou Git/GitHub,
* prácu so stavmi hry a inventárom.
