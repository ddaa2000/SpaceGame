/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author myx36
 */
public class BatteryData implements IData {
    private int life;
    private float distance; 
    private float counter;
    private Vector3f location;
    private Quaternion rotation; 
    
    public BatteryData(int Life, float Distance, float Counter, Vector3f Location,
            Quaternion Rotation){
        life = Life;
        distance = Distance;
        counter = Counter;
        location = Location;
        rotation = Rotation;
    }
    
    public int getLife(){
        return life;
    }
    public float getDis(){
        return distance;
    }
    public float getCounter(){
        return counter;
    }
    public Vector3f getLocation(){
        return location;
    }
    public Quaternion getRotation(){
        return rotation;
    }
}
