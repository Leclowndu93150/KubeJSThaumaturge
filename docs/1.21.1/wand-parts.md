# Wand caps and rods

Thaumaturge wands are built from a cap and a rod. One call gives you the crafting item players hold, the part it puts on the wand, and the name, stats and tooltip that go with it.

You do not write any assembly recipes. Register a cap or a rod and the arcane workbench recipes for every pairing with every other part appear on their own, priced from the craft costs you set.

## A cap

```js
StartupEvents.registry('minecraft:item', event => {
    event.create('mypack:wand_cap_netherite', 'thaumaturge:wand_cap')
        .costModifier(0.75)
        .aspectCost('thaumaturge:ignis', 0.6)
        .aspectCost('thaumaturge:perditio', 0.6)
        .craftCost(12)
        .wandName('Netherite Shod')
})
```

The item id prefix `wand_cap_` is stripped to form the part id, so this cap is `mypack:netherite` on a wand.

## Colour or textures

The quickest way to give a part its look is a colour:

```js
event.create('mypack:wand_cap_netherite', 'thaumaturge:wand_cap').recolor(0x4A4348)
event.create('mypack:wand_rod_netherite', 'thaumaturge:wand_rod').recolor('#5C4E4A')
```

`.recolor(...)` takes a number, a `'#RRGGBB'` string or a colour name. It recolours the mod's iron cap and silverwood rod, both the inventory item and the part on the wand model. The recolour keeps every pixel's shading and only swaps in your hue and saturation, so highlights and shadows stay where the artist put them.

To use your own art instead, skip `.recolor` and put the textures at:

```
kubejs/assets/mypack/textures/item/wand_cap_netherite.png        the inventory item
kubejs/assets/mypack/textures/models/wand_cap_netherite.png      the cap on the wand model
```

You can mix the two: `.recolor(...)` with `.capTexture(id)` or `.rodTexture(id)` recolours only the inventory item and uses your texture on the wand.

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
| `.aspectCost(aspect, float)` | none | A modifier for one primal that replaces `costModifier` for it. Call once per aspect |
| `.craftCost(int)` | 1 | Multiplied with the rod's value to price assembly recipes |
| `.recolor(color)` | none | Recolours the iron cap for both the item and the wand model |
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
| `.runes(bool)` | false | Draws glowing rune strips along all four sides of the rod and gives socketed foci a free potency level |
| `.assemblyGate(research)` | unlock_auromancy | Research entry required for assembly recipes with this rod |
| `.onAssemble((wand, input) => {})` | none | Runs when a wand is assembled from this rod, see below |
| `.onUpdate((wand, player) => {})` | none | Runs on the server every tick while a player carries the wand, on top of `primalCharge` |
| `.visStorage(read, write)` | wand component | Keeps the wand's vis somewhere of your choosing, see below |
| `.recolor(color)` | none | Recolours the silverwood rod for both the item and the wand model. Staff rods use the silverwood staff item as their base |
| `.rodTexture(id)` | convention path | Texture on the wand model |
| `.wandName(text)` | "Netherite" | The rod half of the wand's display name |

Sceptres built from your parts get the rotating ring of runes around the cap on their own, like every other sceptre. `.runes(true)` is the separate strip of runes running along the rod itself.

## Rod behaviour

`onUpdate` runs on the server every tick for each wand with your rod that a player carries. Use it for rods that charge under their own rules:

```js
event.create('mypack:wand_rod_tidal', 'thaumaturge:wand_rod')
    .capacity(100)
    .onUpdate((wand, player) => {
        if (player.tickCount % 40 != 0 || !player.isInWater()) return
        if (WandVis.getVis(wand, 'thaumaturge:aqua') < 5000) {
            WandVis.addVis(wand, 'thaumaturge:aqua', 100)
        }
    })
```

`WandVis.addVis` changes the wand in place. Amounts are centivis, so this adds 1 vis every two seconds up to 50.

`visStorage` replaces where the wand keeps its vis. `read` returns the stored vis as an object of aspect id to centivis, and `write` gets the same shape whenever the vis changes. Everything that touches wand vis goes through them, including the `WandVis` binding. This rod makes every wand built from it draw on one shared pool:

```js
global.hivePool = {}

StartupEvents.registry('minecraft:item', event => {
    event.create('mypack:wand_rod_hive', 'thaumaturge:wand_rod')
        .capacity(500)
        .visStorage(
            wand => global.hivePool,
            (wand, vis) => {
                Object.keys(global.hivePool).forEach(aspect => delete global.hivePool[aspect])
                vis.forEach((aspect, amount) => { global.hivePool[aspect] = amount })
            })
})
```

That pool lives in memory and is gone after a restart. Where you keep the numbers is up to you, the two callbacks are the whole contract.

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
