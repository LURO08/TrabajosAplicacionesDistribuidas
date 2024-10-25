package voto;

import java.awt.event.KeyEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class PantallaElectoral extends javax.swing.JFrame {

    public String votocandidato = null;

    public PantallaElectoral() {
        initComponents();
        panelDatosVotante.setVisible(false);
        this.setSize((int) this.getSize().getWidth(), 90);
        this.setLocationRelativeTo(null);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnAceptarDNI = new javax.swing.JButton();
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
        jLabel8 = new javax.swing.JLabel();
        rbt_candidatoA = new javax.swing.JRadioButton();
        rbt_CandidatoB = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        rbt_Abstencion = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

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

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Interfaz Votante");
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, 30));

        jPanel1.setBackground(new java.awt.Color(52, 54, 56));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DNI del elector:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 22, -1, -1));

        btnAceptarDNI.setText("Aceptar");
        btnAceptarDNI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarDNIActionPerformed(evt);
            }
        });
        jPanel1.add(btnAceptarDNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(298, 19, -1, -1));

        panelDatosVotante.setBackground(new java.awt.Color(52, 54, 56));
        panelDatosVotante.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelDatosVotante.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nombre");
        panelDatosVotante.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, -1));
        panelDatosVotante.add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 231, -1));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Apellido Materno");
        panelDatosVotante.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));
        panelDatosVotante.add(txtAM, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 231, -1));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Apellido Paterno");
        panelDatosVotante.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, -1));
        panelDatosVotante.add(txtAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 231, -1));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Direccion");
        panelDatosVotante.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, -1, -1));
        panelDatosVotante.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 231, -1));
        panelDatosVotante.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 48, 81, 53));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/andres.jpg"))); // NOI18N
        jLabel8.setText("jLabel7");
        panelDatosVotante.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 98, 53));

        rbt_candidatoA.setBackground(new java.awt.Color(52, 54, 56));
        buttonGroup1.add(rbt_candidatoA);
        rbt_candidatoA.setForeground(new java.awt.Color(255, 255, 255));
        rbt_candidatoA.setText("Andres Manuel Lopez Obrador");
        rbt_candidatoA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbt_candidatoAActionPerformed(evt);
            }
        });
        panelDatosVotante.add(rbt_candidatoA, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, -1, -1));

        rbt_CandidatoB.setBackground(new java.awt.Color(52, 54, 56));
        buttonGroup1.add(rbt_CandidatoB);
        rbt_CandidatoB.setForeground(new java.awt.Color(255, 255, 255));
        rbt_CandidatoB.setText("Enrique Peña Nieto");
        rbt_CandidatoB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbt_CandidatoBActionPerformed(evt);
            }
        });
        panelDatosVotante.add(rbt_CandidatoB, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, 163, -1));

        jButton1.setText("Votar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelDatosVotante.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 390, 151, 41));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Vota por Mexico, piensa en ti!");
        panelDatosVotante.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 184, 380, -1));

        rbt_Abstencion.setBackground(new java.awt.Color(52, 54, 56));
        buttonGroup1.add(rbt_Abstencion);
        rbt_Abstencion.setForeground(new java.awt.Color(255, 255, 255));
        rbt_Abstencion.setSelected(true);
        rbt_Abstencion.setText("Abstencion");
        rbt_Abstencion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbt_AbstencionActionPerformed(evt);
            }
        });
        panelDatosVotante.add(rbt_Abstencion, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, -1, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/peña.jpg"))); // NOI18N
        jLabel10.setText("jLabel7");
        panelDatosVotante.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 99, 53));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Datos Del Votante");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        panelDatosVotante.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 14, 410, -1));

        jPanel1.add(panelDatosVotante, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, 54, 430, 450));

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 140, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 430, 510));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarDNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarDNIActionPerformed
        if (jTextField1.getText().length() == 8) {
             this.setSize((int) this.getSize().getWidth(), 540);
             this.setLocationRelativeTo(null);
            try {
                Registry register = LocateRegistry.getRegistry("192.168.231.150", 1099);
                Validar_Cliente validarInterfaz = (Validar_Cliente) register.lookup("validar_cliente");

                String datos[] = new String[4];
                datos = validarInterfaz.verificar_votante(Integer.parseInt(jTextField1.getText()));

                panelDatosVotante.setVisible(true);
                txtnombre.setText(datos[0]);
                txtAP.setText(datos[1]);
                txtAM.setText(datos[2]);
                txtDireccion.setText(datos[3]);
                if (jTextField1.getText().length() == 0) {
                    JOptionPane.showMessageDialog(null, "Dni incorrecto");
                }
            } catch (RemoteException ex) {
                Logger.getLogger(PantallaElectoral.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(PantallaElectoral.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("DNI DE LONGITUD incorecta");
        }

    }//GEN-LAST:event_btnAceptarDNIActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try {
            // Conectar al registro RMI
            Registry register = LocateRegistry.getRegistry("192.168.231.150", 1099);
            Validar_Cliente enviar_voto = (Validar_Cliente) register.lookup("validar_cliente");

            // Obtener el DNI del campo de texto
            int dni = Integer.parseInt(jTextField1.getText());

            // Verificar si el votante ya ha votado
            boolean yaVoto = enviar_voto.verificar_voto(dni);
            System.out.println("Voto verificado como: " + yaVoto);

            if (!yaVoto) {
                if (votocandidato != null) {
                    System.out.println("Voto por: " + votocandidato);
                    enviar_voto.enviar_voto(votocandidato, dni);
                    JOptionPane.showMessageDialog(null, "Voto realizado correctamente", "MENSAJE DE CONFIRMACIÓN", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una opción de voto", "ERROR", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                // Mensaje si ya ha votado
                JOptionPane.showMessageDialog(null, "ERROR: El solicitante ya realizó su voto", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "DNI inválido. Ingrese un número válido", "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(null, "Error de comunicación con el servidor: " + e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: " + e.toString());
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void rbt_candidatoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbt_candidatoAActionPerformed
        votocandidato = "Andres Manuel Lopez Obrador";
    }//GEN-LAST:event_rbt_candidatoAActionPerformed

    private void rbt_CandidatoBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbt_CandidatoBActionPerformed
        votocandidato = "Enrique Peña Nieto";
    }//GEN-LAST:event_rbt_CandidatoBActionPerformed

    private void rbt_AbstencionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbt_AbstencionActionPerformed
        votocandidato = "Abstencion";
    }//GEN-LAST:event_rbt_AbstencionActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
       System.exit(0);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
          if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        btnAceptarDNIActionPerformed(null);
    }//GEN-LAST:event_jTextField1KeyPressed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaElectoral().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptarDNI;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel panelDatosVotante;
    private javax.swing.JRadioButton rbt_Abstencion;
    private javax.swing.JRadioButton rbt_CandidatoB;
    private javax.swing.JRadioButton rbt_candidatoA;
    private javax.swing.JTextField txtAM;
    private javax.swing.JTextField txtAP;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtnombre;
    // End of variables declaration//GEN-END:variables

}
