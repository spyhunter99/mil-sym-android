/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.geo.utilities;

/**
 *
 * @author Michael Deutch
 */
public class StringBuilder {
    private String str=null;
    public StringBuilder()
    {
        str="";
    }
    public StringBuilder(String str2)
    {
        str=str2;
    }
    public void append(Object obj)
    {
        if(obj instanceof String)
        {
            String str2=(String)obj;
            str+=str2;
        }
        else if (obj instanceof Double)
        {
            double d=(Double)obj;
            str+= Double.toString(d);
        }
    }
    @Override
    public String toString()
    {
        return str;
    }
    public void replace(int first, int last, String str2)
    {
        String startStr="", endStr="";
        startStr=str.substring(0,first);
        endStr=str.substring(last, str.length());
        str=startStr+str2+endStr;
    }
    public int indexOf(String str2)
    {
        return str.indexOf(str2);
    }
    public int indexOf2(String str2, int index)
    {
        return str.indexOf(str2,index);
    }
    
}
