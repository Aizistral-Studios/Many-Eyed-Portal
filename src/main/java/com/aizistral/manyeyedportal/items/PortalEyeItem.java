package com.aizistral.manyeyedportal.items;

import java.util.Random;
import java.util.UUID;

import com.aizistral.manyeyedportal.ManyEyedPortal;
import com.aizistral.manyeyedportal.blocks.PortalFrameBlock;
import com.aizistral.manyeyedportal.handlers.ConfigHandler;
import com.aizistral.manyeyedportal.handlers.SuperpositionHandler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class PortalEyeItem extends Item implements ICurioItem {
	private final int index;
	private final UUID modifierID;

	public PortalEyeItem(int index) {
		super(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant().tab(CreativeModeTab.TAB_MISC));
		this.setRegistryName(new ResourceLocation(ManyEyedPortal.MODID, "portal_eye_" + index));
		this.index = index;
		this.modifierID = Mth.createInsecureUUID(new Random(index * 16 + index * 4 + index));
	}

	public int getIndex() {
		return this.index;
	}

	@Override
	public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
		return false;
	}

	@Override
	public boolean canEquip(SlotContext slotContext, ItemStack stack) {
		return !SuperpositionHandler.hasCurio(slotContext.entity(), this)
				&& ICurioItem.super.canEquip(slotContext, stack);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();

		if (ConfigHandler.areEyeAbilitiesEnabled()) {
			CuriosApi.getCuriosHelper().addSlotModifier(attributes, "charm", this.modifierID, 1, Operation.ADDITION);
		}

		return attributes;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = level.getBlockState(blockpos);
		if (blockstate.getBlock() == ManyEyedPortal.FRAME_BLOCKS[this.index-1] && !blockstate.getValue(PortalFrameBlock.HAS_EYE)) {
			if (level.isClientSide)
				return InteractionResult.SUCCESS;
			else {
				BlockState newState = blockstate.setValue(PortalFrameBlock.HAS_EYE, Boolean.valueOf(true));
				Block.pushEntitiesUp(blockstate, newState, level, blockpos);
				level.setBlock(blockpos, newState, 2);
				level.updateNeighbourForOutputSignal(blockpos, newState.getBlock());
				context.getItemInHand().shrink(1);
				level.levelEvent(1503, blockpos, 0);
				BlockPattern.BlockPatternMatch match = PortalFrameBlock.getPortalShape()
						.find(level, blockpos);

				if (match != null) {
					BlockPos blockpos1 = match.getFrontTopLeft().offset(-3, 0, -3);

					for (int i = 0; i < 3; ++i) {
						for(int j = 0; j < 3; ++j) {
							level.setBlock(blockpos1.offset(i, 0, j), Blocks.END_PORTAL.defaultBlockState(), 2);
						}
					}

					level.globalLevelEvent(1038, blockpos1.offset(1, 0, 1), 0);
				}

				return InteractionResult.CONSUME;
			}
		} else
			return InteractionResult.PASS;
	}

}
