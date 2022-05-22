package com.aizistral.manyeyedportal.handlers;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static ForgeConfigSpec config;
	private static ForgeConfigSpec.BooleanValue eyeAbilitiesEnabled;
	private static ForgeConfigSpec.BooleanValue eyesRemovable;

	public static ForgeConfigSpec getConfig() {
		if (config == null) {
			ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

			builder.comment("Options that allow to disable/enable Univesal Clock and adjust it").push("Generic Config");

			eyeAbilitiesEnabled = builder
					.comment("Whether or not Universal Clock should be displayed in the HUD.")
					.define("clockHudEnabled", true);

			eyesRemovable = builder
					.comment("Whether or not eyes should be removable from End Portal Frames, allowing player to deactivate the portal.")
					.define("eyesRemovable", false);

			builder.pop();

			config = builder.build();
		}

		return config;
	}

	public static boolean areEyeAbilitiesEnabled() {
		return eyeAbilitiesEnabled.get();
	}

	public static boolean areEyesRemovable() {
		return eyesRemovable.get();
	}

}
