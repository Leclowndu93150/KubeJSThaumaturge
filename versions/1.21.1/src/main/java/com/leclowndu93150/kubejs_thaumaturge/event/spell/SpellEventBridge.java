package com.leclowndu93150.kubejs_thaumaturge.event.spell;

import com.leclowndu93150.kubejs_thaumaturge.event.ThaumaturgeEvents;
import com.leclowndu93150.thaumaturge.api.spell.event.ScriptedSpellEvent;
import com.leclowndu93150.thaumaturge.api.spell.event.SpellCastEvent;
import com.leclowndu93150.thaumaturge.api.spell.event.SpellEffectEvent;
import com.leclowndu93150.thaumaturge.api.spell.event.SpellInscribeEvent;
import com.leclowndu93150.thaumaturge.api.spell.event.SpellNodeEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.neoforged.bus.api.IEventBus;

public final class SpellEventBridge {
    private SpellEventBridge() {}

    public static void register(IEventBus bus) {
        bus.addListener(SpellEventBridge::onPreCast);
        bus.addListener(SpellEventBridge::onPostCast);
        bus.addListener(SpellEventBridge::onEffect);
        bus.addListener(SpellEventBridge::onNode);
        bus.addListener(SpellEventBridge::onScripted);
        bus.addListener(SpellEventBridge::onInscribe);
    }

    private static void onPreCast(SpellCastEvent.Pre event) {
        if (ThaumaturgeEvents.SPELL_CAST.hasListeners()) {
            ThaumaturgeEvents.SPELL_CAST
                    .post(ScriptType.SERVER, new SpellPreCastKubeEvent(event))
                    .applyCancel(event);
        }
    }

    private static void onPostCast(SpellCastEvent.Post event) {
        if (ThaumaturgeEvents.SPELL_CASTED.hasListeners()) {
            ThaumaturgeEvents.SPELL_CASTED.post(ScriptType.SERVER, new SpellCastKubeEvent(event));
        }
    }

    private static void onEffect(SpellEffectEvent event) {
        if (ThaumaturgeEvents.SPELL_EFFECT.hasListeners()) {
            ThaumaturgeEvents.SPELL_EFFECT
                    .post(ScriptType.SERVER, new SpellEffectKubeEvent(event))
                    .applyCancel(event);
        }
    }

    private static void onNode(SpellNodeEvent event) {
        if (ThaumaturgeEvents.SPELL_NODE.hasListeners()) {
            ThaumaturgeEvents.SPELL_NODE
                    .post(ScriptType.SERVER, new SpellNodeKubeEvent(event))
                    .applyCancel(event);
        }
    }

    private static void onScripted(ScriptedSpellEvent event) {
        if (ThaumaturgeEvents.SCRIPTED_SPELL.hasListeners(event.key())) {
            ThaumaturgeEvents.SCRIPTED_SPELL.post(ScriptType.SERVER, event.key(), new ScriptedSpellKubeEvent(event));
        }
    }

    private static void onInscribe(SpellInscribeEvent event) {
        if (ThaumaturgeEvents.SPELL_INSCRIBE.hasListeners()) {
            ThaumaturgeEvents.SPELL_INSCRIBE
                    .post(ScriptType.SERVER, new SpellInscribeKubeEvent(event))
                    .applyCancel(event);
        }
    }
}
