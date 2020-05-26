/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

/**
 *
 * @author ddaa
 */
public class AudioNodeControl extends AdvancedControl{

    AudioNode audioNode = null;
    
    private float curTime = -1f;
    private float fxTime = 10f;
    public float startDelay = 0;
    private float startDelayCount = 0;
    private boolean emitted = false;
    
    @Override
    public void initialize(){
        super.initialize();
    }
    
    public void setDieDownTime(float dieDownTime){
        curTime = 0f;
        fxTime = dieDownTime;
    }
    
    public void setAudio(String name, Boolean loop, float volume ){
        if(audioNode==null){
            audioNode = new AudioNode(gameMain.getAssetManager(),name, false);
            ((Node)spatial).attachChild(audioNode);
        }
            
        audioNode.setPositional(false);      
        audioNode.setLooping(loop);
        if(volume!=0)
            audioNode.setVolume(volume);
        
    }
    public void playAudio(){
        audioNode.play();
    }
    
    @Override
    protected void controlUpdate(float tpf)
    {
        super.controlUpdate(tpf);

        if (curTime >= 0) {
            curTime += tpf;
            if (curTime > fxTime) {
                curTime = -1;
                System.out.println("effect disappear");
                removeSelfObject();
            }
        }
            
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
}
