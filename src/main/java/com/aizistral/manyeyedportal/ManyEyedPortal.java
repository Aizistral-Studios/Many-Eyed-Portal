package com.aizistral.manyeyedportal;

import com.aizistral.manyeyedportal.blocks.PortalFrameBlock;
import com.aizistral.manyeyedportal.handlers.ConfigHandler;
import com.aizistral.manyeyedportal.handlers.PortalEventHandler;
import com.aizistral.manyeyedportal.handlers.SuperpositionHandler;
import com.aizistral.manyeyedportal.items.PortalEyeItem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ManyEyedPortal.MODID)
public class ManyEyedPortal {
	public static final String MODID = "manyeyedportal";

	public static final PortalEyeItem[] PORTAL_EYES = new PortalEyeItem[12];
	public static final PortalFrameBlock[] FRAME_BLOCKS = new PortalFrameBlock[12];
	public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab("manyEyedPortalTab") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(PORTAL_EYES[0]);
		}
	};

	public ManyEyedPortal() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new PortalEventHandler());

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.getConfig(), "ManyEyedPortal.toml");
	}

	private void onLoadComplete(FMLLoadCompleteEvent event) {
		// NO-OP
	}

	private void setup(FMLCommonSetupEvent event) {
		// NO-OP
	}

	private void doClientStuff(FMLClientSetupEvent event) {
		// NO-OP
	}

	private void enqueueIMC(InterModEnqueueEvent event) {
		SuperpositionHandler.registerCurioType("charm", 1, false, null);
	}

	private void processIMC(InterModProcessEvent event) {
		// NO-OP
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			for (int i = 0; i < FRAME_BLOCKS.length; i++) {
				event.getRegistry().register(FRAME_BLOCKS[i] = new PortalFrameBlock(i + 1));
			}
		}

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			for (int i = 0; i < FRAME_BLOCKS.length; i++) {
				event.getRegistry().register(new BlockItem(FRAME_BLOCKS[i], new Item.Properties()
						.rarity(Rarity.EPIC).stacksTo(64).fireResistant().tab(ManyEyedPortal.CREATIVE_TAB))
						.setRegistryName(MODID, "portal_frame_" + (i + 1)));
			}

			for (int i = 0; i < PORTAL_EYES.length; i++) {
				event.getRegistry().register(PORTAL_EYES[i] = new PortalEyeItem(i + 1));
			}
		}

	}

}
