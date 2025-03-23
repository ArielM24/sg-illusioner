package com.sg.illusioner;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.world.Difficulty;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SGIllusioner implements ModInitializer {
	public static final String MOD_ID = "sg-illusioner";

	public static final Random r = new Random();

	public static final int spawnRatio = 50;

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
			boolean willSpawn = r.nextInt(100) <= spawnRatio;
			if (!willSpawn) {
				return;
			}
			EvokerEntity evoker = (EvokerEntity) entity;
			IllusionerEntity illusioner = EntityType.ILLUSIONER.create(serverLevel, SpawnReason.NATURAL);
			illusioner.setPos(evoker.getPos().getX(), evoker.getPos().getY(), evoker.getPos().getZ());
			serverLevel.spawnNewEntityAndPassengers(illusioner);
		});
	}
}