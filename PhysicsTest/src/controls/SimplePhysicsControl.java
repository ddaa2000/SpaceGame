/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import java.io.IOException;
import mygame.Main;

/**
 *
 * @author ddaa
 */
public abstract class SimplePhysicsControl extends AdvancedControl implements PhysicsCollisionListener {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.

    BulletAppState bulletAppState = null;
    RigidBodyControl rigidBodyControl = null;
    protected boolean isPhysicsSet = false;
    
    /**
     * called before the first update, all the overrides should add super.initialize()
     */
    
    @Override
    public final void collision(PhysicsCollisionEvent event) {
        if(event.getNodeA()==spatial)
        {
            if(event.getNodeB() != null)
                OnCollisionDetected(event.getNodeB(), event);
        }
            
        else if(event.getNodeB()==spatial)
        {
            if(event.getNodeA() != null)
                OnCollisionDetected(event.getNodeA(), event);
        }
    }
    
    public void OnCollisionDetected(Spatial other, PhysicsCollisionEvent event)
    {
     //   System.out.println("collid with "+other.getName());
    }
    
    @Override
    public void initialize()
    {
        super.initialize();
        if(rigidBodyControl==null)
            rigidBodyControl = new RigidBodyControl(1);
    }
    public void physicsInitialize()
    {
        
    }
    public void setMass(float mass)
    {
        if(rigidBodyControl!=null)
            rigidBodyControl.setMass(mass);
    }
    
    /**
     * remove this object, along with the physics controllers
     */
    @Override
    public void removeSelfObject()
    {
        if(this.bulletAppState!=null)
            if(rigidBodyControl!=null)
                bulletAppState.getPhysicsSpace().remove(rigidBodyControl);
        super.removeSelfObject();

    }
    
    private void basicsPhysicsInitialize(BulletAppState bulletAppState)
    {
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
       // System.out.println("set physics successfully");
        isPhysicsSet = true;
        this.bulletAppState = bulletAppState;
       
        if(rigidBodyControl==null)
            rigidBodyControl = new RigidBodyControl(1);
   //     if(collisionShape != null)
     //       rigidBodyControl.setCollisionShape(new MeshCollisionShape(collisionShape.getMesh()));
        setCollisionMask();
        spatial.addControl(rigidBodyControl);
        bulletAppState.getPhysicsSpace().add(rigidBodyControl);
    }
    public void setCollisionMask()
    {
        
    }
    /**
     * all the physics simulation will start after calling this method
     * @param bulletAppState the bulletAppState that we needed for physics simulation
     * @param collisionShape currently no use,just give null
     */
    public void setPhysics(BulletAppState bulletAppState)
    {
        basicsPhysicsInitialize(bulletAppState);
        physicsInitialize();
    }
     public void setPhysics(BulletAppState bulletAppState,float mass)
    {
        basicsPhysicsInitialize(bulletAppState);
        setMass(mass);
        physicsInitialize();
    }
    
    
    /**
     * give impulse on the center of the object, the parameter is  in orld axis
     * @param x
     * @param y
     * @param z 
     */
    public void applyImpulse(float x,float y,float z)
    {
        if(!isPhysicsSet)
            return;
        rigidBodyControl.applyImpulse(new Vector3f(x,y,z), new Vector3f(0,0,0));
    }
    
    /**
     * give torque impulse on the object,the parameter is in world axis
     * @param x
     * @param y
     * @param z 
     */
    public void applyTorqueImpulse(float x,float y,float z)
    {
        if(!isPhysicsSet)
            return;
        rigidBodyControl.applyTorqueImpulse(new Vector3f(x,y,z));
    }
    
    /**
     * give torque impulse on the object,the parameter is in local axis
     * @param x
     * @param y
     * @param z 
     */
    public void applyTorqueImpulseLocal(float x,float y,float z)
    {
        if(!isPhysicsSet)
            return;
        
        Vector3f result = new Vector3f(0,0,0);
        spatial.localToWorld(new Vector3f(x,y,z), result);
        result.x -= spatial.getWorldTranslation().x;
        result.y -= spatial.getWorldTranslation().y;
        result.z -= spatial.getWorldTranslation().z;
        rigidBodyControl.applyTorqueImpulse(result);
    }
    
    /**
     * set the angular damping of the object
     * @param value 
     */
    public void setAngularDamping(float value)
    {
        if(!isPhysicsSet)
            return;
        rigidBodyControl.setAngularDamping(value);
    }
    
    /**
     * set the damping of the object
     * @param value 
     */
    public void setDamping(float value)
    {
        if(!isPhysicsSet)
            return;
        rigidBodyControl.setLinearDamping(value);
    }
    
    /**
     * give impulse on the object,the parameter is in local axis
     * @param x
     * @param y
     * @param z 
     */
    public void applyImpulseLocal(float x,float y,float z)
    {
        if(!isPhysicsSet)
            return;
        Vector3f result = new Vector3f(0,0,0);
        spatial.localToWorld(new Vector3f(x,y,z), result);
        result.x -= spatial.getWorldTranslation().x;
        result.y -= spatial.getWorldTranslation().y;
        result.z -= spatial.getWorldTranslation().z;
        rigidBodyControl.applyImpulse(result, new Vector3f(0,0,0));
    }
    public void setBoxCollider(float x,float y,float z)
    {
        if(!isPhysicsSet)
            return;
        rigidBodyControl.setCollisionShape(new BoxCollisionShape(new Vector3f(x,y,z)));
    }
    public void setSphereCollider(float radius)
    {
        if(!isPhysicsSet)
            return;
        rigidBodyControl.setCollisionShape(new SphereCollisionShape(radius));
    }
    
    public void setLinearVelocity(float x,float y,float z)
    {
        if(!isPhysicsSet)
            return;
        rigidBodyControl.setLinearVelocity(new Vector3f(x,y,z));
    }
    
     public void setLinearVelocityLocal(float x,float y,float z)
    {
        if(!isPhysicsSet)
            return;
        Vector3f result = new Vector3f(0,0,0);
        spatial.localToWorld(new Vector3f(x,y,z), result);
        result.x -= spatial.getWorldTranslation().x;
        result.y -= spatial.getWorldTranslation().y;
        result.z -= spatial.getWorldTranslation().z;
        rigidBodyControl.setLinearVelocity(result);
    }
    
}
