package org.hxcn.excnies.sneakwalk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

/**
 * Client-only configuration, read from {@code config/sneakwalk.properties}.
 * Java 8 compatible so the same class can be used by the 1.8.9/1.12.2 builds.
 */
public final class SneakWalkConfig {
    public enum Mode {
        /** GermMod / HuaYuTing custom bone amplitudes. */
        GERM,
        /** EaseCation ViaBedrockUtility / Bedrock player.animation.json base pose. */
        BEDROCK,
        /** Do not touch the model. */
        VANILLA
    }

    public enum Trigger {
        /** Only while the player is crouching. */
        SNEAKING,
        /** Every third-person player model tick. */
        ALWAYS
    }

    public enum SneakCamera {
        /** Native per-version smoothing. */
        SMOOTH,
        /** Native eye height, but no interpolation (HeyPixel disableSneakAnimation). */
        SNAP,
        /** 1.8 eye height 1.54/1.62 with immediate movement (kkcraft legacySneakEyeHeight). */
        LEGACY18
    }

    private static final String DEFAULT_PATH = "config/sneakwalk.properties";
    private static volatile boolean loaded;

    public static volatile boolean enabled = true;
    public static volatile Mode mode = Mode.GERM;
    public static volatile Trigger trigger = Trigger.SNEAKING;
    public static volatile boolean swapSides = false;
    public static volatile boolean affectLegs = true;
    public static volatile boolean affectArms = true;
    public static volatile SneakCamera sneakCamera = SneakCamera.SMOOTH;
    public static volatile boolean legacySneakPose = false;
    public static volatile boolean legacySwing = false;
    public static volatile boolean legacySwordBlock = false;

    private SneakWalkConfig() {
    }

    public static void ensureLoaded() {
        if (!loaded) {
            load();
        }
    }

    public static synchronized void load() {
        loaded = true;
        File file = new File(System.getProperty("sneakwalk.config", DEFAULT_PATH));
        Properties properties = new Properties();
        if (file.isFile()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8))) {
                properties.load(reader);
            } catch (IOException ignored) {
                // Keep defaults; a broken config must never crash the client.
            }
        } else {
            properties.setProperty("enabled", "true");
            properties.setProperty("mode", "germ");
            properties.setProperty("trigger", "sneaking");
            properties.setProperty("swapSides", "false");
            properties.setProperty("affectLegs", "true");
            properties.setProperty("affectArms", "true");
            properties.setProperty("sneakCamera", "smooth");
            properties.setProperty("legacySneakPose", "false");
            properties.setProperty("legacySwing", "false");
            properties.setProperty("legacySwordBlock", "false");
            writeDefaults(file, properties);
        }

        enabled = bool(properties, "enabled", true);
        mode = parseMode(properties.getProperty("mode", "germ"));
        trigger = parseTrigger(properties.getProperty("trigger", "sneaking"));
        swapSides = bool(properties, "swapSides", false);
        affectLegs = bool(properties, "affectLegs", true);
        affectArms = bool(properties, "affectArms", true);
        sneakCamera = parseSneakCamera(properties.getProperty("sneakCamera", "smooth"));
        legacySneakPose = bool(properties, "legacySneakPose", false);
        legacySwing = bool(properties, "legacySwing", false);
        legacySwordBlock = bool(properties, "legacySwordBlock", false);
    }

    private static void writeDefaults(File file, Properties properties) {
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            properties.store(writer, "SneakWalk - client-side third-person crouch-walk pose");
        } catch (IOException ignored) {
            // The mod still works with in-memory defaults.
        }
    }

    private static boolean bool(Properties properties, String key, boolean fallback) {
        String value = properties.getProperty(key);
        return value == null ? fallback : Boolean.parseBoolean(value.trim());
    }

    private static Mode parseMode(String value) {
        String normalized = value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
        if ("BEDROCK".equals(normalized) || "VBU".equals(normalized) || "EC".equals(normalized)) {
            return Mode.BEDROCK;
        }
        if ("VANILLA".equals(normalized) || "OFF".equals(normalized) || "NONE".equals(normalized)) {
            return Mode.VANILLA;
        }
        return Mode.GERM;
    }

    private static Trigger parseTrigger(String value) {
        String normalized = value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
        return "ALWAYS".equals(normalized) ? Trigger.ALWAYS : Trigger.SNEAKING;
    }

    private static SneakCamera parseSneakCamera(String value) {
        String normalized = value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
        if ("SNAP".equals(normalized) || "DISABLE".equals(normalized) || "OFF".equals(normalized)) {
            return SneakCamera.SNAP;
        }
        if ("LEGACY18".equals(normalized) || "LEGACY".equals(normalized) || "OLD".equals(normalized)) {
            return SneakCamera.LEGACY18;
        }
        return SneakCamera.SMOOTH;
    }

    /** Returns true when the pose override should run for this model tick. */
    public static boolean shouldApply(boolean crouching) {
        ensureLoaded();
        if (!enabled || mode == Mode.VANILLA) {
            return false;
        }
        return trigger == Trigger.ALWAYS || crouching;
    }

    public static boolean shouldApplyLegacySneakPose(boolean crouching) {
        ensureLoaded();
        return enabled && legacySneakPose && crouching;
    }

    public static boolean shouldUseLegacySwing() {
        ensureLoaded();
        return enabled && legacySwing;
    }

    public static boolean shouldUseLegacySwordBlock() {
        ensureLoaded();
        return enabled && legacySwordBlock;
    }

    public static boolean shouldUseLegacySneakCamera() {
        ensureLoaded();
        return enabled && sneakCamera != SneakCamera.SMOOTH;
    }
}
