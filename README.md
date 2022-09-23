## armorkits
Dependency on using Paper, not plain Spigot or Bukkit

Simple plugin inspired by Hardcore Factions kits which are used on most Towny servers; as far as I can tell from only custom implementations; I could not find a released version

Some inspiration taken from HCFPlus but my own work; for this reason it requires Paper instead - if one was inclined they could probably retrofit it to use HCFPlus event on Spigot

Current limitations:<br/>
Armour Kits must be made up of 4 pieces or "AIR" for empty slot; no wildcards
Players can only have one "kit" at a time; which due to above limitation is irrelevant


Configuration:<br/>
kitname:<br/>
  materials:<br/>
    - ANY<br/>
    - FOUR<br/>
    - ARMOUR<br/>
    - MATERIALS<br/>
  effects:<br/>
    EFFECTNAME: AMPLIFIER<br/>
    EFFECTNAME: AMPLIFIER<br/>
    
Default config includes:<br/>
Explorer Kit - Speed 2<br/>
Miner Kit - Night Vision, Fire Resistance, Haste 2<br/>
