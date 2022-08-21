package xyz.aikoyori.thematchmakers.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import xyz.aikoyori.thematchmakers.Thematchmakers;
import xyz.aikoyori.thematchmakers.items.MatchStickItem;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ThematchmakersClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(Thematchmakers.MATCHSTICK_ITEM, new Identifier("matchisburnt"),
                (stack, world, entity, seed) -> {
                    switch (MatchStickItem.getMatchState(stack)) {
                        case UNLIT -> {
                            return 0;
                        }
                        case BURNING -> {
                            return 0.5f;
                        }
                        case BURNT -> {
                            return 1;
                        }
                    }
                    return 0;


        });
    }
}