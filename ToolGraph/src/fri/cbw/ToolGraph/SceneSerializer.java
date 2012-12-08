/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package fri.cbw.ToolGraph;

import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import org.openide.xml.XMLUtil;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import javax.xml.bind.DatatypeConverter;

/**
 * @author David Kaspar
 */
public class SceneSerializer {

    private static final String SCENE_ELEMENT = "Scene"; // NOI18N
    private static final String VERSION_ATTR = "version"; // NOI18N

    private static final String SCENE_NODE_COUNTER_ATTR = "nodeIDcounter"; // NOI18N
    private static final String SCENE_EDGE_COUNTER_ATTR = "edgeIDcounter"; // NOI18N

    private static final String NODE_ELEMENT = "Node"; // NOI18N
    private static final String NODE_ID_ATTR = "id"; // NOI18N
    private static final String NODE_NAME_ATTR = "name"; // NOI18N
    private static final String NODE_X_ATTR = "x"; // NOI18N
    private static final String NODE_Y_ATTR = "y"; // NOI18N
    private static final String NODE_PREFIX = "node"; // NOI18N

    private static final String EDGE_ELEMENT = "Edge"; // NOI18N
    private static final String EDGE_ID_ATTR = "id"; // NOI18N
    private static final String EDGE_SOURCE_ATTR = "source"; // NOI18N
    private static final String EDGE_TARGET_ATTR = "target"; // NOI18N

    private static final String VERSION_VALUE_1 = "1"; // NOI18N

    // call in AWT to serialize scene
    public static void serialize (ToolGraphSceneImpl scene, File file) {
        Document document = XMLUtil.createDocument (SCENE_ELEMENT, null, null, null);
        Integer nodeCounter = 0;
        HashMap <ToolWrapper, String> nodeMap = new HashMap<ToolWrapper, String>();
        
        Node sceneElement = document.getFirstChild ();
        setAttribute (document, sceneElement, VERSION_ATTR, VERSION_VALUE_1);

        for (ToolWrapper node : scene.getNodes ()) {
            Element nodeElement = document.createElement (NODE_ELEMENT);
            
            String nodeId = NODE_PREFIX + nodeCounter++;
            setAttribute (document, nodeElement, NODE_ID_ATTR, nodeId);
            setAttribute (document, nodeElement, NODE_NAME_ATTR, node.getTitle());
            
            Widget widget = scene.findWidget (node);
            Point location = widget.getPreferredLocation ();
            setAttribute (document, nodeElement, NODE_X_ATTR, Integer.toString (location.x));
            setAttribute (document, nodeElement, NODE_Y_ATTR, Integer.toString (location.y));
            
            setNodeValue (document, nodeElement, node);
            
            nodeMap.put(node, nodeId);
            sceneElement.appendChild (nodeElement);
        }
        for (String edge : scene.getEdges ()) {
            Element edgeElement = document.createElement (EDGE_ELEMENT);
            setAttribute (document, edgeElement, EDGE_ID_ATTR, edge);
            ToolWrapper sourceNode = scene.getEdgeSource (edge);
            if (sourceNode != null)
                setAttribute (document, edgeElement, EDGE_SOURCE_ATTR, nodeMap.get(sourceNode));
            ToolWrapper targetNode = scene.getEdgeTarget (edge);
            if (targetNode != null)
                setAttribute (document, edgeElement, EDGE_TARGET_ATTR, nodeMap.get(targetNode));
            sceneElement.appendChild (edgeElement);
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream (file);
            XMLUtil.write (document, fos, "UTF-8"); // NOI18N
        } catch (Exception e) {
            Exceptions.printStackTrace (e);
        } finally {
            try {
                if (fos != null) {
                    fos.close ();
                }
            } catch (Exception e) {
                Exceptions.printStackTrace (e);
            }
        }
    }

    // call in AWT to deserialize scene
    public static void deserialize (ToolGraphSceneImpl scene, File file) {
        Node sceneElement = getRootNode (file);
        HashMap <String, ToolWrapper> nodeMap = new HashMap<String, ToolWrapper>();
        
        if (! VERSION_VALUE_1.equals (getAttributeValue (sceneElement, VERSION_ATTR)))
            return;
        
        for (Node element : getChildNode (sceneElement)) {
            if (NODE_ELEMENT.equals (element.getNodeName ())) {
                ToolWrapper node = getNodeValue(element);
                int x = Integer.parseInt (getAttributeValue (element, NODE_X_ATTR));
                int y = Integer.parseInt (getAttributeValue (element, NODE_Y_ATTR));
                String id = getAttributeValue (element, NODE_ID_ATTR);
                
                Widget nodeWidget = scene.addNode (node);
                nodeWidget.setPreferredLocation (new Point (x, y));
                nodeMap.put(id, node);
            } else if (EDGE_ELEMENT.equals (element.getNodeName ())) {
                String edge = getAttributeValue (element, EDGE_ID_ATTR);
                String sourceNode = getAttributeValue (element, EDGE_SOURCE_ATTR);
                String targetNode = getAttributeValue (element, EDGE_TARGET_ATTR);
                
                ToolWrapper sourceTool = nodeMap.get(sourceNode);
                ToolWrapper targetTool = nodeMap.get(targetNode);
                
                scene.addEdge (edge);
                scene.setEdgeSource (edge, sourceTool);
                scene.setEdgeTarget (edge, targetTool);
            }
        }
    }

    private static void setAttribute (Document xml, Node node, String name, String value) {
        NamedNodeMap map = node.getAttributes ();
        Attr attribute = xml.createAttribute (name);
        attribute.setValue (value);
        map.setNamedItem (attribute);
    }
    
    private static void setNodeValue (Document xml, Node node, ToolWrapper value) {
        try {
            CDATASection data = xml.createCDATASection(encodeToolWrapper(value));
            node.appendChild(data);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
            
    }
    
    private static String getAttributeValue (Node node, String attr) {
        try {
            if (node != null) {
                NamedNodeMap map = node.getAttributes ();
                if (map != null) {
                    node = map.getNamedItem (attr);
                    if (node != null)
                        return node.getNodeValue ();
                }
            }
        } catch (DOMException e) {
            Exceptions.printStackTrace (e);
        }
        return null;
    }
    
    private static ToolWrapper getNodeValue (Node node) {
        try {
            return decodeToolWrapper(node.getTextContent());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
    
    private static Node getRootNode (File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream (file);
            Document doc = XMLUtil.parse (new InputSource (is), false, false, new ErrorHandler() {
                public void error (SAXParseException e) throws SAXException {
                    throw new SAXException (e);
                }

                public void fatalError (SAXParseException e) throws SAXException {
                    throw new SAXException (e);
                }

                public void warning (SAXParseException e) {
                    Exceptions.printStackTrace (e);
                }
            }, null);
            return doc.getFirstChild ();
        } catch (Exception e) {
            Exceptions.printStackTrace (e);
        } finally {
            try {
                if (is != null)
                    is.close ();
            } catch (IOException e) {
                Exceptions.printStackTrace (e);
            }
        }
        return null;
    }

    
    private static Node[] getChildNode (Node node) {
        NodeList childNodes = node.getChildNodes ();
        Node[] nodes = new Node[childNodes != null ? childNodes.getLength () : 0];
        for (int i = 0; i < nodes.length; i++)
            nodes[i] = childNodes.item (i);
        return nodes;
    }
    
    /**
     * Serializes the ToolWrapper to a Base64 string.
     * 
     * @param o
     * @return
     * @throws IOException 
     */
     
    private static String encodeToolWrapper( ToolWrapper o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();

        return DatatypeConverter.printBase64Binary(baos.toByteArray());
    }
    
    /**
     * Decodes Base64 string and deserializes it into a ToolWrapper object
     * 
     * @param s
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    
     private static ToolWrapper decodeToolWrapper( String s ) throws IOException, ClassNotFoundException {
        byte [] data = DatatypeConverter.parseBase64Binary( s );
        System.out.println(s);
        System.out.println(data);
        NBPlatformObjectInputStream ois = new NBPlatformObjectInputStream( new ByteArrayInputStream( data ) );
        ToolWrapper o = (ToolWrapper) ois.readObject();
        ois.close();
        
        return o;
    }

}
