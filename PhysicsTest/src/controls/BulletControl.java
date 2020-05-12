/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import ConstAndMethods.CollisionMasks;
import ConstAndMethods.Types;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author myx36
 */
public class BulletControl extends SimplePhysicsControl{
    //public float velocity = 20;
    public boolean isEnemyBullet = true;
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
        setMass(0.001f);
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
        if(Types.isSpaceship(other)){
            other.getControl(SpaceshipControl.class).damage();
        }
        else if(Types.isBattery(other)){
            other.getControl(BatteryControl.class).damage(); 
        }
       // spatial.removeFromParent();
        this.removeSelfObject(); 
    }
}
