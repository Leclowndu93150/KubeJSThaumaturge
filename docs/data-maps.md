# Item and entity aspects

Every item and mob can carry aspects. Two helpers set them from a script.

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

## The other data maps

Thaumaturge has more data maps you can target with plain datapack files (or `event.json` in `generateData`): `thaumaturge:warp` on items, `thaumaturge:aura_modifier` and `thaumaturge:biome_aspects` on biomes, `thaumaturge:champion_whitelist` on entities and `thaumaturge:infernal_bonus` on items. The value formats are in the mod's generated data, which doubles as documentation.
