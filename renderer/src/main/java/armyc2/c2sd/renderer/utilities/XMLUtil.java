package armyc2.c2sd.renderer.utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author michael.spinelli
 */
public class XMLUtil
{

    /**
     * This method returns the list of objects in a list form.
     * 
     * @param document the document to pull the list from
     * @param tag the name of the element
     * @return the list of items
     */
    public static NodeList getItemList(Document document, String tag)
    {
        return document.getElementsByTagName(tag);
    }

    /**
     * This method parse the xml tag value.
     * 
     * @param node the node to pull an element from
     * @param tag the name of the element
     * @return the text content of the element
     */
    public static String parseTagValue(Node node, String tag)
    {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            return element.getElementsByTagName(tag).item(0).getTextContent();
        }
        return null;
    }
}
