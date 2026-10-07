# Getting started

## Startup scripts vs server scripts

KubeJS has two script folders that matter here, and picking the right one is the difference between things working and silently not existing.

**`kubejs/startup_scripts`** run once when the game launches, on both client and server. Anything that registers a new thing (a block, an item, a golem material) has to live here, because registries are frozen after startup. Changing a startup script needs a full game restart.

**`kubejs/server_scripts`** run when a world loads and again on `/reload`. Recipes, datapack content and game logic go here.

## Your first jar

Create `kubejs/startup_scripts/jars.js`:

```js
StartupEvents.registry('minecraft:block', event => {
    event.create('mypack:copper_jar', 'thaumaturge:jar')
        .capacity(500)
        .displayName('Copper Warded Jar')
})
```

Restart the game. You now have a working warded jar that holds 500 essentia, connects to tubes, accepts labels, shows its contents in Jade, and sits in the creative menu search. Give it to yourself with:

```
/give @s mypack:copper_jar
```

It uses the default jar textures until you supply your own. See [Warded jars](jars.md) for that.

## Your first recipe

Create `kubejs/server_scripts/recipes.js`:

```js
ServerEvents.recipes(event => {
    event.recipes.thaumaturge.crucible(
        Item.of('minecraft:diamond'),
        'minecraft:coal',
        [{ aspect: 'thaumaturge:terra', amount: 20 }]
    )
})
```

Run `/reload` in a world and throw coal in a crucible with 20 terra essentia in it.

## Checking your work

- Startup script output and errors: `logs/kubejs/startup.log`
- Recipe errors: watch the log for `Failed to create` lines after `/reload`
- `/kubejs errors` in game lists anything that went wrong
