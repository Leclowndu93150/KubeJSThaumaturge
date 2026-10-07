# Events

The addon exposes Thaumaturge's research, crafting and knowledge hooks as KubeJS events. Everything here runs server side, except `arcaneCraftCost` which runs on both sides.

## Research

The three research events can be cancelled, which makes the underlying action not happen.

```js
ThaumaturgeEvents.researchUnlocked(event => {
    console.info(`${event.player.username} unlocked ${event.research}`)
})

ThaumaturgeEvents.researchStageAdvanced(event => {
    // event.previousStage and event.newStage are available
    if (event.research == 'thaumaturge:unlock_eldritch') {
        event.player.tell('You feel watched.')
    }
})

ThaumaturgeEvents.researchCompleted(event => {
    if (event.research == 'mypack:frost_magic') {
        event.player.give('mypack:frost_focus')
    }
})
```

## Knowledge

Fires when a player gains theory or observation points in a research category. Cancel it and the points are not added.

```js
ThaumaturgeEvents.knowledgeGained(event => {
    // event.type is THEORY or OBSERVATION, event.category can be null
    if (event.category == 'thaumaturge:eldritch' && !event.player.tags.contains('eldritch_allowed')) {
        event.cancel()
    }
})
```

## Infusion

Fires when an infusion finishes, just before the result replaces the catalyst on the central pedestal. It covers plain infusion, infusion enchanting and runic augments. Set `event.result` to hand out something else. A craft that blows up from instability never gets here.

```js
ThaumaturgeEvents.infusionCrafted(event => {
    // event.player is null when the player who started it is offline
    if (event.result.id == 'thaumaturge:thaumium_sword' && Math.random() < 0.1) {
        event.result = Item.of('thaumaturge:thaumium_sword').enchant('minecraft:sharpness', 5)
    }
})
```

## Crucible

`crucibleCrafted` fires after a crucible recipe completes. It is read only.

```js
ThaumaturgeEvents.crucibleCrafted(event => {
    console.info(`${event.player.username} made ${event.result.id} at ${event.pos}`)
})
```

`crucibleDecompose` fires when an item melts down into essentia. Change `event.aspects` to change what goes in the crucible, or cancel it to keep the item from melting.

```js
ThaumaturgeEvents.crucibleDecompose(event => {
    if (event.item.id == 'minecraft:rotten_flesh') {
        event.aspects = [{ aspect: 'thaumaturge:mortuus', amount: 4 }]
    }
    if (event.item.id == 'minecraft:nether_star') {
        event.cancel()
    }
})
```

## Arcane crafting cost

Fires whenever the arcane workbench works out what a craft costs. You can lower or raise the aura vis, swap the crystals, change what the wand pays, or make the craft free.

```js
ThaumaturgeEvents.arcaneCraftCost(event => {
    if (event.player.stages.has('arcane_adept')) {
        event.auraVis = Math.floor(event.auraVis / 2)
    }
})
```

This event runs on the server **and** the client, because the workbench screen shows the cost before you craft. Put the same handler in both `server_scripts` and `client_scripts`. If only the server one exists, the screen shows the normal cost and the server charges yours. Only check things the client knows about too. KubeJS stages sync to the client, entity tags do not.

Everything you can change:

- `event.auraVis`: vis drained from the aura
- `event.crystals`: crystal aspects consumed from the crystal slots, as an aspect list
- `event.wandCentivis`: centivis drawn from the wand, keyed by aspect id. 100 centivis is 1 vis
- `event.affordable`: whether the craft is allowed. Setting it to false blocks the craft
- `event.free()`: clears the vis, crystal and wand cost in one go

`event.paidFromWand` tells you whether the wand covers the whole cost, and `event.recipe` is the arcane recipe being priced.

## Cancelling

```js
ThaumaturgeEvents.researchCompleted(event => {
    if (event.research == 'thaumaturge:staff_primal' && !event.player.tags.contains('worthy')) {
        event.cancel()
    }
})
```

## Fields

| Event | Fields | Cancellable |
|---|---|---|
| `researchUnlocked` | `player`, `research` | yes |
| `researchStageAdvanced` | `player`, `research`, `previousStage`, `newStage` | yes |
| `researchCompleted` | `player`, `research` | yes |
| `knowledgeGained` | `player`, `type`, `category`, `amount` | yes |
| `infusionCrafted` | `level`, `pos`, `player`, `catalyst`, `result` (settable) | no |
| `crucibleCrafted` | `player`, `pos`, `result`, `aspects` | no |
| `crucibleDecompose` | `player`, `pos`, `item`, `aspects` (settable) | yes |
| `arcaneCraftCost` | `player`, `recipe`, `paidFromWand`, `wandCentivis`, `crystals`, `auraVis`, `affordable` (last four settable) | no |
