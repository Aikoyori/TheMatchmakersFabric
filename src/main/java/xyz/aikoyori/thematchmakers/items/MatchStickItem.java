package xyz.aikoyori.thematchmakers.items;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import xyz.aikoyori.thematchmakers.Thematchmakers;

public class MatchStickItem extends Item {
    public static int MAX_BURN_TIME = 200;

    public MatchStickItem(Settings settings) {
        super(settings);
    }
    public static MatchState getMatchState(ItemStack stack)
    {
        boolean isBurnt = stack.getOrCreateNbt().getBoolean("isBurnt");
        int burnTimeLeft = stack.getOrCreateNbt().getInt("burnTime");
        if(isBurnt && burnTimeLeft <= 0) return MatchState.BURNT;
        if(burnTimeLeft>0) return MatchState.BURNING;
        return MatchState.UNLIT;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("burnTime") > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int burnTimeLeft = stack.getOrCreateNbt().getInt("burnTime");
        return Math.round((float)burnTimeLeft * 13.0F / (float)this.MAX_BURN_TIME);
    }

    @Override
    public Text getName(ItemStack stack) {
        String txt = "";
        switch (getMatchState(stack))
        {

            case UNLIT -> {

            }
            case BURNING -> {
                txt="_burning";
            }
            case BURNT -> {
                txt="_burnt";
            }
        }
        return Text.translatable(this.getTranslationKey(stack)+txt);
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        int burnTimeLeft = stack.getOrCreateNbt().getInt("burnTime");
        if(burnTimeLeft>0)
        {
            burnTimeLeft--;
            stack.getOrCreateNbt().putInt("burnTime",burnTimeLeft);
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return super.getItemBarColor(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getPlayer().getStackInHand(context.getHand());
        ItemStack otherStack = context.getPlayer().getStackInHand(context.getHand()== Hand.MAIN_HAND?Hand.OFF_HAND:Hand.MAIN_HAND);
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        boolean bl = false;

            switch (getMatchState(stack)) {
                case UNLIT -> {
                    bl = false;
                }
                case BURNING -> {
                    bl = true;
                }
                case BURNT -> {
                    bl = false;
                }
            }

            if(bl)
            {
                if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
                    blockPos = blockPos.offset(context.getSide());
                    if (AbstractFireBlock.canPlaceAt(world, blockPos, context.getPlayerFacing())) {
                        this.playUseSound(world, blockPos);
                        world.setBlockState(blockPos, AbstractFireBlock.getState(world, blockPos));
                        world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, blockPos);
                        bl = true;
                    }
                } else {
                    this.playUseSound(world, blockPos);
                    world.setBlockState(blockPos, (BlockState)blockState.with(Properties.LIT, true));
                    world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);

                }
                return ActionResult.success(world.isClient);
            }

        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);
        ItemStack otherStack = user.getStackInHand(hand == Hand.MAIN_HAND?Hand.OFF_HAND:Hand.MAIN_HAND);

        boolean bl = false;
        if(otherStack.getItem()== Thematchmakers.MATCHBOX)
        {

            switch (getMatchState(stack)) {
                case UNLIT -> {
                    ItemStack stack2 = new ItemStack(stack.getItem());

                    stack2.getOrCreateNbt().putBoolean("isBurnt",true);
                    stack2.getOrCreateNbt().putInt("burnTime",MAX_BURN_TIME);
                    user.giveItemStack(stack2);
                    stack.decrement(1);
                    user.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);

                    bl = true;
                }
                case BURNING -> {
                    bl = false;
                }
                case BURNT -> {
                    bl = false;
                }
            }


        }
        return super.use(world, user, hand);
    }

    private void playUseSound(World world, BlockPos pos) {
        Random random = world.getRandom();
        world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
    }
    public enum MatchState{
        UNLIT,
        BURNING,
        BURNT
    }
}
