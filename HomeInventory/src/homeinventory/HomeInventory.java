/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homeinventory;


import java.io.*;
import java.util.*;
import java.text.*;
import java.util.Date;
import datechooser.events.SelectionChangedEvent;
import datechooser.events.SelectionChangedListener;
import java.awt.Color;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.*;
import java.awt.print.*;
/**
 *
 * @author NAMRATA
 */
public class HomeInventory extends javax.swing.JFrame implements ActionListener{
    static final int entriesPerPage = 2;
    static int lastPage;
    final int maximumEntries = 300;
    static int numberEntries;
    
    InventoryItem[] myInventory = new InventoryItem[maximumEntries];
    
    JButton[] searchButton = new JButton[26];
    int currentEntry = 0;
    
    static JTextArea photoTextarea = new JTextArea();

    /**
     * Creates new form HomeInventory
     */
    public HomeInventory() {
        initComponents();
        Assignment();
        
    }
    private void Assignment()
    {
        searchPanel.setPreferredSize(new Dimension(240,160));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Item Search"));
        searchPanel.setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.gridx=1;
        gridConstraints.gridy=7;
        gridConstraints.gridwidth=3;
        gridConstraints.insets= new Insets(10, 0, 10, 0);
        gridConstraints.anchor=GridBagConstraints.CENTER;
        getContentPane().add(searchPanel, gridConstraints);
        
        int x=0,y=0;
        for (int i=0; i < 26; i++) 
        {
            searchButton[i]=new JButton();
            searchButton[i].setText(String.valueOf((char)(65+i)));
            searchButton[i].setMargin(new Insets(-1, 0, -1, 0));
            searchButton[i].setPreferredSize(new Dimension(37,27));
            searchButton[i].setBackground(Color.YELLOW);
            gridConstraints=new GridBagConstraints();
            gridConstraints.gridx=x;
            gridConstraints.gridy=y;
            searchPanel.add(searchButton[i], gridConstraints);
            
            searchButton[i].addActionListener(new  ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    searchButtonActionPerformed(e);
                }
            });
            x++;
            if (x % 6==0) {
                x=0;
                y++;
            }
           
        }
        photoPanel.setPreferredSize(new Dimension(240,160));
        gridConstraints =new GridBagConstraints();
        gridConstraints.gridx=4;
        gridConstraints.gridy=7;
        gridConstraints.gridwidth=3;
        gridConstraints.insets =new Insets(-10, -10, -10, -10);
        gridConstraints.anchor=GridBagConstraints.CENTER;
        getContentPane().add(photoPanel, gridConstraints);
        
        validate();
        itemTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemTextFieldActionPerformed(e);
            }
        });
        locationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                locationComboBoxActionPerformed(e);
            }
        });
        serialTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serialTextFieldActionPerformed(e);
            }
        });
        priceTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                priceTextFieldActionPerformed(e);
            }
        });
        dateDateChooser.addSelectionChangedListener(new SelectionChangedListener() {
            @Override
            public void onSelectionChange(SelectionChangedEvent sce) {
                dateDateChooserOnSelectionChange(sce);
            }
        });
        storeTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                storeTextFieldActionPerformed(e);
            }
        });
        noteTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noteTextFieldActionPerformed(e);
            }
        });
        
        int currentEntry;
        int n;
        try 
        {
            BufferedReader inputFile=new BufferedReader(new FileReader("inventory.txt"));
            numberEntries =Integer.valueOf(inputFile.readLine()).intValue();
            if (numberEntries!=0) 
            {
                for (int i = 0; i < numberEntries; i++)
                {
                    myInventory[i]=new InventoryItem();
                    myInventory[i].description=inputFile.readLine();
                    myInventory[i].location=inputFile.readLine();
                    myInventory[i].serialNumber=inputFile.readLine();
                    myInventory[i].marked=Boolean.valueOf(inputFile.readLine()).booleanValue();
                    myInventory[i].purchasePrice=inputFile.readLine();
                    myInventory[i].purchaseDate=inputFile.readLine();
                    myInventory[i].purchaseLocation=inputFile.readLine();
                    myInventory[i].note=inputFile.readLine();
                    myInventory[i].photoFile=inputFile.readLine();
                    
                }
            }
            n=Integer.valueOf(inputFile.readLine()).intValue();
            if(n!=0)
            {
                for (int i = 0; i < n; i++) 
                {
                    locationComboBox.addItem(inputFile.readLine());
                }
            }
            inputFile.close();
            currentEntry=1;
            showEntry(currentEntry);
        } 
        catch (Exception e)
        {
            numberEntries=0;
            currentEntry=0;
        }
        if (numberEntries==0) {
            newButton.setEnabled(false);
            deleteButton.setEnabled(false);
            nextButton.setEnabled(false);
            previousButton.setEnabled(false);
            printButton.setEnabled(false);
        }
        
    } 
    
    private void showEntry(int j)
    {
        itemTextField.setText(myInventory[j-1].description);
        locationComboBox.setSelectedItem(myInventory[j-1].location);
        markedCheckBox.setSelected(myInventory[j-1].marked);
        serialTextField.setText(myInventory[j-1].serialNumber);
        priceTextField.setText(myInventory[j-1].purchasePrice);
        dateDateChooser.setText(myInventory[j-1].purchaseDate);
        storeTextField.setText(myInventory[j-1].purchaseLocation);
        noteTextField.setText(myInventory[j-1].note);
        showPhoto(myInventory[j-1].photoFile);
        nextButton.setEnabled(true);
        previousButton.setEnabled(true);
        if (j == 1)
            previousButton.setEnabled(false);
        if (j == numberEntries)
            nextButton.setEnabled(false);
        itemTextField.requestFocus();
    }
    
    
    private void showPhoto(String photoFile) {
        if (!photoFile.equals("")) {
            try {
                photoTextArea.setText(photoFile);
            } catch (Exception e) {
                photoTextArea.setText("");
            }
            
        } else {
            photoTextArea.setText("");
        }
        photoPanel.repaint();
    }
    private void blankValues()
    {
        newButton.setEnabled(false);
        deleteButton.setEnabled(false);
        saveButton.setEnabled(true);
        previousButton.setEnabled(false);   
        nextButton.setEnabled(false);
        printButton.setEnabled(false);
        itemTextField.setText("");
        locationComboBox.setSelectedItem("");
        markedCheckBox.setSelected(false);
        serialTextField.setText("");
        priceTextField.setText("");
        dateDateChooser.setText("");
        storeTextField.setText("");
        noteTextField.setText("");
        photoTextArea.setText("");
        photoPanel.repaint();
        itemTextField.requestFocus();
}

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inventoryToolBar = new javax.swing.JToolBar();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        newButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        deleteButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        saveButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        previousButton = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        nextButton = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        printButton = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        exitButton = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        Location = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        itemTextField = new javax.swing.JTextField();
        locationComboBox = new javax.swing.JComboBox<>();
        markedCheckBox = new javax.swing.JCheckBox();
        serialTextField = new javax.swing.JTextField();
        priceTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        dateDateChooser = new datechooser.beans.DateChooserCombo();
        storeTextField = new javax.swing.JTextField();
        noteTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        photoTextArea = new javax.swing.JTextArea();
        photoButton = new javax.swing.JButton();
        searchPanel = new javax.swing.JPanel();
        photoPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        inventoryToolBar.setBackground(new java.awt.Color(51, 51, 255));
        inventoryToolBar.setOrientation(javax.swing.SwingConstants.VERTICAL);
        inventoryToolBar.setRollover(true);
        inventoryToolBar.add(jSeparator7);

        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/newfile.png"))); // NOI18N
        newButton.setText("New");
        newButton.setToolTipText("");
        newButton.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        newButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newButton.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        newButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(newButton);
        inventoryToolBar.add(jSeparator1);

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Delete1.png"))); // NOI18N
        deleteButton.setText("Delete");
        deleteButton.setToolTipText("");
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(deleteButton);
        inventoryToolBar.add(jSeparator2);

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save1.png"))); // NOI18N
        saveButton.setText("Save");
        saveButton.setToolTipText("");
        saveButton.setFocusable(false);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(saveButton);
        inventoryToolBar.add(jSeparator3);

        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/previous1.png"))); // NOI18N
        previousButton.setText("Previous");
        previousButton.setToolTipText("");
        previousButton.setFocusable(false);
        previousButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        previousButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(previousButton);
        inventoryToolBar.add(jSeparator4);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/next1.png"))); // NOI18N
        nextButton.setText("Next");
        nextButton.setToolTipText("");
        nextButton.setFocusable(false);
        nextButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nextButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(nextButton);
        inventoryToolBar.add(jSeparator5);

        printButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/print2.png"))); // NOI18N
        printButton.setText("Print");
        printButton.setToolTipText("");
        printButton.setFocusable(false);
        printButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(printButton);
        inventoryToolBar.add(jSeparator6);

        exitButton.setText("    Exit   ");
        exitButton.setFocusable(false);
        exitButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exitButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        inventoryToolBar.add(exitButton);
        inventoryToolBar.add(jSeparator8);

        jLabel1.setText("Inventory Item");

        Location.setText("Location");

        jLabel3.setText("Serial Number");

        jLabel4.setText("Purchase Price");

        jLabel5.setText("Store/Website");

        jLabel6.setText("Note");

        jLabel7.setText("Photo");

        itemTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemTextFieldActionPerformed(evt);
            }
        });

        locationComboBox.setModel(locationComboBox.getModel());
        locationComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                locationComboBoxItemStateChanged(evt);
            }
        });
        locationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationComboBoxActionPerformed(evt);
            }
        });

        markedCheckBox.setText("Marked?");

        serialTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serialTextFieldActionPerformed(evt);
            }
        });

        priceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceTextFieldActionPerformed(evt);
            }
        });

        jLabel2.setText("Date Purchase");

        dateDateChooser.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    try {
        dateDateChooser.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dateDateChooser.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
        public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
            dateDateChooserOnSelectionChange(evt);
        }
    });

    storeTextField.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            storeTextFieldActionPerformed(evt);
        }
    });

    noteTextField.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            noteTextFieldActionPerformed(evt);
        }
    });

    photoTextArea.setColumns(20);
    photoTextArea.setRows(5);
    jScrollPane1.setViewportView(photoTextArea);

    photoButton.setText("........");
    photoButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            photoButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
    searchPanel.setLayout(searchPanelLayout);
    searchPanelLayout.setHorizontalGroup(
        searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 240, Short.MAX_VALUE)
    );
    searchPanelLayout.setVerticalGroup(
        searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 160, Short.MAX_VALUE)
    );

    photoPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    photoPanel.setPreferredSize(new java.awt.Dimension(240, 160));

    javax.swing.GroupLayout photoPanelLayout = new javax.swing.GroupLayout(photoPanel);
    photoPanel.setLayout(photoPanelLayout);
    photoPanelLayout.setHorizontalGroup(
        photoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 238, Short.MAX_VALUE)
    );
    photoPanelLayout.setVerticalGroup(
        photoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 158, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(inventoryToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(itemTextField))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(serialTextField))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(storeTextField))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(noteTextField))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(Location, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(71, 71, 71)
                            .addComponent(markedCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dateDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(photoButton)))
                    .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(photoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(35, 35, 35))))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(itemTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Location, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(markedCheckBox))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(serialTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addComponent(dateDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(storeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(noteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(photoButton)))
            .addGap(21, 21, 21)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(photoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(69, Short.MAX_VALUE))
        .addComponent(inventoryToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
    );

    inventoryToolBar.getAccessibleContext().setAccessibleParent(inventoryToolBar);
    dateDateChooser.getAccessibleContext().setAccessibleParent(dateDateChooser);

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
            
    private void photoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_photoButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser openChooser = new JFileChooser();
        openChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        openChooser.setDialogTitle("Open Photo File");
        openChooser.addChoosableFileFilter(new FileNameExtensionFilter("Photo Files","jpg"));
        if (openChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        showPhoto(openChooser.getSelectedFile().toString());
    }//GEN-LAST:event_photoButtonActionPerformed
    private void searchButtonActionPerformed(ActionEvent e)
    {
        int i;
        if (numberEntries==0) {
            return;
        }
        String letterClicked = e.getActionCommand();
        i=0;
        do {
            if (myInventory[i].description.substring(0, 1).equals(letterClicked)) {
                currentEntry=i+1;
                showEntry(currentEntry);
                return;
            }
            i++;
            } while (i < numberEntries);
        JOptionPane.showConfirmDialog(null, "No"+letterClicked+"inventory items.","None Found",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
        
    }
    private void itemTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemTextFieldActionPerformed
        // TODO add your handling code here:
        locationComboBox.requestFocus();
    }//GEN-LAST:event_itemTextFieldActionPerformed
    
    private void locationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationComboBoxActionPerformed
        // TODO add your handling code here:
        if (locationComboBox.getItemCount()!=0) {
            for (int i = 0; i < locationComboBox.getItemCount(); i++) {
                if (locationComboBox.getSelectedItem().toString().equals(locationComboBox.getItemAt(i).toString())) {
                    serialTextField.requestFocus();
                    return;
                    
                }
                
            }
        }
        locationComboBox.addItem((String) locationComboBox.getSelectedItem());
        serialTextField.requestFocus();
    }//GEN-LAST:event_locationComboBoxActionPerformed

    private void serialTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serialTextFieldActionPerformed
        // TODO add your handling code here:
        priceTextField.requestFocus();
    }//GEN-LAST:event_serialTextFieldActionPerformed

    private void priceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceTextFieldActionPerformed
        // TODO add your handling code here:
        dateDateChooser.requestFocus();
    }//GEN-LAST:event_priceTextFieldActionPerformed

    private void dateDateChooserOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateDateChooserOnSelectionChange
        // TODO add your handling code here:
        storeTextField.requestFocus();
    }//GEN-LAST:event_dateDateChooserOnSelectionChange

    private void storeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeTextFieldActionPerformed
        // TODO add your handling code here:
      noteTextField.requestFocus();
    }//GEN-LAST:event_storeTextFieldActionPerformed

    private void noteTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteTextFieldActionPerformed
        // TODO add your handling code here:
        photoButton.requestFocus();
    }//GEN-LAST:event_noteTextFieldActionPerformed

    private void locationComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_locationComboBoxItemStateChanged
  
    }//GEN-LAST:event_locationComboBoxItemStateChanged

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        int currentEntry = 0;
        // TODO add your handling code here:
        checkSave();
        currentEntry--;
        showEntry(currentEntry);
        
    }//GEN-LAST:event_previousButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        
        // TODO add your handling code here:
        checkSave();
        currentEntry++;
        showEntry(currentEntry);
    }//GEN-LAST:event_nextButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        // TODO add your handling code here:
        exitForm(null);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        // TODO add your handling code here:
        checkSave();
        blankValues();
    }//GEN-LAST:event_newButtonActionPerformed
    private void checkSave()
    {
        boolean edited=false;
        if (!myInventory[currentEntry - 1].description.equals(itemTextField.getText())) 
        {
            edited=true;
        } else if (!myInventory[currentEntry - 1].location.equals(locationComboBox.getSelectedItem().toString())) {
            edited=true; 
        } else if (myInventory[currentEntry - 1].marked!=markedCheckBox.isSelected()) {
            edited=true;
        } else if (!myInventory[currentEntry -1].serialNumber.equals(serialTextField.getText())) {
            edited=true;
        } else if (!myInventory[currentEntry - 1].purchasePrice.equals(priceTextField.getText())) {
            edited=true;
        } else if (!myInventory[currentEntry - 1].purchaseDate.equals(dateDateChooser.getText())) {
            edited=true;
        } else if (!myInventory[currentEntry - 1].purchaseLocation.equals(storeTextField.getText())) {
            edited=true;
        } else if (!myInventory[currentEntry - 1].note.equals(noteTextField.getText())) {
            edited=true;
        } else if (!myInventory[currentEntry - 1].photoFile.equals(photoTextarea.getText())) {
            edited=true;
        }if (edited) {
                if (JOptionPane.showConfirmDialog(null , "You have edited this item .Do you want to sav the changes?","Save Item",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
                    saveButton.doClick();
                }
        }
    }
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        itemTextField.setText(itemTextField.getText().trim());
        if (itemTextField.getText().equals("")) {
            JOptionPane.showConfirmDialog(null, "Must Have Item Description","Error",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
            itemTextField.requestFocus();
            return;
        }
        if (newButton.isEnabled()) {
            deleteEntry(currentEntry);
        }
        
        String s = itemTextField.getText();
        itemTextField.setText(s.substring(0, 1).toUpperCase() + s.substring(1));
        numberEntries++;
        int currentEntry = 1;
        if (numberEntries!=1) {
            do {                
                if (itemTextField.getText().compareTo(myInventory[currentEntry - 1].description) < 0) {
                    currentEntry++;
                    break;
                    
                    
                }
                
            } while (currentEntry < numberEntries);
            
        }
        if (currentEntry!=numberEntries) {
            for (int i = numberEntries; i < currentEntry+1; i--) {
                myInventory[i-1]=myInventory[i-2];
                myInventory[i-2]=new InventoryItem();
            }
        }
        myInventory[currentEntry - 1] = new InventoryItem();
        myInventory[currentEntry - 1].description = itemTextField.getText();
        myInventory[currentEntry - 1].location =locationComboBox.getSelectedItem().toString();
        myInventory[currentEntry - 1].marked = markedCheckBox.isSelected();
        myInventory[currentEntry - 1].serialNumber = serialTextField.getText();
        myInventory[currentEntry - 1].purchasePrice = priceTextField.getText();
        myInventory[currentEntry - 1].purchaseDate =dateDateChooser.getText();
        myInventory[currentEntry - 1].purchaseLocation = storeTextField.getText();
        myInventory[currentEntry - 1].photoFile = photoTextArea.getText();
        myInventory[currentEntry - 1].note = noteTextField.getText();
        showEntry(currentEntry);
        if (numberEntries < maximumEntries){
            newButton.setEnabled(true);
        }
        else{
            newButton.setEnabled(false);
        }
        deleteButton.setEnabled(true);
        printButton.setEnabled(true);
        
    }//GEN-LAST:event_saveButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this item?","Delete Inventory item ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.NO_OPTION) {
            return;
        }
        deleteEntry(currentEntry);
        if (numberEntries==0) {
            currentEntry=0;
            blankValues();
            
        } else {
            currentEntry--;
            if (currentEntry==0) {
                currentEntry=1;
            }
            showEntry(currentEntry);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        // TODO add your handling code here:
        lastPage =(int)(1+(numberEntries - 1)/entriesPerPage);
        PrinterJob inventoryPrinterJob = PrinterJob.getPrinterJob();
        inventoryPrinterJob.setPrintable( new InventoryDocument());
        if (inventoryPrinterJob.printDialog()) {
            try {
                inventoryPrinterJob.print();
            } catch (Exception e) {
                JOptionPane.showConfirmDialog(null, e.getMessage(),"Print Error",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_printButtonActionPerformed
    private void deleteEntry(int j) {
        if (j!=numberEntries) {
            for (int i = j; i < numberEntries; i++) {
                myInventory[i-1]=new InventoryItem();
                myInventory[i-1]=myInventory[i];
            }
        }
        numberEntries--;
    }
    
    private void exitForm(Object object) 
    {
        if (JOptionPane.showConfirmDialog(null, "Any unsaved chages will be lost .\n Are you sure you want to exit?","Exit Program",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.NO_OPTION) {
            return;
        }
        try 
        {
            PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter("inventory.txt")));
            outputFile.println(numberEntries);
            if (numberEntries!=0) {
                for (int i = 0; i < numberEntries; i++) {
                    outputFile.println(myInventory[i].description);
                    outputFile.println(myInventory[i].location);
                    outputFile.println(myInventory[i].serialNumber);
                    outputFile.println(myInventory[i].marked);
                    outputFile.println(myInventory[i].purchasePrice);
                    outputFile.println(myInventory[i].purchaseDate);
                    outputFile.println(myInventory[i].purchaseLocation);
                    outputFile.println(myInventory[i].note);
                    outputFile.println(myInventory[i].photoFile);
                }
            } 
            outputFile.println(locationComboBox.getItemCount());
            if (locationComboBox.getItemCount()!=0) {
                for (int i = 0; i < locationComboBox.getItemCount(); i++) {
                    outputFile.println(locationComboBox.getItemAt(i));
                    
                }
            } outputFile.close();
        } catch (Exception e) {
        }
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    private void photoTextArea()
    {
        
    }
    
    
    public static void main(String args[]) {
       InventoryItem myItem ;
       myItem =new InventoryItem();
       myItem.description ="This is my inventory item";
       
       PrinterJob myPrinterJob =PrinterJob.getPrinterJob();
       myPrinterJob.setPrintable(new MyDocument());
        try {
            myPrinterJob.print();
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e.getMessage(),"Print Error",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        }
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeInventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeInventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeInventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeInventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HomeInventory().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Location;
    private datechooser.beans.DateChooserCombo dateDateChooser;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JToolBar inventoryToolBar;
    private javax.swing.JTextField itemTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JComboBox<String> locationComboBox;
    private javax.swing.JCheckBox markedCheckBox;
    private javax.swing.JButton newButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JTextField noteTextField;
    private javax.swing.JButton photoButton;
    private javax.swing.JPanel photoPanel;
    private javax.swing.JTextArea photoTextArea;
    private javax.swing.JButton previousButton;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JButton printButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField serialTextField;
    private javax.swing.JTextField storeTextField;
    // End of variables declaration//GEN-END:variables

    

    
    

}

class PhotoPanel extends JPanel
{
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g2D);
        g2D.setPaint(Color.BLACK);
        g2D.draw(new Rectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
        Image photoImage = new ImageIcon(HomeInventory.photoTextarea.getText()).getImage();
        int w =getWidth();
        int h =getHeight();
        double rWidth=(double)getWidth()/(double)photoImage.getWidth(null);
        double rHeight=(double)getHeight()/(double)photoImage.getHeight(null);
        if (rWidth>rHeight) {
            w=(int)(photoImage.getWidth(null)*rHeight);
            
        }else
        {
            h=(int)(photoImage.getHeight(null)*rWidth);
        }
        g2D.drawImage(photoImage, (int) (0.5 * (getWidth() - w)), (int) (0.5 * (getHeight() - h)),w, h, null);
        g2D.dispose();
    }
}
class MyDocument implements Printable
{
    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Graphics2D g2D =(Graphics2D) g;
        return 0;
    }
}
class InventoryDocument implements Printable
{
    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Graphics2D g2D = (Graphics2D) g;
        if ((pageIndex + 1)>HomeInventory.lastPage) {
            return NO_SUCH_PAGE;
        }
        
        int i = 0,iEnd;
       
        g2D.setFont(new Font("Arial", Font.BOLD, 14));
        g2D.drawString("HomeInventory - Page"+ String.valueOf(pageIndex+1),(int)pf.getImageableX(),(int)(pf.getImageableY()+25));
        int dy=(int)g2D.getFont().getStringBounds("S", g2D.getFontRenderContext()).getHeight();
        int y = (int) (pf.getImageableY() + 4 * dy);
        iEnd = HomeInventory.entriesPerPage * (pageIndex + 1);
        if (iEnd>HomeInventory.numberEntries) {
            iEnd=HomeInventory.numberEntries;
        }
        for (int j = 0 + HomeInventory.entriesPerPage*pageIndex; j < iEnd; j++) {
            Line2D.Double dividingLine = new Line2D.Double(pf.getImageableX(),y,pf.getImageableX() + pf.getImageableWidth(),y);
            g2D.draw(dividingLine);
            y+=dy;
            g2D.setFont(new Font("Arial", Font.BOLD, 12));
            g2D.drawString(HomeInventory.myInventory[i].description, (int)pf.getImageableX(), y);
            g2D.drawString(HomeInventory.myInventory[i].description, (int) pf.getImageableX(), y);
            y += dy;
            g2D.setFont(new Font("Arial", Font.PLAIN, 12));
            g2D.drawString("Location: " + HomeInventory.myInventory[i].location, (int)(pf.getImageableX() + 25), y);
            y += dy;
            if (HomeInventory.myInventory[i].marked){
                g2D.drawString("Item is marked with identifying information.", (int)(pf.getImageableX() + 25), y);
            }else{
                g2D.drawString("Item is NOT marked with identifying information.", (int)(pf.getImageableX() + 25), y);  
                y += dy;
            }
            g2D.drawString("Serial Number: " +HomeInventory.myInventory[i].serialNumber, (int) (pf.getImageableX() + 25), y);
            y += dy;
            g2D.drawString("Price: $" + HomeInventory.myInventory[i].purchasePrice + ",Purchased on:" + HomeInventory.myInventory[i].purchaseDate, (int) (pf.getImageableX() +\n" +"25), y);
            y += dy;
            g2D.drawString(HomeInventory.myInventory[i].purchaseLocation +"Purchased at: ", (int) (pf.getImageableX() + 25), y);
            y += dy;
            g2D.drawString("Note: " + HomeInventory.myInventory[i].note, (int)(pf.getImageableX() + 25), y);
            y += dy;
            try {
                Image inventoryImage = new ImageIcon(HomeInventory.myInventory[i].photoFile).getImage();
                double ratio = (double) (inventoryImage.getWidth(null)) / (double) inventoryImage.getHeight(null);
                g2D.drawImage(inventoryImage, (int) (pf.getImageableX() + 25), y, (int) (100 *ratio), 100, null);
                
            } catch (Exception e) {
            }
            y += 2 * dy + 100;
        }
        return PAGE_EXISTS;
        
    }
    
}
