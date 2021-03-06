package gis.gui;

import gis.Utils;
import gis.tasks.task3.Task3;
import java.io.IOException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.Layer;
import org.geotools.swing.dialog.JExceptionReporter;

/**
 * @author Ignas Daukšas
 */
public class Task3Frame extends javax.swing.JFrame {
    private final MainFrame mainFrame;

    public Task3Frame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }
    
    @Override
    public void setVisible(boolean b) {
        setLocationRelativeTo(mainFrame);
        super.setVisible(b);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLayers = new javax.swing.JPanel();
        labelAngles = new javax.swing.JLabel();
        comboBoxAngles = new javax.swing.JComboBox();
        labelSlopes = new javax.swing.JLabel();
        labelRoads = new javax.swing.JLabel();
        labelTerritories = new javax.swing.JLabel();
        labelBuildings = new javax.swing.JLabel();
        comboBoxSlopes = new javax.swing.JComboBox();
        comboBoxRoads = new javax.swing.JComboBox();
        comboBoxTerritories = new javax.swing.JComboBox();
        comboBoxBuildings = new javax.swing.JComboBox();
        buttonReset = new javax.swing.JButton();
        panelSettings = new javax.swing.JPanel();
        labelCond1 = new javax.swing.JLabel();
        scrollPoolTypes = new javax.swing.JScrollPane();
        listPoolTypes = new javax.swing.JList();
        labelCond2Part1 = new javax.swing.JLabel();
        fieldMinPoolArea = new javax.swing.JTextField();
        labelCond2Part2 = new javax.swing.JLabel();
        labelCond3Part1 = new javax.swing.JLabel();
        fieldSmallPoolBuffer = new javax.swing.JTextField();
        labelCond3Part2 = new javax.swing.JLabel();
        fieldLargePoolBuffer = new javax.swing.JTextField();
        labelCond3Part3 = new javax.swing.JLabel();
        labelCond3Part4 = new javax.swing.JLabel();
        labelCond4Part1 = new javax.swing.JLabel();
        fieldBuildingsBuffer = new javax.swing.JTextField();
        labelCond4Part2 = new javax.swing.JLabel();
        labelCond6Part1 = new javax.swing.JLabel();
        fieldRoadsBuffer = new javax.swing.JTextField();
        labelCond6Part2 = new javax.swing.JLabel();
        labelCond5Part1 = new javax.swing.JLabel();
        fieldSlopeDeg = new javax.swing.JTextField();
        labelCond5Part2 = new javax.swing.JLabel();
        buttonStart = new javax.swing.JButton();

        setTitle("Sklypo pirtelei paieška");

        panelLayers.setBorder(javax.swing.BorderFactory.createTitledBorder("Sluoksniai"));

        labelAngles.setText("Nuolydžių krypčių sluoksnis");

        labelSlopes.setText("Nuolydžių sluoksnis");

        labelRoads.setText("Kelių sluoksnis");

        labelTerritories.setText("Plotų sluoksnis");

        labelBuildings.setText("Pastatų sluoksnis");

        buttonReset.setText("Atnaujinti sąrašus");
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayersLayout = new javax.swing.GroupLayout(panelLayers);
        panelLayers.setLayout(panelLayersLayout);
        panelLayersLayout.setHorizontalGroup(
            panelLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelAngles)
                    .addComponent(labelSlopes)
                    .addComponent(labelRoads)
                    .addComponent(labelBuildings)
                    .addComponent(labelTerritories))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboBoxAngles, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxSlopes, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxRoads, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxBuildings, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxTerritories, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelLayersLayout.setVerticalGroup(
            panelLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTerritories)
                    .addComponent(comboBoxTerritories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelBuildings)
                    .addComponent(comboBoxBuildings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRoads)
                    .addComponent(comboBoxRoads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSlopes)
                    .addComponent(comboBoxSlopes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelAngles)
                    .addComponent(comboBoxAngles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonReset)
                .addContainerGap())
        );

        panelSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Parametrai"));

        labelCond1.setText("1. Sklypo ieškoti prie");

        listPoolTypes.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Ežerų", "Tvenkinių" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listPoolTypes.setSelectionInterval(0, 1);
        scrollPoolTypes.setViewportView(listPoolTypes);

        labelCond2Part1.setText("2. Vandens telkinys turi būti ne mažesnio nei");

        fieldMinPoolArea.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fieldMinPoolArea.setText("50000");

        labelCond2Part2.setText("kv. m ploto.");

        labelCond3Part1.setText("3. Sklypas turi prasidėti toliau kaip");

        fieldSmallPoolBuffer.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fieldSmallPoolBuffer.setText("10");

        labelCond3Part2.setText("m");

        fieldLargePoolBuffer.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fieldLargePoolBuffer.setText("50");

        labelCond3Part3.setText("ir baigtis ne toliau kaip");

        labelCond3Part4.setText("m atstumu nuo vandens telkinio kranto.");

        labelCond4Part1.setText("4. Nesiūlyti pirtelės statyti ant esamų pastatų ar šalia jų mažesniu nei");

        fieldBuildingsBuffer.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fieldBuildingsBuffer.setText("40");

        labelCond4Part2.setText("m atstumu.");

        labelCond6Part1.setText("6. Apie sklypą");

        fieldRoadsBuffer.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fieldRoadsBuffer.setText("300");

        labelCond6Part2.setText("m spinduliu turi būti bent žvyrkelis.");

        labelCond5Part1.setText("5. Sklypo nuolydis į vandens telkinį turi būti ne mažesnis nei");

        fieldSlopeDeg.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        fieldSlopeDeg.setText("10");

        labelCond5Part2.setText("°, kad būtų galima įrengti čiuožyklą.");

        javax.swing.GroupLayout panelSettingsLayout = new javax.swing.GroupLayout(panelSettings);
        panelSettings.setLayout(panelSettingsLayout);
        panelSettingsLayout.setHorizontalGroup(
            panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSettingsLayout.createSequentialGroup()
                        .addComponent(labelCond2Part1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldMinPoolArea, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelCond2Part2))
                    .addGroup(panelSettingsLayout.createSequentialGroup()
                        .addComponent(labelCond4Part1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldBuildingsBuffer, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelCond4Part2))
                    .addGroup(panelSettingsLayout.createSequentialGroup()
                        .addComponent(labelCond1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scrollPoolTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelSettingsLayout.createSequentialGroup()
                        .addGroup(panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelCond3Part1)
                            .addComponent(labelCond3Part3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelSettingsLayout.createSequentialGroup()
                                .addComponent(fieldLargePoolBuffer, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelCond3Part4))
                            .addGroup(panelSettingsLayout.createSequentialGroup()
                                .addComponent(fieldSmallPoolBuffer, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelCond3Part2))))
                    .addGroup(panelSettingsLayout.createSequentialGroup()
                        .addComponent(labelCond5Part1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldSlopeDeg, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelCond5Part2))
                    .addGroup(panelSettingsLayout.createSequentialGroup()
                        .addComponent(labelCond6Part1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldRoadsBuffer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelCond6Part2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelSettingsLayout.setVerticalGroup(
            panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCond1)
                    .addComponent(scrollPoolTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCond2Part1)
                    .addComponent(fieldMinPoolArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCond2Part2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCond3Part1)
                    .addComponent(fieldSmallPoolBuffer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCond3Part2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCond3Part4)
                    .addComponent(labelCond3Part3)
                    .addComponent(fieldLargePoolBuffer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCond4Part1)
                    .addComponent(fieldBuildingsBuffer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCond4Part2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCond5Part1)
                    .addComponent(fieldSlopeDeg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCond5Part2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCond6Part1)
                    .addComponent(fieldRoadsBuffer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCond6Part2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonStart.setText("Ieškoti sklypų");
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelLayers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelLayers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panelSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        refreshLayers();
    }//GEN-LAST:event_buttonResetActionPerformed

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
        try {
            if (mainFrame.getSelectionPoly() == null) {
                Utils.showMsg("Klaida", "Nepažymėta pradinė teritorija.");
            }
            else {
                Layer territoriesLayer = Utils.getLayer(mainFrame, comboBoxTerritories);
                SimpleFeatureSource territoriesSource = (SimpleFeatureSource) territoriesLayer.getFeatureSource();
                SimpleFeatureCollection territoriesCol = territoriesSource.getFeatures();

                Layer buildingsLayer = Utils.getLayer(mainFrame, comboBoxBuildings);
                SimpleFeatureSource buildingsSource = (SimpleFeatureSource) buildingsLayer.getFeatureSource();
                SimpleFeatureCollection buildingsCol = buildingsSource.getFeatures();

                Layer roadsLayer = Utils.getLayer(mainFrame, comboBoxRoads);
                SimpleFeatureSource roadsSource = (SimpleFeatureSource) roadsLayer.getFeatureSource();
                SimpleFeatureCollection roadsCol = roadsSource.getFeatures();
                
                Layer slopesLayer = Utils.getLayer(mainFrame, comboBoxSlopes);
                SimpleFeatureSource slopesSource = (SimpleFeatureSource) slopesLayer.getFeatureSource();
                SimpleFeatureCollection slopesCol = slopesSource.getFeatures();
                
                Layer anglesLayer = Utils.getLayer(mainFrame, comboBoxAngles);
                SimpleFeatureSource anglesSource = (SimpleFeatureSource) anglesLayer.getFeatureSource();
                SimpleFeatureCollection anglesCol = anglesSource.getFeatures();
                
                Task3 task3 = new Task3();
                
                task3.setMainFrame(mainFrame);
                task3.setCollections(territoriesCol, buildingsCol, slopesCol, anglesCol, roadsCol);
                
                for (Object poolType : listPoolTypes.getSelectedValuesList()) {
                    if (poolType.equals("Ežerų")) {
                        task3.includeLakes(true);
                    }
                    else if (poolType.equals("Tvenkinių")) {
                        task3.includePonds(true);
                    }
                }
                
                task3.setMinPoolArea(Double.parseDouble(fieldMinPoolArea.getText()));
                task3.setSmallPoolBufferSize(Double.parseDouble(fieldSmallPoolBuffer.getText()));
                task3.setLargePoolBufferSize(Double.parseDouble(fieldLargePoolBuffer.getText()));
                task3.setBuildingsBufferSize(Double.parseDouble(fieldBuildingsBuffer.getText()));
                task3.setSlopeDegree(Double.parseDouble(fieldSlopeDeg.getText()));
                task3.setRoadsBufferSize(Double.parseDouble(fieldRoadsBuffer.getText()));
                
                new Thread(task3).start();
            }
        } catch (IOException ex) {
            JExceptionReporter.showDialog(ex);
        } catch (NumberFormatException ex) {
            JExceptionReporter.showDialog(ex);
        }
    }//GEN-LAST:event_buttonStartActionPerformed

    private void refreshLayers() {
        Utils.fillLayersCombo(mainFrame, comboBoxTerritories, "PLO");
        Utils.fillLayersCombo(mainFrame, comboBoxBuildings, "PAS");
        Utils.fillLayersCombo(mainFrame, comboBoxRoads, "KEL");
        Utils.fillLayersCombo(mainFrame, comboBoxSlopes, "psls");
        Utils.fillLayersCombo(mainFrame, comboBoxAngles, "pass");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonStart;
    private javax.swing.JComboBox comboBoxAngles;
    private javax.swing.JComboBox comboBoxBuildings;
    private javax.swing.JComboBox comboBoxRoads;
    private javax.swing.JComboBox comboBoxSlopes;
    private javax.swing.JComboBox comboBoxTerritories;
    private javax.swing.JTextField fieldBuildingsBuffer;
    private javax.swing.JTextField fieldLargePoolBuffer;
    private javax.swing.JTextField fieldMinPoolArea;
    private javax.swing.JTextField fieldRoadsBuffer;
    private javax.swing.JTextField fieldSlopeDeg;
    private javax.swing.JTextField fieldSmallPoolBuffer;
    private javax.swing.JLabel labelAngles;
    private javax.swing.JLabel labelBuildings;
    private javax.swing.JLabel labelCond1;
    private javax.swing.JLabel labelCond2Part1;
    private javax.swing.JLabel labelCond2Part2;
    private javax.swing.JLabel labelCond3Part1;
    private javax.swing.JLabel labelCond3Part2;
    private javax.swing.JLabel labelCond3Part3;
    private javax.swing.JLabel labelCond3Part4;
    private javax.swing.JLabel labelCond4Part1;
    private javax.swing.JLabel labelCond4Part2;
    private javax.swing.JLabel labelCond5Part1;
    private javax.swing.JLabel labelCond5Part2;
    private javax.swing.JLabel labelCond6Part1;
    private javax.swing.JLabel labelCond6Part2;
    private javax.swing.JLabel labelRoads;
    private javax.swing.JLabel labelSlopes;
    private javax.swing.JLabel labelTerritories;
    private javax.swing.JList listPoolTypes;
    private javax.swing.JPanel panelLayers;
    private javax.swing.JPanel panelSettings;
    private javax.swing.JScrollPane scrollPoolTypes;
    // End of variables declaration//GEN-END:variables
}
