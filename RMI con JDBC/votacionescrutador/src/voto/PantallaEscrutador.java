package voto;

import java.awt.event.KeyEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PantallaEscrutador extends javax.swing.JFrame {

    public String votocandidato = null;
    private final DefaultTableModel modeloTabla;

    public PantallaEscrutador() {
        initComponents();
        this.setLocationRelativeTo(null);
        
        btnActualizarVotante.setVisible(false);


        // Inicializa el modelo de la tabla
        modeloTabla = new DefaultTableModel(new Object[]{"DNI", "Nombre", "Apellido Paterno", "Apellido Materno", "Dirección"}, 0);
        TablaDatos.setModel(modeloTabla);
                MostrarDatos();
        

        // Agregar un MouseListener a la tabla
        TablaDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDatosMouseClicked(evt);
            }
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaDatos = new javax.swing.JTable();
        BtnCancelar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        panelDatosVotante = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAM = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtAP = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        DniElector = new javax.swing.JTextField();
        btnRegistrarVotante = new javax.swing.JButton();
        btnActualizarVotante = new javax.swing.JButton();
        jp_prep = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_results2 = new javax.swing.JTextArea();
        btn_recuento_de_votos = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        ta_results = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Votaciones");
        setBackground(new java.awt.Color(52, 54, 56));
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 51, 51));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("X");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Interfaz Escrutador");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 511, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 30));

        jPanel1.setBackground(new java.awt.Color(52, 54, 56));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(52, 54, 56));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TablaDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(TablaDatos);

        BtnCancelar.setText("Cancelar");
        BtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelarActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Datos del Votante");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(BtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 440, 260));

        panelDatosVotante.setBackground(new java.awt.Color(52, 54, 56));
        panelDatosVotante.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelDatosVotante.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nombre");
        panelDatosVotante.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, -1, -1));
        panelDatosVotante.add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 231, -1));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Apellido Materno");
        panelDatosVotante.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));
        panelDatosVotante.add(txtAM, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 231, -1));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Apellido Paterno");
        panelDatosVotante.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));
        panelDatosVotante.add(txtAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 231, -1));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Direccion");
        panelDatosVotante.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, -1, -1));
        panelDatosVotante.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 231, -1));
        panelDatosVotante.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 48, 81, 53));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Registrar Votante");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelDatosVotante.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 14, 360, -1));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DNI del elector:");
        panelDatosVotante.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        DniElector.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DniElectorKeyPressed(evt);
            }
        });
        panelDatosVotante.add(DniElector, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 230, -1));

        btnRegistrarVotante.setText("Registrar");
        btnRegistrarVotante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarVotanteActionPerformed(evt);
            }
        });
        panelDatosVotante.add(btnRegistrarVotante, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 100, 30));

        btnActualizarVotante.setText("Actualizar");
        btnActualizarVotante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarVotanteActionPerformed(evt);
            }
        });
        panelDatosVotante.add(btnActualizarVotante, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 100, 30));

        jPanel1.add(panelDatosVotante, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 260));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 25, 820, 260));

        jp_prep.setBackground(new java.awt.Color(52, 54, 56));
        jp_prep.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        ta_results2.setEditable(false);
        ta_results2.setColumns(2);
        ta_results2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ta_results2.setRows(3);
        jScrollPane1.setViewportView(ta_results2);

        btn_recuento_de_votos.setText("PREP");
        btn_recuento_de_votos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_recuento_de_votosActionPerformed(evt);
            }
        });

        ta_results.setColumns(1);
        ta_results.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ta_results.setRows(5);
        ta_results.setTabSize(6);
        ta_results.setAutoscrolls(false);
        jScrollPane2.setViewportView(ta_results);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Datos PREP");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jp_prepLayout = new javax.swing.GroupLayout(jp_prep);
        jp_prep.setLayout(jp_prepLayout);
        jp_prepLayout.setHorizontalGroup(
            jp_prepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_prepLayout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btn_recuento_de_votos, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116))
            .addGroup(jp_prepLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jp_prepLayout.setVerticalGroup(
            jp_prepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_prepLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jp_prepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_prepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_prepLayout.createSequentialGroup()
                        .addComponent(btn_recuento_de_votos, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        getContentPane().add(jp_prep, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 820, 200));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void BtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCancelarActionPerformed
       btnActualizarVotante.setVisible(false);
       btnRegistrarVotante.setVisible(true);
       clearFields();
    }//GEN-LAST:event_BtnCancelarActionPerformed

    private void btnActualizarVotanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarVotanteActionPerformed
        if (DniElector.getText().length() == 8) {
            try {
                Registry register = LocateRegistry.getRegistry("192.168.231.150", 1099);
                Validar_Cliente validarInterfaz = (Validar_Cliente) register.lookup("validar_cliente");

                // Actualizar los datos del elector
                boolean actualizado = validarInterfaz.actualizarElector(DniElector.getText(), txtnombre.getText(), txtAP.getText(), txtAM.getText(), txtDireccion.getText());

                if (actualizado) {
                    JOptionPane.showMessageDialog(this, "Elector actualizado con éxito.");
                    MostrarDatos();
                    btnActualizarVotante.setVisible(false);
                    btnRegistrarVotante.setVisible(true);
                    clearFields(); // Método para limpiar campos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar al elector. Por favor, verifica los datos.");
                }

            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(PantallaEscrutador.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex) {
                Logger.getLogger(PantallaEscrutador.class.getName()).log(Level.SEVERE, "DNI debe ser un número", ex);
            }
        } else {
            System.out.println("DNI DE LONGITUD incorrecta");
        }
    }//GEN-LAST:event_btnActualizarVotanteActionPerformed

    private void btnRegistrarVotanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarVotanteActionPerformed
        if (DniElector.getText().length() == 8) {

            try {
                Registry register = LocateRegistry.getRegistry("192.168.231.150", 1099);
                Validar_Cliente validarInterfaz = (Validar_Cliente) register.lookup("validar_cliente");

                // Obtener los datos del elector
                boolean registrado = validarInterfaz.registrarElector(DniElector.getText(), txtnombre.getText(), txtAP.getText(), txtAM.getText(), txtDireccion.getText());

                if (registrado) {
                    JOptionPane.showMessageDialog(this, "Elector registrado con éxito.");
                    MostrarDatos();
                    DniElector.setText("");
                    txtnombre.setText("");
                    txtAP.setText("");
                    txtAM.setText("");
                    txtDireccion.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar al elector. Por favor, verifica los datos.");
                }

            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(PantallaEscrutador.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex) {
                Logger.getLogger(PantallaEscrutador.class.getName()).log(Level.SEVERE, "DNI debe ser un número", ex);
            }
        } else {
            System.out.println("DNI DE LONGITUD incorrecta");
        }
    }//GEN-LAST:event_btnRegistrarVotanteActionPerformed

    private void DniElectorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DniElectorKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        btnRegistrarVotanteActionPerformed(null);
    }//GEN-LAST:event_DniElectorKeyPressed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (DniElector.getText().length() == 8) {
            try {
                Registry register = LocateRegistry.getRegistry("192.168.231.150", 1099);
                Validar_Cliente validarInterfaz = (Validar_Cliente) register.lookup("validar_cliente");

                // Actualizar los datos del elector
                boolean eliminado = validarInterfaz.eliminarElector(DniElector.getText());

                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Elector eliminado con éxito.");
                    btnActualizarVotante.setVisible(false);
                    btnRegistrarVotante.setVisible(true);
                    MostrarDatos();
                    clearFields(); // Método para limpiar campos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar al elector. Por favor, verifica los datos.");
                }

            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(PantallaEscrutador.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex) {
                Logger.getLogger(PantallaEscrutador.class.getName()).log(Level.SEVERE, "DNI debe ser un número", ex);
            }
        } else {
            System.out.println("DNI DE LONGITUD incorrecta");
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btn_recuento_de_votosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_recuento_de_votosActionPerformed
        Registry register;
        try {
            register = LocateRegistry.getRegistry("192.168.239.138", 4070);
            votos miInerfaz = (votos) register.lookup("urna");
            String[][] registro = miInerfaz.recuento();

            // Comenzamos la salida con los encabezados
            StringBuilder salida = new StringBuilder("CANDIDATO\n");
            StringBuilder salida2 = new StringBuilder("VOTOS\n");

            // Iteramos sobre el arreglo y construimos la salida
            for (int i = 0; i < registro.length; i++) {
                if (registro[i][0] != null && registro[i][1] != null) {
                    salida.append(registro[i][0]).append("\n");
                    salida2.append(registro[i][1]).append("\n");
                }
            }

            // Mostramos la salida en el JTextArea
            ta_results.setText(salida.toString());
            ta_results2.setText(salida2.toString());

        } catch (RemoteException ex) {
            System.out.println("" + ex.getMessage());
        } catch (NotBoundException ex) {
            System.out.println("" + ex.getMessage());
        }

    }//GEN-LAST:event_btn_recuento_de_votosActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaEscrutador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCancelar;
    private javax.swing.JTextField DniElector;
    private javax.swing.JTable TablaDatos;
    private javax.swing.JButton btnActualizarVotante;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnRegistrarVotante;
    private javax.swing.JButton btn_recuento_de_votos;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel jp_prep;
    private javax.swing.JPanel panelDatosVotante;
    private javax.swing.JTextArea ta_results;
    private javax.swing.JTextArea ta_results2;
    private javax.swing.JTextField txtAM;
    private javax.swing.JTextField txtAP;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtnombre;
    // End of variables declaration//GEN-END:variables
    
     private void tablaDatosMouseClicked(java.awt.event.MouseEvent evt) {
         
         btnActualizarVotante.setVisible(true);
         btnRegistrarVotante.setVisible(false);
        int filaSeleccionada = TablaDatos.getSelectedRow();
        if (filaSeleccionada != -1) {
            // Cargar los datos de la fila seleccionada en los campos de texto
            DniElector.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtnombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtAP.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtAM.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtDireccion.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
        }
    }
     
      private void clearFields() {
        DniElector.setText("");
        txtnombre.setText("");
        txtAP.setText("");
        txtAM.setText("");
        txtDireccion.setText("");
    }
      
      private void MostrarDatos(){
           try {
            Registry register = LocateRegistry.getRegistry("192.168.231.150", 1099);
            Validar_Cliente validarInterfaz = (Validar_Cliente) register.lookup("validar_cliente");

            // Obtener todos los electores
            List<String[]> electores = validarInterfaz.obtenerTodosElectores();

            // Limpiar el modelo de la tabla antes de agregar nuevos datos
            modeloTabla.setRowCount(0);

            for (String[] datos : electores) {
                modeloTabla.addRow(datos); // Agregar cada fila de datos al modelo
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(PantallaEscrutador.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
}
