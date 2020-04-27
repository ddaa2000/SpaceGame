/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import controls.SimplePhysicsControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import interfaces.IPlayerControl;

/**
 *
 * @author ddaa
 */
public class SpaceshipControl extends SimplePhysicsControl implements IPlayerControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.


    private static final float rotationFactor = 0.003f;
    private static final float planeRotationFactor = 0.006f;
    private static final float speedFactor = 0.6f;
    private static final float angularDamping = 0.4f;
    private static final float linearDamping = 0.5f;

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
       
        setLinearVelocityLocal(0,0,10);
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

    
    
}