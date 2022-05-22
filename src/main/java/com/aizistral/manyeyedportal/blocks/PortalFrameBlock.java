package com.aizistral.manyeyedportal.blocks;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.aizistral.manyeyedportal.ManyEyedPortal;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class PortalFrameBlock extends EndPortalFrameBlock {
	private static BlockPattern portalShape;
	private final int index;

	public PortalFrameBlock(int index) {
		super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).sound(SoundType.GLASS)
				.lightLevel((state) -> 1).strength(-1.0F, 3600000.0F).noDrops());
		this.setRegistryName(new ResourceLocation(ManyEyedPortal.MODID, "portal_frame_" + index));
		this.index = index;
	}

	public static BlockPattern getPortalShape() {
		if (portalShape == null) {
			portalShape = BlockPatternBuilder.start().aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
					.where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))

					.where('^', BlockInWorld.hasState(new AnyFramePredicate()
							.where(HAS_EYE, Predicates.equalTo(true))
							.where(FACING, Predicates.equalTo(Direction.SOUTH))))

					.where('>', BlockInWorld.hasState(new AnyFramePredicate()
							.where(HAS_EYE, Predicates.equalTo(true))
							.where(FACING, Predicates.equalTo(Direction.WEST))))

					.where('v', BlockInWorld.hasState(new AnyFramePredicate()
							.where(HAS_EYE, Predicates.equalTo(true))
							.where(FACING, Predicates.equalTo(Direction.NORTH))))

					.where('<', BlockInWorld.hasState(new AnyFramePredicate()
							.where(HAS_EYE, Predicates.equalTo(true))
							.where(FACING, Predicates.equalTo(Direction.EAST))))

					.build();
		}

		return portalShape;
	}

	private static class AnyFramePredicate implements Predicate<BlockState> {
		private final Map<Property<?>, Predicate<Object>> properties = Maps.newHashMap();

		private AnyFramePredicate() {
			// NO-OP
		}

		@Override
		public boolean test(@Nullable BlockState state) {
			if (state != null && state.getBlock() instanceof PortalFrameBlock) {
				if (this.properties.isEmpty())
					return true;
				else {
					for(Entry<Property<?>, Predicate<Object>> entry : this.properties.entrySet()) {
						if (!this.applies(state, entry.getKey(), entry.getValue()))
							return false;
					}

					return true;
				}
			} else
				return false;
		}

		protected <T extends Comparable<T>> boolean applies(BlockState state, Property<T> property, Predicate<Object> predicate) {
			T t = state.getValue(property);
			return predicate.test(t);
		}

		public <V extends Comparable<V>> AnyFramePredicate where(Property<V> property, Predicate<Object> predicate) {
			this.properties.put(property, predicate);
			return this;
		}
	}

}
