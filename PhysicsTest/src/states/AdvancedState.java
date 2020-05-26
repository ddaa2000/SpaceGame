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
import controls.IPlayerControl;
import mygame.Main;

/**
 *
 * @author ddaa
 */
public abstract class AdvancedState extends AbstractAppState {
    
    protected Main gameMain;
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
        gameMain = (Main)app;
        settings = gameMain.getSettings();
        inputManager = gameMain.getInputManager();
        guiNode = gameMain.getGuiNode();
        rootNode = gameMain.getRootNode();
        this.stateManager = gameMain.getStateManager();
        assetManager = gameMain.getAssetManager();
        cam = gameMain.getCamera();
    }
    
    
}
