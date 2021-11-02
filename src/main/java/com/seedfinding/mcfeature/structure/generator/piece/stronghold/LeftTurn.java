package com.seedfinding.mcfeature.structure.generator.piece.stronghold;

import com.seedfinding.mcfeature.structure.Stronghold;
import com.seedfinding.mcfeature.structure.generator.structure.StrongholdGenerator;
import com.seedfinding.mccore.util.block.BlockBox;
import com.seedfinding.mccore.util.block.BlockDirection;
import com.seedfinding.mccore.util.pos.BPos;
import com.seedfinding.mcseed.rand.JRand;

import java.util.List;

public class LeftTurn extends Stronghold.Piece {

	public LeftTurn(int pieceId, JRand rand, BlockBox boundingBox, BlockDirection facing) {
		super(pieceId);
		this.setOrientation(facing);
		rand.nextInt(5); //Random entrance.
		this.boundingBox = boundingBox;
	}

	public static LeftTurn createPiece(List<Stronghold.Piece> pieces, JRand rand, int x, int y, int z, BlockDirection facing, int pieceId) {
		BlockBox box = BlockBox.rotated(x, y, z, -1, -1, 0, 5, 5, 5, facing.getRotation());
		return Stronghold.Piece.isHighEnough(box) && Stronghold.Piece.getNextIntersectingPiece(pieces, box) == null ? new LeftTurn(pieceId, rand, box, facing) : null;
	}

	@Override
	public void populatePieces(StrongholdGenerator gen, Start start, List<Stronghold.Piece> pieces, JRand rand) {
		BlockDirection facing = this.getFacing();

		if(facing != BlockDirection.NORTH && facing != BlockDirection.EAST) {
			this.generateSmallDoorChildRight(gen, start, pieces, rand, 1, 1);
		} else {
			this.generateSmallDoorChildrenLeft(gen, start, pieces, rand, 1, 1);
		}
	}

	public boolean process(JRand rand, BPos pos) {
		skipWithRandomized(rand, 0, 0, 0, 4, 4, 4, true);
		// door not random
		// 1 call not random (direction dependant)
		return true;
	}

}
