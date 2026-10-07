# Spells

Foci carry a spell: a small tree of parts that starts at the caster. A delivery decides where the spell goes, effects decide what it does there, modifiers change everything after them, and flow parts branch, delay or re-aim it. Players build the tree in the focal manipulator.

Everything a part does comes from two datapack registries, so scripts can add their own:

- `thaumaturge:spell_part` holds every part: its behaviour, icon, colour, complexity, vis cost, settings and research gate.
- `thaumaturge:spell_affinity` holds how each aspect reacts when an aspect-driven effect (strike, burst, imbue) uses it.

Like the other datapack registries, these load at world creation and server start, not on `/reload`.

## Scripted parts

A scripted part is a normal spell part whose behaviour hands control to a script. The part carries a `key`, and the `scriptedSpell` event with the same key runs whenever the part does.

```js
ServerEvents.registry('thaumaturge:spell_part', event => {
    event.create('mypack:lifesteal').json({
        behavior: { type: 'thaumaturge:scripted_effect', key: 'mypack:lifesteal' },
        icon: 'mypack:textures/spell/lifesteal.png',
        color: 0x8A1C2B,
        complexity: 5,
        vis: 0.5,
        research: { entry: 'thaumaturge:focus_curse' },
        settings: [
            { key: 'power', min: 1, max: 4, default: 1, complexity: 3 }
        ]
    })
})
```

```js
ThaumaturgeEvents.scriptedSpell('mypack:lifesteal', event => {
    const caster = event.caster
    event.targets.entities.forEach(entity => {
        if (!entity.living || event.isAlly(entity)) return
        const amount = 2 * event.power * event.getSetting('power')
        entity.attack(amount)
        if (caster) caster.heal(amount / 2)
        event.impact(entity.position())
    })
})
```

Name and description come from lang: `spell.mypack.lifesteal.name` and `spell.mypack.lifesteal.desc`.

There are four scripted behaviours:

| `type` | Kind | Extra fields |
|---|---|---|
| `thaumaturge:scripted_effect` | Effect | `key`, `widens` (default `true`: Widen spills the effect onto creatures around each target) |
| `thaumaturge:scripted_delivery` | Delivery | `key`, `max_children` (default 1), `standalone` |
| `thaumaturge:scripted_modifier` | Modifier | same as delivery |
| `thaumaturge:scripted_flow` | Flow | same as delivery |

A scripted effect fires the event once per target. The other three fire once per run with every incoming target, and whatever is left in `event.targets` afterwards is handed to the part's children. That is how a scripted delivery moves the spell somewhere new:

```js
ThaumaturgeEvents.scriptedSpell('mypack:skyward', event => {
    const level = event.level
    const found = []
    event.targets.all.forEach(target => {
        level.getEntities(null, AABB.ofSize(target.position, 12, 6, 12)).forEach(e => {
            if (e.living && !event.isAlly(e)) found.push(e)
        })
    })
    event.targets.clear()
    found.forEach(e => event.targets.addEntity(e, e.position().subtract(event.caster.position())))
})
```

`event.stop()` ends the spell at this part. `event.proceedLater(ticks)` hands the current targets to the children after a delay instead of right away.

### What a scripted event exposes

| Member | Meaning |
|---|---|
| `key` | The part's key |
| `caster` | The casting entity, or null when the caster is gone (delayed parts, carriers outliving their owner) |
| `level` | The server level |
| `part` | The part id |
| `aspect` | The aspect picked for this part, or null |
| `power` | The power reaching this part, after modifiers and charge |
| `getSetting(key)` | The player's choice for one of the part's settings |
| `getStat(id)` / `setStat(id, value)` | Stats carried down the tree, such as `thaumaturge:radius` or `thaumaturge:duration`. Modifiers write them, later parts read them. Any id works, so two of your own parts can share a stat |
| `targets` | `all`, `entities`, `size()`, `keepIf(fn)`, `clear()`, `add(target)`, `addEntity(entity, direction)` |
| `isAlly(entity)` | Whether the caster counts the entity as a friend |
| `impact(pos)`, `burst(pos, dir)`, `arc(from, to)` | Particle effects in the part's colour, sent to nearby players |
| `hasNext()` | Whether the part has children |

Each target in `targets.all` has `entity`, `blockPos`, `face`, `position`, `direction`, and `isEntity()`, `isBlock()`, `isMiss()`.

## Spell events

All of these run on the server.

```js
// Before vis is paid. Cancel to stop the cast; power and vis cost can be changed.
ThaumaturgeEvents.spellCast(event => {
    if (event.entity.isPlayer() && event.entity.stages.has('silenced')) event.cancel()
    if (event.styleName == 'channeled') event.visCost *= 0.8
})

// After the cast went off.
ThaumaturgeEvents.spellCasted(event => {
    console.info(`${event.entity.name.string} cast a ${event.styleName} spell`)
})

// Once per effect per target. Cancel to skip that target, or change the power.
ThaumaturgeEvents.spellEffect(event => {
    const target = event.target.entity
    if (target && target.type == 'minecraft:villager') event.cancel()
    if (event.part == 'thaumaturge:fire' && event.level.raining) event.power *= 0.5
})

// Every time any part runs, with the targets it is about to receive.
ThaumaturgeEvents.spellNode(event => {
    if (event.part == 'thaumaturge:chain') {
        event.targets.keepIf(t => !t.isEntity() || t.entity.type != 'minecraft:iron_golem')
    }
})

// When a player confirms a design in the focal manipulator, before costs are taken.
ThaumaturgeEvents.spellInscribe(event => {
    if (event.complexity > 40 && !event.player.stages.has('archmage')) {
        event.player.tell('That design is beyond you for now.')
        event.cancel()
    }
})
```

`spellEffect` and `spellNode` fire very often. They are cheap when no script listens, so only add them when you need them.

## The Spells binding

```js
// Build a spell: projectile -> fire burst
const spell = Spells.spell('instant',
    Spells.node('thaumaturge:projectile').then(Spells.node('thaumaturge:burst', 'thaumaturge:ignis')))

// Or a straight chain of parts
const bolt = Spells.chain('instant', ['thaumaturge:bolt', 'thaumaturge:frost'])

// Cast from any living entity, at a given power. No vis is involved.
Spells.cast(entity, bolt, 1.0)

// Read or write the spell on a focus
const current = Spells.spellOf(player.mainHandItem)
Spells.setSpell(focusStack, spell)

// Complexity, vis, problems against the focus tier
const summary = Spells.analyze(player, focusStack, spell)
if (!summary.valid()) console.warn(summary.problems())
```

## Aspect affinities

`thaumaturge:spell_affinity` entries are keyed by aspect id. Strike, burst and imbue effects only offer aspects that have an affinity, so adding one makes a new aspect castable.

```js
ServerEvents.registry('thaumaturge:spell_affinity', event => {
    event.create('mypack:frigus').json({
        fx: 'thaumaturge:frost',
        complexity: 1,
        research: { entry: 'mypack:frost_magic' },
        sound: 'minecraft:block.glass.break',
        entity: [
            { type: 'thaumaturge:damage', damage_type: 'minecraft:freeze', base: 4.0 },
            { type: 'thaumaturge:mob_effect', effect: 'minecraft:slowness', max_amplifier: 3, per_duration: 10.0 }
        ],
        block: [
            { type: 'thaumaturge:freeze_water', max: 8.0, per_power: 1.5 }
        ],
        imbue: [
            { type: 'thaumaturge:mob_effect', effect: 'minecraft:fire_resistance', per_duration: 10.0 }
        ]
    })
})
```

`entity` runs when the aspect strikes a creature, `block` when it strikes a block, and `imbue` when Imbue grants it as a boon. The Thaumaturge jar's `data/thaumaturge/thaumaturge/spell_affinity/` folder has an entry for every shipped aspect, which is the best reference for the action types and their fields.
