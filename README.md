# KubeJS Thaumaturge

KubeJS integration for [Thaumaturge](https://github.com/Leclowndu93150/Thaumaturge), the Thaumcraft rewrite for modern Minecraft.

Lets packs and scripts add custom content to Thaumaturge without touching Java:

- Warded jars with their own cap textures and capacity
- Wand caps and rods, with crafting items and assembly recipes handled for you
- Golem materials, parts, traits and accessories
- Infusion stabilisers and research table aids
- All Thaumaturge recipe types: crucible, infusion, arcane workbench, dust triggers and more
- Aspects, research entries, scans, blueprints and pech trades
- Events for research, knowledge, infusion results, crucible crafting and arcane workbench costs
- Server scripts can read and change aura, warp, taint, essentia, wand vis and research points

## Requirements

NeoForge, on either version:

| | Minecraft 26.1.2 | Minecraft 1.21.1 |
|---|---|---|
| [KubeJS](https://www.curseforge.com/minecraft/mc-mods/kubejs) | 8.0.6+ | 2101.7.2+ |
| [Thaumaturge](https://github.com/Leclowndu93150/Thaumaturge) | 0.2.1+ | 0.4.6+ |

## Quick taste

```js
// startup_scripts/my_jars.js
StartupEvents.registry('minecraft:block', event => {
    event.create('mypack:mega_jar', 'thaumaturge:jar')
        .capacity(1000)
        .topTexture('mypack:block/mega_jar_top')
        .sideTexture('mypack:block/mega_jar_side')
        .displayName('Mega Warded Jar')
})
```

```js
// server_scripts/my_recipes.js
ServerEvents.recipes(event => {
    event.recipes.thaumaturge.crucible(
        Item.of('minecraft:diamond'),
        'minecraft:coal',
        [{ aspect: 'thaumaturge:terra', amount: 20 }]
    )
})
```

## Documentation

Full guide with examples for everything, one per Minecraft version:

- 26.1.2: https://leclowndu93150.github.io/KubeJSThaumaturge/#/26.1.2/
- 1.21.1: https://leclowndu93150.github.io/KubeJSThaumaturge/#/1.21.1/

## License

MIT
