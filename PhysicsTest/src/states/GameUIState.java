/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import Data.SavedGame;
import Exceptions.wrongUserInfoException;
import Web.DemoApplication;
import Web.Result;
import Web.WebSocketClientHandler;
import cn.ac.mryao.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.Main;


/**
 *
 * @author ddaa
 */
public class GameUIState extends AbstractAppState {
    
    Node guiNode,menuNode;
    SimpleApplication app;
    MyButton quitButton,restartButton,menuButton,backButton;
    AppSettings settings;
    SavedGame savedGame =new SavedGame();
    
    
    Label lifeLabel;
    
    public void setLifeText(int life){
        if(lifeLabel!= null)
            lifeLabel.setText("life "+ life);
    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.app = (SimpleApplication)app;
        guiNode = this.app.getGuiNode();
        settings = ((Main)(this.app)).getSettings();
        
        menuNode = new Node();
        

        quitButton = new MyButton("quit");
        quitButton.setFontSize(30);
        quitButton.setPreferredSize(new Vector3f(200,100,0));
        quitButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnQuitButtonClickListener();
            }
        });
        quitButton.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2);
        menuNode.attachChild(quitButton);
        
        restartButton = new MyButton("restart");
        restartButton.setFontSize(30);
        restartButton.setPreferredSize(new Vector3f(200,100,0));
        restartButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnRestartButtonClickListener();
            }
        });
        restartButton.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2+100);
        menuNode.attachChild(restartButton);
        
        backButton = new MyButton("back");
        backButton.setFontSize(30);
        backButton.setPreferredSize(new Vector3f(200,100,0));
        backButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnBackButtonClickListener();
            }
        });
        backButton.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2+200);
        menuNode.attachChild(backButton);
        
        menuButton = new MyButton("menu");
        menuButton.setFontSize(30);
        menuButton.setPreferredSize(new Vector3f(200,100,0));
        menuButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute( Button source ) {
                OnMenuButtonClickListener();
            }
        });
        menuButton.setLocalTranslation(settings.getWidth()-100, settings.getHeight()-50);
        guiNode.attachChild(menuButton);
        
        quitButton.setLocalTranslation(settings.getWidth()/2, settings.getHeight()/2);
        
        lifeLabel = new Label("Space Battle");
        lifeLabel.setPreferredSize(new Vector3f(600,500,0));
        lifeLabel.setFontSize(100);
        lifeLabel.setTextHAlignment(HAlignment.Center);
        lifeLabel.setTextVAlignment(VAlignment.Center);
        lifeLabel.setLocalTranslation(settings.getWidth()/2-300, settings.getHeight()/3*2+250, 0);
        
        guiNode.attachChild(lifeLabel);
      //  savedGame = loadGame();
        
    /*    title = new Label("this is the "+savedGame.playedTimes+" time you play this game");
        title.setPreferredSize(new Vector3f(600,500,0));
        title.setFontSize(60);
        title.setTextHAlignment(HAlignment.Center);
        title.setTextVAlignment(VAlignment.Center);
        title.setLocalTranslation(settings.getWidth()/2-250, settings.getHeight()/2+300, 0);
        guiNode.attachChild(title);*/
    }
    
    public void saveGame(SavedGame savedGame){
        try {
            savedGame.playedTimes++;
            savedGame.playerTimes2+=2;
            String presentData = JSON.toJSONString(savedGame);
            DemoApplication.uploadData(presentData);
        } catch (wrongUserInfoException ex) {
            Logger.getLogger(GameUIState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public SavedGame loadGame(){
        SavedGame savedGame = new SavedGame();
        try {
            String previousData = DemoApplication.downloadData();
            if(previousData!=null)
                savedGame = JSON.parseObject(previousData, savedGame.getClass());
            if(savedGame==null){
                savedGame = new SavedGame();
            }
        } catch (wrongUserInfoException ex) {
            Logger.getLogger(GameUIState.class.getName()).log(Level.SEVERE, null, ex);
        }
        return savedGame;
    }
    
    private void showMenu(){
        guiNode.attachChild(menuNode);
    }
     private void hideMenu(){
        guiNode.detachChild(menuNode);
    }
    public void OnQuitButtonClickListener()
    {
        ((Main)app).saveGame();
        
        ((Main)app).quitGame();
    }
    public void OnBackButtonClickListener()
    {
        hideMenu();
    }
    public void OnRestartButtonClickListener()
    {
        ((Main)app).restartGame();;
    }
    public void OnMenuButtonClickListener()
    {
        showMenu();
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
        guiNode.detachChild(lifeLabel);
        guiNode.detachChild(menuButton);
        guiNode.detachChild(menuNode);
        
    }
    
}
