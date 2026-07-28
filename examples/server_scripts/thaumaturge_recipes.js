// Recipes and datapack content. Server scripts hot-reload with /reload.

ServerEvents.recipes(event => {
    event.recipes.thaumaturge.crucible(
        Item.of('minecraft:diamond'),
        'minecraft:coal',
        [{ aspect: 'thaumaturge:terra', amount: 20 }, { aspect: 'thaumaturge:ordo', amount: 10 }]
    )

    event.recipes.thaumaturge.infusion(
        Item.of('minecraft:netherite_ingot'),
        'minecraft:diamond',
        ['minecraft:gold_ingot', 'minecraft:ancient_debris'],
        [{ aspect: 'thaumaturge:ignis', amount: 50 }]
    ).instability(10)

    event.recipes.thaumaturge.arcane_workbench_shaped(
        Item.of('minecraft:lantern'),
        ['ttt', 't t', 'ttt'],
        { t: 'minecraft:torch' },
        15,
        [{ aspect: 'thaumaturge:ignis', amount: 1 }]
    )

    event.recipes.thaumaturge.arcane_workbench_shapeless(
        Item.of('minecraft:glowstone_dust', 4),
        ['minecraft:redstone', 'minecraft:quartz'],
        25,
        [{ aspect: 'thaumaturge:lux', amount: 2 }]
    )
})

// Aspects, research entries, categories, scans and blueprints are datapack registries,
// so scripts create them as JSON.
// Primal-style aspects (no components) can go through the registry event:
ServerEvents.registry('thaumaturge:aspect', event => {
    event.create('example:frigus').json({
        tag: 'frigus',
        color: 0x9BD5FF
    })
})

// Item and entity aspect assignments go through NeoForge data maps.
ServerEvents.generateData('before_mods', event => {
    // Compound aspects (with components) go through generateData -- the file is
    // parsed by the normal registry loader, so references resolve fine:
    ThaumaturgeData.aspect(event, 'example:gelum', {
        tag: 'gelum', color: 0x9BD5FF,
        components: ['thaumaturge:aqua', 'thaumaturge:ordo']
    })

    ThaumaturgeData.itemAspects(event, {
        'minecraft:packed_ice': [{ aspect: 'example:frigus', amount: 4 }]
    })
})

// Research point pools: strings work everywhere an aspect id is expected.
PlayerEvents.loggedIn(event => {
    if (!AspectPools.isDiscovered(event.player, 'thaumaturge:ignis')) return
    console.info(`ignis points: ${AspectPools.amount(event.player, 'thaumaturge:ignis')}`)
})

ThaumaturgeEvents.researchCompleted(event => {
    console.info(`${event.player.username} completed ${event.research}`)
})
