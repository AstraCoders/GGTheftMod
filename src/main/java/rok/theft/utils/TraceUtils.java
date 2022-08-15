package rok.theft.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/11/2021
 */
public class TraceUtils {

	@Nullable
	public static EntityRayTraceResult rayTraceEntity(Entity shooter, Predicate<Entity> filter, double distance) {
		World world = shooter.level;
		Vector3d startVec = shooter.getEyePosition(1.0F);
		Vector3d endVec = shooter.getEyePosition(1.0F).add(shooter.getViewVector(1.0F).scale(distance));
		double d0 = distance * distance;
		Entity entity = null;
		Vector3d vector3d = null;
		for (Entity entity1 : world.getEntities(shooter, shooter.getBoundingBox().expandTowards(shooter.getViewVector(1.0F).scale(distance * distance)).inflate(1.0D), filter)) {
			AxisAlignedBB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
			Optional<Vector3d> optional = axisalignedbb.clip(startVec, endVec);
			if (axisalignedbb.contains(startVec)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					vector3d = optional.orElse(startVec);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vector3d vector3d1 = optional.get();
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
		return entity == null ? null : new EntityRayTraceResult(entity, vector3d);
	}

	@Nullable
	public static RayTraceResult rayTraceBlock(PlayerEntity player) {
		double distance = player.getAttributeValue(ForgeMod.REACH_DISTANCE.get());
		return rayTraceBlock(player, player.isCreative() ? distance : distance - 0.5D);
	}

	@Nullable
	public static BlockRayTraceResult rayTraceBlock(PlayerEntity player, double range) {
		World world = player.level;

		Vector3d look = player.getLookAngle();
		Vector3d start = new Vector3d(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());

		Vector3d end = new Vector3d(player.getX() + look.x * range, player.getY() + player.getEyeHeight() + look.y * range, player.getZ() + look.z * range);
		RayTraceContext context = new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player);
		return world.clip(context);
	}
}