/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;



/**
 *
 * @author ddaa
 */
public class MyButton extends Button {
    
    public float preferred_x = 50;
    public float preferred_y = 20;
    public MyButton(String s) {
        super(s);
        setTextHAlignment(HAlignment.Center);
        setTextVAlignment(VAlignment.Center);
        super.setPreferredSize(new Vector3f(preferred_x,preferred_y,0));
    }
    @Override
    public void setPreferredSize(Vector3f dest)
    {
        preferred_x = dest.x;
        preferred_y = dest.y;
        super.setPreferredSize(new Vector3f(preferred_x,preferred_y,0));
    }
    @Override
    public void setLocalTranslation(Vector3f dest)
    {
        super.setLocalTranslation(dest.x-preferred_x/2, dest.y-preferred_y/2, 0);
    }
    
    public void setLocalTranslation(float x,float y)
    {
        super.setLocalTranslation(x-preferred_x/2, y-preferred_y/2,0);
    }
    
}
