/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import mygame.Main;


/**
 *
 * @author ddaa
 */
public class GameUIState extends AbstractAppState {
    
    Node guiNode;
    SimpleApplication app;
    MyButton quitButton;
    AppSettings settings;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.app = (SimpleApplication)app;
        guiNode = this.app.getGuiNode();
        settings = ((Main)(this.app)).getSettings();
        
        quitButton = new MyButton("quit");
        guiNode.attachChild(quitButton);
        quitButton.setFontSize(30);
        quitButton.setPreferredSize(new Vector3f(200,100,0));
        quitButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnStartButtonClickListener();
            }
        });
        quitButton.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2);
    }
    public void OnStartButtonClickListener()
    {
        System.out.println("hahaha");
        ((Main)app).quitGame();
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
        guiNode.detachChild(quitButton);
        
    }
    
}
