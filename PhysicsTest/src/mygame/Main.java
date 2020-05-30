package mygame;

import Data.GameData;
import Data.SpaceshipData;
import Exceptions.wrongUserInfoException;
import Web.DemoApplication;
import Web.Result;
import Web.WebSocketClient;
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
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
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
import states.SoundState;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static boolean offLineMode = false;
    
    
    public BulletAppState bulletAppState;
    Spatial spaceship;
    Spatial gameScene;
    Spatial camera;
    PlayerInputState playerInputState;
    Spatial cursorNode;
    
    Game1 game;
    
    SoundState soundState;
    MainMenuAppState mainMenu;
    GameUIState gameUI;
    
    private Texture QR_code = null;
    private TextureKey QR_key = null;
    private byte[] QR_data = null;
    
    public void loadQRAsset(){
        TextureKey key = new TextureKey("Image.jpeg");
        ByteArrayInputStream stream = new ByteArrayInputStream(QR_data);
        if(stream==null)
            System.out.println("stream is null");
        QR_code = assetManager.loadAssetFromStream(key, stream);
        assetManager.addToCache(key, QR_code);
    }
    
    public Texture getQRCode(){
        return QR_code;
    }
    
    public GameUIState getGameUIState(){
        return gameUI;
    }
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
        try{
            WebSocketClient.group.shutdownGracefully();
        }
        catch(Exception ex){
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
        Spatial newObject = assetManager.loadModel(name);
        parent.attachChild(newObject);
        newObject.setLocalTranslation(Vector3f.ZERO);
        if(newObject.getControl(AdvancedControl.class)!=null)
            newObject.getControl(AdvancedControl.class).setGameMain(this);
        return newObject;
    }
    public void gameFailed()
    {
        gameUI.gameOver();
        stateManager.detach(game);
    }
    public void victory(){
        gameUI.victory();
        stateManager.detach(game);
    }
    public void quitGame()
    {
        mainMenu.showMainPanel();
        mainMenu.setEnabled(true);
        stateManager.detach(gameUI);
        //stateManager.attach(mainMenu);
        stateManager.detach(game);
        
    }
    public void startGame(int diff)
    {
        
        mainMenu.hideMainPanel();
        mainMenu.setEnabled(false);
        //stateManager.detach(mainMenu);
        game.setLoadGame(false);
        game.diff = diff;
        stateManager.attach(gameUI);
        stateManager.attach(game);
    }
    
    public void restartGame(){
        mainMenu.hideMainPanel();
        mainMenu.setEnabled(false);
        quitGame();
        startGame(game.diff);
    }
    
    public void loadGame(){
        mainMenu.hideMainPanel();
        mainMenu.setEnabled(false);
        //stateManager.detach(mainMenu);
        stateManager.attach(gameUI);
        game.setLoadGame(true);
        stateManager.attach(game);
    }
    
    public void saveGame(){
        if(game!=null && game.isInitialized())
            game.saveGame();
    }
    
    
    public Game1 getGame(){
        return game;
    }
    public AppSettings getSettings()
    {
        return settings;
    }
    
    public SoundState getSoundState(){
        return soundState;
    }
    
    @Override
    public void simpleInitApp() {
        try{
            DemoApplication.getAccessToken();
        }catch(Exception ex){
            offLineMode = true;
        }
        
        
       
        try
        {
            QR_data = DemoApplication.registerUser();
        }
        catch(IOException ex)
        {
            offLineMode = true;
        }
        catch(Exception ex)
        {
            offLineMode = true;
        }
        
        
        
        
        
       // init_old();
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        setDisplayStatView(false);
        setDisplayFps(false);
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        mainMenu = new MainMenuAppState();
        gameUI = new GameUIState();
        soundState = new SoundState();
        stateManager.attach(soundState);
        stateManager.attach(mainMenu);
        
        game = new Game1();
       // stateManager.attach(game);
        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.SceneAndObjects);
        bloom.setBloomIntensity(2);
        fpp.addFilter(bloom);

        
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
