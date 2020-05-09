/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConstAndMethods;

import com.jme3.scene.Spatial;
import controls.BatteryControl;
import controls.BulletControl;
import controls.SpaceshipControl;

/**
 *
 * @author ddaa
 */
public class Types {
    private static String rockName = "Rock";
    public static boolean isSpaceship(Spatial spatial){
        if(spatial==null)
            return false;
        return spatial.getControl(SpaceshipControl.class)!=null;
    }
    public static boolean isSpaceshipBullet(Spatial spatial){
        if(spatial==null)
            return false;
        if(spatial.getControl(BulletControl.class)==null)
            return false;
        return !spatial.getControl(BulletControl.class).isEnemyBullet;
    }
    public static boolean isEnemyBullet(Spatial spatial){
        if(spatial==null)
            return false;
        if(spatial.getControl(BulletControl.class)==null)
            return false;
        return spatial.getControl(BulletControl.class).isEnemyBullet;
    }
    public static boolean isBattery(Spatial spatial){
        if(spatial==null)
            return false;
        return spatial.getControl(BatteryControl.class)!=null;
    }
    public static boolean isRock(Spatial spatial){
        if(spatial==null)
            return false;
        return spatial.getName().equals(rockName);
    }
}
