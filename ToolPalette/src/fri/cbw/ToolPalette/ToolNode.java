/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.ToolPalette;

/**
 *
 * @author Sašo
 */
import fri.cbw.ToolGraph.ToolWrapper;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;

public class ToolNode extends AbstractNode {

    private ToolWrapper tool = null;

    public ToolNode(ToolWrapper tool) {
        super(Children.LEAF);
        this.tool = tool;
        this.setDisplayName(getLabel());
    }

    public String getHtmlDisplayName() {
        return "<b>" + tool.getTitle() + "</b>";
    }


    private String getLabel() {
        String label = "<html>"
                + "<table cellspacing=\"0\" cellpadding=\"1\">"
                + "<tr>"
                + "<td><b>Title </b></td>"
                + "<td>" + tool.getTitle() + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td><b>Author </b></td>"
                + "<td>" + tool.getAuthor() + "</td>"
                + "</tr>"
                + "<tr>"
                + "</table>"
                + "</html>";

        return label;
    }

    @Override
    public Transferable drag() throws IOException {
        return tool;
    }
}