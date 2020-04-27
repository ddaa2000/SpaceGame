package mygame;

import Web.DemoApplication;
import states.PlayerInputState;
import controls.CameraControl;
import controls.SpaceshipControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import java.io.IOException;
import states.GameUIState;
import states.MainMenuAppState;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private BulletAppState bulletAppState;
    Spatial spaceship;
    Spatial gameScene;
    Spatial camera;
    PlayerInputState playerInputState;
    Spatial cursorNode;
    
    MainMenuAppState mainMenu;
    GameUIState gameUI;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
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
    

    public static void OnClickListener()
    {
        System.out.println("The world is yours2");
    }
    public void quitGame()
    {
        stateManager.detach(gameUI);
        stateManager.attach(mainMenu);
    }
    public void startGame()
    {
        stateManager.detach(mainMenu);
        stateManager.attach(gameUI);
    }
    public AppSettings getSettings()
    {
        return settings;
    }
    @Override
    public void simpleInitApp() {
        playerInputState = new PlayerInputState();
        stateManager.attach(playerInputState);
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0,0,0));
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        gameScene = createGameObject("Scenes/newScene.j3o",rootNode);
        spaceship =  createGameObject("Models/Spaceship.j3o",(Node)gameScene);
        cursorNode = createGameObject("Models/cursorCube.j3o",guiNode);
        

        spaceship.setLocalTranslation(new Vector3f(0,50,0));
        spaceship.getControl(SpaceshipControl.class).setPhysics(bulletAppState);
        spaceship.getControl(SpaceshipControl.class).setMass(1);
        ((Node)spaceship).getChild("Camera").getControl(CameraControl.class).setCamera(cam);
        rootNode.attachChild(geom);

        Node node = new Node("floor");
        rootNode.attachChild(node);
        RigidBodyControl testFloor = new RigidBodyControl(0);
        testFloor.setCollisionShape(new BoxCollisionShape(new Vector3f(200,1,200)));
        bulletAppState.getPhysicsSpace().add(testFloor);
        testFloor.setPhysicsLocation(new Vector3f(0,0,0));
        node.addControl(testFloor);
        bulletAppState.setDebugEnabled(true);
        playerInputState.setPlayer(spaceship.getControl(SpaceshipControl.class));
        
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        setDisplayStatView(false);
        setDisplayFps(false);
        
        mainMenu = new MainMenuAppState();
        gameUI = new GameUIState();
        stateManager.attach(mainMenu);
        
        try
        {
            DemoApplication.httpConnection();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        Vector2f mousePos = inputManager.getCursorPosition();
        cursorNode.setLocalTranslation(mousePos.x,mousePos.y,0);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

}
