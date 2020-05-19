/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector2f;
import com.jme3.system.AppSettings;
import controls.IPlayerControl;
import mygame.Main;

/**
 *
 * @author ddaa
 */
public class PlayerInputState extends AbstractAppState {
    
    Main gameApplication;
    AppSettings settings;
    IPlayerControl playerCT = null;
    InputManager inputManager;
    public void setPlayer(IPlayerControl playerCT)
    {
        this.playerCT = playerCT;
    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        gameApplication = (Main)app;
        settings = gameApplication.getSettings();
        inputManager = gameApplication.getInputManager();
        Keys();   
    }
    
    private void Keys() {
        
        final Trigger Trigger_W = new KeyTrigger(KeyInput.KEY_W); 
        final Trigger Trigger_S = new KeyTrigger(KeyInput.KEY_S);
        final Trigger Trigger_A = new KeyTrigger(KeyInput.KEY_A); 
        final Trigger Trigger_D = new KeyTrigger(KeyInput.KEY_D);
        final Trigger Trigger_Space = new KeyTrigger(KeyInput.KEY_SPACE);
        final Trigger Trigger_MouseMove_Left = new MouseAxisTrigger(MouseInput.AXIS_X, false);
        final Trigger Trigger_MouseMove_Right = new MouseAxisTrigger(MouseInput.AXIS_X, true);
        final Trigger Trigger_MouseMove_Up = new MouseAxisTrigger(MouseInput.AXIS_Y, true);
        final Trigger Trigger_MouseMove_Down = new MouseAxisTrigger(MouseInput.AXIS_Y, false);
        
        inputManager.deleteMapping( SimpleApplication.INPUT_MAPPING_MEMORY );
        
        inputManager.addMapping("Speed_Up", Trigger_W);
        inputManager.addMapping("Speed_Down", Trigger_S);
        inputManager.addMapping("Left_Turn", Trigger_A);
        inputManager.addMapping("Right_Turn", Trigger_D);
        inputManager.addMapping("Shoot", Trigger_Space);
        inputManager.addListener(analogListener, "Speed_Up", "Speed_Down", "Left_Turn", "Right_Turn", "Shoot");
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
         Vector2f cursorLocation = inputManager.getCursorPosition();
        Vector2f cursorLocRela = cursorLocation.subtract(new Vector2f(settings.getWidth()/2, settings.getHeight()/2));
        float proportionX = cursorLocRela.x/settings.getWidth()*2;
        float proportionY = cursorLocRela.y/settings.getHeight()*2;
        if(playerCT != null)
        {
            if(Math.abs(proportionX)>0.1) //if the proportion is too small, then keep the form
                playerCT.setHorizontalRotationValue(proportionX);
            if(Math.abs(proportionY)>0.1)
                playerCT.setVerticalRotationValue(proportionY);
        }
       
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
    private final AnalogListener analogListener = new AnalogListener() {  
        @Override
        public void onAnalog(String name, float intensity, float tpf) {   
            if(playerCT!=null)
            {
                if(name.equals("Speed_Up")){
                playerCT.speedUp(1);
                }
                else if(name.equals("Speed_Down")){
                    playerCT.speedDown(1);
                }
                else if(name.equals("Left_Turn")){
                    playerCT.setPlaneRotationValue(-1);//tpf is a wrong argument, I will change it later 
                }
                else if(name.equals("Right_Turn"))
                {
                    playerCT.setPlaneRotationValue(1);//tpf is a wrong argument, I will change it later 
                }
                else if(name.equals("Left_Rotate")){
                    playerCT.setHorizontalRotationValue(1);
                }
                else if(name.equals("Shoot")){
                    playerCT.attack();
                }
            }
            
        } 
    };
}
