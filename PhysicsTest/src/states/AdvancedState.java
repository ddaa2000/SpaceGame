/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import interfaces.IPlayerControl;
import mygame.Main;

/**
 *
 * @author ddaa
 */
public abstract class AdvancedState extends AbstractAppState {
    
    protected Main gameApplication;
    protected AppSettings settings;
    protected InputManager inputManager;
    protected Node guiNode,rootNode;
    protected AppStateManager stateManager;
    protected AssetManager assetManager;
    protected Camera cam;
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        gameApplication = (Main)app;
        settings = gameApplication.getSettings();
        inputManager = gameApplication.getInputManager();
        guiNode = gameApplication.getGuiNode();
        rootNode = gameApplication.getRootNode();
        this.stateManager = gameApplication.getStateManager();
        assetManager = gameApplication.getAssetManager();
        cam = gameApplication.getCamera();
    }
    
    
}
