package gis.gui;

import gis.tasks.Task2;
import gis.Utils;
import java.io.IOException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.Layer;
import org.geotools.swing.dialog.JExceptionReporter;
import org.opengis.filter.FilterFactory;

/**
 * @author Ignas Daukšas
 */
public class Task2Frame extends javax.swing.JFrame {
    private final FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    private final MainFrame mainFrame;

    public Task2Frame(MainFrame mainFrame) {
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

        panelControl = new javax.swing.JPanel();
        labelMunicipalities = new javax.swing.JLabel();
        comboBoxMunicipalities = new javax.swing.JComboBox();
        labelWaters = new javax.swing.JLabel();
        labelRoads = new javax.swing.JLabel();
        labelTerritories = new javax.swing.JLabel();
        labelBuildings = new javax.swing.JLabel();
        comboBoxWaters = new javax.swing.JComboBox();
        comboBoxRoads = new javax.swing.JComboBox();
        comboBoxTerritories = new javax.swing.JComboBox();
        comboBoxBuildings = new javax.swing.JComboBox();
        buttonStart = new javax.swing.JButton();
        buttonReset = new javax.swing.JButton();
        panelWaters = new javax.swing.JPanel();
        scrollWaters = new javax.swing.JScrollPane();
        tableWaters = new javax.swing.JTable();
        panelRoads = new javax.swing.JPanel();
        scrollRoads = new javax.swing.JScrollPane();
        tableRoads = new javax.swing.JTable();
        panelTerritories = new javax.swing.JPanel();
        scrollTerritories = new javax.swing.JScrollPane();
        tableTerritories = new javax.swing.JTable();
        panelBuildings = new javax.swing.JPanel();
        scrollBuildings = new javax.swing.JScrollPane();
        tableBuildings = new javax.swing.JTable();

        setTitle("GIS 2 užduotis");

        panelControl.setBorder(javax.swing.BorderFactory.createTitledBorder("Nustatymai"));

        labelMunicipalities.setText("Administracinių vienetų sluoksnis");

        labelWaters.setText("Hidro sl.");

        labelRoads.setText("Kelių sl.");

        labelTerritories.setText("Plotų sl.");

        labelBuildings.setText("Pastatų sl.");

        buttonStart.setText("Skaičiuoti");
        buttonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed(evt);
            }
        });

        buttonReset.setText("Atnaujinti sąrašus");
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelControlLayout = new javax.swing.GroupLayout(panelControl);
        panelControl.setLayout(panelControlLayout);
        panelControlLayout.setHorizontalGroup(
            panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelControlLayout.createSequentialGroup()
                        .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelWaters)
                            .addComponent(labelRoads))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelControlLayout.createSequentialGroup()
                                .addComponent(comboBoxRoads, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(labelBuildings)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboBoxBuildings, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelControlLayout.createSequentialGroup()
                                .addComponent(comboBoxWaters, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(labelTerritories)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboBoxTerritories, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelControlLayout.createSequentialGroup()
                        .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelControlLayout.createSequentialGroup()
                                .addComponent(labelMunicipalities)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBoxMunicipalities, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelControlLayout.createSequentialGroup()
                                .addComponent(buttonReset, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(buttonStart, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelControlLayout.setVerticalGroup(
            panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelMunicipalities)
                    .addComponent(comboBoxMunicipalities, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelWaters)
                    .addComponent(comboBoxWaters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTerritories)
                    .addComponent(comboBoxTerritories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRoads)
                    .addComponent(comboBoxRoads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelBuildings)
                    .addComponent(comboBoxBuildings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonReset)
                    .addComponent(buttonStart))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelWaters.setBorder(javax.swing.BorderFactory.createTitledBorder("Bendras hidrografijos tinklo ilgis"));

        scrollWaters.setViewportView(tableWaters);

        javax.swing.GroupLayout panelWatersLayout = new javax.swing.GroupLayout(panelWaters);
        panelWaters.setLayout(panelWatersLayout);
        panelWatersLayout.setHorizontalGroup(
            panelWatersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelWatersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollWaters, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelWatersLayout.setVerticalGroup(
            panelWatersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelWatersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollWaters, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelRoads.setBorder(javax.swing.BorderFactory.createTitledBorder("Bendras kelių tinklo ilgis"));

        scrollRoads.setViewportView(tableRoads);

        javax.swing.GroupLayout panelRoadsLayout = new javax.swing.GroupLayout(panelRoads);
        panelRoads.setLayout(panelRoadsLayout);
        panelRoadsLayout.setHorizontalGroup(
            panelRoadsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRoadsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollRoads, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRoadsLayout.setVerticalGroup(
            panelRoadsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRoadsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollRoads, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTerritories.setBorder(javax.swing.BorderFactory.createTitledBorder("Plotai administraciniame vienete"));

        scrollTerritories.setViewportView(tableTerritories);

        javax.swing.GroupLayout panelTerritoriesLayout = new javax.swing.GroupLayout(panelTerritories);
        panelTerritories.setLayout(panelTerritoriesLayout);
        panelTerritoriesLayout.setHorizontalGroup(
            panelTerritoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTerritoriesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollTerritories, javax.swing.GroupLayout.PREFERRED_SIZE, 946, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelTerritoriesLayout.setVerticalGroup(
            panelTerritoriesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTerritoriesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollTerritories, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelBuildings.setBorder(javax.swing.BorderFactory.createTitledBorder("Pastatai plotuose"));

        scrollBuildings.setViewportView(tableBuildings);

        javax.swing.GroupLayout panelBuildingsLayout = new javax.swing.GroupLayout(panelBuildings);
        panelBuildings.setLayout(panelBuildingsLayout);
        panelBuildingsLayout.setHorizontalGroup(
            panelBuildingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBuildingsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollBuildings, javax.swing.GroupLayout.PREFERRED_SIZE, 946, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelBuildingsLayout.setVerticalGroup(
            panelBuildingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBuildingsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollBuildings, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelWaters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelRoads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelTerritories, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelBuildings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panelWaters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelRoads, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTerritories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBuildings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        refreshLayers();
    }//GEN-LAST:event_buttonResetActionPerformed

    private void buttonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartActionPerformed
        try {
            Layer municipalitiesLayer = Utils.getLayer(mainFrame, comboBoxMunicipalities);
            if (municipalitiesLayer != mainFrame.getFirstSelectedLayer() ||
                mainFrame.getSelectedFeatures().isEmpty()) {
                Utils.showMsg("Klaida", "Administracinių vienetų sluoksnis turi turėti pažymėjimų žemėlapyje.");
            }
            else {
                SimpleFeatureSource municipalitiesSource = (SimpleFeatureSource) municipalitiesLayer.getFeatureSource();
                SimpleFeatureCollection municipalitiesCol = municipalitiesSource.getFeatures(ff.id(mainFrame.getSelectedFeatures()));

                Layer watersLayer = Utils.getLayer(mainFrame, comboBoxWaters);
                SimpleFeatureSource watersSource = (SimpleFeatureSource) watersLayer.getFeatureSource();
                SimpleFeatureCollection watersCol = watersSource.getFeatures();

                Layer roadsLayer = Utils.getLayer(mainFrame, comboBoxRoads);
                SimpleFeatureSource roadsSource = (SimpleFeatureSource) roadsLayer.getFeatureSource();
                SimpleFeatureCollection roadsCol = roadsSource.getFeatures();

                Layer territoriesLayer = Utils.getLayer(mainFrame, comboBoxTerritories);
                SimpleFeatureSource territoriesSource = (SimpleFeatureSource) territoriesLayer.getFeatureSource();
                SimpleFeatureCollection territoriesCol = territoriesSource.getFeatures();

                Layer buildingsLayer = Utils.getLayer(mainFrame, comboBoxBuildings);
                SimpleFeatureSource buildingsSource = (SimpleFeatureSource) buildingsLayer.getFeatureSource();
                SimpleFeatureCollection buildingsCol = buildingsSource.getFeatures();

                Task2 task2 = new Task2();
                task2.setCollections(municipalitiesCol, watersCol, roadsCol, territoriesCol, buildingsCol);
                task2.setTables(tableWaters, tableRoads, tableTerritories, tableBuildings);
                task2.setMainFrame(mainFrame);
                new Thread(task2).start();
            }
        } catch (IOException ex) {
            JExceptionReporter.showDialog(ex);
        }
    }//GEN-LAST:event_buttonStartActionPerformed

    private void refreshLayers() {
        Utils.fillLayersCombo(mainFrame, comboBoxMunicipalities, "SAV");
        Utils.fillLayersCombo(mainFrame, comboBoxWaters, "HID");
        Utils.fillLayersCombo(mainFrame, comboBoxRoads, "KEL");
        Utils.fillLayersCombo(mainFrame, comboBoxTerritories, "PLO");
        Utils.fillLayersCombo(mainFrame, comboBoxBuildings, "PAS");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonStart;
    private javax.swing.JComboBox comboBoxBuildings;
    private javax.swing.JComboBox comboBoxMunicipalities;
    private javax.swing.JComboBox comboBoxRoads;
    private javax.swing.JComboBox comboBoxTerritories;
    private javax.swing.JComboBox comboBoxWaters;
    private javax.swing.JLabel labelBuildings;
    private javax.swing.JLabel labelMunicipalities;
    private javax.swing.JLabel labelRoads;
    private javax.swing.JLabel labelTerritories;
    private javax.swing.JLabel labelWaters;
    private javax.swing.JPanel panelBuildings;
    private javax.swing.JPanel panelControl;
    private javax.swing.JPanel panelRoads;
    private javax.swing.JPanel panelTerritories;
    private javax.swing.JPanel panelWaters;
    private javax.swing.JScrollPane scrollBuildings;
    private javax.swing.JScrollPane scrollRoads;
    private javax.swing.JScrollPane scrollTerritories;
    private javax.swing.JScrollPane scrollWaters;
    private javax.swing.JTable tableBuildings;
    private javax.swing.JTable tableRoads;
    private javax.swing.JTable tableTerritories;
    private javax.swing.JTable tableWaters;
    // End of variables declaration//GEN-END:variables
}