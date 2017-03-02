package sec.geo.shape;

import java.util.ArrayList;
import sec.geo.GeoPoint;

public class Cake /*extends AComposite implements IPivot*/ {

    private GeoPoint pivot;
    private ArrayList elements;

    public Cake() {
        //super();
        elements = new ArrayList();
        pivot = new GeoPoint();
    }

    public void addLayer(Object layer) {    //was AExtrusion
        if (layer instanceof Polyarc) {
            ((Polyarc) layer).setPivot(pivot);
            elements.add(layer);
        } else if (layer instanceof Radarc) {
            ((Radarc) layer).setPivot(pivot);
            elements.add(layer);
        } else {
            throw new IllegalArgumentException();
        }
    }

    //@Override
    public void setPivot(GeoPoint pivot) {
        this.pivot = pivot;
        for (Object layer : elements) {
            if (layer instanceof Polyarc) {
                ((Polyarc) layer).setPivot(pivot);
                elements.add(layer);
            } else if (layer instanceof Radarc) {
                ((Radarc) layer).setPivot(pivot);
                elements.add(layer);
            } else if (layer instanceof Circle) {
                ((Circle) layer).setPivot(pivot);
                elements.add(layer);
            }
            //add other cases AArc, Circle etc
        }
    }

    public ArrayList getElements() {
        return elements;
    }
}
