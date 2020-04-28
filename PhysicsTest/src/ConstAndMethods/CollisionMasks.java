/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConstAndMethods;

/**
 *
 * @author ddaa
 */
public class CollisionMasks {
    public static final int group_spaceship = 0x2,group_rock = 0x4,
            group_enemyBullet = 0x8,group_spaceshipBullet = 0x10,
            group_environment = 0x20,group_enemy = 0x40,group_default = 0x1;
    public static final int mask_spaceship = group_rock | group_enemyBullet | group_environment | group_enemy;
    public static final int mask_enemy     = group_spaceship | group_spaceshipBullet;
    public static final int mask_enemyBullet = group_spaceship | group_rock | group_environment;
    public static final int mask_rock = group_rock | group_spaceship | group_enemyBullet | group_spaceshipBullet;
    public static final int mask_spaceshipBullet = group_environment | group_rock | group_enemy;
    
}
