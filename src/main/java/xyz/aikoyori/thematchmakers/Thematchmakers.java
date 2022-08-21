package xyz.aikoyori.thematchmakers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.aikoyori.thematchmakers.items.MatchStickItem;

public class Thematchmakers implements ModInitializer {
    public static String MOD_ID = "thematchmakers";
    public static final MatchStickItem MATCHSTICK_ITEM = new MatchStickItem(new FabricItemSettings().group(ItemGroup.TOOLS));
    public static final Item MATCHBOX = new Item(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1));
    @Override
    public void onInitialize() {

        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"matchstick"),MATCHSTICK_ITEM);
        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"matchbox"),MATCHBOX);
    }
}
