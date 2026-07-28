# KubeJS Thaumaturge

KubeJS integration for [Thaumaturge](https://github.com/Leclowndu93150/Thaumcraft), the Thaumcraft rewrite for modern Minecraft.

Lets packs and scripts add custom content to Thaumaturge without touching Java:

- Warded jars with their own cap textures and capacity
- Wand caps and rods, with crafting items and assembly recipes handled for you
- Golem materials, parts and traits
- All Thaumaturge recipe types: crucible, infusion, arcane workbench, dust triggers and more
- Aspects, research entries, scans and blueprints
- Server scripts can read and change aura, warp, taint and research points

## Requirements

- Minecraft 26.1.2, NeoForge
- [KubeJS](https://www.curseforge.com/minecraft/mc-mods/kubejs) 8.0+
- [Thaumaturge](https://maven.leclowndu93150.dev/releases) 0.1.4+

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

Full guide with examples for everything: https://leclowndu93150.github.io/KubeJSThaumaturge/

## License

MIT
