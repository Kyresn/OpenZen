package shit.zen.modules.impl.render;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import shit.zen.modules.Category;
import shit.zen.modules.Module;
import shit.zen.settings.impl.MultiSelectSetting;

public class XRay extends Module {
    public static XRay INSTANCE;

    public final MultiSelectSetting blocks = new MultiSelectSetting(
            "Blocks",
            "diamond_ore",
            "deepslate_diamond_ore",
            "gold_ore",
            "deepslate_gold_ore",
            "iron_ore",
            "deepslate_iron_ore",
            "coal_ore",
            "deepslate_coal_ore",
            "emerald_ore",
            "deepslate_emerald_ore",
            "lapis_ore",
            "deepslate_lapis_ore",
            "redstone_ore",
            "deepslate_redstone_ore",
            "copper_ore",
            "deepslate_copper_ore",
            "ancient_debris"
    ).withDefaults(
            "diamond_ore",
            "deepslate_diamond_ore",
            "gold_ore",
            "deepslate_gold_ore",
            "iron_ore",
            "deepslate_iron_ore",
            "coal_ore",
            "deepslate_coal_ore",
            "emerald_ore",
            "deepslate_emerald_ore",
            "lapis_ore",
            "deepslate_lapis_ore",
            "redstone_ore",
            "deepslate_redstone_ore",
            "copper_ore",
            "deepslate_copper_ore",
            "ancient_debris"
    );

    public XRay() {
        super("XRay", Category.RENDER);
        INSTANCE = this;
    }

    public boolean isTarget(Block block) {
        if (block == null) {
            return false;
        }
        return this.blocks.isSelected(BuiltInRegistries.BLOCK.getKey(block).getPath());
    }

    @Override
    protected void onEnable() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.levelRenderer != null) {
            minecraft.levelRenderer.allChanged();
        }
    }

    @Override
    protected void onDisable() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.levelRenderer != null) {
            minecraft.levelRenderer.allChanged();
        }
    }
}
