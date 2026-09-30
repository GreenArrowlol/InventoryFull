# InventoryFull (26.2)

InventoryFull by **OakLoaf** (Dave), updated to work on Paper 26.2

the original repo (github.com/OakLoaf/InventoryFull) is gone now so this is rebuilt from the last public jar. original is GPL-3.0 so this stays GPL-3.0 as well, see LICENSE

## changes in this fork
- added `/invfull toggle` so players can turn the alerts off for themself (perm `inventoryfull.toggle`, default true). its saved on the player so it stays after relog
- inventory check now runs on the player scheduler instead of async thread
- full check fixed, it was counting armor and offhand slots so it wasnt firing properly. now its just main inventory
- paper detection fixed for chat colors on newer paper
- if message-type in config is invalid it falls back to normal chat message instead of breaking
- MorePaperLib pulled from maven (0.4.3, LGPL-3.0) and shaded like the original

## build
needs JDK 25
```
mvn package
```
jar will be in `target/`

## credits
all the original work is OakLoaf's, i just updated it for 26.2
