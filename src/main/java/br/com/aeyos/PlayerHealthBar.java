package br.com.aeyos;

import java.awt.Point;
import java.util.List;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

@ModEntry
@ModMethodPatch(target = Mob.class, name = "addDrawables", arguments = { List.class, OrderableDrawables.class,
        OrderableDrawables.class, OrderableDrawables.class, Level.class, TickManager.class, GameCamera.class,
        PlayerMob.class })
public class PlayerHealthBar {
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    static boolean onEnter(@Advice.This Mob mob, @Advice.Argument(3) OrderableDrawables overlayList,
            @Advice.Argument(4) Level level, @Advice.Argument(5) TickManager tickManager,
            @Advice.Argument(6) GameCamera camera,
            @Advice.Argument(7) PlayerMob perspective) {
        Point drawPos = mob.getDrawPos();
        if (mob == perspective) {
            mob.addStatusBarDrawable(overlayList, level, drawPos.x, drawPos.y, tickManager, camera, null);
        }
        return false;
    }
}
