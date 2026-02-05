package examplemod.examples.mobs;

import java.awt.*;
import java.util.List;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.MobDrawable;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.ai.behaviourTree.trees.PlayerFollowerCollisionChaserAI;
import necesse.entity.mobs.summon.summonFollowingMob.attackingFollowingMob.AttackingFollowingMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

public class ExampleSummonWeaponMob extends AttackingFollowingMob {

    // Loaded in examplemod.ExampleMod.initResources()
    public static GameTexture texture;

    public ExampleSummonWeaponMob() {
        super(20);                 // health
        setSpeed(60.0F);
        setFriction(2.0F);
        this.attackCooldown = 500;

        this.collision = new Rectangle(-10, -7, 20, 14);
        this.hitBox     = new Rectangle(-12, -14, 24, 24);
        this.selectBox  = new Rectangle(-13, -14, 26, 24);
    }

    @Override
    public void init() {
        super.init();

        // Range, damage, knockback, cooldown etc.
        // This uses this.summonDamage which SummonToolItem injects at spawn time.
        this.ai = new BehaviourTreeAI<>(this,
                new PlayerFollowerCollisionChaserAI<>(
                        576,            // target range
                        this.summonDamage,
                        30,             // knockback
                        500,            // attack windup?
                        640,            // chase range
                        64              // give up / pathing distance
                )
        );
    }

    @Override
    protected void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);
        // Tile positions are basically level positions divided by 32. getTileX() does this for us etc.
        GameLight light = level.getLightLevel(getTileX(), getTileY());
        int drawX = camera.getDrawX(x) - 32;
        int drawY = camera.getDrawY(y) - 51;

        // A helper method to get the sprite of the current animation/direction of this mob
        Point sprite = getAnimSprite(x, y, getDir());

        drawY += getBobbing(x, y);
        drawY += getLevel().getTile(getTileX(), getTileY()).getMobSinkingAmount(this);

        DrawOptions drawOptions = texture.initDraw()
                .sprite(sprite.x, sprite.y, 64)
                .light(light)
                .pos(drawX, drawY);

        list.add(new MobDrawable() {
            @Override
            public void draw(TickManager tickManager) {
                drawOptions.draw();
            }
        });

        addShadowDrawables(tileList, level, x, y, light, camera);
    }

    @Override
    public int getRockSpeed() {
        // Change the speed at which this mobs animation plays
        return 20;
    }
}