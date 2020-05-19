/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controls;

/**
 *
 * @author ddaa
 */
public interface IPlayerControl {
    public void setHorizontalRotationValue(double value);
    public void setVerticalRotationValue(double value);
    public void speedUp(double strength);
    public void speedDown(double strength);
    public void setPlaneRotationValue(double value);
    public void attack();
}