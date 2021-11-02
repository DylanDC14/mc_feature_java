package com.seedfinding.mcfeature.structure;

import com.seedfinding.mccore.version.MCVersion;

public abstract class OldStructure<T extends OldStructure<T>> extends UniformStructure<T> {

	public OldStructure(RegionStructure.Config config, MCVersion version) {
		super(config, version);
	}

	public static String name() {
		return "old_structure";
	}

	public static class Config extends RegionStructure.Config {
		public static final int SPACING = 32;
		public static final int SEPARATION = 8;

		public Config(int salt) {
			super(SPACING, SEPARATION, salt);
		}
	}

}
