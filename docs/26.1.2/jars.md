# Warded jars

A jar in Thaumaturge is one shared glass model. What makes the normal jar and the void jar look different is just three texture slots: side, top and bottom. This addon exposes those slots plus the capacity, so a new jar kind is a startup script and two textures.

## Basic jar

```js
StartupEvents.registry('minecraft:block', event => {
    event.create('mypack:mega_jar', 'thaumaturge:jar')
        .capacity(1000)
        .topTexture('mypack:block/mega_jar_top')
        .sideTexture('mypack:block/mega_jar_side')
        .displayName('Mega Warded Jar')
})
```

Put the textures at:

```
kubejs/assets/mypack/textures/block/mega_jar_top.png
kubejs/assets/mypack/textures/block/mega_jar_side.png
```

The easiest way to make them is to copy `jar_top.png` and `jar_side.png` from the Thaumaturge jar and recolor the metal band. The band is rows 2 and 3 of the side texture plus the lid pixels of the top texture. The glass stays untouched.

## Builder methods

| Method | Default | What it does |
|---|---|---|
| `.capacity(int)` | 250 | Essentia the jar holds. The fill render scales with it |
| `.sideTexture(id)` | normal jar side | Also used as the particle texture |
| `.topTexture(id)` | normal jar top | The lid |
| `.bottomTexture(id)` | normal jar bottom | Usually fine to leave alone |

All the normal KubeJS block builder methods work too (`displayName`, `tags`, and so on).

## As an item

A jar you pick up keeps its essentia, just like the mod's jars. Your jars also get Thaumaturge's essentia item capabilities, so phials, essentia transfer and anything else that fills or drains a held jar treats them like the real thing, capacity included. Scripts can do the same with `Essentia.fillItem` and `Essentia.drainItem` from the [Bindings](bindings.md) page.

## Capacity and the fill line

The renderer draws the fill height as a fraction of your capacity. A jar with `capacity(4000)` holding 250 essentia looks nearly empty while the standard jar next to it looks full. That is intended, and useful feedback for players.

## A tiered set

```js
StartupEvents.registry('minecraft:block', event => {
    const TIERS = [
        { name: 'copper',    cap: 500  },
        { name: 'iron',      cap: 750  },
        { name: 'gold',      cap: 1000 },
        { name: 'diamond',   cap: 2000 },
        { name: 'netherite', cap: 4000 },
    ]
    TIERS.forEach(tier => {
        event.create(`mypack:jar_${tier.name}`, 'thaumaturge:jar')
            .capacity(tier.cap)
            .topTexture(`mypack:block/jar_${tier.name}_top`)
            .sideTexture(`mypack:block/jar_${tier.name}_side`)
            .displayName(`${tier.name.charAt(0).toUpperCase() + tier.name.slice(1)} Warded Jar`)
    })
})
```

Then give them crafting recipes on the [Recipes](recipes.md) page.
