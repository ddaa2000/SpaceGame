/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 *
 * @author ddaa
 */
public class GameData implements IData {
    
    public SpaceshipData spaceshipData = new SpaceshipData();
    public BatteryData[] batteryData = null;
    public BulletData[] bulletData = null;
    public RockData[] rockData = null;
    public int diff = 0;
}
