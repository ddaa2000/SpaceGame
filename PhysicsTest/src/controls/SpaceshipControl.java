/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import Data.SpaceshipData;
import Data.IGameSavable;
import Data.IData;
import ConstAndMethods.CollisionMasks;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import controls.SimplePhysicsControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author ddaa
 */
public class SpaceshipControl extends SimplePhysicsControl implements IPlayerControl,IGameSavable {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.

    public int life = 1000;
    private static final float rotationFactor = 0.01f;
    private static final float planeRotationFactor = 0.02f;
    private static final float speedFactor = 0.3f;
    private static final float angularDamping = 0.4f;
    private static final float linearDamping = 0.5f;
    private static final float normalAcceleration = 10f;
    private static final float coolingDownTime = 0.2f;
    private float counter = coolingDownTime;
    private boolean CanShoot = true;
    
    @Override
    public void physicsInitialize()
    {
        rigidBodyControl.setCollisionGroup(CollisionMasks.group_spaceship);
        rigidBodyControl.setCollideWithGroups(CollisionMasks.mask_spaceship);
    }
    @Override
    public void initialize()
    {
       
        super.initialize();
        setAngularDamping(angularDamping);
        setDamping(linearDamping);
        setBoxCollider(2,2,2);
    }
    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
        super.controlUpdate(tpf);
        if(counter>0)
           counter-=tpf;
        float acceleration = normalAcceleration*tpf;
        applyImpulseLocal(0,0,acceleration);
        if(gameMain.getGameUIState()!=null){
            gameMain.getGameUIState().setLifeText(life);
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        SpaceshipControl control = new SpaceshipControl();
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
    public void setHorizontalRotationValue(double value) {
        applyTorqueImpulseLocal(0,-(float)value*rotationFactor,0);
    }

    @Override
    public void setVerticalRotationValue(double value) {
        applyTorqueImpulseLocal(-(float)value*rotationFactor,0,0);
    }

    @Override
    public void speedUp(double strength) {
        applyImpulseLocal(0,0,(float)strength*speedFactor);
    }

    @Override
    public void speedDown(double strength) {
        applyImpulseLocal(0,0,-(float)strength*speedFactor);
    }

    @Override
    public void setPlaneRotationValue(double value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        applyTorqueImpulseLocal(0,0,(float)value*planeRotationFactor);
    }

    @Override
    public void attack(){
        if(counter <= 0){
            Spatial bullet = createGameObject("Models/bullet.j3o",spatial.getParent()); 
            BulletControl tempControl = bullet.getControl(BulletControl.class);         
            bullet.setLocalTranslation(spatial.getLocalTranslation());
            bullet.setLocalScale(1); 
            bullet.setLocalRotation(spatial.getLocalRotation());
            tempControl.setPhysics(gameMain.bulletAppState);
            tempControl.setAsSpaceshipBullet();
            tempControl.setLinearVelocityLocal(0,0,400);         
            counter = coolingDownTime;
        }

    }
    
    @Override
    public IData save(){
        SpaceshipData tmp = new SpaceshipData();
        tmp.translation = rigidBodyControl.getPhysicsLocation();
        tmp.rotation = rigidBodyControl.getPhysicsRotation();
        tmp.linearvelocity = rigidBodyControl.getLinearVelocity();
        tmp.angularvelocity = rigidBodyControl.getAngularVelocity();
        tmp.life = life;
        tmp.counter = counter;
        tmp.CanShoot = CanShoot;
        System.out.println("save");
        return tmp;
       
    }
    
    @Override
    public void load(IData data){
        SpaceshipData tmp = (SpaceshipData)data;
        rigidBodyControl.setPhysicsLocation(tmp.translation);
        rigidBodyControl.setPhysicsRotation(tmp.rotation);
        rigidBodyControl.setLinearVelocity(tmp.linearvelocity);
        rigidBodyControl.setAngularVelocity(tmp.angularvelocity);
        life = tmp.life;
        counter = tmp.counter;
        CanShoot = tmp.CanShoot;
         System.out.println("load");
    }
    
    public void damage(){
        life -= 1;
        if(life == 0){
            this.removeSelfObject();
        }
    }
}
