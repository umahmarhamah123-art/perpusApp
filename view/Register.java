package view;

import config.Koneksi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Register extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister, btnKembali;

    public Register() {
        setTitle("Register LibraTech");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(240, 245, 250));
        setLayout(null);

        JLabel lblTitle = new JLabel("Daftar Akun Baru", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBounds(50, 20, 300, 40);
        add(lblTitle);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(50, 80, 300, 270);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        add(panel);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(20, 20, 100, 25);
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(20, 45, 260, 30);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 85, 100, 25);
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(20, 110, 260, 30);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(txtPassword);

        btnRegister = new JButton("Daftar");
        btnRegister.setBounds(20, 160, 260, 40);
        btnRegister.setBackground(new Color(0, 153, 255));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createEmptyBorder());
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnRegister);

        btnRegister.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnRegister.setBackground(new Color(0, 123, 230));
            }

            public void mouseExited(MouseEvent e) {
                btnRegister.setBackground(new Color(0, 153, 255));
            }
        });

        btnKembali = new JButton("Kembali ke Login");
        btnKembali.setBounds(20, 215, 260, 35);
        btnKembali.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnKembali.setFocusPainted(false);
        btnKembali.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnKembali);

        btnKembali.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        btnRegister.addActionListener(e -> prosesRegister());
    }

    private void prosesRegister() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!");
            return;
        }

        try (Connection conn = Koneksi.getConnection()) {
            String cekQuery = "SELECT * FROM register WHERE username = ?";
            try (PreparedStatement cekStmt = conn.prepareStatement(cekQuery)) {
                cekStmt.setString(1, user);
                ResultSet rs = cekStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username sudah digunakan, silakan pilih yang lain.");
                    return;
                }
            }

            String insertQuery = "INSERT INTO register (username, password) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
                ps.setString(1, user);
                ps.setString(2, pass);
                int rowsInserted = ps.executeUpdate();

                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Pendaftaran berhasil!");
                    new Login().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mendaftar.");
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Register().setVisible(true));
    }
}
