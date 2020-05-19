/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Data.IData;

/**
 *
 * @author ddaa
 */
public interface IGameSavable {
    public IData save();
    public void load(IData data);
}
