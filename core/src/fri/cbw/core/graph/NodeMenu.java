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
package fri.cbw.core.graph;

import fri.cbw.core.graph.GraphSceneImpl;
import fri.cbw.core.palette.Tool;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;

/**
 *
 * @author alex
 */
public class NodeMenu implements PopupMenuProvider, ActionListener {
    
    private static final String DELETE_NODE_ACTION = "deleteNodeAction"; // NOI18N
    private static final String EDIT_NODE_ACTION = "openEditorAction";
    
    private JPopupMenu menu;
    private IconNodeWidget node;

    private Point point;
    private GraphSceneImpl scene;
    private Tool tool;
    
    public NodeMenu(GraphSceneImpl scene, Tool t) {
        this.scene=scene;
        this.tool=t;
        menu = new JPopupMenu("Node Menu");
        JMenuItem item;
        
        item = new JMenuItem("Delete Node");
        item.setActionCommand(DELETE_NODE_ACTION);
        item.addActionListener(this);
        menu.add(item);
        
        item = new JMenuItem("Open Editor");
        item.setActionCommand(EDIT_NODE_ACTION);
        item.addActionListener(this);
        menu.add(item);
    }
    
    public JPopupMenu getPopupMenu(Widget widget,Point point){
        this.point=point;
        this.node=(IconNodeWidget)widget;
        
        System.out.println(widget.getClass());
        
        return menu;
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(DELETE_NODE_ACTION)){
            scene.removeNodeWithEdges((Tool)scene.findObject (node));
            scene.validate();
        }else if(e.getActionCommand().equals(EDIT_NODE_ACTION)){
            tool.getGt().openEditor();
        }
    }

    
}
