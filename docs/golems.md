# Golems

A Thaumaturge golem is a combination, not a single entity: one material plus a head, arms, legs and an addon. The material decides what the golem is made of (body texture, health, armor, damage). The other parts are functional variants like scout heads or flyer legs and work with every material.

Because of that, adding one material gives you a full new golem line. Every existing part combines with it, the golem press offers it, and the creative tab gets a placer for it. All automatic.

## A material

```js
StartupEvents.registry('thaumaturge:golem_material', event => {
    event.create('mypack:diamond')
        .health(26)
        .armor(11)
        .damage(4)
        .itemColor(0x4AEDD9)
        .base('minecraft:diamond')
        .mechanism('thaumaturge:mechanism_simple')
        .traits(['thaumaturge:heavy', 'thaumaturge:armored'])
        .partName('Diamond')
})
```

Body texture goes at:

```
kubejs/assets/mypack/textures/entity/golems/mat_diamond.png
```

Copy `mat_iron.png` from the mod and recolor it. Arms, legs and heads pick up the material color on their own, so the whole golem reads as diamond, not just the body.

## Material methods

| Method | What it does |
|---|---|
| `.health(int)` | Added to the golem base of 10 |
| `.armor(int)` | Armor rating |
| `.damage(int)` | Base melee damage |
| `.itemColor(rgb)` | Tint of the placer item |
| `.base(item)` | The material's base crafting component |
| `.mechanism(item)` | The material's mechanism component |
| `.traits([ids])` | Traits every golem of this material carries |
| `.research([ids])` | Research entries gating it in the press. Empty means always available |
| `.texture(id)` | Override the body texture location |
| `.partName(text)` / `.partText(text)` | Name and description in the press and tooltips |

## Parts

Heads, arms, legs and addons work the same way with their own registries:

```js
StartupEvents.registry('thaumaturge:golem_head', event => {
    event.create('mypack:diving')
        .traits(['thaumaturge:smart'])
        .component('minecraft:glass')
        .materialBase()
        .partName('Diving Helm')
        .partText('Lets the golem think under water. Allegedly.')
})
```

`component(item)` adds a fixed crafting component. `materialBase()` and `materialMechanism()` add slots that cost whatever the golem's material uses, so an iron golem pays iron plates and a diamond one pays diamonds.

Parts can reuse an existing model with `.model(id)` and `.modelTexture(id)`, or skip the model entirely and be stat only. Registries: `thaumaturge:golem_head`, `thaumaturge:golem_arm`, `thaumaturge:golem_leg`, `thaumaturge:golem_addon`.

## Traits

Traits are behavior tags. The mod ships nineteen (heavy, light, fragile, armored, smart, fireproof and so on) and you can add your own:

```js
StartupEvents.registry('thaumaturge:golem_trait', event => {
    event.create('mypack:aquatic').partName('Aquatic')
    event.create('mypack:landlocked').opposite('mypack:aquatic').partName('Landlocked')
})
```

Opposite traits cancel each other when a golem would have both, like heavy and light do. Trait icons go at `kubejs/assets/mypack/textures/misc/golem/tag_aquatic.png`.

## Testing

Grab a placer straight from the creative tab, or:

```
/give @s thaumaturge:golem_placer[thaumaturge:golem_properties={material:"mypack:diamond",head:"thaumaturge:basic",arms:"thaumaturge:basic",legs:"thaumaturge:walker",addon:"thaumaturge:none"}]
```
