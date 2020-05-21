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
    
   /* private float curTime = -1.0f;
    private static final float fxTime = 0.5f;*/
    
  /*  @Override
    public void initialize(){
        super.initialize();
        prepareEffect(gameMain.getAssetManager());
    }
    
    private void prepareEffect(AssetManager assetManager) {
        int COUNT_FACTOR = 1;
        float COUNT_FACTOR_F = 1f;
        effect = new ParticleEmitter("Flame", ParticleMesh.Type.Triangle, 32 * COUNT_FACTOR);
        effect.setSelectRandomImage(true);
        effect.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, (float) (1f / COUNT_FACTOR_F)));
        effect.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
        effect.setStartSize(1.3f);
        effect.setEndSize(2f);
        effect.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        effect.setParticlesPerSec(0);
        effect.setGravity(0, -5f, 0);
        effect.setLowLife(.4f);
        effect.setHighLife(.5f);
        effect.getParticleInfluencer()
                .setInitialVelocity(new Vector3f(0, 7, 0));
        effect.getParticleInfluencer().setVelocityVariation(1f);
        effect.setImagesX(2);
        effect.setImagesY(2);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/flame.png"));
        effect.setMaterial(mat);
        System.out.println("effect created");
    }*/
    
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
        this.removeSelfObject(); 
    }
}
