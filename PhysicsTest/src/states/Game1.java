/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

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
import controls.CameraControl;
import controls.SpaceshipControl;

/**
 *
 * @author ddaa
 */
public class Game1 extends AdvancedState {
   
    private BulletAppState bulletAppState;
    Spatial spaceship;
    Spatial gameScene;
    PlayerInputState playerInputState;
    Spatial cursorNode;
    Node Floor;
    RigidBodyControl testFloor;
    
    /**
     * use this to simplify creating gameObject from assets
     * @param name the path of the asset
     * @param parent
     * @return 
     */
    
    public Spatial createGameObject(String name,Node parent)
    {
        Spatial newObject = assetManager.loadModel(name);
        parent.attachChild(newObject);
        newObject.setLocalTranslation(Vector3f.ZERO);
        return newObject;
    }
    
    public void quitGame()
    {
        stateManager.detach(playerInputState);
        stateManager.detach(bulletAppState);
        spaceship.getParent().detachAllChildren();
        rootNode.detachAllChildren();
        
    }
    public void startGame()
    {
        
        //initialize states
        playerInputState = new PlayerInputState();
        stateManager.attach(playerInputState);
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0,0,0));
        
        //when delete or initialize, operate "gameScene"
        gameScene = createGameObject("Scenes/newScene.j3o",rootNode);
        spaceship =  createGameObject("Models/Spaceship.j3o",(Node)gameScene);
        //spaceship add bullets and camera 
        spaceship.setLocalTranslation(new Vector3f(0,50,0));
        spaceship.getControl(SpaceshipControl.class).setPhysics(bulletAppState);//, null);
        spaceship.getControl(SpaceshipControl.class).setMass(1);
        ((Node)spaceship).getChild("Camera").getControl(CameraControl.class).setCamera(cam);
        
        Floor= new Node("floor");
        rootNode.attachChild(Floor);
        testFloor = new RigidBodyControl(0);
        testFloor.setCollisionShape(new BoxCollisionShape(new Vector3f(200,1,200)));
        bulletAppState.getPhysicsSpace().add(testFloor);
        testFloor.setPhysicsLocation(new Vector3f(0,0,0));
        Floor.addControl(testFloor);
        bulletAppState.setDebugEnabled(true);
        playerInputState.setPlayer(spaceship.getControl(SpaceshipControl.class));
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
        
        cursorNode = createGameObject("Models/cursorCube.j3o",guiNode); 
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        Vector2f mousePos = inputManager.getCursorPosition();
        cursorNode.setLocalTranslation(mousePos.x,mousePos.y,0);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
}
