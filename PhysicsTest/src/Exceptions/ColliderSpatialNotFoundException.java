/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

import com.jme3.scene.Spatial;

/**
 *
 * @author ddaa
 */
public class ColliderSpatialNotFoundException extends RuntimeException {
    public Spatial collisionObject = null;
    public String reason;
    public ColliderSpatialNotFoundException(String reason,Spatial collisionObject)
    {
        super(reason+" Can not find the other collider");
        this.reason = reason+" Can not find the other collider";
        this.collisionObject = collisionObject;
        
    }
}
