package info.shreygupta.testapp.Models;

import java.util.ArrayList;

/**
 * Created by XCODER on 2/24/2018.
 */

public class Configurations {
    String title;
    ArrayList<String> options;
    int selected;
    String shortDesc;
    String longDesc;

    public Configurations(String title, ArrayList<String> options, String shortDesc, String longDesc) {
        this.title = title;
        this.options = options;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }

    public Configurations() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
