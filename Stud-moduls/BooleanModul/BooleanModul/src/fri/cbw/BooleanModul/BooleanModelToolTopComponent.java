/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.BooleanModul;

import fri.cbw.GenericTool.AbstractGenericTool;
import fri.cbw.GenericTool.AbstractModelTool;
import fri.cbw.GenericTool.AbstractReaction;
import fri.cbw.GenericTool.ToolTopComponent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.awt.ActionID;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@TopComponent.Description(
    preferredID = "BooleanModelToolTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "fri.cbw.BooleanModul.BooleanModelToolTopComponent")
@Messages({
    "CTL_BooleanModelToolAction=BooleanModelTool",
    "CTL_BooleanModelToolTopComponent=BooleanModelTool Window",
    "HINT_BooleanModelToolTopComponent=This is a BooleanModelTool window"
})
public final class BooleanModelToolTopComponent extends ToolTopComponent {

    public BooleanModelToolTopComponent(AbstractGenericTool gt) {
        super(gt);
        initComponents();
        setName(Bundle.CTL_BooleanModelToolTopComponent());
        setToolTipText(Bundle.HINT_BooleanModelToolTopComponent());
            
        List<AbstractReaction> reacts = (List<AbstractReaction>) ((AbstractModelTool)getGenericTool()).getReactions();
        if(reacts != null) {
            Vector<String> rules = new Vector<String>();
        
            for(AbstractReaction reakcija : reacts) {
                BooleanModelFunctionsTypeImpl test = (BooleanModelFunctionsTypeImpl) reakcija;
                rules.add(test.getBooleanFunctions());
            } 
        
            evaluateComponents(rules);
        }
    }
    
    public void evaluateComponents(Vector<String> rules) {
        Vector<String> functions = BoolNetLibrary.getBoolTransferFunctions(rules);
            Vector<String> ele = BoolNetLibrary.getSetOfAllElements(rules);
            jList1.setListData(functions); 
            
            Vector<String> res = BoolNetLibrary.getTruthTableFromBooleanFunctions(BoolNetLibrary.getSetOfAllElements(rules), functions);
            Vector<String> truth = BoolNetLibrary.getTruthTable(ele.size());
        
            // Za vse kombinacije vozlisc izracunamo tranzicije in jih
            // prikazemo jList2
            Vector<String> tr = new Vector<String>();
            tr.add(res.get(0));
            for(int i=0; i<truth.size(); i++){
                Vector<String> trans = BoolNetLibrary.getTransitions(truth.get(i), truth, res);
                    
                String n = "";
                for(int j=0; j<trans.size();j++){
                    n = n + trans.get(j) + "->";
                }
                tr.add(n);
            }
            jList2.setListData(tr);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(BooleanModelToolTopComponent.class, "BooleanModelToolTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        jScrollPane2.setViewportView(jScrollPane1);

        jScrollPane3.setViewportView(jList2);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(BooleanModelToolTopComponent.class, "BooleanModelToolTopComponent.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT Files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            try {     
                
                // Branje datoteke ****************************************************
                String filename = chooser.getSelectedFile().getName();
                String path = chooser.getSelectedFile().getPath();
                System.out.println("You chose to open this file: " +
                filename);
                //System.out.println("path " + path);
                FileInputStream fstream = new FileInputStream(path);
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
      
                //Read File Line By Line
                Vector<String> rules = new Vector<String>();
                ArrayList reactions = new ArrayList<BooleanModelFunctionsTypeImpl>();
                
                // Na jList dodajamo boolove funkcije
                jList1.removeAll();
                while ((strLine = br.readLine()) != null)   {
                   rules.add(strLine);
                    //System.out.println (strLine);
                   reactions.add(new BooleanModelFunctionsTypeImpl(strLine));
                   if(reactions != null) {
                       System.out.println("Reactions je poln!");
                   } else {
                       System.out.println("Reactions je prazen!");
                   }
                }
                
                ((AbstractModelTool)getGenericTool()).setReactions(reactions);
                
                evaluateComponents(rules);
            
                //Close the input stream
                in.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }      
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void doSave() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}