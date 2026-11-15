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
| `thaumaturge:tainted_profile` | entities | what the mob turns into when it gets tainted: `attributes`, `traits`, `hunts_villagers`, `hunts_animals`, `loot_table`, and `natural_spawns` to also taint it when it spawns on tainted land. Every field is optional |
| `thaumaturge:champion_whitelist` | entities | 0 to 100, how likely the mob spawns as a champion |
| `thaumaturge:infernal_bonus` | items | extra output from the infernal furnace |
| `thaumaturge:golem_accessory` | items | `{ "accessory": "<id>" }`, see [Golem accessories](golem-accessories.md) |
| `thaumaturge:spell_focus` | items | `{ "complexity": 20, "depth": 6, "repeats": 1, "branches": 2 }`, makes the item a focus that can hold spells up to those limits |
| `thaumaturge:trim_bonus` | trim materials | `{ "vis_discount": 1, "warp": 1 }`, what a piece of armor trimmed with the material gives |
| `thaumaturge:trim_material` | aspects | the trim material a vis crystal of that aspect counts as |
| `thaumaturge:smelter_stats` | blocks | `{ "efficiency": 0.8, "smelt_interval": 15 }` for the essentia smelters |
| `thaumaturge:infusion_pedestal` | blocks | `{ "cost": 0.0, "cycle_time": 0, "stability": 0.0 }` for each pedestal around a running infusion matrix. Only Thaumaturge pedestals count |
| `thaumaturge:infusion_pillar_set` | blocks | Same shape, applied once when all four pillars under the matrix are the same block. Only Thaumaturge pillars count |
| `thaumaturge:infusion_matrix_upgrade` | blocks | Same shape, for each block three below the matrix at its four diagonal corners. Any block works |

For the infusion maps, `cost` is added to the essentia cost multiplier (negative is cheaper), `cycle_time` is added to the ticks between infusion steps, and `stability` is added to how fast the altar regains stability. The mod's ancient and eldritch pillars and pedestals and its two matrix upgrades are the reference values.

The exact value formats are in the mod's generated data under `data/thaumaturge/data_maps/`, which doubles as documentation.

## Helpers for the bigger maps

Four of them have a `ThaumaturgeData` helper, used like `itemAspects`:

```js
ServerEvents.generateData('before_mods', event => {
    ThaumaturgeData.taintedProfile(event, {
        'minecraft:goat': {
            attributes: { 'minecraft:max_health': 20, 'minecraft:attack_damage': 4 },
            traits: ['thaumaturge:leaping', 'thaumaturge:taint_grazing'],
            hunts_animals: true,
            natural_spawns: true
        }
    })

    ThaumaturgeData.trimBonus(event, {
        'minecraft:netherite': { vis_discount: 2, warp: 1 }
    })

    ThaumaturgeData.spellFocus(event, {
        'mypack:focus_master': { complexity: 120, depth: 18, repeats: 6, branches: 6 }
    })
})

ServerEvents.generateData('after_mods', event => {
    ThaumaturgeData.smelterStats(event, {
        'thaumaturge:smelter_basic': { efficiency: 0.5, smelt_interval: 20 }
    })
})
```

`before_mods` is fine for adding new entries. To change an entry Thaumaturge already ships, like its own smelters or tainted mobs, generate it in `after_mods`, otherwise the mod's value wins.

