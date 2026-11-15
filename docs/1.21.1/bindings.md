# Bindings

These globals exist in every script type, though nearly all of them belong in server scripts. They wrap the Thaumaturge API so you can read and change game state from events, custom commands, or anywhere else KubeJS lets you run code.

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

## Research

Read and change a player's research. Research ids are plain strings. The methods that change something need a server player and sync to the client on their own.

```js
Research.status(player, 'mypack:frost_magic')        // UNKNOWN, IN_PROGRESS or COMPLETE
Research.isKnown(player, 'mypack:frost_magic')       // unlocked, at any stage
Research.isComplete(player, 'thaumaturge:unlock_alchemy')
Research.stage(player, 'mypack:frost_magic')         // current stage, 0 when unknown
Research.stageCount(player, 'mypack:frost_magic')    // how many stages the entry has
Research.hasReachedStage(player, 'mypack:frost_magic', 2)

Research.known(player)       // ids of everything unlocked
Research.completed(player)   // ids of everything finished
```

Changing progress:

```js
Research.unlock(player, 'mypack:frost_magic')     // like clicking it in the book: needs its parents, respects unlock requirements
Research.advance(player, 'mypack:frost_magic')    // one stage forward, skipping the stage's item and knowledge costs
Research.advance(player, 'mypack:frost_magic', true)  // one stage forward only if the player can pay, and takes the cost
Research.setStage(player, 'mypack:frost_magic', 2)
Research.complete(player, 'mypack:frost_magic')   // finishes this one entry, parents or not
Research.forget(player, 'mypack:frost_magic')     // removes this one entry
```

All of these return `true` when something changed. `unlock`, `advance`, `setStage` and `complete` fire the [research events](events.md), so a script that cancels those also blocks these calls.

The bigger hammers, the same ones the `/thaumaturge research` commands use. They return how many entries were touched:

```js
Research.grant(player, 'mypack:frost_magic')      // completes the entry and everything it depends on
Research.prepare(player, 'mypack:frost_magic')    // completes what it depends on and leaves the entry itself unlocked at stage 0
Research.revoke(player, 'mypack:frost_magic')     // forgets the entry and everything that depends on it
Research.completeCategory(player, 'thaumaturge:alchemy')
Research.grantAll(player)                         // every entry, every aspect
Research.reset(player)                            // wipes research and aspect pools, then reapplies the starting entries
```

Theory and observation points, per research category. A level is 32 theory points or 16 observation points. The category can be `null` for uncategorised points.

```js
Research.knowledge(player, 'THEORY', 'thaumaturge:alchemy')        // whole levels
Research.knowledgePoints(player, 'THEORY', 'thaumaturge:alchemy')  // raw points
Research.addKnowledge(player, 'OBSERVATION', 'thaumaturge:alchemy', 16)   // negative amounts take points away
```

`addKnowledge` fires `knowledgeGained` and shows the usual popup.

Each entry also carries interface flags: `'PAGE'` (the player has read it in the Thaumonomicon), `'RESEARCH'` (it is being researched) and `'POPUP'` (a popup for it is still pending).

```js
Research.hasFlag(player, 'mypack:frost_magic', 'PAGE')
Research.addFlag(player, 'mypack:frost_magic', 'PAGE')
Research.removeFlag(player, 'mypack:frost_magic', 'RESEARCH')
```

### Unlock requirements

`Research.require` adds a condition an entry needs before it can be unlocked, on top of its parents. The optional message shows up in the Thaumonomicon while the condition fails.

```js
// startup_scripts
Research.require('mypack:frost_magic',
    player => player.stages.has('visited_the_north'),
    Text.of('Travel north first').blue())

Research.require('thaumaturge:unlock_eldritch', player => Research.isComplete(player, 'mypack:frost_magic'))
```

Put these in `startup_scripts`. The book checks the condition on the client to grey the entry out and show the message, and the server checks it again when the player tries to unlock it, so the script has to run on both sides and may only look at things both sides know. KubeJS stages and research are synced, entity tags are not. A requirement in `server_scripts` still blocks the unlock, but the book will not show why.

`Knowledge.of(player)` from older versions of the addon still works.

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

// setVis and addVis change the stack you pass in and return the new amount
WandVis.setVis(player.mainHandItem, 'thaumaturge:ignis', 5000)
WandVis.addVis(player.mainHandItem, 'thaumaturge:ignis', -250)

// withVis returns a changed copy, it does not touch the original stack
let full = WandVis.withVis(player.mainHandItem, 'thaumaturge:ignis', 5000)
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
Goggles.wearsRevealingGear(player)  // revealing gear on the head or in a curio slot, held items do not count
Goggles.isRevealing(stack)          // whether one item reveals
Goggles.totalVisDiscount(player)    // summed vis discount from held and worn gear, in whole percent
```

## Scanning

The thaumometer's scanning logic. Targets can be an item stack, a block position or an entity.

```js
Scanning.scan(player, target)               // as if the player scanned it
Scanning.isStillScannable(player, target)   // false once it has been scanned
Scanning.aspectsOf(player, target)          // the aspects a scan of it would show
Scanning.stackOf(player, target)            // the item a scan of it counts as, for blocks and dropped items

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

## MobTraits

Traits are what make a mob tainted or a champion, and what give tainted mobs their abilities. Works on any living entity.

```js
EntityEvents.spawned(event => {
    const mob = event.entity
    if (mob.type == 'minecraft:zombie' && Math.random() < 0.1) {
        MobTraits.add(mob, 'thaumaturge:warping_strike')
    }
})

MobTraits.has(mob, 'thaumaturge:tainted')
MobTraits.remove(mob, 'thaumaturge:tainted')
MobTraits.traits(mob)        // ids of every trait on the mob
MobTraits.isTainted(mob)
MobTraits.isChampion(mob)
MobTraits.champion(mob)      // id of its champion trait, or null
```

You can register your own traits, see [Mob traits](registries.md#mob-traits). Thaumaturge's traits are `tainted`, the champion ones (`armor`, `bold`, `fiery`, `grim`, `infested`, `mighty`, `sickly`, `spine`, `undying`, `vampiric`, `venomous`, `warded`, `warp`) and the tainted mob abilities (`flux_milk`, `leaping`, `lifting_strike`, `swarm_burst`, `taint_arrows`, `taint_blast`, `taint_brewing`, `taint_brood`, `taint_grazing`, `taint_laying`, `taint_shearing`, `taintacle_lure`, `warping_strike`), all under the `thaumaturge` namespace.

## ThaumaturgeData

Datapack file helpers used inside `ServerEvents.generateData`. Covered on the [Registries](registries.md) and [Data maps](data-maps.md) pages: `aspect`, `itemAspects`, `entityAspects`, `itemWarp`, `taintedProfile`, `trimBonus`, `spellFocus`, `smelterStats`.
