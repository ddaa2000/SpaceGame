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
    public int diff = 1;
    
    public void setLoadGame(boolean shouldLoadGame){
        this.shouldLoadGame = shouldLoadGame;
    }
    
    private void rockAndCannonInit(int diff){
        int rockNum = 0, cannonNum = 0;
        switch(diff){
            case 0:
                rockNum = 30;
                cannonNum = 10;
                break;
            case 1:
                rockNum = 60;
                cannonNum = 20;
                break;
            case 2:
                rockNum = 100;
                cannonNum = 30;
                break;
        }
        //Rock init
        for(int i = 0;i<rockNum;i++)
        {
            Spatial rock = createGameObject("Models/rock/rock.j3o",(Node)gameScene);
            float randomScale = (float)Math.random()*6+3;
            
            rock.setLocalScale(new Vector3f(randomScale,randomScale,randomScale));
            rock.setLocalTranslation((float)Math.random()*300, (float)Math.random()*300, (float)Math.random()*300);
            
            rock.getControl(RockControl.class).setPhysics(bulletAppState);
            
        }
        
        
        int[] cannonLoc = {                    //30points in total
            0,0,0,    50,0,50,   100,0,100,    //9points
            0,50,50,  50,50,100, 100,50,0,
            0,100,100,50,100,0,  100,100,50,
            
            50, 50, 50,                         //center point
            
            25,0,25,  25,0,75,   75,0,75,   75,0,25,  //12points
            25,25,25, 25,25,75,  75,25,75,  75,25,25,
            25,75,25, 25,75,75,  75,75,75,  75,75,25,
            25,100,25,25,100,75, 75,100,75, 75,100,25,
            
            25,25,0,  75,75,0,   75,25,100, 25,75,100, //8points
            0,25,25,  0,75,75,   100,75,25, 100,25,75
        };
        
        //Cannon init
        for(int i = 0;i<cannonNum;i++)
        {
            Spatial cannon = createGameObject("Models/cannon.j3o",(Node)gameScene);
            
            cannon.setLocalTranslation((float)cannonLoc[3*i]+100, (float)cannonLoc[3*i+1]+100, (float)cannonLoc[3*i+2]+100);
            cannon.getControl(BatteryControl.class).setPhysics(bulletAppState);
            cannon.getControl(BatteryControl.class).setSpaceship(spaceship);
            
        }
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
        
        
        if(shouldLoadGame)
            loadGame();
        else
            rockAndCannonInit(diff);
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
