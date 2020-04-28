/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

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
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void OnCollisionDetected(Spatial other, PhysicsCollisionEvent event)
    {
        if(other.getName().equals("SpaceShip")){
            other.getControl(SimplePhysicsControl.class).removeSelfObject(); 
        }
        else if(other.getName().equals("Battery")){
            other.getControl(BatteryControl.class).damage(); 
        }
        else 
            other.getControl(SimplePhysicsControl.class).removeSelfObject();
        this.removeSelfObject(); //只要子弹击中就会扣除血量
    }
}
