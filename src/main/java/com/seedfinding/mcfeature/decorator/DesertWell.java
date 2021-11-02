package com.seedfinding.mcfeature.decorator;

import com.seedfinding.mcbiome.biome.Biome;
import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcbiome.biome.Biomes;
import com.seedfinding.mccore.state.Dimension;
import com.seedfinding.mccore.version.VersionMap;
import com.seedfinding.mcterrain.TerrainGenerator;

public class DesertWell extends BiomelessDecorator<DesertWell.Config, DesertWell.Data> {

	public static final VersionMap<DesertWell.Config> CONFIGS = new VersionMap<DesertWell.Config>()
		.add(MCVersion.v1_13, new DesertWell.Config(3, 1, 0.001F))
		.add(MCVersion.v1_16, new DesertWell.Config(4, 13, 0.001F));

	public DesertWell(MCVersion version) {
		super(CONFIGS.getAsOf(version), version);
	}

	public DesertWell(DesertWell.Config config) {
		super(config, null);
	}

	@Override
	public String getName() {
		return name();
	}

	public static String name() {
		return "desert_well";
	}

	public float getChance() {
		return this.getConfig().chance;
	}

	@Override
	public boolean canStart(DesertWell.Data data, long structureSeed, ChunkRand rand) {
		super.canStart(data, structureSeed, rand);
		if(rand.nextFloat() >= this.getChance()) return false;
		if(rand.nextInt(16) != data.offsetX) return false;
		if(rand.nextInt(16) != data.offsetZ) return false;
		return true;
	}

	@Override
	public boolean canGenerate(Data data, TerrainGenerator generator) {
		return true;
	}

	@Override
	public Dimension getValidDimension() {
		return Dimension.OVERWORLD;
	}

	@Override
	public boolean isValidBiome(Biome biome) {
		return biome == Biomes.DESERT || biome == Biomes.DESERT_HILLS || biome == Biomes.DESERT_LAKES;
	}

	@Override
	public Data getData(long structureSeed, int chunkX, int chunkZ, ChunkRand rand) {
		this.setDecoratorSeed(structureSeed, chunkX, chunkZ, rand);
		if(rand.nextFloat() >= this.getChance()) return null;
		int blockX = (chunkX << 4) + rand.nextInt(16);
		int blockZ = (chunkZ << 4) + rand.nextInt(16);
		return new DesertWell.Data(this, blockX, blockZ);
	}

	public DesertWell.Data at(int blockX, int blockZ) {
		return new DesertWell.Data(this, blockX, blockZ);
	}

	public static class Config extends BiomelessDecorator.Config {
		public final float chance;

		public Config(int index, int step, float chance) {
			super(index, step);
			this.chance = chance;
		}
	}

	public static class Data extends BiomelessDecorator.Data<DesertWell> {
		public final int blockX;
		public final int blockZ;
		public final int offsetX;
		public final int offsetZ;

		public Data(DesertWell feature, int blockX, int blockZ) {
			super(feature, blockX >> 4, blockZ >> 4);
			this.blockX = blockX;
			this.blockZ = blockZ;
			this.offsetX = blockX & 15;
			this.offsetZ = blockZ & 15;
		}
	}

}
