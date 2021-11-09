package com.seedfinding.mcfeature.examples.loot.explanation;

import com.seedfinding.mcbiome.source.BiomeSource;
import com.seedfinding.mcbiome.source.OverworldBiomeSource;
import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mccore.rand.seed.WorldSeed;
import com.seedfinding.mccore.state.Dimension;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcfeature.loot.ChestContent;
import com.seedfinding.mcfeature.loot.LootContext;
import com.seedfinding.mcfeature.loot.MCLootTables;
import com.seedfinding.mcfeature.loot.item.ItemStack;
import com.seedfinding.mcfeature.loot.item.Items;
import com.seedfinding.mcfeature.structure.DesertPyramid;
import com.seedfinding.mcfeature.structure.RegionStructure;
import com.seedfinding.mcfeature.structure.generator.structure.DesertPyramidGenerator;
import com.seedfinding.mcterrain.TerrainGenerator;

import java.util.List;

public class LootTestForwardGoldenApple {
	public static final DesertPyramid DESERT_TEMPLE = new DesertPyramid(MCVersion.v1_16);

	public static void main(String[] args) {
		System.out.println("Running old version");
		oldVersion();
		System.out.println("Running new version");
		newVersion();
	}

	public static void newVersion() {
		ChunkRand rand = new ChunkRand();
		// go at /tp @p 12512 90 25344 in worldseed 1
		long worldSeed = 1L;
		// warning this will not work in old version, please stick to 1.14+
		MCVersion version = MCVersion.v1_16;
		BiomeSource source = BiomeSource.of(Dimension.OVERWORLD, version, worldSeed);
		TerrainGenerator generator = TerrainGenerator.of(Dimension.OVERWORLD, source);
		DesertPyramidGenerator desertPyramidGenerator = new DesertPyramidGenerator(version);
		for(int chunkX = -2000; chunkX < 2000; chunkX++) {
			for(int chunkZ = -2000; chunkZ < 2000; chunkZ++) {
				// get the offset in that chunk for that burried treasure at chunkX and chunkZ
				RegionStructure.Data<?> desertPyramidData = DESERT_TEMPLE.at(chunkX, chunkZ);
				// check that the structure can generate in that chunk (it's luck based with a nextfloat)
				if(!desertPyramidData.testStart(WorldSeed.toStructureSeed(worldSeed), rand)) continue;
				// test if the biomes are correct at that place
				if(!desertPyramidData.testBiome(source)) continue;
				// must generate the chest position (you can pass null as the TerrainGenerator for
				// this specific one since it doesn't use it
				desertPyramidGenerator.generate(generator, chunkX, chunkZ, rand);
				//desertPyramidGenerator.generate(null,chunkX,chunkZ,rand);
				// get the loot
				List<ChestContent> chests = DESERT_TEMPLE.getLoot(worldSeed, desertPyramidGenerator, rand, false);
				// calculate the number of chest with at least an enchanted golden apple
				int count = chests.stream().mapToInt(chest -> chest.getCount(Items.ENCHANTED_GOLDEN_APPLE)).sum();
				if(count >= 3) {
					System.out.printf("/tp @p %d 90 %d%n", chunkX * 16, +chunkZ * 16);
				}
			}
		}
	}


	public static void oldVersion() {
		ChunkRand rand = new ChunkRand();
		// go at /tp @p 12512 90 25344 in worldseed 1
		long worldSeed = 1L;
		// warning this will not work in old version, please stick to 1.14+
		MCVersion version = MCVersion.v1_16;
		OverworldBiomeSource source = new OverworldBiomeSource(version, worldSeed);
		for(int chunkX = -2000; chunkX < 2000; chunkX++) {
			for(int chunkZ = -2000; chunkZ < 2000; chunkZ++) {
				// get the offset in that chunk for that burried treasure at chunkX and chunkZ
				RegionStructure.Data<?> desertPyramidData = DESERT_TEMPLE.at(chunkX, chunkZ);
				// check that the structure can generate in that chunk (it's luck based with a nextfloat)
				if(!desertPyramidData.testStart(WorldSeed.toStructureSeed(worldSeed), rand)) continue;
				// test if the biomes are correct at that place
				if(!desertPyramidData.testBiome(source)) continue;
				// we get the decoration Seed (used to place all the decorators)
				rand.setDecoratorSeed(worldSeed, chunkX * 16, chunkZ * 16, 40003, version);
				// we get the loot table seed
				int sum = 0;
				long lootTableSeed = rand.nextLong();
				LootContext context = new LootContext(lootTableSeed);
				List<ItemStack> loot1 = MCLootTables.DESERT_PYRAMID_CHEST.apply(version).generate(context);
				boolean b1 = loot1.contains(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));

				lootTableSeed = rand.nextLong();
				context = new LootContext(lootTableSeed);
				List<ItemStack> loot2 = MCLootTables.DESERT_PYRAMID_CHEST.apply(version).generate(context);
				boolean b2 = loot2.contains(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));

				lootTableSeed = rand.nextLong();
				context = new LootContext(lootTableSeed);
				List<ItemStack> loot3 = MCLootTables.DESERT_PYRAMID_CHEST.apply(version).generate(context);
				boolean b3 = loot3.contains(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));

				lootTableSeed = rand.nextLong();
				context = new LootContext(lootTableSeed);
				List<ItemStack> loot4 = MCLootTables.DESERT_PYRAMID_CHEST.apply(version).generate(context);
				boolean b4 = loot4.contains(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
				sum = (b1 ? 1 : 0) + (b2 ? 1 : 0) + (b3 ? 1 : 0) + (b4 ? 1 : 0);
				// boolean b= loot.stream().anyMatch(itemStack -> itemStack.getItem() == Item.ENCHANTED_GOLDEN_APPLE);
				if(sum >= 3) {
					System.out.printf("/tp @p %d 90 %d%n", chunkX * 16, +chunkZ * 16);
					// System.out.println(Arrays.toString(loot.toArray()));
				}
			}
		}
	}
}
