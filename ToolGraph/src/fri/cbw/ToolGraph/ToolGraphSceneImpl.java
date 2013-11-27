/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.ToolGraph;

import fri.cbw.GenericTool.ToolWrapper;
import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import javax.swing.JFileChooser;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;

/**
 *
 * @author Sašo
 */
public class ToolGraphSceneImpl extends GraphScene<ToolWrapper, String>{
    private static ToolGraphSceneImpl istance;
    
    private LayerWidget mainLayer;
    private LayerWidget connectionLayer;
    private LayerWidget interractionLayer = new LayerWidget(this);
    private LayerWidget backgroundLayer = new LayerWidget(this);
    private WidgetAction moveAction = ActionFactory.createMoveAction();
    private Router router = RouterFactory.createFreeRouter();
    
    private WidgetAction connectAction = ActionFactory.createExtendedConnectAction(interractionLayer, new SceneConnectProvider(this));
    private WidgetAction reconnectAction = ActionFactory.createReconnectAction(new SceneReconnectProvider(this));
    private WidgetAction moveControlPointAction = ActionFactory.createFreeMoveControlPointAction();
    private WidgetAction selectAction = ActionFactory.createSelectAction(new ObjectSelectProvider());
    
    private EdgeMenu edgeMenu=new EdgeMenu(this);
    
    
    public static ToolGraphSceneImpl getnIstance(){
    if (istance == null)
        istance = new ToolGraphSceneImpl();		
    return istance;
  }

    
    public ToolGraphSceneImpl() {
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);
        
        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);
        
        addChild(interractionLayer);
        
        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {

            @Override
            public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
                return ConnectorState.ACCEPT;
            }

            @Override
            public void accept(Widget widget, Point point, Transferable transferable) {
                ToolWrapper t;
                try {
                    t = (ToolWrapper) getToolFromTranferable(transferable).clone();
                    Widget w = ToolGraphSceneImpl.this.addNode(t);
                    t.getNodeGenericTool().setNode(w);
                    w.setPreferredLocation(widget.convertLocalToScene(point));
                } catch (CloneNotSupportedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            private ToolWrapper getToolFromTranferable(Transferable transferable) {
                ToolWrapper t = null;
                try {
                    t = (ToolWrapper) transferable.getTransferData(ToolWrapper.DATA_FLAVOR);
                } catch (UnsupportedFlavorException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return t;
            }
        }));
    }
    
    @Override
    protected Widget attachNodeWidget(ToolWrapper tool) {
        IconNodeWidget widget = new IconNodeWidget(this);
        widget.setLabel(tool.getTitle());
        widget.setBorder(BorderFactory.createLineBorder(3));
        widget.setForeground(Color.yellow);
        
        widget.getActions().addAction(connectAction);
        widget.getActions().addAction(moveAction);
        
        mainLayer.addChild(widget);
        
        widget.getActions().addAction(ActionFactory.createPopupMenuAction(new NodeMenu(this)));
        
        return widget;
        
    }

    @Override
    protected Widget attachEdgeWidget(String e) {
        ConnectionWidget connection = new ConnectionWidget(this);
        connection.setRouter(router);
        connection.setToolTipText("Double-click for Add/Remove Control Point");
        connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        connection.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
        connection.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
        connectionLayer.addChild(connection);
        connection.getActions().addAction(reconnectAction);
        connection.getActions().addAction(createSelectAction());
        connection.getActions().addAction(ActionFactory.createAddRemoveControlPointAction());
        connection.getActions().addAction(moveControlPointAction);
        connection.getActions().addAction(ActionFactory.createPopupMenuAction(edgeMenu));
        return connection;
    }

    @Override
    protected void attachEdgeSourceAnchor(String edge, ToolWrapper oldSourceNode, ToolWrapper sourceNode) {
        ConnectionWidget widget = (ConnectionWidget) findWidget(edge);
        Widget sourceNodeWidget = findWidget (sourceNode);
        widget.setSourceAnchor(sourceNodeWidget != null ? AnchorFactory.createFreeRectangularAnchor(sourceNodeWidget, true) : null);
    }

    @Override
    protected void attachEdgeTargetAnchor(String edge, ToolWrapper oldTargetNode, ToolWrapper targetNode) {
        ConnectionWidget widget = (ConnectionWidget) findWidget(edge);
        Widget targetNodeWidget = findWidget (targetNode);
        widget.setTargetAnchor(targetNodeWidget != null ? AnchorFactory.createFreeRectangularAnchor(targetNodeWidget, true) : null);
    }
    
    
    private class ObjectSelectProvider implements SelectProvider {
        
        public boolean isAimingAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return false;
        }
        
        public boolean isSelectionAllowed(Widget widget, Point localLocation, boolean invertSelection) {
            return true;
        }
        
        public void select(Widget widget, Point localLocation, boolean invertSelection) {
            Object object = findObject(widget);
            
            if (object != null) {
                if (getSelectedObjects().contains(object))return;
                userSelectionSuggested(Collections.singleton(object), invertSelection);
            } else
                userSelectionSuggested(Collections.emptySet(), invertSelection);
        }
    }
    
    
    public void load () {
        JFileChooser chooser = new JFileChooser ();
        chooser.setDialogTitle ("Load Scene ...");
        chooser.setMultiSelectionEnabled (false);
        chooser.setFileSelectionMode (JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(getView()) == JFileChooser.APPROVE_OPTION) {
            clear();
            SceneSerializer.deserialize (this, chooser.getSelectedFile ());
            validate ();
        }
    }

    public void save () {
        
        for (ToolWrapper node : new ArrayList<ToolWrapper>(getNodes())) {
            try{
                node.getNodeGenericTool().getTc().doSave();
            }catch(Exception e){
                e.printStackTrace();
                /*DialogDisplayer.getDefault().notify(
                        new NotifyDescriptor.Message("Prišlo je do napake pri shranjevanju orodja!"));*/
                
                //TODO javi uporabniku da je prišlo do napak
            }
        }
        
        JFileChooser chooser = new JFileChooser ();
        chooser.setDialogTitle ("Save Scene ...");
        chooser.setMultiSelectionEnabled (false);
        chooser.setFileSelectionMode (JFileChooser.FILES_ONLY);
        if (chooser.showSaveDialog (getView ()) == JFileChooser.APPROVE_OPTION) {
            SceneSerializer.serialize (this, chooser.getSelectedFile ());
        }
    }
    
    public void clear() {
        
        /* Deleting connections and nodes form scene */
        for (String edge : new ArrayList<String>(getEdges())) {
            removeEdge(edge);
        }
        for (ToolWrapper node : new ArrayList<ToolWrapper>(getNodes())) {
            removeNode(node);
        }
        
        /* Closing all opend windows except core and palette */
        Set<TopComponent> odprti = TopComponent.getRegistry().getOpened();
        for (TopComponent topComponent : odprti) {
            if (!(topComponent.getClass().toString().contains("fri.cbw.core.CoreTopComponent") 
                    || topComponent.getClass().toString().contains("PaletteTopComponent"))) {
                topComponent.close();
            }
        }
    }
}
