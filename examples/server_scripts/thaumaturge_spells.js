// Spell hooks. Spell part registries load at server start, not on /reload.

ServerEvents.registry('thaumaturge:spell_part', event => {
    event.create('mypack:lifesteal').json({
        behavior: { type: 'thaumaturge:scripted_effect', key: 'mypack:lifesteal' },
        icon: 'thaumaturge:textures/foci/curse.png',
        color: 0x8A1C2B,
        complexity: 5,
        vis: 0.5,
        research: { entry: 'thaumaturge:focus_curse' },
        settings: [
            { key: 'power', min: 1, max: 4, default: 1, complexity: 3 }
        ]
    })
})

ThaumaturgeEvents.scriptedSpell('mypack:lifesteal', event => {
    const caster = event.caster
    event.targets.entities.forEach(entity => {
        if (!entity.living || event.isAlly(entity)) return
        const amount = 2 * event.power * event.getSetting('power')
        entity.attack(amount)
        if (caster) caster.heal(amount / 2)
        event.impact(entity.position())
    })
})

// Villagers shrug off every spell effect.
ThaumaturgeEvents.spellEffect(event => {
    const target = event.target.entity
    if (target && target.type == 'minecraft:villager') event.cancel()
})

// Channeled spells cost a little less for players with the stage.
ThaumaturgeEvents.spellCast(event => {
    if (event.styleName == 'channeled' && event.entity.isPlayer() && event.entity.stages.has('focused_mind')) {
        event.visCost *= 0.8
    }
})

// Gate big designs behind a stage.
ThaumaturgeEvents.spellInscribe(event => {
    if (event.complexity > 40 && !event.player.stages.has('archmage')) {
        event.player.tell('That design is beyond you for now.')
        event.cancel()
    }
})
