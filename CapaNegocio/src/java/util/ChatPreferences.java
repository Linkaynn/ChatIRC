package util;

import javax.ejb.Stateful;

@Stateful
public class ChatPreferences implements ChatPreferencesLocal {
    
    private int color;  
    /**
     *
     * @return the color of the UI
     */
    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }
}
