package net.danygames2014.spawneggs;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.danygames2014.spawneggs.item.SpawnEggItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;

import java.awt.image.BufferedImage;
import java.util.Random;

public class ColorUtil {
    /**
     * Calculates Delta E 2000 between two colors
     *
     * @param color1 The first color (e.g. 0x007D00)
     * @param color2 The second color (e.g. 0xFF5500)
     */
    public static double getDeltaE2000(int color1, int color2) {
        double[] lab1 = intToLab(color1 & 0xFFFFFF);
        double[] lab2 = intToLab(color2 & 0xFFFFFF);

        double L1 = lab1[0], a1 = lab1[1], b1 = lab1[2];
        double L2 = lab2[0], a2 = lab2[1], b2 = lab2[2];

        double avgL = (L1 + L2) / 2.0;
        double C1 = Math.sqrt(a1 * a1 + b1 * b1);
        double C2 = Math.sqrt(a2 * a2 + b2 * b2);
        double avgC = (C1 + C2) / 2.0;

        double G = 0.5 * (1 - Math.sqrt(Math.pow(avgC, 7) / (Math.pow(avgC, 7) + Math.pow(25, 7))));
        double a1p = (1 + G) * a1;
        double a2p = (1 + G) * a2;

        double C1p = Math.sqrt(a1p * a1p + b1 * b1);
        double C2p = Math.sqrt(a2p * a2p + b2 * b2);
        double avgCp = (C1p + C2p) / 2.0;

        double h1p = Math.toDegrees(Math.atan2(b1, a1p));
        if (h1p < 0) h1p += 360;
        double h2p = Math.toDegrees(Math.atan2(b2, a2p));
        if (h2p < 0) h2p += 360;

        double avgHp;
        if (Math.abs(h1p - h2p) > 180) {
            avgHp = (h1p + h2p + 360) / 2.0;
        } else {
            avgHp = (h1p + h2p) / 2.0;
        }
        if (avgHp >= 360) avgHp -= 360;

        double T = 1 - 0.17 * Math.cos(Math.toRadians(avgHp - 30)) + 0.24 * Math.cos(Math.toRadians(2 * avgHp)) +
                0.32 * Math.cos(Math.toRadians(3 * avgHp + 6)) - 0.20 * Math.cos(Math.toRadians(4 * avgHp - 63));

        double diffHp = h2p - h1p;
        if (Math.abs(diffHp) > 180) {
            if (h2p <= h1p) diffHp += 360;
            else diffHp -= 360;
        }

        double deltaLp = L2 - L1;
        double deltaCp = C2p - C1p;
        double deltaHp = 2 * Math.sqrt(C1p * C2p) * Math.sin(Math.toRadians(diffHp / 2.0));

        double Sl = 1 + (0.015 * Math.pow(avgL - 50, 2)) / Math.sqrt(20 + Math.pow(avgL - 50, 2));
        double Sc = 1 + 0.045 * avgCp;
        double Sh = 1 + 0.015 * avgCp * T;

        double deltaRo = 30 * Math.exp(-Math.pow((avgHp - 275) / 25, 2));
        double Rc = 2 * Math.sqrt(Math.pow(avgCp, 7) / (Math.pow(avgCp, 7) + Math.pow(25, 7)));
        double Rt = -Math.sin(Math.toRadians(2 * deltaRo)) * Rc;

        return Math.sqrt(Math.pow(deltaLp / Sl, 2) + Math.pow(deltaCp / Sc, 2) + Math.pow(deltaHp / Sh, 2) + Rt * (deltaCp / Sc) * (deltaHp / Sh));
    }

    /**
     * Converts an color represented by an integer to the LAB color space
     *
     * @param color The color to convert
     * @return The same color in LAB color space
     */
    public static double[] intToLab(int color) {
        // 1. Extract RGB using bitmasking
        int rInt = (color >> 16) & 0xFF;
        int gInt = (color >> 8) & 0xFF;
        int bInt = color & 0xFF;

        // 2. Normalize to 0.0 - 1.0
        double r = rInt / 255.0;
        double g = gInt / 255.0;
        double b = bInt / 255.0;

        // 3. Linearize (sRGB Inverse Gamma)
        r = (r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92;
        g = (g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92;
        b = (b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92;

        // 4. Convert to XYZ (D65 Illuminant)
        double x = (r * 0.4124 + g * 0.3576 + b * 0.1805) / 0.95047;
        double y = (r * 0.2126 + g * 0.7152 + b * 0.0722);
        double z = (r * 0.0193 + g * 0.1192 + b * 0.9505) / 1.08883;

        // 5. Convert XYZ to LAB
        x = (x > 0.008856) ? Math.pow(x, 1.0 / 3.0) : (7.787 * x) + (16.0 / 116.0);
        y = (y > 0.008856) ? Math.pow(y, 1.0 / 3.0) : (7.787 * y) + (16.0 / 116.0);
        z = (z > 0.008856) ? Math.pow(z, 1.0 / 3.0) : (7.787 * z) + (16.0 / 116.0);

        return new double[]{
                (116.0 * y) - 16.0,  // L
                500.0 * (x - y),      // a
                200.0 * (y - z)       // b
        };
    }

    public static boolean calculateEggColorFromTexture(SpawnEggItem item) {
        Entity entity = null;

        try {
            //noinspection unchecked
            Class<Entity> var3 = (Class<Entity>) EntityRegistry.idToClass.get(item.spawnedEntity);
            if (var3 != null) {
                entity = var3.getConstructor(World.class).newInstance((World) null);
            }

            if (entity == null) {
                return false;
            }

            String textureId = entity.getTexture();
            if (textureId != null && !textureId.contains("/mob/char.png") && !textureId.isBlank()) {
                BufferedImage texture = TextureHelper.getTexture(textureId);

                if (texture == null) {
                    return false;
                }

                EggColors colors = extractColors(texture, 5, 10);

                ColorizationHandler.registerSpawnEggColorInt(
                        item.spawnedEntity,
                        colors.outerOverlay,
                        colors.innerLayer,
                        colors.innerLayerOverlay
                );

                return true;
            }
        } catch (Exception ignored) {
            return false;
        }

        return false;
    }

    public static EggColors extractColors(BufferedImage texture, int k, int maxIterations) {
        int width = texture.getWidth();
        int height = texture.getHeight();
        int[] rawPixels = new int[width * height];
        texture.getRGB(0, 0, width, height, rawPixels, 0, width);

        // Filter alpha and ultra-black shading
        IntArrayList validPixels = new IntArrayList();
        for (int colorInt : rawPixels) {
            int a = (colorInt >> 24) & 0xFF;
            int r = (colorInt >> 16) & 0xFF;
            int g = (colorInt >> 8) & 0xFF;
            int b = colorInt & 0xFF;

            if (a >= 128 && (r + g + b) >= 20) {
                validPixels.add((r << 16) | (g << 8) | b);
            }
        }

        if (validPixels.isEmpty()) {
            return new EggColors(0xA1A1A1, 0x444444);
        }

        int actualK = Math.min(k, validPixels.size());
        ObjectArrayList<Cluster> clusters = new ObjectArrayList<>();
        Random rand = new Random(42);

        for (int i = 0; i < actualK; i++) {
            int rgb = validPixels.getInt(rand.nextInt(validPixels.size()));
            clusters.add(new Cluster(rgb));
        }

        // K-Means Iteration
        for (int iter = 0; iter < maxIterations; iter++) {
            for (Cluster c : clusters) c.clear();

            for (int rgb : validPixels) {
                Cluster nearest = null;
                double minDeltaE = Double.MAX_VALUE;

                for (Cluster c : clusters) {
                    double de = getDeltaE2000(rgb, c.centerRgb);
                    if (de < minDeltaE) {
                        minDeltaE = de;
                        nearest = c;
                    }
                }
                if (nearest != null) nearest.assignedPixels.add(rgb);
            }

            boolean shifted = false;
            for (Cluster c : clusters) {
                int oldCenter = c.centerRgb;
                c.updateCenter();
                if (getDeltaE2000(oldCenter, c.centerRgb) > 0.5) {
                    shifted = true;
                }
            }
            if (!shifted) break;
        }

        clusters.sort((c1, c2) -> Integer.compare(c2.pixelCount, c1.pixelCount));

        Cluster primary = clusters.get(0);
        Cluster secondary = null;

        // Find secondary cluster with sufficient DeltaE contrast
        double minDeltaEThreshold = 14.0;
        double maxScore = -1;

        for (int i = 1; i < clusters.size(); i++) {
            Cluster c = clusters.get(i);
            if (c.pixelCount == 0) continue;

            double de = getDeltaE2000(primary.centerRgb, c.centerRgb);
            if (de >= minDeltaEThreshold) {
                double score = c.pixelCount * de;
                if (score > maxScore) {
                    maxScore = score;
                    secondary = c;
                }
            }
        }

        int primaryRgb = primary.centerRgb;
        int secondaryRgb;

        if (secondary != null) {
            secondaryRgb = secondary.centerRgb;
        } else if (clusters.size() > 1 && clusters.get(1).pixelCount > 0) {
            // Fallback: Use second most frequent cluster even if DeltaE is small
            secondaryRgb = clusters.get(1).centerRgb;
        } else {
            // Monochromatic Fallback (e.g., Skeleton/Sheep):
            // If primary is light, make spots darker. If primary is dark, make spots lighter.
            double lightness = getLightness(primaryRgb);
            if (lightness > 128) {
                secondaryRgb = darken(primaryRgb, 0.6f);
            } else {
                secondaryRgb = lighten(primaryRgb, 1.5f);
            }
        }

        return new EggColors(primaryRgb, secondaryRgb);
    }

    private static double getLightness(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return (0.299 * r + 0.587 * g + 0.114 * b);
    }

    @SuppressWarnings("SameParameterValue")
    private static int lighten(int rgb, float factor) {
        int r = Math.min(255, (int) (((rgb >> 16) & 0xFF) * factor));
        int g = Math.min(255, (int) (((rgb >> 8) & 0xFF) * factor));
        int b = Math.min(255, (int) ((rgb & 0xFF) * factor));
        return (r << 16) | (g << 8) | b;
    }

    public static int darken(int rgb, double factor) {
        int r = (int) (((rgb >> 16) & 0xFF) * factor);
        int g = (int) (((rgb >> 8) & 0xFF) * factor);
        int b = (int) ((rgb & 0xFF) * factor);
        return (r << 16) | (g << 8) | b;
    }

    private static class Cluster {
        int centerRgb;
        int pixelCount;
        IntArrayList assignedPixels = new IntArrayList();

        Cluster(int rgb) {
            this.centerRgb = rgb & 0xFFFFFF;
        }

        void clear() {
            assignedPixels.clear();
        }

        void updateCenter() {
            if (assignedPixels.isEmpty()) return;

            long sumR = 0, sumG = 0, sumB = 0;
            for (int rgb : assignedPixels) {
                sumR += (rgb >> 16) & 0xFF;
                sumG += (rgb >> 8) & 0xFF;
                sumB += rgb & 0xFF;
            }

            pixelCount = assignedPixels.size();
            int r = (int) (sumR / pixelCount);
            int g = (int) (sumG / pixelCount);
            int b = (int) (sumB / pixelCount);

            this.centerRgb = (r << 16) | (g << 8) | b;
        }
    }

    public static class EggColors {
        public final int innerLayer;
        public final int innerLayerOverlay;
        public final int outerOverlay;

        public EggColors(int innerLayer, int innerLayerOverlay) {
            this.innerLayer = innerLayer;
            this.innerLayerOverlay = innerLayerOverlay;
            this.outerOverlay = ColorUtil.darken(innerLayer, 0.35f);
        }
    }
}
