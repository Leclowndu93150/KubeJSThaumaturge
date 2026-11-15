# Infusion stabilisers

Blocks placed symmetrically around the infusion altar keep an infusion stable. Candles and skulls are the classic ones. There are two ways to add your own.

## Plain blocks through the tag

Any block in the `thaumaturge:infusion_stabilisers` block tag counts as a stabiliser at the default strength of 0.1:

```js
ServerEvents.tags('block', event => {
    event.add('thaumaturge:infusion_stabilisers', 'minecraft:amethyst_cluster')
})
```

That is a server script and works on `/reload`.

## A stabiliser block with its own strength

The `thaumaturge:infusion_stabiliser` block type makes a new block and lets you set how much it stabilises:

```js
StartupEvents.registry('minecraft:block', event => {
    event.create('mypack:warding_totem', 'thaumaturge:infusion_stabiliser')
        .stabilization(0.25)
        .displayName('Warding Totem')
})
```

It is a normal KubeJS block otherwise, so `box`, `soundType`, `hardness` and the rest all work.

| Method | Default | What it does |
|---|---|---|
| `.stabilization(float)` | 0.1 | Stability added per matched pair. 0 turns the block off as a stabiliser |
| `.countsAs(block)` | itself | Treat this block as another one for symmetry and diminishing returns |
| `.researchAid(float)` | 0 | Chance to not spend an aspect at a research table, see below |

## Symmetry and diminishing returns

Stabilisers only help when the block on the opposite side of the altar is the same kind with the same strength. A lone or mismatched block costs stability instead. Each extra pair of the same kind adds a bit less than the one before, so a mix of kinds beats a wall of one kind.

`countsAs` changes what "the same kind" means. Say you add colored totems and want any two to pair up:

```js
['red', 'blue', 'green'].forEach(color => {
    event.create(`mypack:${color}_totem`, 'thaumaturge:infusion_stabiliser')
        .stabilization(0.2)
        .countsAs('mypack:red_totem')
})
```

Now a red totem across from a blue one is a valid pair, and all three colors share the same diminishing returns.

## Instability outcomes

When an infusion's instability triggers, one outcome is picked by weight from the `thaumaturge:instability_outcome` registry. Scripts can add outcomes or redefine the mod's own (`thaumaturge:zap`, `thaumaturge:warp`, `thaumaturge:explosion` and so on):

```js
ServerEvents.registry('thaumaturge:instability_outcome', event => {
    event.create('mypack:lingering_poison').json({
        weight: 3,
        effect: {
            type: 'thaumaturge:harm',
            effects: [{ id: 'minecraft:poison', duration: 200, amplifier: 1 }]
        }
    })
})
```

Effect types: `thaumaturge:eject` (`consume`, `aftermath`, `mitigation`, `pollution`, `explosion_power`), `thaumaturge:zap` (`damage`, `everyone`), `thaumaturge:harm` (`effects`, `everyone`), `thaumaturge:warp` (`permanent_chance`, `permanent`, `temporary`) and `thaumaturge:explode` (`power`). Redefine an id with `weight: 0` to switch a built-in outcome off. Like the other datapack registries, this loads at server start, not on `/reload`.

Pedestals, pillars and matrix upgrades that change cost, speed and stability are data maps, see [Data maps](data-maps.md).

## Research table aid

A block with `researchAid` helps research when it sits directly on top of a research table. Whenever a player writes an aspect onto a note or combines two aspects, there is that chance the aspect is not taken from their pool. The blocks above both halves of the table add up.

```js
event.create('mypack:scholars_candle', 'thaumaturge:infusion_stabiliser')
    .stabilization(0.1)
    .researchAid(0.15)
```

Set `stabilization(0)` for a block that only helps research.
