package mygame;

import Web.DemoApplication;
import Web.Result;
import cn.ac.mryao.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import states.PlayerInputState;
import controls.CameraControl;
import controls.SpaceshipControl;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetKey;
import com.jme3.asset.TextureKey;
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
import static com.jme3.scene.plugins.fbx.mesh.FbxLayerElement.Type.Texture;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import controls.AdvancedControl;
import controls.BatteryControl;
import controls.RockControl;
import controls.SimplePhysicsControl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import states.Game1;
import states.GameUIState;
import states.MainMenuAppState;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public BulletAppState bulletAppState;
    Spatial spaceship;
    Spatial gameScene;
    Spatial camera;
    PlayerInputState playerInputState;
    Spatial cursorNode;
    
    Game1 game;
    
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
        if(newObject.getControl(AdvancedControl.class)!=null)
            newObject.getControl(AdvancedControl.class).setGameMain(this);
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
        game.quitGame();
        stateManager.detach(game);
        
    }
    public void startGame()
    {
        stateManager.detach(mainMenu);
        stateManager.attach(gameUI);
        game.startGame();
    }
    public AppSettings getSettings()
    {
        return settings;
    }
    
    public void init_old()
    {
        playerInputState = new PlayerInputState();
        stateManager.attach(playerInputState);
        
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        gameScene = createGameObject("Scenes/newScene.j3o",rootNode);
        spaceship =  createGameObject("Models/Spaceship.j3o",(Node)gameScene);
        cursorNode = createGameObject("Models/cursorCube.j3o",guiNode);
        
        
        
        
        
        
        
        
       // Material rockmat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //((Geometry)rock).setMaterial(rockmat);

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
    }
    @Override
    public void simpleInitApp() {
        
        DemoApplication.getAccessToken();
        
        byte[] data = null;
        try
        {
            data = DemoApplication.registerUser();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        TextureKey key = new TextureKey("Image.jpeg");
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        if(stream==null)
            System.out.println("stream is null");
        Texture texture = assetManager.loadAssetFromStream(key, stream);
        assetManager.addToCache(key, texture);
        
        
        
       // init_old();
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        setDisplayStatView(false);
        setDisplayFps(false);
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0,0,0));
        
        mainMenu = new MainMenuAppState();
        gameUI = new GameUIState();
        stateManager.attach(mainMenu);
        
        game = new Game1();
        stateManager.attach(game);

        
    }
    public void Pause()
    {
        paused = true;
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code


    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

}
