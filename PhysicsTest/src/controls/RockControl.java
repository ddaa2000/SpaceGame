/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import ConstAndMethods.CollisionMasks;
import Data.IData;
import Data.IGameSavable;
import Data.RockData;
import Data.SpaceshipData;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author ddaa
 */
public class RockControl extends SimplePhysicsControl implements IGameSavable {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.


    @Override
    public void physicsInitialize()
    {
        setMass(1);
        applyTorqueImpulse((float)(Math.random()*5)-2.5f,(float)(Math.random()*5)-2.5f,(float)(Math.random()*5)-2.5f);
        applyImpulse((float)(Math.random()*5)-2.5f,(float)(Math.random()*5)-2.5f,(float)(Math.random()*5)-2.5f);
        setSphereCollider(2*spatial.getLocalScale().x);
        rigidBodyControl.setCollisionGroup(CollisionMasks.group_rock);
        rigidBodyControl.setCollisionGroup(CollisionMasks.mask_rock);
    }
    
    
    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        RockControl control = new RockControl();
        //TODO: copy parameters to new Control
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }

    @Override
    public IData save(){
        RockData tmp = new RockData();
        tmp.translation = rigidBodyControl.getPhysicsLocation();
        tmp.rotation = rigidBodyControl.getPhysicsRotation();
        tmp.linearvelocity = rigidBodyControl.getLinearVelocity();
        tmp.angularvelocity = rigidBodyControl.getAngularVelocity();
        tmp.scale = spatial.getLocalScale();
        System.out.println("save");
        return tmp;
       
    }
    
    @Override
    public void load(IData data){
        RockData tmp = (RockData)data;
        rigidBodyControl.setPhysicsLocation(tmp.translation);
        rigidBodyControl.setPhysicsRotation(tmp.rotation);
        rigidBodyControl.setLinearVelocity(tmp.linearvelocity);
        rigidBodyControl.setAngularVelocity(tmp.angularvelocity);
        spatial.setLocalScale(tmp.scale);
        System.out.println("load");
    }
    
}
