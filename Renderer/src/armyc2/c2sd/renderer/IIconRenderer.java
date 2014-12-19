package armyc2.c2sd.renderer;

import java.util.Map;

import armyc2.c2sd.renderer.utilities.ImageInfo;

/**
 * @deprecated
 * @author michael.spinelli
 */
public interface IIconRenderer {

	public Boolean CanRender(String symbolID, Map<String,String> modifiers);
	
	public ImageInfo RenderIcon(String symbolID, Map<String,String> modifiers);
	
	public String getRendererID();
	
}
