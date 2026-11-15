# Aspects, research and more

Thaumaturge keeps a lot of its content in datapack registries: aspects, research categories and entries, scans, blueprints, pech trades, spell parts and the eldritch labyrinth. Scripts can create all of them. These are server scripts, but note that datapack registries only load at world creation and server start, not on `/reload`.

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

Each pech type has one trade table: `thaumaturge:forager`, `thaumaturge:mage` and `thaumaturge:stalker`. A trade is a result item and a tier from 1 to 5. Higher tiers unlock as a player keeps trading with pechs.

You change pech trades by defining the same table id again, which replaces the whole table. So start from the mod's list and edit it:

```js
ServerEvents.registry('thaumaturge:pech_trade', event => {
    event.create('thaumaturge:forager').json({
        trades: [
            { result: { id: 'thaumaturge:cluster_iron' }, tier: 1 },
            { result: { id: 'thaumaturge:cluster_gold' }, tier: 1 },
            { result: { id: 'minecraft:blaze_rod' }, tier: 2 },
            { result: { id: 'mypack:frost_shard', count: 3 }, tier: 3 },
            { result: { id: 'minecraft:diamond' }, tier: 5 }
        ]
    })
})
```

Results take an optional `count` and an optional `components` block, so enchanted books and potions work as trade goods. The mod's full tables are in its jar under `data/thaumaturge/thaumaturge/pech_trade/` if you want the complete starting point.

## Mob traits

Your own champion types and tainted mob abilities. Register them in a startup script, then hand them out with the [MobTraits](bindings.md#mobtraits) binding or a `tainted_profile` [data map](data-maps.md).

```js
StartupEvents.registry('thaumaturge:mob_trait', event => {
    event.create('mypack:frostbitten')
        .champion()
        .traitName('Frostbitten')
        .aspect('thaumaturge:gelum', 5)
        .attribute('minecraft:max_health', 10, 'add_value')
        .attribute('minecraft:attack_damage', 0.25, 'add_multiplied_base')
        .onAttack((mob, target, source, amount) => {
            target.setTicksFrozen(200)
            return amount + 2
        })
        .onHurt((mob, attacker, source, amount) => attacker == null ? amount : amount * 0.5)
        .onTick(mob => {
            if (mob.isOnFire()) mob.extinguishFire()
        })
})
```

| Method | What it does |
|---|---|
| `.champion()` | Makes it a champion type. Champions roll one champion trait at random, so yours joins that pool |
| `.taint()` | Mobs carrying it count as tainted |
| `.replacesAi()` | The mob drops its own goals while it has the trait |
| `.traitName(text)` | Display name, the capitalised id by default |
| `.aspect(aspect, amount)` | Aspects the trait adds to a scan of the mob. Call once per aspect |
| `.attribute(attribute, amount, operation)` | Attribute modifier while the trait is on the mob. Operations are `add_value`, `add_multiplied_base` and `add_multiplied_total` |
| `.baseAttribute(attribute, value)` | Sets the attribute's base value instead |
| `.onAdded(mob => {})`, `.onRemoved(mob => {})` | Run when the trait is put on or taken off a mob |
| `.onTick(mob => {})` | Runs every tick on the server |
| `.onAttack((mob, target, source, amount) => damage)` | Runs when the mob deals damage. Return the new damage, or nothing to leave it alone |
| `.onHurt((mob, attacker, source, amount) => damage)` | The same for damage the mob takes. `attacker` can be null |

The callbacks run for every mob with the trait, so keep `onTick` cheap.

## Spell parts and affinities

`thaumaturge:spell_part` and `thaumaturge:spell_affinity` have their own page: [Spells](spells.md).

## Infusion instability

`thaumaturge:instability_outcome` holds what can go wrong during an infusion. See [Infusion stabilisers](infusion.md#instability-outcomes).

## The eldritch labyrinth

The labyrinth behind the eldritch obelisks is built from four registries, all scriptable the same way as the ones above:

| Registry | Holds |
|---|---|
| `thaumaturge:labyrinth` | A maze: its size, layout settings, room pools, encounters and block palette |
| `thaumaturge:labyrinth_room` | A room: its structure templates, footprint, door sockets and how often it may appear |
| `thaumaturge:labyrinth_encounter` | Something that happens in a room, picked by `type` |
| `thaumaturge:obelisk_site` | An obelisk structure: its template, behaviour, awakening and the aura node it holds |
