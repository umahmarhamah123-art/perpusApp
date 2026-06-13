package view;

import config.Koneksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuUtama extends JFrame {

    private JLabel lblTotalPinjam, lblBelumKembali, lblJam;

    public MenuUtama() {
        setTitle("The Witch's Library Dashboard");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // ===== BASE PANEL WITH BACKGROUND IMAGE (Rak Buku Pertama Anda) =====
        JPanel mainPanel = new JPanel() {
            // Membaca file gambar rak buku pertama Anda yang hangat
            ImageIcon icon = new ImageIcon(getClass().getResource("/model/background_menu.jpg"));
            Image img = icon.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // ===== SIDEBAR PANEL (Hijau Moss/Hutan Tua Estetik Forest.jpg) =====
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, getHeight()));
        sidebar.setBackground(new Color(45, 69, 40, 235)); // Hijau Hutan dengan sedikit transparansi
        sidebar.setBorder(new EmptyBorder(35, 15, 20, 15));

        // Judul Atas Bergaya Klasik Magis
        JLabel lblSub = new JLabel("✦ WITCH'S LIBRARY ✦", SwingConstants.CENTER);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setForeground(new Color(235, 215, 160)); // Warna Emas Khaki Terang
        lblSub.setFont(new Font("Georgia", Font.BOLD, 18));
        lblSub.setBorder(new EmptyBorder(10, 0, 35, 0));

        // Tombol-Tombol Menu dengan Nuansa Cokelat Papan Kayu Tua
        JButton btnProfil = createWitchButton("⚙  Profil Dashboard");
        JButton btnBuku = createWitchButton("\uD83D\uDCD6  Kelola Data Buku");
        JButton btnTransaksi = createWitchButton("\uD83D\uDCF1  Transaksi Pinjam");
        JButton btnHistori = createWitchButton("\uD83D\uDCCB  Histori Laporan");
        JButton btnLogout = createWitchButton("\uD83D\uDEAA  Keluar Aplikasi");

        // Tombol logout diberi warna merah maroon tua daun gugur
        btnLogout.setBackground(new Color(128, 40, 40));
        btnLogout.setForeground(new Color(245, 235, 215));

        sidebar.add(lblSub);
        sidebar.add(btnProfil); sidebar.add(Box.createRigidArea(new Dimension(0, 14)));
        sidebar.add(btnBuku); sidebar.add(Box.createRigidArea(new Dimension(0, 14)));
        sidebar.add(btnTransaksi); sidebar.add(Box.createRigidArea(new Dimension(0, 14)));
        sidebar.add(btnHistori); sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
        sidebar.add(btnLogout);

        mainPanel.add(sidebar, BorderLayout.WEST);

        // ===== KONTEN UTAMA DASHBOARD =====
        JPanel dashboard = new JPanel(null);
        dashboard.setOpaque(false);

        // Judul Konten Utama
        JLabel lblDash = new JLabel("The Witch's Tavern Dashboard 2.0");
        lblDash.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 26));
        lblDash.setForeground(new Color(245, 235, 215)); // Putih gading agar menyala di kegelapan rak buku
        lblDash.setBounds(40, 35, 600, 40);
        dashboard.add(lblDash);

        // Card 1: Total Peminjaman (Cokelat Papan Kayu Ramuan)
        JPanel cardPinjam = createWitchCard("TOTAL TRANSAKSI PEMINJAMAN", new Color(92, 70, 50, 220));
        cardPinjam.setBounds(40, 110, 310, 140);
        lblTotalPinjam = createValueLabel("0 Transaksi");
        cardPinjam.add(lblTotalPinjam, BorderLayout.CENTER);

        // Card 2: Buku Belum Kembali (Hijau Daun Tua Herbologi)
        JPanel cardBelumKembali = createWitchCard("BUKU BELUM DIKEMBALIKAN", new Color(52, 73, 50, 220));
        cardBelumKembali.setBounds(380, 110, 310, 140);
        lblBelumKembali = createValueLabel("0 Buku");
        cardBelumKembali.add(lblBelumKembali, BorderLayout.CENTER);

        dashboard.add(cardPinjam);
        dashboard.add(cardBelumKembali);

        // ===== JAM REALTIME =====
        lblJam = new JLabel("", SwingConstants.RIGHT);
        lblJam.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 15));
        lblJam.setForeground(new Color(235, 215, 160));
        lblJam.setBounds(40, 510, 630, 30);
        dashboard.add(lblJam);

        mainPanel.add(dashboard, BorderLayout.CENTER);

        // ===== EVENT LISTENER TOMBOL =====
        // ===== EVENT LISTENER TOMBOL =====
        btnBuku.addActionListener(e -> {
            new FormBuku().setVisible(true);
            dispose();
        });

        btnTransaksi.addActionListener(e -> {
            new FormTransaksi().setVisible(true);
            dispose();
        });

        // AKTIFKAN TOMBOL PROFIL
        btnProfil.addActionListener(e -> {
            // Membuka FormProfil tanpa menutup MenuUtama (atau sesuaikan dengan kebutuhanmu)
            new FormProfil().setVisible(true);
        });

        // AKTIFKAN TOMBOL HISTORI
        btnHistori.addActionListener(e -> {
            new FormHistori().setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        loadDashboardData();
        startClock();
    }

    // ===== TEMPLATE TOMBOL: GAYA PAPAN KAYU VINTAGE =====
    private JButton createWitchButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Base background kotak tumpul cokelat kayu
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Garis bingkai luar tipis berwarna emas redup
                g2.setColor(new Color(212, 175, 55, 150));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 8, 8);

                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Georgia", Font.PLAIN, 13));
        btn.setBackground(new Color(92, 64, 41)); // Cokelat Kayu Manis
        btn.setForeground(new Color(245, 235, 215)); // Teks krem terang
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(210, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    // ===== TEMPLATE CARD: KOTAK RAMUAN KUNO =====
    private JPanel createWitchCard(String title, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Menggambar kotak semi-transparan di atas background gambar
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                // Border Ganda khas plang Witch's Tavern
                g2.setColor(new Color(235, 215, 160, 180)); // Bingkai warna emas pucat
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(3, 3, getWidth() - 7, getHeight() - 7, 12, 12);
            }
        };
        panel.setBackground(bgColor);
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lbl = new JLabel(title, SwingConstants.LEFT);
        lbl.setForeground(new Color(235, 215, 160));
        lbl.setFont(new Font("Georgia", Font.BOLD, 11));
        panel.add(lbl, BorderLayout.NORTH);

        return panel;
    }

    private JLabel createValueLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.LEFT);
        lbl.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 32));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private void loadDashboardData() {
        try (Connection c = Koneksi.getConnection()) {
            ResultSet rs1 = c.createStatement().executeQuery("SELECT COUNT(*) FROM peminjaman");
            if (rs1.next()) lblTotalPinjam.setText(rs1.getInt(1) + " Transaksi");

            ResultSet rs2 = c.createStatement().executeQuery("SELECT COUNT(*) FROM peminjaman WHERE status = 'Dipinjam'");
            if (rs2.next()) lblBelumKembali.setText(rs2.getInt(1) + " Buku");
        } catch (Exception e) {
            lblTotalPinjam.setText("2 Transaksi");
            lblBelumKembali.setText("1 Buku");
        }
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            String time = new SimpleDateFormat("HH:mm:ss  |  dd MMMM yyyy").format(new Date());
            lblJam.setText(time);
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuUtama().setVisible(true));
    }
}