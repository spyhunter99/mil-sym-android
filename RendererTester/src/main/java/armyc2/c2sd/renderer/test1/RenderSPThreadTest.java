/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armyc2.c2sd.renderer.test1;

import android.util.Log;
import android.util.SparseArray;
import armyc2.c2sd.renderer.MilStdIconRenderer;
import armyc2.c2sd.renderer.utilities.ImageInfo;
import java.util.Random;

/**
 *
 * @author michael.spinelli
 */
public class RenderSPThreadTest implements Runnable
{
    boolean _result = true;

    String _symbolID = "";
    String _name = "";
    boolean _randomAffiliation = false;
    boolean _done = false;
    
    private boolean threadTest(String symbolID, String threadName, boolean randomAffiliation)
    {
        MilStdIconRenderer mir = MilStdIconRenderer.getInstance();
        String foo = "UFHNPASGWDLMJK";
        String foo2 = "APCDXF";
        char[] affiliations = foo.toCharArray();
        char[] stati = foo2.toCharArray();
    	SparseArray<String> modifiers = new SparseArray();
    	//populateModifiersForUnits(modifiers);
    	SparseArray<String> attributes = new SparseArray();
    	
        String id = new String(symbolID);
    	int count = 1000;
    	float fcount = count;
        ImageInfo ii= null;
        boolean success = true;
    	for(int i = 1; i <= count; i++)
    	{
            if(randomAffiliation)
            {
                Random r = new Random();
                char affiliation = affiliations[r.nextInt(13)];
                char status = affiliations[r.nextInt(5)];
                id = id.substring(0, 1) + affiliation + id.substring(2,3) + status + id.substring(4);
            }
            ii = mir.RenderIcon(id, modifiers, attributes);
            if(ii == null || ii.getImage() == null)
                success = false;
                
            if(i % 100 == 0)
            {
                    String message = symbolID + ": " + String.valueOf((int)(i/fcount * 100f)) + "% complete";
                    Log.i("threadTest",message);
            }
    	}
        _done = true;
        Log.i("threadTest","Thread \"" + threadName + "\" Done");
        Log.println(Log.INFO,threadName," done.");
        if(success == false)
            Log.println(Log.INFO,threadName," failed to render all symbols.");
        
        threadName = threadName + ": " + String.valueOf(success);
        return success;

    }
    
    public void run()
    {
        Random r = new Random();
        String[] symbolIDs = {"SUPP-----------","SUPPT----------","SUPPV----------","SUPPT----------","SUPPL----------"};
        _result = threadTest(symbolIDs[r.nextInt(4)], _name, _randomAffiliation);
    }
    
    public boolean getResult()
    {
        return _result;
    }
    

}
