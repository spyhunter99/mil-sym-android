/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.sun.awt.geom;
import java.util.ArrayList;
/**
 *
 * @author Michael Deutch
 */
public class Vector {
    private ArrayList _vector=null;
    private Enumeration _elements=null;
    public Vector()
    {
        _vector=new ArrayList();
        _elements=new Enumeration(_vector);
    }
    protected Enumeration elements()
    {
        return _elements;
    }
    protected Object elementAt(int j)
    {
        return _vector.get(j);
    }
    protected int size()
    {
        return _vector.size();
    }
    protected Object get(int j)
    {   
        return _vector.get(j);
    }
    protected boolean isEmpty()
    {
        return _vector.isEmpty();
    }
    protected Object[] toArray2()
    {
        return _vector.toArray();
    }
    protected void toArray(Object[] obj)
    {
        int j=0;
        int n=obj.length;
        //for(j=0;j<obj.length;j++)
        for(j=0;j<n;j++)
        {
            if(_vector.size()>j)
                obj[j]=_vector.get(j);
            else
                obj[j]=null;
        }
    }
    protected void add(Object obj)
    {
        _vector.add(obj);
//        if(obj instanceof Order1)
//        {
//            double x=7;
//        }
    }
    protected void clear()
    {
        _vector.clear();
    }
}
