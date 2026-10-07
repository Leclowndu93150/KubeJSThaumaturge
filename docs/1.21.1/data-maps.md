# Data maps

Data maps attach values to items, mobs and biomes: their aspects, the warp they give, and more. The addon has helpers for the common ones.

## Items

```js
ServerEvents.generateData('before_mods', event => {
    ThaumaturgeData.itemAspects(event, {
        'minecraft:packed_ice': [{ aspect: 'thaumaturge:aqua', amount: 4 }],
        'minecraft:blue_ice':   [{ aspect: 'thaumaturge:aqua', amount: 8 },
                                 { aspect: 'thaumaturge:ordo', amount: 2 }]
    })
})
```

Items with no explicit entry get their aspects derived from crafting recipes, so you only need this for base materials and things with no recipe.

## Entities

```js
ServerEvents.generateData('before_mods', event => {
    ThaumaturgeData.entityAspects(event, {
        'minecraft:camel': [{ aspect: 'thaumaturge:bestia', amount: 10 },
                            { aspect: 'thaumaturge:terra', amount: 5 }]
    })
})
```

Entity aspects feed the scanning system.

## Warp

Items that give warp while carried, like the mod's forbidden knowledge items:

```js
ServerEvents.generateData('before_mods', event => {
    ThaumaturgeData.itemWarp(event, {
        'minecraft:wither_rose': 2,
        'minecraft:echo_shard': 1
    })
})
```

Amounts go from 1 to 500.

## The other data maps

Thaumaturge has more data maps you can target with plain datapack files (or `event.json` in `generateData`):

| Data map | On | Value |
|---|---|---|
| `thaumaturge:aura_modifier` | biomes | `{ "value": 0.5 }`, multiplies the base aura of new chunks. 0 to 1, default 0.5 |
| `thaumaturge:biome_aspects` | biomes | aspects the biome's aura leans toward |
| `thaumaturge:taint_conversion` | entities | `{ "into": "<entity>", "natural_spawns": true }`, the tainted version of the mob. With `natural_spawns` it also replaces the mob when it spawns on tainted land |
| `thaumaturge:champion_whitelist` | entities | 0 to 100, how likely the mob spawns as a champion |
| `thaumaturge:infernal_bonus` | items | extra output from the infernal furnace |
| `thaumaturge:golem_accessory` | items | `{ "accessory": "<id>" }`, see [Golem accessories](golem-accessories.md) |

The exact value formats are in the mod's generated data under `data/thaumaturge/data_maps/`, which doubles as documentation.

```js
ServerEvents.generateData('before_mods', event => {
    event.json('thaumaturge:data_maps/entity_type/taint_conversion', {
        values: {
            'minecraft:goat': { into: 'thaumaturge:taint_sheep' }
        }
    })
})
```
