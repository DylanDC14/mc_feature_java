package com.seedfinding.mcfeature.loot;

import com.seedfinding.mcfeature.loot.item.Item;
import com.seedfinding.mcfeature.loot.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

public class LootChest {

	public final Map<Item, List<Stack>> stacksMap = new HashMap<>();
	public static BiPredicate<Integer, Integer> EQUAL_TO = Integer::equals;
	public static BiPredicate<Integer, Integer> NOT_EQUAL_TO = (a, b) -> !a.equals(b);
	public static BiPredicate<Integer, Integer> LESS_THAN = (a, b) -> a < b;
	public static BiPredicate<Integer, Integer> MORE_THAN = (a, b) -> a > b;
	public static BiPredicate<Integer, Integer> LESS_OR_EQUAL_TO = (a, b) -> a <= b;
	public static BiPredicate<Integer, Integer> MORE_OR_EQUAL_TO = (a, b) -> a >= b;

	public LootChest(LootChest.Stack... stacks) {
		for(Stack stack : stacks) {
			Item item = stack.item;

			if(!this.stacksMap.containsKey(item)) {
				this.stacksMap.put(item, new ArrayList<>());
			}

			this.stacksMap.get(item).add(stack);
		}
	}

	public static LootChest.Stack stack(Item item, BiPredicate<Integer, Integer> predicate, int amount) {
		return new LootChest.Stack(item, predicate, amount);
	}

	public boolean testLoot(long lootTableSeed, LootTable lootTable) {
		LootContext context = new LootContext(lootTableSeed);

		List<ItemStack> itemStacks = lootTable.generate(context);
		Set<Item> foundItems = new HashSet<>();

		for(ItemStack itemStack : itemStacks) {
			List<Stack> stacks = this.stacksMap.get(itemStack.getItem());
			if(stacks == null) continue;

			boolean matches = true;

			for(Stack stack : stacks) {
				if(!stack.test(itemStack.getCount())) {
					matches = false;
					break;
				}
			}

			if(matches) {
				foundItems.add(itemStack.getItem());
			}
		}

		return foundItems.size() == this.stacksMap.size();
	}

	public static class Stack {
		private final Item item;
		private final BiPredicate<Integer, Integer> predicate;
		private final int amount;

		public Stack(Item item, BiPredicate<Integer, Integer> predicate, int amount) {
			this.item = item;
			this.predicate = predicate;
			this.amount = amount;
		}

		public boolean test(int count) {
			return this.predicate.test(count, this.amount);
		}

		@Override
		public int hashCode() {
			return item.hashCode() * 31 + this.amount;
		}
	}

}
