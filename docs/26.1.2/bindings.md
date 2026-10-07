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

Taint seeds and the per chunk ecological pressure that feeds taint spread.

```js
Taint.addTaintSeed(level, pos)
Taint.removeTaintSeed(level, pos)
Taint.isNearTaintSeed(level, pos)
Taint.isAtTaintSeedEdge(level, pos)
Taint.spreadFibres(level, pos, false)

Taint.isTainted(level, pos)        // tainted biome, or the chunk's pressure is past the threshold
Taint.hasActiveSource(level, pos)  // a live taint seed is close enough to spread here

let pressure = Taint.getEcologicalPressure(level, pos)
Taint.addEcologicalPressure(level, pos, 5.0)
Taint.cleanEcologicalPressure(level, pos, 5.0)
```

Pressure is stored per chunk and runs from 0 to 1. It decays slowly on its own, slower where the aura is full of flux. `isAtTaintSeedEdge` tells you whether a spot sits where new seeds can sprout.

## WandVis

Read and write the primal vis stored on a wand item. Amounts are centivis, 100 centivis is 1 vis.

```js
let ignis = WandVis.getVis(wandStack, 'thaumaturge:ignis')
let all = WandVis.getAllVis(wandStack)

// withVis returns a changed copy, it does not touch the original stack
player.mainHandItem = WandVis.withVis(player.mainHandItem, 'thaumaturge:ignis', 5000)
```

This goes through the rod's vis storage, so it also works for rods that keep their vis somewhere unusual.

## Essentia

Jars, alembics and anything else that stores essentia. Aspects are plain ids. `face` is the side you reach in from, like `'up'`. Blocks only answer on the sides a tube could connect to, which for jars is the top.

```js
// level is any server level, event.level in most server events
const jar = new BlockPos(10, 64, 10)

let contents = Essentia.contents(level, jar, 'up')             // an aspect list
let aqua = Essentia.amount(level, jar, 'up', 'thaumaturge:aqua')

Essentia.insert(level, jar, 'up', 'thaumaturge:aqua', 16)      // returns how much went in
Essentia.extract(level, jar, 'up', 'thaumaturge:aqua', 8)      // returns how much came out

// move essentia between two tube-connected blocks, returns the amount moved
Essentia.transfer(level, jar, 'up', new BlockPos(10, 65, 10), 'down', 'thaumaturge:aqua', 10)
```

`Essentia.isStorage(level, pos, face)` and `Essentia.isTransport(level, pos, face)` check whether a block takes part at all.

Items work too, for phials, filled jars and anything else that holds essentia:

```js
let inJar = Essentia.itemContents(stack)
let room = Essentia.itemCapacity(stack, 'thaumaturge:ignis')

let result = Essentia.fillItem(stack, 'thaumaturge:ignis', 10)
player.give(result.resultingStack())
console.info(`moved ${result.amountMoved()}`)

Essentia.drainItem(stack, 'thaumaturge:ignis', 10)   // same shape of result
```

`fillItem` and `drainItem` never change the stack you pass in. Use the returned stack.

Essentia crystals and aspect filters (the ones on labels and filter tubes):

```js
let crystals = Essentia.crystal('thaumaturge:ordo', 4)
let aspect = Essentia.crystalAspect(stack)          // null if not a configured crystal

let filter = Essentia.aspectFilter(stack)           // null if unfiltered
let filtered = Essentia.withAspectFilter(stack, 'thaumaturge:aqua')
let cleared = Essentia.withAspectFilter(stack, null)
```

## VisRelay

Draw primal vis from the vis relay network that reaches a position. Amounts are centivis, 100 centivis is 1 vis.

```js
let got = VisRelay.drain(level, pos, 'thaumaturge:ignis', 500)       // actually drains
let could = VisRelay.available(level, pos, 'thaumaturge:ignis', 500) // only checks
```

Both return how much the network could supply, up to what you asked for.

## Recharge

Charge on rechargeable items such as the traveller's boots, the grapple gun or the verdant charm.

```js
Recharge.getCharge(stack)
Recharge.getChargePercentage(stack, player)
Recharge.consumeCharge(stack, player, 10)           // true if there was enough

Recharge.rechargeItem(level, stack, pos, player, 20)  // pulls vis from the aura at pos
Recharge.rechargeItemBlindly(stack, player, 20)       // free charge, no aura needed
```

## Goggles

```js
Goggles.wearsGoggles(player)      // goggles of revealing or anything that counts as them
Goggles.revealsNodes(player)      // can see aura nodes right now
Goggles.totalVisDiscount(player)  // summed vis discount from held and worn gear, in whole percent
```

## Scanning

The thaumometer's scanning logic. Targets can be an item stack, a block position or an entity.

```js
Scanning.scanTheThing(player, target)            // as if the player scanned it
Scanning.isThingStillScannable(player, target)   // false once it has been scanned

Scanning.itemAspects(stack)
Scanning.entityAspects(entity)

Scanning.progressResearch(player, 'mypack:frost_magic')
Scanning.addKnowledge(player, 'OBSERVATION', 'thaumaturge:alchemy', 10)
```

## Golems

Golem logistics and seals.

```js
// ask golems to bring an item to a spot or an entity
Golems.requestProvisioning(level, pos, 'up', Item.of('minecraft:coal', 16))
Golems.requestProvisioning(level, player, Item.of('minecraft:bread', 4))

Golems.getProvisionRequests(level)
Golems.getSealStack('thaumaturge:pickup')      // the placeable seal item
```

## ThaumaturgeData

Datapack file helpers used inside `ServerEvents.generateData`. Covered on the [Registries](registries.md) and [Data maps](data-maps.md) pages: `aspect`, `itemAspects`, `entityAspects`, `itemWarp`.
