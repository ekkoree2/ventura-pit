> Never add comments to the code
> Name everything in code:
"""
1. Variables & Fields
   Style: lowerCamelCase
   Examples:
   lastAttacker, megaActive, hatActive, uberMysticChance, goldReq, contractsLeft, enderChestRows

2. Boolean Variables
   Style: lowerCamelCase (semantic prefixes)
   Examples:
   megaActive, executed, hatActive, canUberstreak, isContractActive, hasRenownPerk

3. Methods
   Style: lowerCamelCase (verb-based)
   Examples:
   save, load, addXP, updateStatus, resetContractTimer, getAutobuyItems

4. Classes
   Style: UpperCamelCase (PascalCase)
   Examples:
   PlayerModel, ContractRewardModel, PlayerEffectsModel, PlayerContractModel

5. Enums (Types)
   Style: UpperCamelCase
   Examples:
   PitMegastreak, CombatState

6. Enum Constants
   Style: UPPER_SNAKE_CASE
   Examples:
   OVERDRIVE, IDLING, MEGASTREAK, FIGHTING

7. Collections (Lists / Sets)
   Style: lowerCamelCase (plural nouns)
   Examples:
   megastreaks, purchasedPerks, pendingContracts, wolves, autobuyItems

8. Maps
   Style: lowerCamelCase (domain-based naming)
   Examples:
   renownPerks, passives, activeContracts, contractProgress, equippedPerks

9. Constants / Fixed Values
   Style: inline literals (no named constants used)
   Examples:
   120, 86400000L, 5000, 10000 

10.Packages
    Style: lowercase, dot-separated
    Examples:
    pit.sandbox.model, pit.sandbox.service, pit.sandbox.util.helper
"""
> Do not add documentation
> Never use full package names to import something. Examples:
- pit.sandbox.Main -> Main
> Do not repeat same code if not needed, add utils if code repeats. Not in same class though, rather special one for utils. Always re-use it if there is opportunity
> Fully utilize these projects if you want to inspire some The-Pit mechanics or code approach. 
- **C:\Users\Admin\Desktop\Data\projects\the-pit\src\main\java\pit\sandbox**
- **C:\Users\Admin\Desktop\Data\projects\PitSim**
> Code is based on **1.21.4** Spigot API
> Do not change existing strings (colors, messages) unless specified.
> If adding any string (message, line ....) Always use Strings class. ALWAYS. Same for all Sounds.
> When adding new class, ALWAYS INCLUDE:
"""
Example:

import com.example.ExampleImport;

/**
* author: ekkoree
* created at: 1/18/2026
  */
public class ClassName {
  // Code goes here.
}
"""
* Above class
> WHEN IMPLEMENTING SOMETHING FROM THE-PIT, DO NOT ADD YOUR OWN CUSTOM STUFF TO CODE. RE-IMPLEMENT 1:1 LOGIC, BASICALLY EVERYTHING, UNLESS SPECIFIED 