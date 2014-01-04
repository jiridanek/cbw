/*
 * (C) Copyright 2013 Computational Biology Workspace (http://lrss.fri.uni-lj.si/bio/cbw.html)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     David Petrunov
 *     Aleksander Bešir
 *     Jirka Daněk <dnk@mail.muni.cz>
 *     Biserka Cvetkovska
 */
package fri.cbw.ThermodynamicModelTool;

import fri.cbw.GenericTool.AbstractGenericTool;
import fri.cbw.GenericTool.ToolTopComponent;
//import fri.cbw.ThermodynamicalModul.Bundle;
import java.util.Vector;
import javax.swing.JTable;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.awt.ActionID;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@TopComponent.Description(
    preferredID = "ThermodynamicModelToolTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "fri.cbw.ThermodynamicModelTool.ThermodynamicModelToolTopComponent")
@Messages({
    "CTL_ThermodynamicModelToolAction=ThermodynamicModelTool",
    "CTL_ThermodynamicModelToolTopComponent=ThermodynamicModelTool Window",
    "HINT_ThermodynamicModelToolTopComponent=This is a ThermodynamicModelTool window"
})
public final class ThermodynamicModelToolTopComponent extends ToolTopComponent {

    public ThermodynamicModelToolTopComponent(AbstractGenericTool gt) {
        
        super(gt);
        
        initComponents();
        setName(Bundle.CTL_ThermodynamicModelToolTopComponent());
        setToolTipText(Bundle.HINT_ThermodynamicModelToolTopComponent());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lacZConcentration = new javax.swing.JTextField();
        lacYConcentration = new javax.swing.JTextField();
        lacZProductConcentration = new javax.swing.JTextField();
        lacYProductConcentration = new javax.swing.JTextField();
        GeConcentration = new javax.swing.JTextField();
        LeConcentration = new javax.swing.JTextField();

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel4.setForeground(java.awt.Color.blue);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.jLabel4.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.jLabel5.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.jLabel6.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.jLabel7.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.jLabel8.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.jLabel9.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.jLabel10.text_1")); // NOI18N

        lacZConcentration.setText(org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.lacZConcentration.text_1")); // NOI18N

        lacYConcentration.setText(org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.lacYConcentration.text_1")); // NOI18N

        lacZProductConcentration.setText(org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.lacZProductConcentration.text_1")); // NOI18N

        lacYProductConcentration.setText(org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.lacYProductConcentration.text_1")); // NOI18N

        GeConcentration.setText(org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.GeConcentration.text_1")); // NOI18N

        LeConcentration.setText(org.openide.util.NbBundle.getMessage(ThermodynamicModelToolTopComponent.class, "ThermodynamicModelToolTopComponent.LeConcentration.text_1")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lacZConcentration, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                            .addComponent(lacYConcentration)
                            .addComponent(lacZProductConcentration)
                            .addComponent(lacYProductConcentration)
                            .addComponent(GeConcentration)
                            .addComponent(LeConcentration))))
                .addContainerGap(921, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lacZConcentration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lacYConcentration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lacZProductConcentration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lacYProductConcentration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(GeConcentration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(LeConcentration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(633, 633, 633))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField GeConcentration;
    private javax.swing.JTextField LeConcentration;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField lacYConcentration;
    private javax.swing.JTextField lacYProductConcentration;
    private javax.swing.JTextField lacZConcentration;
    private javax.swing.JTextField lacZProductConcentration;
    // End of variables declaration//GEN-END:variables
    
    //Ge
    public int getGeConcentration()
    {
        return Integer.parseInt(GeConcentration.getText());
    }
    
    //Le
    public int getLeConcentration()
    {
        return Integer.parseInt(LeConcentration.getText());
    }
    
    //mb
    public int getlacYConcentration()
    {
        return Integer.parseInt(lacYConcentration.getText());
    }
    
    //mp
    public int getlacZConcentration()
    {
        return Integer.parseInt(lacZConcentration.getText());
    }
    
    //P
    public int getlacZProductConcentration()
    {
        return Integer.parseInt(lacZProductConcentration.getText());
    }
    
    //B
    public int getlacYProductConcentration()
    {
        return Integer.parseInt(lacYProductConcentration.getText());
    }
    
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
