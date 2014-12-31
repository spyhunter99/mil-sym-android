package sec.geo.shape;

import java.util.ArrayList;

public class Track /*extends AComposite*/ {

    ArrayList elements;

    public Track() {
        elements = new ArrayList();
    }

    public void addRoute(Route route) {
        elements.add(route);
    }
}
