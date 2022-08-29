package xyz.aikoyori.thematchmakers.compat;

import dev.lambdaurora.lambdynlights.api.DynamicLightHandler;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSource;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSources;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import xyz.aikoyori.thematchmakers.Thematchmakers;
import xyz.aikoyori.thematchmakers.items.MatchStickItem;

public class LambDynamicLightsCompat implements DynamicLightsInitializer {
    @Override
    public void onInitializeDynamicLights() {

        ItemLightSources.registerItemLightSource(new ItemLightSource(new Identifier(Thematchmakers.MOD_ID,"matchstick"),Thematchmakers.MATCHSTICK_ITEM,true) {
            @Override
            public int getLuminance(ItemStack itemStack) {
                if(!(itemStack.getItem() instanceof MatchStickItem)) return 0;
                switch (MatchStickItem.getMatchState(itemStack))
                {

                    case UNLIT, BURNT -> {
                        return 0;
                    }
                    case BURNING -> {
                        int burnTimeLeft = itemStack.getOrCreateNbt().getInt("burnTime");
                        return (int) (9 + (burnTimeLeft/(MatchStickItem.MAX_BURN_TIME*1.0))*7);
                    }
                }
                return 0;
            }
        });

    }

}
