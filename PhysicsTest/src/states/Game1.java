/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import Data.SavedGame;
import Data.SpaceshipData;
import Exceptions.wrongUserInfoException;
import Web.DemoApplication;
import com.alibaba.fastjson.JSON;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import controls.BatteryControl;
import controls.CameraControl;
import controls.RockControl;
import controls.SpaceshipControl;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ddaa
 */
public class Game1 extends AdvancedState {
   
    private BulletAppState bulletAppState;
    public Spatial spaceship;
    Spatial gameScene;
    PlayerInputState playerInputState;
    Spatial cursorNode;
    Node Floor;
    RigidBodyControl testFloor;
    boolean shouldLoadGame = false;
    
    public void setLoadGame(boolean shouldLoadGame){
        this.shouldLoadGame = shouldLoadGame;
    }
    
    /**
     * use this to simplify creating gameObject from assets
     * @param name the path of the asset
     * @param parent
     * @return 
     */
    
    public Spatial createGameObject(String name,Node parent)
    {
        return gameApplication.createGameObject(name, parent);
    }
    
    public void quitGame()
    {
        stateManager.detach(playerInputState);
        bulletAppState.getPhysicsSpace().removeAll(gameScene);
        gameScene.removeFromParent();
        gameScene = null;
        spaceship = null;
        
        
    }
    public void startGame()
    {
        
        //initialize states
        playerInputState = new PlayerInputState();
        stateManager.attach(playerInputState);
        bulletAppState = gameApplication.bulletAppState;
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0,0,0));
        
        //when delete or initialize, operate "gameScene"
        gameScene = createGameObject("Scenes/newScene.j3o",rootNode);
        spaceship =  createGameObject("Models/Spaceship.j3o",(Node)gameScene);
        //spaceship add bullets and camera 
        spaceship.setLocalTranslation(new Vector3f(0,50,0));
        spaceship.getControl(SpaceshipControl.class).setPhysics(bulletAppState);//, null);
        spaceship.getControl(SpaceshipControl.class).setMass(1);
        ((Node)spaceship).getChild("Camera").getControl(CameraControl.class).setCamera(cam);
        

       // bulletAppState.setDebugEnabled(true);
        playerInputState.setPlayer(spaceship.getControl(SpaceshipControl.class));
        
        
        for(int i = 0;i<100;i++)
        {
            Spatial rock = createGameObject("Models/rock/rock.j3o",(Node)gameScene);
            float randomScale = (float)Math.random()*2+1;
            
            rock.setLocalScale(new Vector3f(randomScale,randomScale,randomScale));
            rock.setLocalTranslation((float)Math.random()*100+40, (float)Math.random()*100, (float)Math.random()*100+40);
            
            rock.getControl(RockControl.class).setPhysics(bulletAppState);
            
        }
        for(int i = 0;i<30;i++)
        {
            Spatial cannon = createGameObject("Models/cannon.j3o",(Node)gameScene);
            
            cannon.setLocalTranslation((float)Math.random()*100+40, (float)Math.random()*100, (float)Math.random()*100+40);
            cannon.getControl(BatteryControl.class).setPhysics(bulletAppState);
            cannon.getControl(BatteryControl.class).setSpaceship(spaceship);
            
        }
        if(shouldLoadGame)
            loadGame();
    }    
    
    public void saveGame(){
        SavedGame savedGame = new SavedGame();
        savedGame.spaceshipData = (SpaceshipData)spaceship.getControl(SpaceshipControl.class).save();
        try {
            savedGame.playedTimes++;
            savedGame.playerTimes2+=2;
            String presentData = JSON.toJSONString(savedGame);
            DemoApplication.uploadData(presentData);
        } catch (wrongUserInfoException ex) {
            Logger.getLogger(GameUIState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadGame(){
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
        spaceship.getControl(SpaceshipControl.class).load(savedGame.spaceshipData);
    }
    
    public AppSettings getSettings()
    {
        return settings;
    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
       
        //this is called on the OpenGL thread after the AppState has been attached
        System.out.println("initialize");
        //cursorNode = createGameObject("Models/cursorCube.j3o",guiNode); 
        startGame();
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        Vector2f mousePos = inputManager.getCursorPosition();
        //cursorNode.setLocalTranslation(mousePos.x,mousePos.y,0);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        quitGame();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
}
