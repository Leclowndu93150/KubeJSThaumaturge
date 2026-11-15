# Golem accessories

Accessories are the little hats, glasses and bowties you put on a golem. Each one is an item with a set of stat changes, and the mod ships five. You can add more with the `thaumaturge:golem_accessory` item type.

## An accessory

```js
StartupEvents.registry('minecraft:item', event => {
    event.create('mypack:monocle', 'thaumaturge:golem_accessory')
        .group('thaumaturge:eyes')
        .rangeFactor(1.25)
        .displayName('Golem Monocle')
})
```

That is the whole thing. The item exists, golems accept it, and the item-to-accessory mapping is written for you. Item texture goes at `kubejs/assets/mypack/textures/item/monocle.png` like any other KubeJS item.

## Stats

| Method | Default | What it does |
|---|---|---|
| `.health(int)` | 0 | Flat max health added while worn |
| `.armor(int)` | 0 | Flat armor added while worn |
| `.rangeFactor(float)` | 1.0 | Multiplies sight and work range |
| `.speedFactor(float)` | 1.0 | Multiplies movement speed |
| `.regenFactor(float)` | 1.0 | Multiplies the self-repair interval. Below 1 heals faster |
| `.killCredit(bool)` | false | Mobs the golem kills count as kills by its owner |
| `.group(id)` | `thaumaturge:none` | See below |
| `.accessoryId(id)` | the item id | Id of the accessory itself, if you want it to differ from the item |

## Groups

A golem wears at most one accessory per group. The mod uses `thaumaturge:hat` (top hat, fez) and `thaumaturge:eyes` (glasses, visor). `thaumaturge:none` never conflicts with anything, which is what the bowtie uses.

Any other id makes a new group:

```js
event.create('mypack:red_scarf', 'thaumaturge:golem_accessory').group('mypack:neck').speedFactor(1.1)
event.create('mypack:blue_scarf', 'thaumaturge:golem_accessory').group('mypack:neck').regenFactor(0.8)
```

A golem can wear one of those two scarves, not both.

## How it looks on the golem

The accessory's item model is drawn on the golem, on the head by default.

| Method | Default | What it does |
|---|---|---|
| `.renderOn('head' or 'body')` | `'head'` | Which part it follows. Head accessories turn with the head |
| `.renderOffset(x, y, z)` | 0, 0.5, 0 | Moves it from the anchor point, in model units |
| `.renderScale(float)` | 0.42 | Size of the item model, in model units |
| `.renderRotation(degrees)` | 0 | Turns it around the vertical axis |
| `.noRender()` | | Invisible accessory, stats only |

Golems are drawn with Minecraft's copper golem model, shrunk to 0.6. Offsets and scale are measured on the model before that shrink, so 1 unit is one block of the copper golem model (0.6 of a real block) and 1/16 is one model pixel. `+Y` is up and `-Z` is the golem's front.

| Anchor | Origin | Size around it |
|---|---|---|
| `head` | The head pivot | The head reaches 5/16 up, 4/16 to each side and 5/16 forward and back |
| `body` | The bottom of the torso | The chest reaches 6/16 up, 4/16 to each side and 3/16 forward and back |

The defaults sit the item just above the top of the head. Every model sits differently, so place a golem, put the accessory on and tune the numbers until it looks right. Changes need a game restart like any startup script.

```js
event.create('mypack:crown', 'thaumaturge:golem_accessory')
    .group('thaumaturge:hat')
    .health(4)
    .renderOffset(0, 0.45, 0)
    .renderScale(0.33)
```

Anything in the `thaumaturge:hat` group also hides the antenna on materials that have one.

A bowtie-style accessory goes on the body:

```js
event.create('mypack:medal', 'thaumaturge:golem_accessory')
    .killCredit(true)
    .renderOn('body')
    .renderOffset(0, 0.3, -0.21)
    .renderScale(0.25)
```

The item model is always drawn opaque, so it stays solid on an invisible golem that a spectator or teammate still sees as a faint ghost.

## Existing items as accessories

To turn an item that already exists into an accessory without a new item, map it with the `thaumaturge:golem_accessory` data map. The accessory id has to be one that is registered, so this only makes sense for reusing the mod's own accessories on another item:

```js
ServerEvents.generateData('before_mods', event => {
    event.json('thaumaturge:data_maps/item/golem_accessory', {
        values: {
            'minecraft:carved_pumpkin': { accessory: 'thaumaturge:top_hat' }
        }
    })
})
```
