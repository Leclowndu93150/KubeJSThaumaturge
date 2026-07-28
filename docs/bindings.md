# Bindings

These globals are available in server scripts. They wrap the Thaumaturge API so you can read and change game state from events, custom commands, or anywhere else KubeJS lets you run code.

## Aura

Chunk aura and flux.

```js
ServerEvents.tick(event => {
    const level = event.server.overworld()
    const pos = new BlockPos(0, 64, 0)

    let vis = Aura.getVis(level, pos)
    let flux = Aura.getFlux(level, pos)

    Aura.addVis(level, pos, 5.0)
    Aura.drainVis(level, pos, 2.0, false)   // false means actually drain, true simulates
    Aura.polluteAura(level, pos, 1.0, true) // adds flux, true shows the effect
})
```

Also there: `getAuraBase`, `getTotalAura`, `getFluxSaturation`, `drainFlux`, `canAcceptVis`.

## Warp

```js
PlayerEvents.loggedIn(event => {
    const player = event.player
    let total = Warp.getActualWarp(player)
    if (total > 50) {
        player.tell('The whispers are getting louder.')
    }
})
```

`Warp.addWarp(player, amount, type)` takes `'PERMANENT'`, `'NORMAL'` or `'TEMPORARY'`.

## AspectPools

The research point economy. Aspects are plain strings here.

```js
// give a player 20 ignis research points
AspectPools.grant(player, 'thaumaturge:ignis', 20)

// gate something on points
if (AspectPools.amount(player, 'thaumaturge:praecantatio') >= 10) {
    AspectPools.spend(player, 'thaumaturge:praecantatio', 10)
    // do the thing
}

AspectPools.isDiscovered(player, 'mypack:gelum')
AspectPools.canAfford(player, [{ aspect: 'thaumaturge:aqua', amount: 5 }])
AspectPools.spendAll(player, [{ aspect: 'thaumaturge:aqua', amount: 5 }])
```

## Aspects

The aspect index, which knows the aspect makeup of every item.

```js
let aspects = Aspects.of('minecraft:diamond')
```

## Knowledge

Player research state.

```js
const k = Knowledge.of(player)
k.isResearchComplete('thaumaturge:unlock_alchemy')
k.researchStage('mypack:frost_magic')
k.addResearch('mypack:frost_magic')
k.sync(player)   // call after changing things
```

## Taint

```js
Taint.addTaintSeed(level, pos)
Taint.isNearTaintSeed(level, pos)
Taint.spreadFibres(level, pos, false)
```

## WandVis

Read and write the primal vis stored on a wand item.

```js
let ignis = WandVis.getVis(wandStack, 'thaumaturge:ignis')
```

## ThaumaturgeData

Datapack file helpers used inside `ServerEvents.generateData`. Covered on the [Registries](registries.md) and [Data maps](data-maps.md) pages: `aspect`, `itemAspects`, `entityAspects`.
