/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import Data.BatteryData;
import Data.BulletData;
import Data.GameData;
import Data.IGameSavable;
import Data.RockData;
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
import controls.BulletControl;
import controls.CameraControl;
import controls.RockControl;
import controls.SpaceshipControl;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ddaa
 */
public class Game1 extends AdvancedState{
   
    private BulletAppState bulletAppState;
    public Spatial spaceship;
    Spatial gameScene;
    PlayerInputState playerInputState;
    Spatial cursorNode;
    Node Floor;
    RigidBodyControl testFloor;
    boolean shouldLoadGame = false;
    public int diff = 0;
    List<Spatial> bullets = new LinkedList<>();
    List<Spatial> cannons = new LinkedList<>();
    List<Spatial> rocks = new LinkedList<>();
    
    public void setLoadGame(boolean shouldLoadGame){
        this.shouldLoadGame = shouldLoadGame;
    }
    
    public void addBullet(Spatial bullet){
        bullets.add(bullet);
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
            rocks.add(rock);
            
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
            
            cannons.add(cannon);
            
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
        return gameMain.createGameObject(name, parent);
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
        bulletAppState = gameMain.bulletAppState;
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0,0,0));
        
        //when delete or initialize, operate "gameScene"
        gameScene = createGameObject("Scenes/newScene.j3o",rootNode);
        spaceship =  createGameObject("Models/Spaceship.j3o",(Node)gameScene);
        //spaceship add bullets and camera 
        spaceship.setLocalTranslation(new Vector3f(0,50,-400));
        spaceship.getControl(SpaceshipControl.class).setPhysics(bulletAppState);//, null);
        spaceship.getControl(SpaceshipControl.class).setMass(1);
        ((Node)spaceship).getChild("Camera").getControl(CameraControl.class).setCamera(cam);
        

       // bulletAppState.setDebugEnabled(true);
        playerInputState.setPlayer(spaceship.getControl(SpaceshipControl.class));
        
        bullets.clear();
        cannons.clear();
        rocks.clear();
        
        if(shouldLoadGame)
            loadGame();
        else
            rockAndCannonInit(diff);
    }    
    
    
    public void saveGame(){
        GameData savedGame = new GameData();
        organizeLists();
        
        savedGame.diff = diff;
        savedGame.batteryData = new BatteryData[cannons.size()];
        savedGame.bulletData = new BulletData[bullets.size()];
        savedGame.rockData = new RockData[rocks.size()];
        
        int present = 0;
        for(Spatial s : cannons){
            if(s.getControl(BatteryControl.class)!=null){
                 savedGame.batteryData[present] = (BatteryData)s.getControl(BatteryControl.class).save();
                present++;
            }
        }
        
        present = 0;
        for(Spatial s : bullets){
            if(s.getControl(BulletControl.class)!=null){
                 savedGame.bulletData[present] = (BulletData)s.getControl(BulletControl.class).save();
                present++;
            }
        }
        
        present = 0;
        for(Spatial s : rocks){
            if(s.getControl(RockControl.class)!=null){
                 savedGame.rockData[present] = (RockData)s.getControl(RockControl.class).save();
                present++;
            }
        }
        
        savedGame.spaceshipData = (SpaceshipData)spaceship.getControl(SpaceshipControl.class).save();
        try {
            String presentData = JSON.toJSONString(savedGame);
                                    DemoApplication.uploadData(presentData);
        } catch (wrongUserInfoException ex) {
            Logger.getLogger(GameUIState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadGame(){
        GameData savedGame = new GameData();
        try {
            String previousData = DemoApplication.downloadData();
            if(previousData!=null)
                savedGame = JSON.parseObject(previousData, savedGame.getClass());
            if(savedGame==null){
                savedGame = new GameData();
            }
        } catch (wrongUserInfoException ex) {
            Logger.getLogger(GameUIState.class.getName()).log(Level.SEVERE, null, ex);
        }
        spaceship.getControl(SpaceshipControl.class).load(savedGame.spaceshipData);
        diff = savedGame.diff;
        for(RockData data : savedGame.rockData){
            Spatial rock = createGameObject("Models/rock/rock.j3o",(Node)gameScene);
            
            rock.getControl(RockControl.class).setPhysics(bulletAppState);
            rock.getControl(RockControl.class).load(data);
            rocks.add(rock);
        }
        
        for(BatteryData data : savedGame.batteryData){
            Spatial cannon = createGameObject("Models/cannon.j3o",(Node)gameScene);
            
            
            cannon.getControl(BatteryControl.class).setPhysics(bulletAppState);
            cannon.getControl(BatteryControl.class).setSpaceship(spaceship);
            cannon.getControl(BatteryControl.class).load(data);
            cannons.add(cannon);
        }
        
        for(BulletData data : savedGame.bulletData){
           Spatial bullet = createGameObject("Models/bullet.j3o",(Node)gameScene); 
            BulletControl tempControl = bullet.getControl(BulletControl.class);         
            bullet.setLocalScale(1); 
            tempControl.setPhysics(gameMain.bulletAppState);
            tempControl.load(data);
            addBullet(bullet);
        }
        
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
    
    private void organizeLists(){
        List<Spatial> destroyedBullets = new LinkedList<>();
        for(Spatial s : bullets){
            if(s == null || 
                    (s!=null && s.getControl(BulletControl.class)!=null 
                    && s.getControl(BulletControl.class).isDestroyed()))
            {
                destroyedBullets.add(s);
            }
        }
        bullets.removeAll(destroyedBullets);
        
        List<Spatial> destroyedCannons = new LinkedList<>();
        for(Spatial s : cannons){
            if(s == null || 
                    (s!=null && s.getControl(BatteryControl.class)!=null 
                    && s.getControl(BatteryControl.class).isDestroyed()))
            {
                destroyedBullets.add(s);
            }
        }
        System.out.println(cannons.size());
        cannons.removeAll(destroyedBullets);
    }
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        Vector2f mousePos = inputManager.getCursorPosition();
        //cursorNode.setLocalTranslation(mousePos.x,mousePos.y,0);
        
        organizeLists();
        if(cannons.isEmpty())
            gameMain.victory();
        
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
