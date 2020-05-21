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

/**
 *
 * @author ddaa
 */
public class ParticleEffectControl extends AdvancedControl{

    ParticleEmitter effect = null;
    
    private float curTime = -1.0f;
    private static final float fxTime = 0.5f;
    public float startDelay = 0;
    private float startDelayCount = 0;
    private boolean emitted = false;
    
    @Override
    public void initialize(){
        super.initialize();
        prepareEffect(gameMain.getAssetManager());
        
        
        
        
    }
    
    private void prepareEffect(AssetManager assetManager) {
        int COUNT_FACTOR = 1;
        float COUNT_FACTOR_F = 1f;
        effect = new ParticleEmitter("Flame", ParticleMesh.Type.Triangle, 32 * COUNT_FACTOR);
        effect.setSelectRandomImage(true);
        effect.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, (float) (1f / COUNT_FACTOR_F)));
        effect.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
        effect.setStartSize(1.3f);
        effect.setEndSize(2f);
        effect.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        effect.setParticlesPerSec(0);
        effect.setGravity(0, -5f, 0);
        effect.setLowLife(.4f);
        effect.setHighLife(.5f);
        effect.getParticleInfluencer()
                .setInitialVelocity(new Vector3f(0, 7, 0));
        effect.getParticleInfluencer().setVelocityVariation(1f);
        effect.setImagesX(2);
        effect.setImagesY(2);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/flame.png"));
        effect.setMaterial(mat);
        System.out.println("effect created");
    }
    
    @Override
    protected void controlUpdate(float tpf)
    {
        super.controlUpdate(tpf);
        startDelayCount+=tpf;
        if(startDelayCount>=startDelay){
            if (effect != null && spatial.getParent() != null && !emitted) {
                emitted = true;
                curTime = 0;
                effect.setLocalTranslation(spatial.getLocalTranslation());
                effect.setLocalScale(1);
                spatial.getParent().attachChild(effect);
                effect.emitAllParticles();
                System.out.println("effect emitted");
            }
        }
        
        if (curTime >= 0) {
            curTime += tpf;
            if (curTime > fxTime) {
                curTime = -1;
                effect.removeFromParent();
                System.out.println("effect disappear");
                removeSelfObject();
            }
        }
            
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
}
