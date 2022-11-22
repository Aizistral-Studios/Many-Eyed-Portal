package com.aizistral.manyeyedportal.blocks;

import com.aizistral.manyeyedportal.ManyEyedPortal;
import com.aizistral.manyeyedportal.handlers.ConfigHandler;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class PortalFrameBlock extends EndPortalFrameBlock {
	private static BlockPattern portalShape;
	private final int index;

	public PortalFrameBlock(int index) {
		super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).sound(SoundType.GLASS)
				.lightLevel((state) -> 1).strength(-1.0F, 3600000.0F));
		this.index = index;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (ConfigHandler.areEyesRemovable())
			if (player.getItemInHand(hand).isEmpty() && state.hasProperty(HAS_EYE) && state.getValue(HAS_EYE)) {
				if (level.isClientSide)
					return InteractionResult.SUCCESS;

				BlockPattern.BlockPatternMatch match = PortalFrameBlock.getPortalShape()
						.find(level, pos);

				if (match != null) {
					BlockPos blockpos1 = match.getFrontTopLeft().offset(-3, 0, -3);

					for (int i = 0; i < 3; ++i) {
						for(int j = 0; j < 3; ++j) {
							BlockPos portalPos = blockpos1.offset(i, 0, j);

							if (level.getBlockState(portalPos).is(Blocks.END_PORTAL)) {
								level.setBlock(portalPos, Blocks.AIR.defaultBlockState(), 2);
							}
						}
					}

					level.playSound(null, blockpos1.offset(1, 0, 1), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 4, 1);
				}

				BlockState newState = state.setValue(PortalFrameBlock.HAS_EYE, false);
				Block.pushEntitiesUp(state, newState, level, pos);
				level.setBlock(pos, newState, 2);
				level.updateNeighbourForOutputSignal(pos, newState.getBlock());
				level.levelEvent(1503, pos, 0);

				player.setItemInHand(hand, new ItemStack(ManyEyedPortal.PORTAL_EYES[this.index-1]));

				return InteractionResult.SUCCESS;
			}

		return InteractionResult.PASS;
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
