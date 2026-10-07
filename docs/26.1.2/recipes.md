# Recipes

Every Thaumaturge recipe type has a schema. They all live under `event.recipes.thaumaturge.*` in `ServerEvents.recipes`, hot reload with `/reload`, and can be removed and filtered like any KubeJS recipe.

Aspect lists are written as arrays of `{ aspect, amount }`. Results accept items, item stacks or strings.

## Crucible

Throw the catalyst into a boiling crucible holding the aspects.

```js
event.recipes.thaumaturge.crucible(
    Item.of('minecraft:diamond'),                       // result
    'minecraft:coal',                                   // catalyst ingredient
    [{ aspect: 'thaumaturge:terra', amount: 20 },
     { aspect: 'thaumaturge:ordo', amount: 10 }]        // aspects consumed
)
```

## Infusion

Runic matrix craft with a catalyst on the center pedestal and components around it.

```js
event.recipes.thaumaturge.infusion(
    Item.of('minecraft:netherite_ingot'),               // result
    'minecraft:diamond',                                // catalyst
    ['minecraft:gold_ingot', 'minecraft:ancient_debris'], // components
    [{ aspect: 'thaumaturge:ignis', amount: 50 }]       // essentia
).instability(10)
```

`instability` is optional, 0 to 100, default 0.

## Infusion that modifies the catalyst

Some infusions do not produce a new item. They apply data components to the catalyst itself, the way fortress armor gains its masks. Use `infusion_patch` and pass a component patch instead of a result:

```js
event.recipes.thaumaturge.infusion_patch(
    { 'minecraft:unbreakable': {} },                    // patch applied to the catalyst
    'minecraft:netherite_sword',                        // catalyst
    ['minecraft:nether_star'],                          // components
    [{ aspect: 'thaumaturge:ordo', amount: 64 }]
)
```

## Infusion enchantment

The eight special enchantments use their own type. The first argument is the enchantment name in lowercase.

```js
event.recipes.thaumaturge.infusion_enchantment(
    'collector',                                        // the enchantment
    'minecraft:diamond_pickaxe',                        // shown as the catalyst in displays
    ['minecraft:hopper', 'minecraft:ender_pearl'],      // components
    [{ aspect: 'thaumaturge:aqua', amount: 30 }]
)
```

## Arcane workbench

Shaped and shapeless, both with a vis cost and optional crystal costs. Crystals are primal aspects paid with essentia crystals in the six slots.

```js
event.recipes.thaumaturge.arcane_workbench_shaped(
    Item.of('minecraft:lantern'),                       // result
    ['ttt', 't t', 'ttt'],                              // pattern
    { t: 'minecraft:torch' },                           // key
    15,                                                 // vis
    [{ aspect: 'thaumaturge:ignis', amount: 1 }]        // crystals
)

event.recipes.thaumaturge.arcane_workbench_shapeless(
    Item.of('minecraft:glowstone_dust', 4),
    ['minecraft:redstone', 'minecraft:quartz'],
    25,
    []                                                  // no crystals needed
)
```

Both take an optional `.show_notification(false)` to skip the recipe unlock toast.

## Dust triggers

What happens when salis mundus is used on a block. Three flavors.

```js
// one block turns into an item drop
event.recipes.thaumaturge.dust_trigger_simple(
    Item.of('mypack:arcane_core'),                      // result
    'minecraft:lodestone'                               // target block
)

// any block in a tag
event.recipes.thaumaturge.dust_trigger_tag(
    Item.of('mypack:living_wood'),
    'minecraft:logs'                                    // block tag, no # needed
)

// a whole multiblock defined by a blueprint
event.recipes.thaumaturge.dust_trigger_multiblock(
    Item.of('mypack:altar'),
    'mypack:altar_blueprint'                            // blueprint id, see Registries page
)
```

## Research gates

Every recipe above takes an optional research requirement so it only works for players who unlocked the right entry:

```js
event.recipes.thaumaturge.crucible(
    Item.of('minecraft:diamond'), 'minecraft:coal',
    [{ aspect: 'thaumaturge:terra', amount: 20 }]
).research({ entry: 'thaumaturge:unlock_alchemy' })
```

You can also require a specific stage with `{ entry: '...', stage: 1 }` or invert it with `negate: true`.

## Removing and changing mod recipes

Standard KubeJS filtering works on Thaumaturge types:

```js
event.remove({ type: 'thaumaturge:crucible', output: 'minecraft:gunpowder' })
event.remove({ id: 'thaumaturge:wand/assembly/gold_greatwood' })
```
