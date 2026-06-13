package view;

import config.Koneksi;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import config.Session;


public class Login extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    private JCheckBox chkRemember;

    public Login() {
        setTitle("Login Aplikasi Perpustakaan Digital");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel() {
            ImageIcon icon = new ImageIcon(getClass().getResource("/model/background.jpg"));
            Image img = icon.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);

        JLabel lblAppTitle = new JLabel("PERPUSTAKAAN DIGITAL", SwingConstants.CENTER);
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblAppTitle.setForeground(new Color(255, 215, 0));
        lblAppTitle.setBounds(0, 60, 500, 50);
        panel.add(lblAppTitle);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 16);
        Font fontField = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontButton = new Font("Segoe UI", Font.BOLD, 18);

        int xField = 100;
        int widthField = 300;
        int y = 150;

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(xField, y, widthField, 20);
        lblUsername.setFont(fontLabel);
        lblUsername.setForeground(Color.WHITE);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(xField, y + 25, widthField, 40);
        txtUsername.setFont(fontField);
        addPlaceholder(txtUsername, "Masukkan username");
        panel.add(txtUsername);

        y += 80;

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(xField, y, widthField, 20);
        lblPassword.setFont(fontLabel);
        lblPassword.setForeground(Color.WHITE);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(xField, y + 25, widthField, 40);
        txtPassword.setFont(fontField);
        addPlaceholder(txtPassword, "Masukkan password");
        panel.add(txtPassword);

        y += 70;

        chkRemember = new JCheckBox("Ingat saya");
        chkRemember.setBounds(xField, y, 150, 20);
        chkRemember.setForeground(Color.WHITE);
        chkRemember.setOpaque(false);
        panel.add(chkRemember);

        y += 40;

        btnLogin = new JButton("Login");
        btnLogin.setBounds(xField, y, widthField, 45);
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(fontButton);
        btnLogin.addActionListener(e -> prosesLogin());
        panel.add(btnLogin);

        y += 65;

        btnRegister = new JButton("Register");
        btnRegister.setBounds(xField, y, widthField, 40);
        btnRegister.setBackground(new Color(40, 40, 40));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.addActionListener(e -> {
            new Register().setVisible(true);
            dispose();
        });
        panel.add(btnRegister);

        add(panel, BorderLayout.CENTER);
    }

    private void addPlaceholder(JTextComponent field, String text) {
        field.setText(text);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void prosesLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        String sql = "SELECT * FROM user WHERE username=? AND password=?";

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login berhasil");
                new MenuUtama().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
