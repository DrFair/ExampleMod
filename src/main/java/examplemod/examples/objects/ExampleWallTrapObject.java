package examplemod.examples.objects;

import examplemod.examples.objectentity.ExampleTrapObjectEntity;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.gameObject.WallObject;
import necesse.level.gameObject.WallTrapObject;
import necesse.level.maps.Level;

/*
 * A wall trap you can place in the world.
 * It uses "examplearrowtrap" as its texture name.
 */
public class ExampleWallTrapObject extends WallTrapObject {

    public ExampleWallTrapObject(WallObject wallObject) {
        // Tells the game which texture to use (objects/examplearrowtrap.png)
        super(wallObject, "examplewalltrap");
    }

    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        // Creates the object entity that handles the trap behavior.
        return new ExampleTrapObjectEntity(level, x, y);
    }

}