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
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.VAlignment;
import mygame.Main;

/**
 *
 * @author ddaa
 */
public class MainMenuAppState extends AbstractAppState {
    
    Node guiNode;
    SimpleApplication app;
    MyButton startButton;
    Label title;
    AppSettings settings;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.app = (SimpleApplication)app;
        guiNode = this.app.getGuiNode();
        settings = ((Main)(this.app)).getSettings();
        startButton = new MyButton("start");
        startButton.setFontSize(30);
        startButton.setPreferredSize(new Vector3f(200,100,0));
        guiNode.attachChild(startButton);
        startButton.setSize(Vector3f.ZERO);
        startButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnStartButtonClickListener();
            }
        });
        startButton.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2);
            

        
        title = new Label("Test UI");
        title.setPreferredSize(new Vector3f(500,200,0));
        title.setFontSize(60);
        title.setTextHAlignment(HAlignment.Center);
        title.setTextVAlignment(VAlignment.Center);
        title.setLocalTranslation(settings.getWidth()/2-250, settings.getHeight()/2+300, 0);
        guiNode.attachChild(title);
    }
    public void OnStartButtonClickListener()
    {
        System.out.println("haha");
        ((Main)app).startGame();
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
        guiNode.detachChild(startButton);
        guiNode.detachChild(title);
    }
    
}
