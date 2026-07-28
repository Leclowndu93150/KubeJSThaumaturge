# Troubleshooting

## My block or item does not exist

Registration lives in `startup_scripts` and needs a full game restart, not `/reload`. If you put a `StartupEvents.registry` call in `server_scripts` it never runs.

## Unknown type 'thaumaturge:jar'

The addon is not loaded, or an old version of it. Check the log for `Found plugin source kubejs_thaumaturge`.

## My recipes silently do nothing

Watch the log during `/reload`. Every broken recipe prints a `Failed to create` line with the reason. `/kubejs errors` in game shows the same list.

## My compound aspect fails with "Failed to get element"

Aspects with components cannot go through `ServerEvents.registry`. Use `ThaumaturgeData.aspect` inside `generateData` instead. The [Registries](registries.md) page shows both paths.

## Names show up as raw keys like golem.material.mypack.diamond

For golem content and wand parts the addon generates names for you, and `partName` or `wandName` set custom ones. For research entries you write the lang keys yourself in `kubejs/assets/<namespace>/lang/en_us.json`.

## The wand or golem renders with missing textures

Registration works without assets, rendering does not. Check the exact paths on each content page. Texture files live under `kubejs/assets/`, which behaves like a resource pack.

## Startup scripts changed but nothing happened

Startup scripts only load at game launch. Restart the whole game, not just the world.
