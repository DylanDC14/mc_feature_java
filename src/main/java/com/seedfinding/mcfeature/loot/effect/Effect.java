package com.seedfinding.mcfeature.loot.effect;

import com.seedfinding.mccore.util.data.Pair;
import com.seedfinding.mcfeature.loot.effect.attribute.Attribute;
import com.seedfinding.mcfeature.loot.effect.attribute.AttributeModifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Effect {
	private final Map<Attribute, AttributeModifier> attributeModifiers = new HashMap<>();
	private final EffectType category;
	private final int color;
	private String description;

	protected Effect(EffectType effectType, int color) {
		this.category = effectType;
		this.color = color;
	}

	public EffectType getCategory() {
		return category;
	}

	public int getColor() {
		return color;
	}

	public Map<Attribute, AttributeModifier> getAttributeModifiers() {
		return attributeModifiers;
	}

	public String getDescription() {
		if(this.description == null) {
			this.description = Effects.getEffects().entrySet().stream()
				.filter(e -> e.getValue().equals(this))
				.map(Map.Entry::getKey)
				.map(Pair::getSecond)
				.findFirst()
				.orElse(null);
		}
		return this.description;
	}

	public boolean isInstantenous() {
		return false;
	}

	public Effect addAttributeModifier(Attribute attribute, String name, double amount, AttributeModifier.Operation operation) {
		AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(name), this::getDescription, amount, operation);
		this.attributeModifiers.put(attribute, attributemodifier);
		return this;
	}

	@Override
	public String toString() {
		return "Effect{" +
			"category=" + category +
			", description='" + getDescription() + '\'' +
			'}';
	}

	public enum EffectType {
		BENEFICIAL,
		HARMFUL,
		NEUTRAL
	}

	public static class InstantEffect extends Effect {
		public InstantEffect(EffectType effectType, int color) {
			super(effectType, color);
		}

		public boolean isInstantenous() {
			return true;
		}

	}

	public static class HealthBoostEffect extends Effect {
		public HealthBoostEffect(EffectType effectType, int color) {
			super(effectType, color);
		}
	}

	public static class AbsorptionEffect extends Effect {
		protected AbsorptionEffect(EffectType effectType, int color) {
			super(effectType, color);
		}
	}

	public static class AttackDamageEffect extends Effect {
		protected final double multiplier;

		protected AttackDamageEffect(EffectType effectType, int color, double multiplier) {
			super(effectType, color);
			this.multiplier = multiplier;
		}
	}
}

