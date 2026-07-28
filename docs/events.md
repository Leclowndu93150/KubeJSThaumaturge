# Events

The addon exposes Thaumaturge's research lifecycle as KubeJS events. All three run server side and can be cancelled, which makes the underlying action not happen.

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

## Cancelling

```js
ThaumaturgeEvents.researchCompleted(event => {
    if (event.research == 'thaumaturge:staff_primal' && !event.player.tags.contains('worthy')) {
        event.cancel()
    }
})
```

## Fields

| Event | Fields |
|---|---|
| `researchUnlocked` | `player`, `research` |
| `researchStageAdvanced` | `player`, `research`, `previousStage`, `newStage` |
| `researchCompleted` | `player`, `research` |
