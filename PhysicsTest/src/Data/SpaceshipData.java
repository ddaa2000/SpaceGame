/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Data.IData;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author Gengyipeng
 */
public class SpaceshipData implements IData {
    public Vector3f translation;
    public Quaternion rotation;
    public Vector3f linearvelocity;
    public Vector3f angularvelocity;
    public int life;
    public float counter;
    public boolean CanShoot;

}
