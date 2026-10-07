# Wand caps and rods

Thaumaturge wands are built from a cap and a rod. One call gives you the crafting item players hold, the part it puts on the wand, and the name, stats and tooltip that go with it.

You do not write any assembly recipes. Register a cap or a rod and the arcane workbench recipes for every pairing with every other part appear on their own, priced from the craft costs you set.

## A cap

```js
StartupEvents.registry('minecraft:item', event => {
    event.create('mypack:wand_cap_netherite', 'thaumaturge:wand_cap')
        .costModifier(0.75)
        .craftCost(12)
        .wandName('Netherite Shod')
})
```

The item id prefix `wand_cap_` is stripped to form the part id, so this cap is `mypack:netherite` on a wand. Textures:

```
kubejs/assets/mypack/textures/item/wand_cap_netherite.png        the inventory item
kubejs/assets/mypack/textures/models/wand_cap_netherite.png      the cap on the wand model
```

## A rod

```js
StartupEvents.registry('minecraft:item', event => {
    event.create('mypack:wand_rod_netherite', 'thaumaturge:wand_rod')
        .capacity(200)
        .craftCost(12)
        .primalCharge('thaumaturge:ignis')
        .glow(true)
        .assemblyGate('thaumaturge:rod_silverwood')
        .wandName('Netherite')
})
```

Textures follow the same pattern with `wand_rod_` names.

## Cap methods

| Method | Default | What it does |
|---|---|---|
| `.costModifier(float)` | 1.0 | Vis cost multiplier when casting. Below 1 is a discount |
| `.specialAspects([ids], float)` | none | These aspects use a different modifier than the base one |
| `.craftCost(int)` | 1 | Multiplied with the rod's value to price assembly recipes |
| `.capTexture(id)` | convention path | Texture on the wand model |
| `.wandName(text)` | "Netherite Capped" | The cap half of the wand's display name |

## Rod methods

| Method | Default | What it does |
|---|---|---|
| `.capacity(int)` | 25 | Vis stored per primal pool |
| `.craftCost(int)` | 1 | Assembly recipe pricing, like the cap |
| `.primalCharge(aspect)` | off | Rod slowly recharges that primal, like the mod's elemental rods |
| `.primalCharge()` | off | Recharges all six primals, like the primal staff |
| `.glow(bool)` | false | Wand model renders fullbright |
| `.staff(bool)` | false | Staff core: bigger, casts, cannot craft |
| `.runes(bool)` | false | Free potency level for socketed foci |
| `.assemblyGate(research)` | unlock_auromancy | Research entry required for assembly recipes with this rod |
| `.onAssemble((wand, input) => {})` | none | Runs when a wand is assembled from this rod, see below |
| `.rodTexture(id)` | convention path | Texture on the wand model |
| `.wandName(text)` | "Netherite" | The rod half of the wand's display name |

## Carrying data onto the wand

`onAssemble` runs every time the arcane workbench builds a wand, sceptre or staff from your rod. `wand` is the finished stack and you change it in place. `input` is the crafting grid: `input.items()`, `input.getItem(x, y)` and `input.player()`.

```js
event.create('mypack:wand_rod_bloodwood', 'thaumaturge:wand_rod')
    .capacity(150)
    .onAssemble((wand, input) => {
        input.items().forEach(stack => {
            if (stack.id == 'mypack:wand_rod_bloodwood' && stack.customName) {
                wand.customName = stack.customName
            }
        })
    })
```

The callback also runs for the preview in the result slot, so it can see the same grid more than once. Only touch `wand`, never the grid items.

## Naming

The wand name is composed from both parts. With the examples above a finished wand reads "Netherite Shod Netherite Wand". The `wandName` values are exactly the fragments the mod uses, compare "Gold Banded" or "Silver Bossed" on the built in caps.

## Testing without crafting

```
/give @s thaumaturge:wand[thaumaturge:wand_parts={cap:"mypack:netherite",rod:"mypack:netherite"}]
```

## Parts without a crafting item

Sometimes you want a cap that players can never craft, say for a wand that only drops from a boss. Register it on the part registry instead and no item or recipes are created:

```js
StartupEvents.registry('thaumaturge:wand_cap', event => {
    event.create('mypack:cursed').costModifier(0.5)
})
```

Rods work the same through `thaumaturge:wand_rod`, and take the same methods as the rod item, `assemblyGate` and `onAssemble` included.
