/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import ConstAndMethods.CollisionMasks;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

/**
 *
 * @author myx36
 */
public class BulletControl extends SimplePhysicsControl{
    //public float velocity = 20;
    boolean isEnemyBullet = true;
    public static final float radius = 3f;
    private static final float maxLifeTime = 8f;
    private float presentLifeTime = maxLifeTime;
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    @Override
    protected void controlUpdate(float tpf)
    {
        presentLifeTime -= tpf;
        if(presentLifeTime<0)
            removeSelfObject();
            
    }
    public void setAsSpaceshipBullet()
    {
        isEnemyBullet = false;
        rigidBodyControl.setCollisionGroup(CollisionMasks.group_spaceshipBullet);
        rigidBodyControl.setCollideWithGroups(CollisionMasks.mask_spaceshipBullet);
        
    }
    
    @Override
    public void physicsInitialize()
    {
        setSphereCollider(3);
        if(isEnemyBullet)
        {
            rigidBodyControl.setCollisionGroup(CollisionMasks.group_enemyBullet);
            rigidBodyControl.setCollideWithGroups(CollisionMasks.mask_enemyBullet);
        }
        else
        {
            rigidBodyControl.setCollisionGroup(CollisionMasks.group_spaceshipBullet);
            rigidBodyControl.setCollideWithGroups(CollisionMasks.mask_spaceshipBullet);
        }
    }
    @Override
    public void OnCollisionDetected(Spatial other, PhysicsCollisionEvent event)
    {
        System.out.println("bullet collide with "+other.getName());
   /*     if(other.getName().equals("Spaceship")){
            other.getControl(SimplePhysicsControl.class).removeSelfObject(); 
        }
        else if(other.getName().equals("Battery")){
            other.getControl(BatteryControl.class).damage(); 
        }
        else 
            other.getControl(SimplePhysicsControl.class).removeSelfObject();
        this.removeSelfObject(); //只要子弹击中就会扣除血量*/
    }
}
