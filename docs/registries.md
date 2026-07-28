# Aspects, research and more

Thaumaturge keeps a lot of its content in datapack registries: aspects, research categories and entries, scans, blueprints and pech trades. Scripts can create all of them. These are server scripts, but note that datapack registries only load at world creation and server start, not on `/reload`.

## Aspects

Simple aspects (no components) go through the registry event:

```js
ServerEvents.registry('thaumaturge:aspect', event => {
    event.create('mypack:frigus').json({
        tag: 'frigus',
        color: 0x9BD5FF
    })
})
```

Compound aspects reference two other aspects as components. Those need the second form below, the registry event runs too early for them:

```js
ServerEvents.generateData('before_mods', event => {
    ThaumaturgeData.aspect(event, 'mypack:gelum', {
        tag: 'gelum',
        color: 0x9BD5FF,
        components: ['thaumaturge:aqua', 'thaumaturge:ordo']
    })
})
```

Aspect texture: `kubejs/assets/mypack/textures/aspects/gelum.png`. Without one the aspect uses the default location and shows missing if the file is absent.

## Research entries

Research entries are the nodes in the Thaumonomicon. The minimal shape is a category, a position, an icon and one stage:

```js
ServerEvents.registry('thaumaturge:research_entry', event => {
    event.create('mypack:frost_magic').json({
        category: 'thaumaturge:basics',
        name: 'research.mypack.frost_magic.title',
        parents: ['~thaumaturge:unlock_alchemy'],
        column: 4,
        row: -2,
        icons: ['minecraft:packed_ice'],
        stages: [
            { text: 'research.mypack.frost_magic.stage_0' }
        ]
    })
})
```

Notes from the trenches:

- `parents` entries are strings with a small grammar. A `~` prefix means no connecting line is drawn. `@2` after an id requires that stage of the parent.
- `icons` accept an item id or a texture path ending in `.png`.
- Stages can carry `recipes`, `obtain`, `craft`, `required_research`, `warp` and more. Open the Thaumaturge jar and look at `data/thaumaturge/thaumaturge/research_entry/` for real examples of every field, they are plain JSON and good reference material.
- The title and stage text are lang keys. Add them in `kubejs/assets/mypack/lang/en_us.json`.

## Scans

Make things give research points when scanned with the thaumometer:

```js
ServerEvents.registry('thaumaturge:scan', event => {
    event.create('mypack:scan_lodestone').json({
        key: 'mypack:lodestone_secrets',
        blocks: ['minecraft:lodestone']
    })
})
```

`blocks`, `items` and `entities` all accept ids or `#tags`. The key becomes a research flag you can use as a gate.

## Blueprints

Blueprints define multiblocks for dust triggers. Layers go top to bottom:

Each key maps a symbol to a source block (what the player builds) and a target block (what it becomes when the trigger fires):

```js
ServerEvents.registry('thaumaturge:blueprint', event => {
    event.create('mypack:altar_blueprint').json({
        x_size: 3, y_size: 2, z_size: 3,
        keys: {
            'S': {
                source: { type: 'block', block: 'minecraft:stone_bricks' },
                target: { type: 'block', block: 'minecraft:chiseled_stone_bricks' }
            },
            'C': {
                source: { type: 'block', block: 'minecraft:cauldron' },
                target: { type: 'block', block: 'minecraft:cauldron' }
            }
        },
        pattern: [
            '   ', ' C ', '   ',
            'SSS', 'SSS', 'SSS'
        ]
    })
})
```

Layer y=0 is the top of the structure and the pattern runs x rows of z characters. The mod's own blueprints in `data/thaumaturge/thaumaturge/blueprint/` are the best reference before building big ones.

## Pech trades

The pech trade tables are a registry too (`thaumaturge:pech_trade`), server side only. Same `.json()` pattern; crib the format from the mod jar.
