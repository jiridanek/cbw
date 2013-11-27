/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.TauLeapingModel;

/**
 *
 *
 * @author Boštjan Cigan
 * 
 * Data input example:
 * R1 + R2 => R3 
 * R3 + R2 => R1
 * R4 => R3
 * R3 => R6
 * N = 6, M = 4 int, int
 * v0 = [-1,-1,1]; 2D array of ints
 * v1 = [-1,-1,1];
 * v2 = [-1,1];
 * v3 = [-1,1];
 * RS0 = [0,1,2]; 2D array of ints
 * RS1 = [2,1,0];
 * RS2 = [3,2];
 * RS3 = [2,5];
 * 
 * + k parameter za vsako reakcijo (če je dvosmerna sta dva)
 * Če je reakcija dvosmerna se to pretvori v dve vrstici v tabeli
 */
import fri.cbw.GenericTool.AbstractModelTool;
import javax.xml.ws.ServiceMode;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = AbstractModelTool.class)
public class TauLeapingModule extends AbstractModelTool {

    @Override
    public String getName() {
        return "Tau Leaping Module";
    }

    @Override
    public String getAuthor() {
        return "Boštjan Cigan, Jernej Hartman, Domen Mladovan";
    }

    @Override
    public Class getTopComponentClass() {
        return TauLeapingModelWindowTopComponent.class;
    }
    
}
