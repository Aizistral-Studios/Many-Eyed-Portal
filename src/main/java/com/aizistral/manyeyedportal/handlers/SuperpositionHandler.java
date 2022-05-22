package com.aizistral.manyeyedportal.handlers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

public class SuperpositionHandler {

	public static void registerCurioType(String identifier, int slots, boolean isHidden, @Nullable ResourceLocation icon) {
		SlotTypeMessage.Builder message = new SlotTypeMessage.Builder(identifier);

		message.size(slots);

		if (isHidden) {
			message.hide();
		}

		if (icon != null) {
			message.icon(icon);
		}

		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, message::build);
	}

	public static boolean hasCurio(LivingEntity entity, Item curio) {
		Optional<ImmutableTriple<String, Integer, ItemStack>> data = CuriosApi.getCuriosHelper().findEquippedCurio(curio, entity);
		return data.isPresent();
	}

}
