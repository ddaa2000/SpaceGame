/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import Web.DemoApplication;
import Web.WebSocketClientHandler;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.ColoredComponent;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.core.GuiComponent;
import mygame.Main;

/**
 *
 * @author ddaa
 */
public class MainMenuAppState extends AbstractAppState {
    
    Node guiNode;
    SimpleApplication app;
    MyButton startEasy,offLineButton,startMiddle,startHard,gameLoad;
    Label QR_code;
    Label title;
    AppSettings settings;
    
    public void initializeBasicPanel(){
        offLineButton = new MyButton("play offline");
        offLineButton.setFontSize(30);
        offLineButton.setPreferredSize(new Vector3f(200,100,0));
        offLineButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnStartButtonClickListener();
            }
        });
        offLineButton.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2);
        
        
        QR_code = new Label("");
        QR_code.setPreferredSize(new Vector3f(600,500,0));
        QR_code.setFontSize(1);
        QR_code.setTextHAlignment(HAlignment.Center);
        QR_code.setTextVAlignment(VAlignment.Center);
        QR_code.setLocalTranslation(settings.getWidth()/2-300, settings.getHeight()/2+300, 0);
        QR_code.setColor(ColorRGBA.White);
        QuadBackgroundComponent bg = new QuadBackgroundComponent();
        bg.setColor(ColorRGBA.White);
        QR_code.setBackground(bg);
        QR_code.setText(null);
        QR_code.setIcon(new IconComponent("Image.jpeg"));
    }
    public void initializeMainPanel(){
        title = new Label("Space Battle");
        title.setPreferredSize(new Vector3f(600,500,0));
        title.setFontSize(100);
        title.setTextHAlignment(HAlignment.Center);
        title.setTextVAlignment(VAlignment.Center);
        title.setLocalTranslation(settings.getWidth()/2-300, settings.getHeight()/3*2+250, 0);
        
        startEasy = new MyButton("easy mode");
        startEasy.setFontSize(30);
        startEasy.setPreferredSize(new Vector3f(200,50,0));
        startEasy.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnStartButtonClickListener();
            }
        });
        startEasy.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2);
        
        startMiddle = new MyButton("normal mode");
        startMiddle.setFontSize(30);
        startMiddle.setPreferredSize(new Vector3f(200,50,0));
        startMiddle.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnStartButtonClickListener();
            }
        });
        startMiddle.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2-100);
        
        startHard = new MyButton("hard mode");
        startHard.setFontSize(30);
        startHard.setPreferredSize(new Vector3f(200,50,0));
        startHard.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnStartButtonClickListener();
            }
        });
        startHard.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2-200);
        
        gameLoad = new MyButton("load game");
        gameLoad.setFontSize(30);
        gameLoad.setPreferredSize(new Vector3f(200,50,0));
        gameLoad.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnLoadButtonClickListener();
            }
        });
        gameLoad.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2-300);
    }
    public void showBasicPanel(){
        guiNode.attachChild(QR_code);
        guiNode.attachChild(offLineButton);
        
    }
    public void hideBasicPanel(){
        guiNode.detachChild(QR_code);
        guiNode.detachChild(offLineButton);
    }
    
    public void showMainPanel(){
        guiNode.attachChild(startEasy);
        guiNode.attachChild(startMiddle);
        guiNode.attachChild(startHard);
        guiNode.attachChild(gameLoad);
        guiNode.attachChild(title);
        hideBasicPanel();
    }
    
    public void hideMainPanel(){
        guiNode.detachChild(startEasy);
        guiNode.detachChild(startMiddle);
        guiNode.detachChild(startHard);
        guiNode.detachChild(gameLoad);
        guiNode.detachChild(title);
        hideBasicPanel();
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.app = (SimpleApplication)app;
        guiNode = this.app.getGuiNode();
        settings = ((Main)(this.app)).getSettings();
        
        
        
        
        initializeBasicPanel();
        initializeMainPanel();
        showBasicPanel();
        
        //guiNode.attachChild(title);
    }
    public void OnStartButtonClickListener(){
        ((Main)app).startGame();
    }
    public void OnLoadButtonClickListener(){
        ((Main)app).loadGame();
    }
    
    public void OnOffLineButtonClickListener(){
        
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        if(DemoApplication.hasLoggedIn()){
            showMainPanel();
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
        guiNode.detachChild(startEasy);
        guiNode.detachChild(QR_code);
        hideBasicPanel();
        hideMainPanel();
    }
    
}
