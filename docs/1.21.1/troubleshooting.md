# Troubleshooting

## My block or item does not exist

Registration lives in `startup_scripts` and needs a full game restart, not `/reload`. If you put a `StartupEvents.registry` call in `server_scripts` it never runs.

## Unknown type 'thaumaturge:jar'

The addon is not loaded, or an old version of it. Check the log for `Found plugin source kubejs_thaumaturge`.

## The addon does not load, or a class is missing on startup

This build is for Minecraft 1.21.1 and needs Thaumaturge 1.1.0 or newer. Older Thaumaturge versions are missing API the addon uses. On Minecraft 26.1.2 you need the 26.1.2 build of the addon, which has [its own docs](/26.1.2/).

## My recipes silently do nothing

Watch the log during `/reload`. Every broken recipe prints a `Failed to create` line with the reason. `/kubejs errors` in game shows the same list.

## My compound aspect fails with "Failed to get element"

Aspects with components cannot go through `ServerEvents.registry`. Use `ThaumaturgeData.aspect` inside `generateData` instead. The [Registries](registries.md) page shows both paths.

## Names show up as raw keys like golem.material.mypack.diamond

For golem content and wand parts the addon generates names for you, and `partName` or `wandName` set custom ones. For research entries you write the lang keys yourself in `kubejs/assets/<namespace>/lang/en_us.json`.

## The wand or golem renders with missing textures

Registration works without assets, rendering does not. Check the exact paths on each content page. Texture files live under `kubejs/assets/`, which behaves like a resource pack.

## The workbench shows one cost and charges another

`arcaneCraftCost` runs on both sides. If only a server script changes the cost, the screen still shows the old one. Put the same handler in `client_scripts` too. See [Events](events.md#arcane-crafting-cost).

## My golem accessory floats in the wrong spot

The item model is placed with `renderOffset`, `renderScale` and `renderRotation`. The defaults are a guess that suits a small item on the head. Tune them and restart. See [Golem accessories](golem-accessories.md).

## Startup scripts changed but nothing happened

Startup scripts only load at game launch. Restart the whole game, not just the world.
