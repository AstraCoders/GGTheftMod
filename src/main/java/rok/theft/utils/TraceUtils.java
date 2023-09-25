package rok.theft.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/11/2021
 */
public class TraceUtils {

	@Nullable
	public static EntityHitResult rayTraceEntity(Entity shooter, Predicate<Entity> filter, double distance) {
		Level world = shooter.level();
		Vec3 startVec = shooter.getEyePosition(1.0F);
		Vec3 endVec = shooter.getEyePosition(1.0F).add(shooter.getViewVector(1.0F).scale(distance));
		double d0 = distance * distance;
		Entity entity = null;
		Vec3 vector3d = null;
		for (Entity entity1 : world.getEntities(shooter, shooter.getBoundingBox().expandTowards(shooter.getViewVector(1.0F).scale(distance * distance)).inflate(1.0D), filter)) {
			AABB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
			Optional<Vec3> optional = axisalignedbb.clip(startVec, endVec);
			if (axisalignedbb.contains(startVec)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					vector3d = optional.orElse(startVec);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vec3 vector3d1 = optional.get();
				double d1 = startVec.distanceToSqr(vector3d1);
				if (d1 < d0 || d0 == 0.0D) {
					if (entity1.getRootVehicle() == shooter.getRootVehicle() && !entity1.canRiderInteract()) {
						if (d0 == 0.0D) {
							entity = entity1;
							vector3d = vector3d1;
						}
					} else {
						entity = entity1;
						vector3d = vector3d1;
						d0 = d1;
					}
				}
			}
		}
		return entity == null ? null : new EntityHitResult(entity, vector3d);
	}

	@Nullable
	public static HitResult rayTraceBlock(ServerPlayer player) {
		double distance = player.getAttributeValue(ForgeMod.ENTITY_REACH.get());
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