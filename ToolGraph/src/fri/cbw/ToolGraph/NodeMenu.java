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

import fri.cbw.GenericTool.ToolWrapper;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Lookup;

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
    private ToolGraphSceneImpl scene;
    
    public NodeMenu(ToolGraphSceneImpl scene) {
        this.scene=scene;
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
            scene.removeNodeWithEdges((ToolWrapper)scene.findObject (node));
            scene.validate();
        }else if(e.getActionCommand().equals(EDIT_NODE_ACTION)){
            ToolWrapper tool = (ToolWrapper)scene.findObject (node);
            tool.getNodeGenericTool().openEditor();
        }
    }

    
}
