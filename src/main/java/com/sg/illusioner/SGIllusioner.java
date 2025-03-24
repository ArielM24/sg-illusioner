package com.sg.illusioner;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Difficulty;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SGIllusioner implements ModInitializer {
	public static final String MOD_ID = "sg-illusioner";

	public static final Random r = new Random();

	public static final int spawnRatio = 85;

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, serverLevel) -> {
			if (!entity.getWorld().getDifficulty().equals(Difficulty.HARD)) {
				return;
			}
			if (!entity.getType().equals(EntityType.EVOKER)) {
				return;
			}
			EvokerEntity evoker = (EvokerEntity) entity;
			NbtElement check = ((IEntityDataSaver)evoker).getPersistentData().get("illusioner_spawn");
			if(check != null){
				return;
			}
			((IEntityDataSaver)evoker).getPersistentData().putString("illusioner_spawn", "checked");
			boolean willSpawn = r.nextInt(100) <= spawnRatio;
			if (!willSpawn) {
				return;
			}
			int xOffset = 1 + r.nextInt(2);
			int zOffset = 1 + r.nextInt(2);
			xOffset = r.nextBoolean() ? xOffset : -xOffset;
			zOffset = r.nextBoolean() ? zOffset : -zOffset;
			BlockPos pos = evoker.getBlockPos();
			BlockPos spawnPos = new BlockPos(pos.getX() + xOffset, pos.getY(), pos.getZ() + zOffset);
			BlockPos floor = spawnPos.offset(Direction.DOWN);
			BlockPos air = spawnPos.offset(Direction.UP, 1);
			if(!evoker.getWorld().getBlockState(floor).isOpaqueFullCube()){
				return;
			};
			if(!evoker.getWorld().getBlockState(spawnPos).isAir()){
				return;
			};
			if(!evoker.getWorld().getBlockState(air).isAir()){
				return;
			};
			IllusionerEntity illusioner = EntityType.ILLUSIONER.create(serverLevel, SpawnReason.NATURAL);
			illusioner.setPosition(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
			serverLevel.spawnNewEntityAndPassengers(illusioner);
		});
	}
}