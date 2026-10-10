package rok.theft.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import org.jetbrains.annotations.Nullable;
import java.util.function.Predicate;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/11/2021
 */
public class TraceUtils {

	@Nullable
	public static EntityHitResult rayTraceEntity(Entity shooter, Predicate<Entity> filter, double distance) {
		Vec3 view = shooter.getViewVector(1.0F);
		Vec3 start = shooter.getEyePosition(1.0F);
		double distanceSquared = distance * distance;
		AABB searchBox = shooter.getBoundingBox().expandTowards(view.scale(distance)).inflate(1.0D);
		return ProjectileUtil.getEntityHitResult(shooter, start, start.add(view.scale(distance)), searchBox, filter, distanceSquared);
	}

	@Nullable
	public static HitResult rayTraceBlock(ServerPlayer player) {
		double distance = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
		return rayTraceBlock(player, player.isCreative() ? distance : distance - 0.5D);
	}

	@Nullable
	public static BlockHitResult rayTraceBlock(ServerPlayer player, double range) {
		Level world = player.level();

		Vec3 look = player.getLookAngle();
		Vec3 start = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());

		Vec3 end = new Vec3(player.getX() + look.x * range, player.getY() + player.getEyeHeight() + look.y * range, player.getZ() + look.z * range);
		ClipContext context = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
		return world.clip(context);
	}
}
