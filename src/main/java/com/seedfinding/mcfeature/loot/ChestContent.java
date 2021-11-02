package com.seedfinding.mcfeature.loot;

import com.seedfinding.mcfeature.loot.item.Item;
import com.seedfinding.mcfeature.loot.item.ItemStack;
import com.seedfinding.mcfeature.structure.generator.Generator;
import com.seedfinding.mccore.util.pos.BPos;

import java.util.List;
import java.util.function.Predicate;

public class ChestContent {
	private final Generator.ILootType lootType;
	private final ChestType chestType;
	private final List<ItemStack> items;
	private final BPos pos;
	private final boolean indexed;

	public ChestContent(Generator.ILootType lootType, List<ItemStack> items, BPos pos) {
		this(lootType, lootType.getChestType(), items, pos);
	}

	public ChestContent(Generator.ILootType lootType, ChestType chestType, List<ItemStack> items, BPos pos) {
		this(lootType, chestType, items, pos, false);
	}

	public ChestContent(Generator.ILootType lootType, List<ItemStack> items, BPos pos, boolean indexed) {
		this(lootType, lootType.getChestType(), items, pos);
	}

	public ChestContent(Generator.ILootType lootType, ChestType chestType, List<ItemStack> items, BPos pos, boolean indexed) {
		this.lootType = lootType;
		this.chestType = chestType;
		this.items = items;
		this.pos = pos;
		this.indexed = indexed;
	}

	public boolean contains(Item item) {
		return this.containsAtLeast(item, 1);
	}

	public boolean containsAtLeast(Item item, int count) {
		return this.getCount(e -> e.equalsName(item)) >= count;
	}

	public boolean containsExact(Item item) {
		return this.containsExactlyAtLeast(item, 1);
	}

	public boolean containsExactlyAtLeast(Item item, int count) {
		return this.getCount(e -> e.equals(item)) >= count;
	}

	public boolean contains(Predicate<Item> predicate) {
		return this.containsAtLeast(predicate, 1);
	}

	public boolean containsAtLeast(Predicate<Item> predicate, int count) {
		return this.getCount(predicate) >= count;
	}

	public boolean containsExact(Predicate<Item> predicate) {
		return this.containsExactlyAtLeast(predicate, 1);
	}

	public boolean containsExactlyAtLeast(Predicate<Item> predicate, int count) {
		return this.getCount(predicate) >= count;
	}

	public int getCountExact(Item item) {
		return getCount(e -> e.equals(item));
	}

	public int getCount(Item item) {
		return getCount(e -> e.equalsName(item));
	}

	public int getCount(Predicate<Item> predicate) {
		return this.items.stream().filter(e -> e != null && e.getItem() != null).filter(e -> predicate.test(e.getItem())).mapToInt(ItemStack::getCount).sum();
	}

	public boolean ofType(Generator.ILootType lootType){
		return this.lootType==lootType;
	}

	public Generator.ILootType getLootType() {
		return lootType;
	}

	public ChestType getChestType() {
		return chestType;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public BPos getPos() {
		return pos;
	}

	public boolean isIndexed() {
		return indexed;
	}

	public enum ChestType {
		SINGLE_CHEST(1),
		DOUBLE_CHEST(2),
		UNKNOWN(0);

		public static final int ITEMS_PER_ROW = 9;
		public static final int NUMBER_ROWS = 3;
		private int size;

		ChestType(int size) {
			this.size = size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public int getSize() {
			return size;
		}

		public int getNumberSlots() {
			return this.getNumberRows() * ITEMS_PER_ROW;
		}

		public int getNumberRows() {
			return this.size * NUMBER_ROWS;
		}
	}

	@Override public String toString() {
		return "ChestContent{" +
			"lootType=" + lootType +
			", chestType=" + chestType +
			", items=" + items +
			", pos=" + pos +
			", indexed=" + indexed +
			'}';
	}
}
