/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import ConstAndMethods.CollisionMasks;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.Main;

/**
 *
 * @author myx36
 */
public class BatteryControl extends AdvancedControl implements PhysicsCollisionListener{
    public int life = 10;
    public float distance = 1000;
    Spatial spaceship = null; 
    BulletAppState bulletAppState = null;
    RigidBodyControl rigidbodyControl = null;
    private float boundary = 10;
    
    private static final float coolingDownTime = 10f;
    private float counter = coolingDownTime;
    
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void controlUpdate(float tpf)
    {
        spatial.lookAt(spaceship.getWorldTranslation(), new Vector3f(0,1,0));
        super.controlUpdate(tpf);
        if(counter>0)
            counter-=tpf;
        else if(spaceship!=null && spaceshipInRange())
        {
            counter = coolingDownTime;
            shoot();
        }
            
        
    }
    public void damage(){
        life -= 1;
        if(life == 0){
            this.removeSelfObject();
        }
    }
    @Override
    public void removeSelfObject()
    {
        if(this.bulletAppState!=null){
            if(rigidbodyControl != null)
                bulletAppState.getPhysicsSpace().remove(rigidbodyControl);
        }
        super.removeSelfObject();
    }
    @Override
    public void initialize()
    {
        super.initialize();
        if(rigidbodyControl==null)
            rigidbodyControl = new RigidBodyControl(0);
        rigidbodyControl.setCollisionShape(new SphereCollisionShape(boundary));
    }
    
    public void setSpaceship(Spatial spaceship){
        this.spaceship = spaceship;
    }

    
     public void setPhysics(BulletAppState bulletAppState)
    {
        this.bulletAppState = bulletAppState;
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        if(rigidbodyControl == null)
            rigidbodyControl = new RigidBodyControl(0);
        spatial.addControl(rigidbodyControl);
        rigidbodyControl.setCollisionGroup(CollisionMasks.group_enemy);
        rigidbodyControl.setCollideWithGroups(CollisionMasks.mask_enemy);
        bulletAppState.getPhysicsSpace().add(rigidbodyControl);
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        
    }
    
    public void shoot()
    {
        Spatial bullet = createGameObject("Models/bullet.j3o",spatial.getParent());
        BulletControl tempControl = bullet.getControl(BulletControl.class);
        bullet.setLocalTranslation(spatial.getLocalTranslation());
        bullet.setLocalScale(1);
        bullet.lookAt(spaceship.getWorldTranslation(), new Vector3f(0,1,0));
        tempControl.setPhysics(gameMain.bulletAppState);
        tempControl.setLinearVelocityLocal(0, 0, 200);
        
        
    }
    public boolean spaceshipInRange(){
        Vector3f ship = spaceship.getWorldTranslation();
        Vector3f battery = spatial.getWorldTranslation();
        float dis = FastMath.sqrt(FastMath.pow((ship.x - battery.x),2)+
                FastMath.pow((ship.y - battery.y),2)+
                FastMath.pow((ship.z - battery.z),2));
        return dis <= distance;
    }


 }
