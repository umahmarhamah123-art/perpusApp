package view;

import javax.swing.*;
import java.awt.*;

public class FormProfil extends JFrame {

    private JTextField txtUsername, txtRole;

    public FormProfil() {
        setTitle("Profil Pengguna");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // ===== BACKGROUND =====
        JPanel background = new JPanel() {
            ImageIcon icon = new ImageIcon(getClass().getResource("/model/background_menu.jpg"));
            Image img = icon.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setLayout(null);
        add(background);

        // ===== CARD PROFIL =====
        JPanel card = new JPanel(null);
        card.setBackground(new Color(0, 0, 0, 180));
        card.setBounds(260, 80, 380, 380);
        card.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        background.add(card);

        // ===== ICON USER =====
        JLabel lblIcon = new JLabel();
        lblIcon.setIcon(new ImageIcon(getClass().getResource("/model/user.png")));
        lblIcon.setBounds(140, 20, 100, 100);
        card.add(lblIcon);

        // ===== USERNAME =====
        JLabel lblUser = new JLabel("Username", SwingConstants.CENTER);
        lblUser.setForeground(Color.ORANGE);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setBounds(40, 130, 300, 20);
        card.add(lblUser);

        txtUsername = new JTextField("staff perpus");
        txtUsername.setBounds(40, 155, 300, 30);
        txtUsername.setHorizontalAlignment(JTextField.CENTER);
        card.add(txtUsername);

        // ===== ROLE =====
        JLabel lblRole = new JLabel("Role", SwingConstants.CENTER);
        lblRole.setForeground(Color.ORANGE);
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRole.setBounds(40, 195, 300, 20);
        card.add(lblRole);

        txtRole = new JTextField("STAFF");
        txtRole.setBounds(40, 220, 300, 30);
        txtRole.setHorizontalAlignment(JTextField.CENTER);
        card.add(txtRole);

        // ===== BUTTON =====
        JButton btnSimpan = new JButton("Edit / Simpan");
        btnSimpan.setBounds(40, 270, 140, 35);
        btnSimpan.setBackground(new Color(255, 165, 0));
        btnSimpan.setForeground(Color.BLACK);
        card.add(btnSimpan);

        JButton btnKembali = new JButton("Kembali");
        btnKembali.setBounds(200, 270, 140, 35);
        card.add(btnKembali);

        // ===== KETERANGAN APLIKASI =====
        JLabel lblInfo = new JLabel(
                "<html><center>PERPUSTAKAAN DIGITAL<br>Versi Desktop Java Swing</center></html>",
                SwingConstants.CENTER
        );
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfo.setBounds(20, 320, 340, 40);
        card.add(lblInfo);

        // ===== EVENT =====
        btnSimpan.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui")
        );

        btnKembali.addActionListener(e -> dispose());
    }
}
