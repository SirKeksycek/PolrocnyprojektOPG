🎮 Hra [X]

Atribúty

player : Player

rooms : Map<String, Room>

Metódy

getCurrentRoom()

checkGameOver()

go(direction)

take(itemName)

use(itemName)

Vzťahy

1 × Player

1 × * Room

🧍 Player [X]

Atribúty

currentRoomId : String

inventory : Map<String, Item>

Vzťahy

1 × * Item

🏠 Room [X]

Atribúty

id : String

label : String

description : String

gameOver : String

Kolekcie

exits : Map<String, Exit>

items : Map<String, Item>

uses : Map<String, UseAction>

Vzťahy

1 × * Exit

1 × * Item

1 × * UseAction

🚪 Exit [X]

Atribúty

direction : String

target : String

Vzťahy

→ 1 Room (cieľová miestnosť)

🎒 Item [X]

Atribúty

name : String

desc : String

Použitie

môže byť v miestnosti

môže byť v inventári hráča

🛠️ UseAction [X]

Atribúty

itemId : String

message : String

replace : Replace

Vzťahy

1 → 0..1 Replace

🔄 Replace [X]

Atribúty

newDesc : String

newExits : List<Exit>

Vzťahy

1 × * Exit

▶️ Main [X]

Metódy

main(String[] args)

Zodpovednosť

spúšťa Engine

inicializuje hru




