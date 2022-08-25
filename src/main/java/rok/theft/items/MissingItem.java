package rok.theft.items;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rok.theft.Config;
import rok.theft.server.TheftAction;
import rok.theft.utils.TraceUtils;

import java.awt.event.ContainerEvent;

/*
 * @author Rok, Pedro Lucas N M Machado created on 24/08/2022
 */
public class MissingItem extends Item {
	public MissingItem() {
		super(new Properties().stacksTo(1));
	}


}
