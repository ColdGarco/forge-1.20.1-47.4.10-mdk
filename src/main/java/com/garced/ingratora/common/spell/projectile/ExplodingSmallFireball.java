package com.garced.ingratora.common.spell.projectile;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.entity.Entity;
// --------------------------------------
//     Sets hit block on fire imports
// import net.minecraft.core.BlockPos;
// import net.minecraft.world.entity.Mob;
// import net.minecraft.world.level.block.BaseFireBlock;
// import net.minecraft.world.phys.BlockHitResult;

public class ExplodingSmallFireball extends SmallFireball {
    public ExplodingSmallFireball(Level level, LivingEntity shooter, double xPower, double yPower, double zPower) {
        super(level, shooter, xPower, yPower, zPower);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37386_) {
      super.onHitEntity(p_37386_);
      if (!this.level().isClientSide) {
         Entity entity = p_37386_.getEntity();
         Entity entity1 = this.getOwner();
         int i = entity.getRemainingFireTicks();
         entity.setSecondsOnFire(5);
         if (!entity.hurt(this.damageSources().fireball(this, entity1), 5.0F)) {
            entity.setRemainingFireTicks(i);
         } else if (entity1 instanceof LivingEntity) {
            this.doEnchantDamageEffects((LivingEntity)entity1, entity);
         }

      }
   }
//    ------------------------
//     Sets hit block on fire
//     @Override
//     protected void onHitBlock(BlockHitResult p_37384_) {
//       super.onHitBlock(p_37384_);
//       if (!this.level().isClientSide) {
//          Entity entity = this.getOwner();
//          if (!(entity instanceof Mob) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), entity)) {
//             BlockPos blockpos = p_37384_.getBlockPos().relative(p_37384_.getDirection());
//             if (this.level().isEmptyBlock(blockpos)) {
//                this.level().setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.level(), blockpos));
//             }
//          }

//       }
//    }

//    ------------------------

    @Override
    protected void onHit(HitResult p_37388_) {
      super.onHit(p_37388_);
      if (!this.level().isClientSide) {
            this.level().explode(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    2.0F,
                    Level.ExplosionInteraction.NONE
            );
        }
      if (!this.level().isClientSide) {
         this.discard();
      }

}
}
