/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import mygame.Main;

/**
 *
 * @author ddaa
 */
public abstract class AdvancedControl extends AbstractControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.

    boolean initialized = false;
    
    protected Main gameMain = null;
    
    public void setGameMain(Main gameMain)
    {
        //System.out.println("set main"+spatial.getName());
        this.gameMain = gameMain;
    }
    
    public Spatial createGameObject(String name,Node parent)
    {
        if(gameMain==null)
        {
            return null;
            
        }
            
        return gameMain.createGameObject(name, parent);
    }
    
    public void initialize()
    {
        initialized = true;
    }
    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
        if(!initialized)
            initialize();
            
    }
    public void removeSelfObject()
    {
        spatial.removeFromParent();
    }
    
   
    
}
