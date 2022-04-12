# Jamtastic

A mod that adds jam. Jam that you can add many, many things to. Beef, slime, and honey jam. Any takers?

![Demo GIF](demo.gif)

Jamtastic can be downloaded from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/jam-fabric).

## Mod Authors

If, for one reason or another, you want to add some kind of support for Jamtastic, here are some instructions.

### Adding Valid Jam Ingredients

If you want to make your item a valid jam ingredient, there are two options:

1. Give your item a `FoodComponent`, using the `.food()` method in your item settings. After that, everything else is
   handled automatically. Nothing else needs to be done by you.
2. If your item doesn't have a food component by design (e.g. cake, because it needs to be placed), you can add it to
   the `jam_ingredients` folder:

   Make a new folder: `src/main/resources/data/<your mod id>/jam_ingredients`, and add a JSON file (it can be named
   whatever you like).

   The JSON file should look like this:

    ```json
   { 
     "ingredients": [
       {
         "item": "minecraft:cake",
         "hunger": 14,
         "saturation": 2.8 
       }
     ]
   }
   ```

   That example adds cake as a valid ingredient, with 14 hunger, and 2.8 saturation.

### Adding Other Compatibility

If you want to add a different kind of compatibility feature, please open
an [issue](https://github.com/JamCoreModding/Jamtastic/issues), so we can discuss the best course of action. For
example, when adding support for Sandwichable it made the most sense to put the code on my side rather than in
Sandwichable.
