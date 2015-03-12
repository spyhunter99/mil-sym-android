/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package armyc2.c2sd.renderer.utilities;

/**
*
* @author michael.spinelli
*/
public class UnitDef {

	/**
	* Just a category in the milstd hierarchy.
	* Not something we draw.
	* WILL NOT RENDER
	*/
   static public final int DRAW_CATEGORY_DONOTDRAW = 0;
    /**
	* Shape is defined by a single point
	* 0 control points
	*/
   static public final int DRAW_CATEGORY_POINT = 8;


   private String _basicSymbolId = "";
   private String _description = "";
   private int _drawCategory = 0;
   private String _hierarchy = "";
   private String _path = "";
   /**
    * 
    * @param symbolID
    * @param description
    * @param idc drar
    * @param hierarchy
    * @param path
    */
   public UnitDef(String basicSymbolID, String description, int drawCategory, String hierarchy, String path)
   {
           //Set fields to their default values.
           _basicSymbolId = "";
           _description = "";
           _drawCategory = drawCategory;
           _hierarchy = hierarchy;
           _path = path;
   }



   /**
    * The basic 15 character basic symbol Id.
    */
   
   public String getBasicSymbolId()
   {
           return _basicSymbolId;
   }


   /**
    * The description of this tactical graphic.  Typically the name of the tactical graphic in MIL-STD-2525B.
    */
   
   public String getDescription()
   {
           return _description;
   }


   /**
    * 8 is singlepoint unit, 0 is category
    * (do not draw because it's just a category node in the tree)
    * @return
    */
   public int getDrawCategory()
   {
           return _drawCategory;
   }



   /**
    * Defines where the symbol goes in the ms2525 hierarchy.
    * 2.X.whatever
    */

   public String getHierarchy()
   {
           return _hierarchy;
   }



    /**
    * Defines where the symbol goes in the ms2525 hierarchy.
    * STBOPS.INDIV.WHATEVER
    */
   /*private String _strAlphaHierarchy;
   public String getAlphaHierarchy()
   {
           return _strAlphaHierarchy;
   }//*/


           
	/**
	* Defines where the symbol goes in the ms2525 hierarchy.
	 * Warfighting/something/something
	*/
   public String getFullPath()
   {
           return _path;
   }



}
