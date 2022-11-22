package com.aizistral.manyeyedportal;

import com.aizistral.manyeyedportal.blocks.PortalFrameBlock;
import com.aizistral.manyeyedportal.handlers.ConfigHandler;
import com.aizistral.manyeyedportal.handlers.CuriosCompatHandler;
import com.aizistral.manyeyedportal.items.PortalEyeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(ManyEyedPortal.MODID)
public class ManyEyedPortal {
	public static final String MODID = "manyeyedportal";

	public static final PortalEyeItem[] PORTAL_EYES = new PortalEyeItem[12];
	public static final PortalFrameBlock[] FRAME_BLOCKS = new PortalFrameBlock[12];
	public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab("manyEyedPortalTab") {

		@Override
		public ItemStack makeIcon() {
			return new ItemStack(PORTAL_EYES[0]);
		}
	};

	public ManyEyedPortal() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);


		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.getConfig(), "ManyEyedPortal.toml");
	}


	private void enqueueIMC(InterModEnqueueEvent event) {
		CuriosCompatHandler.registerCurioType("charm", 1, false, null);
	}


	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
	public static class RegistryEvents {


		@SubscribeEvent
		public static void registerBlocks(RegisterEvent event) {

			if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
				for (int i = 0; i < FRAME_BLOCKS.length; i++) {
					int finalIndex = i;
					event.register(ForgeRegistries.Keys.BLOCKS, new ResourceLocation(ManyEyedPortal.MODID, "portal_frame_" + i), () -> FRAME_BLOCKS[finalIndex] = new PortalFrameBlock(finalIndex + 1));
				}
			}
			if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
				for (int i = 0; i < FRAME_BLOCKS.length; i++) {
					final int finalIndex = i;
					event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(MODID, "portal_frame_" + (i + 1)), () -> new BlockItem(FRAME_BLOCKS[finalIndex], new Item.Properties()
							.rarity(Rarity.EPIC).stacksTo(64).fireResistant().tab(ManyEyedPortal.CREATIVE_TAB)));
				}

				for (int i = 1; i < PORTAL_EYES.length + 1; i++) {
					final int index = i;
					event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(ManyEyedPortal.MODID, "portal_eye_" + i), () -> PORTAL_EYES[index - 1] = new PortalEyeItem(index));
				}
			}
		}

	}

}
