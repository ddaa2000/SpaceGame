/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import controls.AudioNodeControl;

/**
 *
 * @author ddaa
 */
public class SoundState extends AdvancedState {
    Node bgmNode;
    AudioNodeControl bgmControl;
    
     @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        bgmControl = new AudioNodeControl();
        bgmNode = new Node();
        bgmNode.addControl(bgmControl);
        bgmControl.setGameMain(gameMain);
        bgmControl.setAudio("Sounds/bg.wav", true, 1);
        bgmControl.playAudio();
        rootNode.attachChild(bgmNode);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
        rootNode.detachChild(bgmNode);
    }
    
    public void playSound(String name){
        Node soundNode;
        AudioNodeControl tempAudioControl;
        tempAudioControl = new AudioNodeControl();
        soundNode = new Node();
        soundNode.addControl(tempAudioControl);
        tempAudioControl.setGameMain(gameMain);
        tempAudioControl.setAudio(name, false, 1);
        tempAudioControl.playAudio();
        tempAudioControl.setDieDownTime(15);
        rootNode.attachChild(soundNode);
    }
}
