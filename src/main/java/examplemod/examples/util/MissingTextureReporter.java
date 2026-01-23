package examplemod.examples.util;

import java.util.HashSet;

import necesse.engine.GameLog;
import necesse.engine.util.GameUtils;
import necesse.engine.window.GameWindow;
import necesse.engine.window.WindowManager;
import necesse.gfx.GameResources;
import necesse.gfx.Renderer;
import necesse.gfx.gameFont.FontOptions;
import necesse.engine.screenHudManager.ScreenFloatTextFade;
import necesse.gfx.gameTexture.GameTexture;

public class MissingTextureReporter {

    private static final HashSet<String> seen = new HashSet<>();
    private static GameTexture fallback;

    public static GameTexture reportMissingTexture(String filePath, boolean outsideGame, boolean forceNotFinalize, Throwable thrown) {
        // Normalize extension (matches vanilla)
        String normalized = filePath == null ? "null" : GameUtils.formatFileExtension(filePath, "png");

        // Find “who asked for this texture” (best effort)
        String caller = findUsefulCaller();

        // Log once per texture path (avoid spam)
        if (seen.add(normalized)) {
            GameLog.warn.println("[MissingTexture] Could not load: " + normalized
                    + " outsideGame=" + outsideGame
                    + " forceNotFinalize=" + forceNotFinalize
                    + (caller != null ? " caller=" + caller : "")
                    + " (" + thrown.getClass().getSimpleName() + ": " + thrown.getMessage() + ")");

            // On-screen notice (client HUD)
            showOnScreen("Missing texture: " + normalized);
        }

        // Return a safe texture so the game keeps running
        if (GameResources.error != null) return GameResources.error;
        return getOrCreateFallback();
    }

    private static GameTexture getOrCreateFallback() {
        if (fallback != null) return fallback;

        // 1x1 magenta fallback
        GameTexture t = new GameTexture("missing-texture-fallback", 1, 1);
        t.setPixel(0, 0, 255, 0, 255, 255);
        t.makeFinal();
        fallback = t;
        return fallback;
    }

    private static void showOnScreen(String msg) {
        try {
            GameWindow window = WindowManager.getWindow();
            if (window == null) return;

            int x = Math.max(1, window.getHudWidth() / 2);
            int y = 40;

            FontOptions opt = new FontOptions(16)
                    .color(255, 80, 80)
                    .outline(0, 0, 0);

            ScreenFloatTextFade text = new ScreenFloatTextFade(x, y, msg, opt);
            text.avoidOtherText = true;
            text.riseTime = 600;
            text.hoverTime = 400;
            text.fadeTime = 2200;

            Renderer.hudManager.addElement(text);
        } catch (Throwable ignored) {
            // Never crash from the reporter itself
        }
    }

    private static String findUsefulCaller() {
        try {
            StackTraceElement[] st = new Exception().getStackTrace();
            for (StackTraceElement e : st) {
                String c = e.getClassName();
                if (c == null) continue;

                // Skip obvious internal frames
                if (c.startsWith("net.bytebuddy.")) continue;
                if (c.startsWith("examplemod.examples.patches.")) continue;
                if (c.startsWith("examplemod.examples.util.MissingTextureReporter")) continue;
                if (c.startsWith("necesse.gfx.gameTexture.GameTexture")) continue;

                return c + "#" + e.getMethodName() + ":" + e.getLineNumber();
            }
        } catch (Throwable ignored) {
        }
        return null;
    }
}
