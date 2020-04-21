package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import com.google.gson.JsonElement;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    MainMenuAppState mainMenu;
    GameUIState gameUI;
    public AppSettings _settings;
    private BitmapText testText;
    
    Spatial gameScene;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        _settings = settings;
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        setDisplayStatView(false);
        setDisplayFps(false);
        
        flyCam.setEnabled(false);
        mainMenu = new MainMenuAppState();
        gameUI = new GameUIState();
        stateManager.attach(mainMenu);
        
       
    }
    public void startGame()
    {
        gameScene = assetManager.loadModel("Scenes/newScene.j3o");
        cam.setLocation(new Vector3f(20,20,20));
        cam.lookAt(new Vector3f(0,0,0), new Vector3f(0,1,0));
        rootNode.attachChild(gameScene);
        
        stateManager.detach(mainMenu);
        stateManager.attach(gameUI);
    }
    public void quitGame()
    {
        rootNode.detachChild(gameScene);
        stateManager.detach(gameUI);
        stateManager.attach(mainMenu);
    }
    
    public static void OnClickListener()
    {
        System.out.println("The world is yours2");
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
