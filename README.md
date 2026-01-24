🧭 Adventúrka: Hawkins & Upside Down

Textová adventúrna hra inšpirovaná svetom Stranger Things, v ktorej sa ocitneš v okolí mesta Hawkins. Tvojou úlohou je preskúmavať lokácie, zbierať predmety, používať ich v správny čas a rozhodnutiami ovplyvniť, ako celý príbeh skončí.
Pozor – nie každá cesta vedie k dobrému koncu 👀
Niektoré chyby ťa môžu stáť život… alebo celý Hawkins.

🎮 O čom hra je

Hra je textová adventúra, kde hráč:
sa pohybuje medzi miestnosťami (les, búda, tunel, laboratórium, portál…),
zbiera predmety (kľúč, baterka, mapa, páka),
používa predmety na správnych miestach,
odhaľuje skryté cesty a alternatívne endingy.
Existuje viacero koncov:
❌ zlý ending (Demogorgon 😬),
✅ dobrý ending (záchrana Hawkinsu),
⚠️ alternatívny ending (poklad, ale otvorený portál).

🕹️ Ovládanie hry

Hra funguje cez textové príkazy, napríklad:
go les – pohyb do inej miestnosti
take kluc – zobratie predmetu
use baterka – použitie predmetu
smerové príkazy závisia od aktuálnej miestnosti
Všetko, čo môžeš robiť, závisí od toho, kde sa práve nachádzaš a čo máš v inventári.

▶️ Ako hru spustiť

Otvor projekt v Java IDE (napr. IntelliJ IDEA, Eclipse)
Skontroluj, že máš:
Java JDK 8 alebo novšie
Spusti hlavnú triedu projektu (main)
Hra sa ovláda cez konzolu / terminál.

📦 Použité knižnice

Projekt používa externú knižnicu na prácu s JSON súbormi (na mapu sveta hry):
org.json:json:20240303
Ak používaš Maven, pridaj do pom.xml.
Ak IDE, tak ju stačí pridať ako externú knižnicu.

🗺️ Mapa hry

Mapa sveta je uložená v JSON súbore, ktorý definuje:
miestnosti,
prechody medzi nimi,
predmety,
použitia predmetov,
a rôzne konce hry (game over stavy).
Vďaka tomu sa dá hra jednoducho rozširovať o nové lokácie a príbehy.

✨ Zaujímavosti

Nie všetky miestnosti sú dostupné hneď
Niektoré akcie zmenia samotnú mapu hry
Jeden predmet môže úplne zmeniť priebeh príbehu
Skrytý ending existuje… ale nie je ľahké ho nájsť 😉

👤 Autor

Projekt vytvorený ako školská Java adventúrna hra.
Cieľom je precvičiť:
objektovo orientované programovanie,
prácu s JSON,
práca s github,
logiku hry a stavov.