package com.seedfinding.mcfeature.examples.loot.simple;

import com.seedfinding.mcbiome.source.BiomeSource;
import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mccore.state.Dimension;
import com.seedfinding.mccore.util.pos.CPos;
import com.seedfinding.mccore.util.pos.RPos;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcfeature.loot.ChestContent;
import com.seedfinding.mcfeature.structure.BuriedTreasure;
import com.seedfinding.mcfeature.structure.DesertPyramid;
import com.seedfinding.mcfeature.structure.RegionStructure;
import com.seedfinding.mcfeature.structure.Shipwreck;
import com.seedfinding.mcfeature.structure.generator.Generator;
import com.seedfinding.mcfeature.structure.generator.Generators;
import com.seedfinding.mcfeature.structure.generator.structure.BuriedTreasureGenerator;
import com.seedfinding.mcfeature.structure.generator.structure.DesertPyramidGenerator;
import com.seedfinding.mcfeature.structure.generator.structure.ShipwreckGenerator;
import com.seedfinding.mcterrain.TerrainGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class GetLoot {
	// this is to remove dynamic dispatch
	private static void assertTrue(boolean condition) {
		assert condition;
	}

	public static void main(String[] args) {
		// For now stick to 1.14+ and especially 1.16.5
		MCVersion version = MCVersion.v1_16;
		long worldSeed = 1L;
//		desertPyramid(version, worldSeed);
//		buriedTreasure(version, worldSeed);
//		shipwreck(version, worldSeed);
		buriedTreasure(version, 1L, 3, 4);
	}

	public static void buriedTreasure(MCVersion version, long structureSeed, int regX, int regZ) {
		ChunkRand rand = new ChunkRand();
		BuriedTreasure buriedTreasure = new BuriedTreasure(version);
		BuriedTreasureGenerator generator = new BuriedTreasureGenerator(version);
		CPos pos = buriedTreasure.getInRegion(structureSeed, regX, regZ, rand);
		if(pos == null) return;
		generator.generate(null, pos, new ChunkRand());
		List<ChestContent> chestContents = buriedTreasure.getLoot(structureSeed, generator, new ChunkRand(), true);
		chestContents.stream().map(ChestContent::getItems).forEach(System.out::println);

	}

	public static void desertPyramid(MCVersion version, long worldSeed) {
		// For optimization we ask you to create the chunkRand, this should be done per thread
		ChunkRand rand = new ChunkRand();
		// Create my structure
		DesertPyramid desertPyramid = new DesertPyramid(version);
		// Create my factory
		Generator.GeneratorFactory<?> generatorFactory = Generators.get(desertPyramid.getClass());
		assert generatorFactory != null;
		// Create my generator
		Generator structureGenerator = generatorFactory.create(version);
		assert structureGenerator instanceof DesertPyramidGenerator;
		// Generate my biome source
		BiomeSource source = BiomeSource.of(Dimension.OVERWORLD, version, worldSeed);
		// Generate my TerrainGenerator
		TerrainGenerator terrainGenerator = TerrainGenerator.of(Dimension.OVERWORLD, source);

		// Choose a valid chunk position for my structure
		// here we chose 24 and 49 as the region coordinates, remember region are structure dependant
		// so use structure#getSpacing() to change base
		// getInRegion guarantee that in that region that structure canStart
		CPos pos = desertPyramid.getInRegion(worldSeed, 24, 49, rand);
		assertTrue(pos.toRegionPos(desertPyramid.getSpacing()).equals(new RPos(24, 49, desertPyramid.getSpacing())));
		// Alternatively you can get the data at a specific chunk position, however you will need to check canStart then
		// We don't recommend this as you usually never know the chunk position beforehand...
		@SuppressWarnings("unchecked")
		RegionStructure.Data<DesertPyramid> data = (RegionStructure.Data<DesertPyramid>)desertPyramid.at(782, 1584);
		assertTrue(desertPyramid.canStart(data, worldSeed, rand));

		// Verify that this chunk position is a valid spot to spawn
		assertTrue(desertPyramid.canSpawn(pos, source));
		// Alternatively if you had a data
		assertTrue(desertPyramid.canSpawn(data, source));

		// Verify that this chunk position is a valid spot to generate terrain wise
		// (not all structure have this check, desert pyramid doesn't need it for instance)
		assertTrue(desertPyramid.canGenerate(pos, terrainGenerator));
		// Alternatively if you had a data
		assertTrue(desertPyramid.canGenerate(data, terrainGenerator));

		// Generate the chest position for that structure at that valid chunk position (rand is optional but encouraged)
		assertTrue(structureGenerator.generate(terrainGenerator, pos, rand));
		// Alternatively if you had a data
		assertTrue(structureGenerator.generate(terrainGenerator, data.chunkX, data.chunkZ, rand));

		// You can chest the chest positions here
		System.out.println(structureGenerator.getChestsPos());
		assertTrue(structureGenerator.getChestsPos().size() == 4);

		// Get the loot that generated in those chest positions (indexed allow to create a chest as in Minecraft with randomized slots and EMPTY_ITEM)
		List<ChestContent> chests = desertPyramid.getLoot(worldSeed, structureGenerator, rand, false);
		// the result is a hashmap with each possible type of chest (there could be multiple instance of that type of chest)
		// with a list of chest content attached to each.

		// Here desert pyramid have 4 chest with a single chest per list
		List<ChestContent> chest1 = chests.stream().filter(e -> e.ofType(DesertPyramidGenerator.LootType.CHEST_1)).collect(Collectors.toList());
		assertTrue(chest1.size() == 1);
		System.out.println(chest1);
		List<ChestContent> chest2 = chests.stream().filter(e -> e.ofType(DesertPyramidGenerator.LootType.CHEST_2)).collect(Collectors.toList());
		assertTrue(chest2.size() == 1);
		System.out.println(chest2);
		List<ChestContent> chest3 = chests.stream().filter(e -> e.ofType(DesertPyramidGenerator.LootType.CHEST_3)).collect(Collectors.toList());
		assertTrue(chest3.size() == 1);
		System.out.println(chest3);
		List<ChestContent> chest4 = chests.stream().filter(e -> e.ofType(DesertPyramidGenerator.LootType.CHEST_4)).collect(Collectors.toList());
		assertTrue(chest4.size() == 1);
		System.out.println(chest4);

	}

	public static void buriedTreasure(MCVersion version, long worldSeed) {
		// For optimization we ask you to create the chunkRand, this should be done per thread
		ChunkRand rand = new ChunkRand();
		// Create my structure
		BuriedTreasure buriedTreasure = new BuriedTreasure(version);
		// Create my factory
		Generator.GeneratorFactory<?> generatorFactory = Generators.get(buriedTreasure.getClass());
		assert generatorFactory != null;
		// Create my generator
		Generator structureGenerator = generatorFactory.create(version);
		assert structureGenerator instanceof BuriedTreasureGenerator;
		// Generate my biome source
		BiomeSource source = BiomeSource.of(Dimension.OVERWORLD, version, worldSeed);
		// Generate my TerrainGenerator
		TerrainGenerator terrainGenerator = TerrainGenerator.of(Dimension.OVERWORLD, source);

		// Choose a valid chunk position for my structure
		// here we chose -25, -20 as the region coordinates, remember region are structure dependant
		// so use structure#getSpacing() to change base
		// getInRegion guarantee that in that region that structure canStart
		CPos pos = buriedTreasure.getInRegion(worldSeed, -25, -20, rand);
		assertTrue(pos.toRegionPos(buriedTreasure.getSpacing()).equals(new RPos(-25, -20, buriedTreasure.getSpacing())));
		// Alternatively you can get the data at a specific chunk position, however you will need to check canStart then
		// We don't recommend this as you usually never know the chunk position beforehand...
		RegionStructure.Data<BuriedTreasure> data = buriedTreasure.at(-25, -20);
		assertTrue(buriedTreasure.canStart(data, worldSeed, rand));

		// Verify that this chunk position is a valid spot to spawn
		assertTrue(buriedTreasure.canSpawn(pos, source));
		// Alternatively if you had a data
		assertTrue(buriedTreasure.canSpawn(data, source));

		// Verify that this chunk position is a valid spot to generate terrain wise
		// (not all structure have this check, buried treasure doesn't need it for instance)
		assertTrue(buriedTreasure.canGenerate(pos, terrainGenerator));
		// Alternatively if you had a data
		assertTrue(buriedTreasure.canGenerate(data, terrainGenerator));

		// Generate the chest position for that structure at that valid chunk position (rand is optional but encouraged)
		assertTrue(structureGenerator.generate(terrainGenerator, pos, rand));
		// Alternatively if you had a data
		assertTrue(structureGenerator.generate(terrainGenerator, data.chunkX, data.chunkZ, rand));

		// You can chest the chest positions here
		System.out.println(structureGenerator.getChestsPos());
		assertTrue(structureGenerator.getChestsPos().size() == 1);

		// Get the loot that generated in those chest positions (indexed allow to create a chest as in Minecraft with randomized slots and EMPTY_ITEM)
		List<ChestContent> chests = buriedTreasure.getLoot(worldSeed, structureGenerator, rand, false);
		// the result is a hashmap with each possible type of chest (there could be multiple instance of that type of chest)
		// with a list of chest content attached to each.

		// Here buried treasure have a single chest per list
		assertTrue(chests.size() == 1);
		ChestContent chest1 = chests.get(0);
		System.out.println(chest1);
	}


	public static void shipwreck(MCVersion version, long worldSeed) {
		// For optimization we ask you to create the chunkRand, this should be done per thread
		ChunkRand rand = new ChunkRand();
		// Create my structure
		Shipwreck shipwreck = new Shipwreck(version);
		// Create my factory
		Generator.GeneratorFactory<?> generatorFactory = Generators.get(shipwreck.getClass());
		assert generatorFactory != null;
		// Create my generator
		Generator structureGenerator = generatorFactory.create(version);
		assert structureGenerator instanceof ShipwreckGenerator;
		// Generate my biome source
		BiomeSource source = BiomeSource.of(Dimension.OVERWORLD, version, worldSeed);
		// Generate my TerrainGenerator
		TerrainGenerator terrainGenerator = TerrainGenerator.of(Dimension.OVERWORLD, source);

		// Choose a valid chunk position for my structure
		// here we chose the blockpos -615,9 that we convert to region
		// as the region coordinates, remember region are structure dependant
		// so use structure#getSpacing() to change base
		// getInRegion guarantee that in that region that structure canStart
		CPos pos = shipwreck.getInRegion(worldSeed, -615 / 16 / shipwreck.getSpacing(), 9 / 16 / shipwreck.getSpacing(), rand);
		assertTrue(pos.toRegionPos(shipwreck.getSpacing()).equals(new RPos(-1, 0, shipwreck.getSpacing())));
		// Alternatively you can get the data at a specific chunk position, however you will need to check canStart then
		// We don't recommend this as you usually never know the chunk position beforehand...
		@SuppressWarnings("unchecked")
		RegionStructure.Data<Shipwreck> data = (RegionStructure.Data<Shipwreck>)shipwreck.at(-39, 0);
		assertTrue(shipwreck.canStart(data, worldSeed, rand));

		// Verify that this chunk position is a valid spot to spawn
		assertTrue(shipwreck.canSpawn(pos, source));
		// Alternatively if you had a data
		assertTrue(shipwreck.canSpawn(data, source));

		// Verify that this chunk position is a valid spot to generate terrain wise
		// (not all structure have this check, shipwreck doesn't need it for instance)
		assertTrue(shipwreck.canGenerate(pos, terrainGenerator));
		// Alternatively if you had a data
		assertTrue(shipwreck.canGenerate(data, terrainGenerator));

		// Generate the chest position for that structure at that valid chunk position (rand is optional but encouraged)
		assertTrue(structureGenerator.generate(terrainGenerator, pos, rand));
		// Alternatively if you had a data
		assertTrue(structureGenerator.generate(terrainGenerator, data.chunkX, data.chunkZ, rand));

		// You can chest the chest positions here
		System.out.println(structureGenerator.getChestsPos());
		assertTrue(structureGenerator.getChestsPos().size() == 1);

		// Get the loot that generated in those chest positions (indexed allow to create a chest as in Minecraft with randomized slots and EMPTY_ITEM)
		List<ChestContent> chests = shipwreck.getLoot(worldSeed, structureGenerator, rand, false);
		// the result is a hashmap with each possible type of chest (there could be multiple instance of that type of chest)
		// with a list of chest content attached to each.

		// Here shipwreck have 3 chest with a single chest per list
		List<ChestContent> treasureChest = chests.stream().filter(e -> e.ofType(ShipwreckGenerator.LootType.TREASURE_CHEST)).collect(Collectors.toList());
		assertTrue(treasureChest.size() == 1);
		System.out.println(treasureChest);
		List<ChestContent> mapChest = chests.stream().filter(e -> e.ofType(ShipwreckGenerator.LootType.MAP_CHEST)).collect(Collectors.toList());
		assertTrue(mapChest.size() == 1);
		System.out.println(mapChest);
		List<ChestContent> supplyChest = chests.stream().filter(e -> e.ofType(ShipwreckGenerator.LootType.SUPPLY_CHEST)).collect(Collectors.toList());
		assertTrue(supplyChest.size() == 1);
		System.out.println(supplyChest);
	}

	// etc for End cities, ruined portal...

}
