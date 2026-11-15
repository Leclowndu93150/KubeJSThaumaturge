package com.leclowndu93150.kubejs_thaumaturge.client;

import dev.latvian.mods.kubejs.client.LoadedTexture;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import net.minecraft.resources.ResourceLocation;

public final class TextureColorizer {
    private static final String TEXTURES_PREFIX = "textures/";
    private static final String PNG_SUFFIX = ".png";
    private static final int OPAQUE = 255;
    private static final float LIGHTNESS_EPSILON = 0.0001F;

    private TextureColorizer() {}

    public static void generate(
            KubeAssetGenerator generator, ResourceLocation target, ResourceLocation base, int rgb, boolean opaqueOnly) {
        generator.texture(spriteId(target), colorize(generator.loadTexture(spriteId(base)), rgb, opaqueOnly));
    }

    public static ResourceLocation spriteId(ResourceLocation texture) {
        String path = texture.getPath();
        if (path.startsWith(TEXTURES_PREFIX)) {
            path = path.substring(TEXTURES_PREFIX.length());
        }
        if (path.endsWith(PNG_SUFFIX)) {
            path = path.substring(0, path.length() - PNG_SUFFIX.length());
        }
        return texture.withPath(path);
    }

    public static LoadedTexture colorize(LoadedTexture base, int rgb, boolean opaqueOnly) {
        if (base.width <= 0 || base.height <= 0) {
            return base;
        }
        float weighted = 0.0F;
        float weights = 0.0F;
        for (int pixel : base.pixels) {
            int alpha = pixel >>> 24;
            if (affected(alpha, opaqueOnly)) {
                weighted += lightness(pixel) * alpha;
                weights += alpha;
            }
        }
        if (weights == 0.0F) {
            return base;
        }
        float baseLightness = Math.clamp(weighted / weights, LIGHTNESS_EPSILON, 1.0F - LIGHTNESS_EPSILON);
        float[] target = toHsl(rgb);
        int[] pixels = new int[base.pixels.length];
        for (int index = 0; index < pixels.length; index++) {
            int pixel = base.pixels[index];
            int alpha = pixel >>> 24;
            if (!affected(alpha, opaqueOnly)) {
                pixels[index] = pixel;
                continue;
            }
            float lightness = remap(lightness(pixel), baseLightness, target[2]);
            pixels[index] = alpha << 24 | fromHsl(target[0], target[1], lightness);
        }
        return new LoadedTexture(base.width, base.height, pixels, base.mcmeta);
    }

    private static boolean affected(int alpha, boolean opaqueOnly) {
        return opaqueOnly ? alpha == OPAQUE : alpha > 0;
    }

    private static float remap(float lightness, float baseLightness, float targetLightness) {
        float mapped = targetLightness >= baseLightness
                ? lightness + (1.0F - lightness) * (targetLightness - baseLightness) / (1.0F - baseLightness)
                : lightness * targetLightness / baseLightness;
        return Math.clamp(mapped, 0.0F, 1.0F);
    }

    private static float lightness(int pixel) {
        int red = pixel >> 16 & 0xFF;
        int green = pixel >> 8 & 0xFF;
        int blue = pixel & 0xFF;
        return (Math.max(red, Math.max(green, blue)) + Math.min(red, Math.min(green, blue))) / 510.0F;
    }

    private static float[] toHsl(int rgb) {
        float red = (rgb >> 16 & 0xFF) / 255.0F;
        float green = (rgb >> 8 & 0xFF) / 255.0F;
        float blue = (rgb & 0xFF) / 255.0F;
        float max = Math.max(red, Math.max(green, blue));
        float min = Math.min(red, Math.min(green, blue));
        float lightness = (max + min) / 2.0F;
        float delta = max - min;
        if (delta == 0.0F) {
            return new float[] {0.0F, 0.0F, lightness};
        }
        float saturation = lightness > 0.5F ? delta / (2.0F - max - min) : delta / (max + min);
        float hue;
        if (max == red) {
            hue = (green - blue) / delta + (green < blue ? 6.0F : 0.0F);
        } else if (max == green) {
            hue = (blue - red) / delta + 2.0F;
        } else {
            hue = (red - green) / delta + 4.0F;
        }
        return new float[] {hue / 6.0F, saturation, lightness};
    }

    private static int fromHsl(float hue, float saturation, float lightness) {
        if (saturation == 0.0F) {
            int grey = channel(lightness);
            return grey << 16 | grey << 8 | grey;
        }
        float q = lightness < 0.5F ? lightness * (1.0F + saturation) : lightness + saturation - lightness * saturation;
        float p = 2.0F * lightness - q;
        return channel(hueToRgb(p, q, hue + 1.0F / 3.0F)) << 16
                | channel(hueToRgb(p, q, hue)) << 8
                | channel(hueToRgb(p, q, hue - 1.0F / 3.0F));
    }

    private static float hueToRgb(float p, float q, float t) {
        if (t < 0.0F) {
            t += 1.0F;
        }
        if (t > 1.0F) {
            t -= 1.0F;
        }
        if (t < 1.0F / 6.0F) {
            return p + (q - p) * 6.0F * t;
        }
        if (t < 0.5F) {
            return q;
        }
        if (t < 2.0F / 3.0F) {
            return p + (q - p) * (2.0F / 3.0F - t) * 6.0F;
        }
        return p;
    }

    private static int channel(float value) {
        return Math.clamp(Math.round(value * 255.0F), 0, 255);
    }
}
