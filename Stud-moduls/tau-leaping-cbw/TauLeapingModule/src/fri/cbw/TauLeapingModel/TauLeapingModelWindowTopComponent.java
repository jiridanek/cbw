/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.TauLeapingModel;

import fri.cbw.GenericTool.AbstractGenericTool;
import fri.cbw.GenericTool.ToolTopComponent;
import fri.cbw.GenericTool.ToolWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.openide.awt.ActionID;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@TopComponent.Description(
    preferredID = "TauLeapingModelWindowTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "fri.cbw.TauLeapingModel.TauLeapingModelWindowTopComponent")
@Messages({
    "CTL_TauLeapingModelWindowAction=TauLeapingModelWindow",
    "CTL_TauLeapingModelWindowTopComponent=TauLeapingModelWindow Window",
    "HINT_TauLeapingModelWindowTopComponent=This is a TauLeapingModelWindow window"
})
public final class TauLeapingModelWindowTopComponent extends ToolTopComponent {

    public TauLeapingModelWindowTopComponent(AbstractGenericTool gt) {
        super(gt);
        initComponents();
        setName(Bundle.CTL_TauLeapingModelWindowTopComponent());
        setToolTipText(Bundle.HINT_TauLeapingModelWindowTopComponent());

    }

    public static int getFrontNumber(String input) {
        
        System.out.println("klicu get front number");
        
        String number = "";
        
        boolean isDigit;
        
        for(int i=0; i<input.length(); i++) {
            char c = input.charAt(i);
            isDigit = Character.isDigit(c);
            if(isDigit) {
                number += c;
            }
            else {
                break;
            }
        }
        
        int n = 1;
        
        if(number.length() > 0) {
            n = Integer.parseInt(number);
        }
       
        return n;
        
    }
    
    public static String vrniReaktant(String input){
        boolean digit = false;
        int save = input.length();
        for (int i=input.length()-1; i>=0; i--){
//            if(Character.isDigit(input.charAt(i))){
//                digit = true;
//            }
            if(Character.isLetter(input.charAt(i))){
                save = i;
            }
        }
        
        return input.substring(save).trim();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        species = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        reactions = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        k = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        omega = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        error = new javax.swing.JTextField();
        time = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        x0 = new javax.swing.JTextField();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.jLabel1.text")); // NOI18N

        species.setText(org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.species.text")); // NOI18N

        reactions.setColumns(20);
        reactions.setRows(5);
        jScrollPane1.setViewportView(reactions);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.jLabel3.text")); // NOI18N

        k.setText(org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.k.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.jLabel4.text")); // NOI18N

        omega.setText(org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.omega.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.jLabel5.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.jLabel6.text")); // NOI18N

        error.setText(org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.error.text")); // NOI18N

        time.setText(org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.time.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.jLabel7.text")); // NOI18N

        x0.setText(org.openide.util.NbBundle.getMessage(TauLeapingModelWindowTopComponent.class, "TauLeapingModelWindowTopComponent.x0.text")); // NOI18N
        x0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                x0ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(species)
                        .addComponent(k)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(time, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(error, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(omega, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(65, 65, 65)
                        .addComponent(jButton1))
                    .addComponent(x0, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(species, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(92, 92, 92))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(x0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(k, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(omega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5))
                    .addComponent(error, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(14, 14, 14))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // V glavnem preber pa sprav reakcije v -1, -1, -1 stil (če je obojesmerna
        // sam obrn arraye to je v[][] array
        // indekse pa sprav v stil 2, 2, 5 za vsako reakcijo pa sam obrn če je
        // obojesmerna
        String[] R = reactions.getText().split("\n");
        String[] ks = k.getText().split(",");
        String[] S = species.getText().split(",");
        String[] x0_v = x0.getText().split(",");
        
        double err = Double.parseDouble(error.getText());
        long ome = Long.parseLong(omega.getText());
        double time_ = Double.parseDouble(time.getText());
        
        HashMap<String, Integer> indexes = new HashMap<String, Integer>();
        for(int i=0; i<S.length; i++) {
            if(!indexes.containsKey(S[i])) {
                indexes.put(S[i].trim(), i);
            }
        }

        TauReaction tau = new TauReaction();
        tau.setN(indexes.size());
        tau.setM(R.length);
                
        ArrayList<ArrayList<Integer>> indexesRAll = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> valueRAll = new ArrayList<ArrayList<Integer>>();
        for(int i=0; i<R.length; i++) {
            String reaction = R[i];

            if(reaction.contains("<=>")) {
                String[] split = reaction.split("<=>");
                ArrayList<Integer> indexesR = new ArrayList<Integer>();
                ArrayList<Integer> valueR = new ArrayList<Integer>();
//                for(int j=0; j<split.length; j++) {
                    String[] species = split[0].split("\\+");
                    String[] products = split[1].split("\\+");

                    for(int k=0; k<species.length; k++) {
                        System.out.println("Species "+species[k]);
                        
                        int n = getFrontNumber(species[k].trim());
                        System.out.println("Reaktant: "+vrniReaktant(species[k]));
                        int idx = indexes.get(vrniReaktant(species[k]));
                        indexesR.add(idx);
                        System.out.println(n);
                        
                        valueR.add(n*-1);
                    }

                    for(int k=0; k<products.length; k++) {
                        System.out.println("Products "+products[k]);
                        
                        int n = getFrontNumber(products[k].trim());
                        System.out.println("Reaktant: "+vrniReaktant(products[k]));
                        int idx = indexes.get(vrniReaktant(products[k]));
                        indexesR.add(idx);
                        System.out.println(n);
                        
                        valueR.add(n);
                    }

//                }
                ArrayList<Integer> indexObrni = new ArrayList<Integer>();
                for(int k=indexesR.size()-1; k>=0; k--){
                    indexObrni.add(indexesR.get(k));
                }
                indexesRAll.add(indexesR);
                indexesRAll.add(indexObrni);
                ArrayList<Integer> valueObrniNegiraj = new ArrayList<Integer>();
                for(int k=valueR.size()-1; k>=0; k--){
                    valueObrniNegiraj.add(valueR.get(k)*-1);
                }
                valueRAll.add(valueR);
                valueRAll.add(valueObrniNegiraj);
            }
            else {
                String[] split = reaction.split("=>");
                ArrayList<Integer> indexesR = new ArrayList<Integer>();
                ArrayList<Integer> valueR = new ArrayList<Integer>();
//                for(int j=0; j<split.length; j++) {
                    String[] species = split[0].split("\\+");
                    String[] products = split[1].split("\\+");
                    
                    for(int k=0; k<species.length; k++) {
                        System.out.println("Species "+species[k]);
                        
                        int n = getFrontNumber(species[k].trim());
                        System.out.println(n);
                        System.out.println("Reaktant: "+vrniReaktant(species[k]));
                        int idx = indexes.get(vrniReaktant(species[k]));
                        indexesR.add(idx);
                        
                        valueR.add(n*-1);
                    }

                    for(int k=0; k<products.length; k++) {
                        System.out.println("Products "+products[k]);
                        
                        int n = getFrontNumber(products[k].trim());
                        System.out.println(n);
                        int idx = indexes.get(vrniReaktant(products[k]));
                        indexesR.add(idx);
                        
                        valueR.add(n);
                    }
//                }
                ArrayList<Integer> indexObrni = new ArrayList<Integer>();
                for(int k=indexesR.size()-1; k>=0; k--){
                    indexObrni.add(indexesR.get(k));
                }
                indexesRAll.add(indexesR);
                indexesRAll.add(indexObrni);
                ArrayList<Integer> valueObrniNegiraj = new ArrayList<Integer>();
                for(int k=valueR.size()-1; k>=0; k--){
                    valueObrniNegiraj.add(valueR.get(k)*-1);
                }
                valueRAll.add(valueR);
                valueRAll.add(valueObrniNegiraj);
            }
        }
        long[][] indexTab = new long[indexesRAll.size()][];
        long[][] valueTab = new long[valueRAll.size()][];
        int max = 0;
        for(int i=0; i<indexesRAll.size(); i++){
            if (max<indexesRAll.get(i).size())
                max = indexesRAll.get(i).size();
        }
        for (int i=0; i<indexTab.length; i++){
            indexTab[i] = new long[max];
            for (int j=0; j<indexTab[i].length; j++)
                indexTab[i][j] = -1;
            valueTab[i] = new long[max];
        }
        System.out.println("Max dolzina je "+max);
        for (int i=0; i<indexesRAll.size(); i++){
            System.out.println("trenutna dolzina je "+indexesRAll.get(i).size());
            for(int j=0; j<indexesRAll.get(i).size(); j++){
                indexTab[i][j] = 
                        indexesRAll.get(i).get(j);
                valueTab[i][j] = 
                        valueRAll.get(i).get(j);
            }
        }
        
        //izpis
        for (long[] i : indexTab){
            for (long j : i)
                System.out.print(j+" ");
            System.out.println();
        }
        for (long[] i : valueTab){
            for (long j : i)
                System.out.print(j+" ");
            System.out.println();
        }
        
        long[] x0_vectors = new long[x0_v.length];
        for(int i=0; i<x0_v.length; i++) {
            x0_vectors[i] = Long.parseLong(x0_v[i].trim());
        }
        
        double[] k0_vector = new double[ks.length];
        for(int i=0; i<ks.length; i++) {
            k0_vector[i] = Double.parseDouble(ks[i].trim()); 
        }
        
        tau.setIndexes(indexTab);
        tau.setV(valueTab);
        tau.setOmega(ome);
        tau.setK(k0_vector);
        tau.setX0(x0_vectors);
        
        ArrayList reactions = new ArrayList<TauReaction>();
        reactions.add(tau);
        
        List species = new ArrayList<String>();
        for(int i=0; i<S.length; i++) {
            species.add(S[i]);
        }
        
        getTauLeapModelTool().setReactions(reactions);
        //getTauLeapModelTool().setSpecies(reactions);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void x0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_x0ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_x0ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField error;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField k;
    private javax.swing.JTextField omega;
    private javax.swing.JTextArea reactions;
    private javax.swing.JTextField species;
    private javax.swing.JTextField time;
    private javax.swing.JTextField x0;
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
    
    private void setReactions(ArrayList reactions) {
        getTauLeapModelTool().setReactions(reactions);
    }
       
    private TauLeapingModule getTauLeapModelTool(){
        return (TauLeapingModule) getGenericTool();
    }

    @Override
    public void doSave() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
}
