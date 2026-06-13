package view;

import config.Koneksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormTransaksi extends JFrame {

    private JTable tabel;
    private DefaultTableModel model;
    private JTextField txtIdAnggota, txtIdBuku;
    private JButton btnPinjam, btnKembaliKeMenu;

    public FormTransaksi() {
        setTitle("BooksHub - Transaksi Peminjaman Buku");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== 1. HEADER ATAS =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 240, 225));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblBrand = new JLabel("BooksHub / Sirkulasi");
        lblBrand.setFont(new Font("Century Gothic", Font.BOLD, 16));
        lblBrand.setForeground(new Color(60, 60, 60));

        JLabel lblTagline = new JLabel("Buku Dipinjam & Pengembalian", SwingConstants.CENTER);
        lblTagline.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 22));
        lblTagline.setForeground(new Color(19, 107, 107));

        btnKembaliKeMenu = new JButton("Dashboard");
        btnKembaliKeMenu.addActionListener(e -> { new MenuUtama().setVisible(true); dispose(); });

        header.add(lblBrand, BorderLayout.WEST);
        header.add(lblTagline, BorderLayout.CENTER);
        header.add(btnKembaliKeMenu, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== 2. PANEL TENGAH (Banner + Tabel) =====
        JPanel panelTengah = new JPanel();
        panelTengah.setLayout(new BoxLayout(panelTengah, BoxLayout.Y_AXIS));
        panelTengah.setBackground(Color.WHITE);
        panelTengah.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel infoCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        infoCard.setBackground(new Color(19, 107, 107));
        infoCard.setOpaque(false);
        infoCard.setPreferredSize(new Dimension(650, 80));
        infoCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        infoCard.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblInfo = new JLabel("<html><span style='font-size:13px; font-weight:bold;'>PANDUAN TRANSAKSI</span><br><span style='font-size:11px;'>Pastikan ID Anggota dan ID Buku sudah terdaftar di database sebelum memproses pinjaman.</span></html>");
        lblInfo.setForeground(new Color(245, 240, 225));
        infoCard.add(lblInfo, BorderLayout.CENTER);

        // Tabel Data Transaksi
        model = new DefaultTableModel(new Object[]{"ID Pinjam", "ID Anggota", "ID Buku", "Tgl Pinjam", "Status"}, 0);
        tabel = new JTable(model);
        tabel.setRowHeight(25);
        tabel.getTableHeader().setFont(new Font("Century Gothic", Font.BOLD, 12));

        JScrollPane scrollTabel = new JScrollPane(tabel);
        scrollTabel.setBorder(BorderFactory.createLineBorder(new Color(230, 225, 210)));

        panelTengah.add(infoCard);
        panelTengah.add(Box.createRigidArea(new Dimension(0, 20)));
        panelTengah.add(scrollTabel);
        add(panelTengah, BorderLayout.CENTER);

        // ===== 3. PANEL KANAN (Form Input) =====
        JPanel panelInput = new JPanel();
        panelInput.setLayout(new BoxLayout(panelInput, BoxLayout.Y_AXIS));
        panelInput.setPreferredSize(new Dimension(280, getHeight()));
        panelInput.setBackground(new Color(248, 249, 250));
        panelInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
                new EmptyBorder(30, 20, 20, 20)
        ));

        JLabel lblFormTitle = new JLabel("Input Peminjaman Baru");
        lblFormTitle.setFont(new Font("Century Gothic", Font.BOLD, 14));

        txtIdAnggota = new JTextField(); txtIdAnggota.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtIdBuku = new JTextField(); txtIdBuku.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        btnPinjam = new JButton("Proses Pinjam Buku");
        btnPinjam.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnPinjam.setBackground(new Color(194, 43, 38));
        btnPinjam.setForeground(Color.WHITE);
        btnPinjam.setFocusPainted(false);
        btnPinjam.addActionListener(e -> aksiPinjamBuku());

        panelInput.add(lblFormTitle); panelInput.add(Box.createRigidArea(new Dimension(0, 20)));
        panelInput.add(new JLabel("ID Anggota / Peminjam:")); panelInput.add(txtIdAnggota); panelInput.add(Box.createRigidArea(new Dimension(0, 15)));
        panelInput.add(new JLabel("ID Buku yang Dipinjam:")); panelInput.add(txtIdBuku); panelInput.add(Box.createRigidArea(new Dimension(0, 25)));
        panelInput.add(btnPinjam);

        add(panelInput, BorderLayout.EAST);

        tampilTabelTransaksi();
    }

    private void tampilTabelTransaksi() {
        model.setRowCount(0);
        String sql = "SELECT id_pinjam, id_anggota, id_buku, tgl_pinjam, status FROM peminjaman WHERE status = 'Dipinjam' ORDER BY id_pinjam DESC";
        try (Connection c = Koneksi.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(sql)) {

            while (r.next()) {
                model.addRow(new Object[]{
                        r.getInt("id_pinjam"),
                        r.getInt("id_anggota"),
                        r.getInt("id_buku"),
                        r.getDate("tgl_pinjam"),
                        r.getString("status")
                });
            }
        } catch (Exception e) {
            // SATU CATCH JOPTIONPANE: Menampilkan pesan error asli database
            JOptionPane.showMessageDialog(this, "Gagal memuat data transaksi dari Database:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiPinjamBuku() {
        if (txtIdAnggota.getText().isEmpty() || txtIdBuku.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua ID input wajib diisi!");
            return;
        }

        String query = "INSERT INTO peminjaman (id_anggota, id_buku, tgl_pinjam, status) VALUES (?, ?, CURDATE(), 'Dipinjam')";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement p = c.prepareStatement(query)) {
            p.setInt(1, Integer.parseInt(txtIdAnggota.getText()));
            p.setInt(2, Integer.parseInt(txtIdBuku.getText()));
            p.executeUpdate();

            JOptionPane.showMessageDialog(this, "Transaksi Berhasil Ditambahkan!");
            tampilTabelTransaksi();
            txtIdAnggota.setText(""); txtIdBuku.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memproses pinjaman ke Database:\n" + e.getMessage(), "Execution Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormTransaksi().setVisible(true));
    }
}