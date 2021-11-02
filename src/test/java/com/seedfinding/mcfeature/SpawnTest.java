package com.seedfinding.mcfeature;

import com.seedfinding.mcfeature.misc.SpawnPoint;
import com.seedfinding.mcbiome.source.OverworldBiomeSource;
import com.seedfinding.mccore.util.pos.BPos;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcterrain.terrain.OverworldTerrainGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpawnTest {
	@Test
	@DisplayName("Test Spawn for 1.12")
	public void testSpawn12() {
		OverworldBiomeSource bs = new OverworldBiomeSource(MCVersion.v1_12, 4L);
		assertEquals(new BPos(-76, 64, 128), SpawnPoint.getApproximateSpawn(bs));
		OverworldTerrainGenerator ts = new OverworldTerrainGenerator(bs);
		assertEquals(new BPos(-710, 64, 569), SpawnPoint.getSpawn(ts));
	}

	@Test
	@DisplayName("Test Spawn for 1.16")
	public void testSpawn16() {
		OverworldBiomeSource bs = new OverworldBiomeSource(MCVersion.v1_16, 4L);
		assertEquals(new BPos(-56, 64, 120), SpawnPoint.getApproximateSpawn(bs));
		OverworldTerrainGenerator ts = new OverworldTerrainGenerator(bs);
		assertEquals(new BPos(-64, 65, 112), SpawnPoint.getSpawn(ts));
	}
}
