/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package armyc2.c2sd.renderer.test3;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.ArrayList;

/**
 *
 * @author Michael Deutch
 */
public final class parseKML {
/**
     * @param kmlRecords
     * @param coordStrings new ArrayList passed by the caller to hold the coordinates
     * @throws Exception 
     */
    public static void parse(String kmlRecords, ArrayList<String> coordStrings) throws Exception {
//    String xmlRecords = "<data><employee><name>A</name>"
//        + "<title>Manager</title></employee></data>";
//    
//    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//    InputSource is = new InputSource();
//    
//    is.setCharacterStream(new StringReader(xmlRecords));
//    Document doc = db.parse(is);
//    NodeList nodes = doc.getElementsByTagName("employee");
//
//    for (int i = 0; i < nodes.getLength(); i++) {
//      Element element = (Element) nodes.item(i);
//
//      NodeList name = element.getElementsByTagName("name");
//      Element line = (Element) name.item(0);
//      System.out.println("Name: " + getCharacterDataFromElement(line));
//
//      NodeList title = element.getElementsByTagName("title");
//      line = (Element) title.item(0);
//      System.out.println("Title: " + getCharacterDataFromElement(line));
//    }

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(kmlRecords));
        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName("LineString");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);

            NodeList alt = element.getElementsByTagName("altitudeMode");
            Element line = (Element) alt.item(0);
            System.out.println("altitude mode: " + getCharacterDataFromElement(line));

            NodeList coords = element.getElementsByTagName("coordinates");
            line = (Element) coords.item(0);
            System.out.println("coordinates: " + getCharacterDataFromElement(line));
            coordStrings.add(getCharacterDataFromElement(line));
        }
        return;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public static void parseLLTR(String kmlRecords, ArrayList<String> coordStrings) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(kmlRecords));
        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName("outerBoundaryIs");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);

            //NodeList alt = element.getElementsByTagName("altitudeMode");
            //Element line = (Element) alt.item(0);
            //System.out.println("altitude mode: " + getCharacterDataFromElement(line));

            NodeList coords = element.getElementsByTagName("coordinates");
            Element line = (Element) coords.item(0);
            System.out.println("coordinates: " + getCharacterDataFromElement(line));
            coordStrings.add(getCharacterDataFromElement(line));
        }
        return;
    }

}
