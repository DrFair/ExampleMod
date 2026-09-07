package examplemod;

import necesse.engine.GameLog;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

public class ExampleModSettings extends ModSettings {

    // Your config values
    public boolean exampleBoolean = true;
    public int exampleInt = 1;
    public String exampleString = "Hello! from the config file ";

    @Override
    public void addSaveData(SaveData data) {
        // This is what gets written to cfg/mods/<modid>.cfg under SETTINGS { ... }
        data.addBoolean("exampleBoolean", exampleBoolean);
        data.addInt("exampleInt", exampleInt);
        data.addSafeString("exampleString", exampleString);
    }

    @Override
    public void applyLoadData(LoadData data) {
        // This is what gets read back from cfg/mods/<modid>.cfg
        exampleBoolean = data.getBoolean("exampleBoolean", exampleBoolean);
        exampleInt = data.getInt("exampleInt", exampleInt);
        // If print warning is false, it won't print a warning if the data is not found and the default value is used
        exampleString = data.getSafeString("exampleString", exampleString, false);
    }

    public void logLoadedSettings() {
        GameLog.out.println("[ExampleMod] Settings loaded:");
        GameLog.out.println("  exampleBoolean = " + exampleBoolean);
        GameLog.out.println("  exampleInt     = " + exampleInt);
        GameLog.out.println("  exampleString  = \"" + exampleString + "\"");
    }

}
