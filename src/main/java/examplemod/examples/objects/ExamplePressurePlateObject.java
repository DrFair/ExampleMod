package examplemod.examples.objects;

import necesse.level.gameObject.MaskedPressurePlateObject;

import java.awt.*;

public class ExamplePressurePlateObject extends MaskedPressurePlateObject {

    public ExamplePressurePlateObject() {
        super(
                "pressureplatemask", // Texture mask name
                "exampletile", // Tile texture name
                new Color(120, 80, 200) // Minimap color
        );

        // MaskedPressurePlateObject sets the important flags internally (including isPressurePlate)
        // and uses a default trigger hitbox through its object entity.
    }

}