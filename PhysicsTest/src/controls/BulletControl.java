/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import ConstAndMethods.CollisionMasks;
import ConstAndMethods.Types;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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
    ParticleEmitter effect = null;
    

    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    @Override
    protected void controlUpdate(float tpf)
    {
        super.controlUpdate(tpf);
        presentLifeTime -= tpf;
        if(presentLifeTime<0)
            removeSelfObject();
        /*if (curTime >= 0) {
            curTime += tpf;
            if (curTime > fxTime) {
                curTime = -1;
                this.removeSelfObject(); 
                effect.removeFromParent();
                System.out.println("effect disappear");
            }
        }*/
            
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
        
        /*if (effect != null && spatial.getParent() != null) {
            curTime = 0;
            effect.setLocalTranslation(spatial.getLocalTranslation());
            spatial.getParent().attachChild(effect);
            effect.emitAllParticles();
            System.out.println("effect emitted");
        }*/
        if(spatial.getParent()!=null){
            Node particleEffect = new Node();
            ParticleEffectControl control = new ParticleEffectControl();
            control.setGameMain(gameMain);
            particleEffect.addControl(control);
            spatial.getParent().attachChild(particleEffect);
            particleEffect.setLocalTranslation(spatial.getLocalTranslation());
        }
        gameMain.getSoundState().playSound("Sounds/shoot.wav");
        this.removeSelfObject(); 
    }
}
