package view;

import config.Koneksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormHistori extends JFrame {

    private JTable tabel;
    private DefaultTableModel model;
    private JLabel lblTotalPinjam, lblBelumKembali;

    public FormHistori() {
        setTitle("BooksHub - Histori Lengkap Perpustakaan");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== 1. HEADER ATAS =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 240, 225));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblBrand = new JLabel("BooksHub / Log Aktivitas");
        lblBrand.setFont(new Font("Century Gothic", Font.BOLD, 16));
        lblBrand.setForeground(new Color(60, 60, 60));

        JLabel lblTagline = new JLabel("Arsip Riwayat Peminjaman Buku", SwingConstants.CENTER);
        lblTagline.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 22));
        lblTagline.setForeground(new Color(194, 43, 38));

        JButton btnKembali = new JButton("Dashboard");
        btnKembali.addActionListener(e -> { new MenuUtama().setVisible(true); dispose(); });

        header.add(lblBrand, BorderLayout.WEST);
        header.add(lblTagline, BorderLayout.CENTER);
        header.add(btnKembali, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== 2. PANEL TENGAH (Summary Card + Tabel) =====
        JPanel panelTengah = new JPanel();
        panelTengah.setLayout(new BoxLayout(panelTengah, BoxLayout.Y_AXIS));
        panelTengah.setBackground(Color.WHITE);
        panelTengah.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel card1 = buatKotakSummary("TOTAL SELURUH TRANSAKSI", new Color(194, 43, 38));
        lblTotalPinjam = new JLabel("0 Transaksi", SwingConstants.CENTER);
        lblTotalPinjam.setFont(new Font("Georgia", Font.BOLD, 18));
        lblTotalPinjam.setForeground(Color.WHITE);
        card1.add(lblTotalPinjam, BorderLayout.CENTER);

        JPanel card2 = buatKotakSummary("BUKU YANG MASIH DIPINJAM", new Color(92, 70, 50));
        lblBelumKembali = new JLabel("0 Buku", SwingConstants.CENTER);
        lblBelumKembali.setFont(new Font("Georgia", Font.BOLD, 18));
        lblBelumKembali.setForeground(Color.WHITE);
        card2.add(lblBelumKembali, BorderLayout.CENTER);

        summaryPanel.add(card1);
        summaryPanel.add(card2);

        // Tabel Riwayat
        model = new DefaultTableModel(
                new Object[]{"ID Pinjam", "Nama Peminjam", "Judul Buku", "Tgl Pinjam", "Tgl Kembali", "Status"}, 0
        );
        tabel = new JTable(model);
        tabel.setRowHeight(25);
        tabel.getTableHeader().setFont(new Font("Century Gothic", Font.BOLD, 12));

        JScrollPane scrollTabel = new JScrollPane(tabel);
        scrollTabel.setBorder(BorderFactory.createLineBorder(new Color(230, 225, 210)));

        panelTengah.add(summaryPanel);
        panelTengah.add(Box.createRigidArea(new Dimension(0, 20)));
        panelTengah.add(scrollTabel);

        add(panelTengah, BorderLayout.CENTER);

        loadDataHistori();
        loadAngkaSummary();
    }

    private JPanel buatKotakSummary(String title, Color c) {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        p.setBackground(c);
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Century Gothic", Font.BOLD, 10));
        lblTitle.setForeground(new Color(245, 240, 225));
        p.add(lblTitle, BorderLayout.NORTH);
        return p;
    }

    private void loadDataHistori() {
        model.setRowCount(0);
        String query = "SELECT p.id_pinjam, m.nama AS nama_peminjam, b.judul, " +
                "p.tgl_pinjam, p.tgl_kembali, p.status " +
                "FROM peminjaman p " +
                "LEFT JOIN anggota m ON p.id_anggota = m.id_anggota " +
                "LEFT JOIN buku b ON p.id_buku = b.id_buku " +
                "ORDER BY p.id_pinjam DESC";

        try (Connection c = Koneksi.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(query)) {

            while (r.next()) {
                String namaPeminjam = r.getString("nama_peminjam") != null ? r.getString("nama_peminjam") : "Tanpa Nama";
                String judulBuku = r.getString("judul") != null ? r.getString("judul") : "Buku Terhapus";
                String tglKembali = r.getDate("tgl_kembali") != null ? r.getDate("tgl_kembali").toString() : "-";

                model.addRow(new Object[]{
                        r.getInt("id_pinjam"), namaPeminjam, judulBuku, r.getDate("tgl_pinjam"), tglKembali, r.getString("status")
                });
            }
        } catch (Exception e) {
            // SATU CATCH JOPTIONPANE: Menampilkan pesan error asli database jika relasi table bermasalah
            JOptionPane.showMessageDialog(this, "Gagal memuat log histori dari Database:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAngkaSummary() {
        try (Connection c = Koneksi.getConnection()) {
            ResultSet r1 = c.createStatement().executeQuery("SELECT COUNT(*) FROM peminjaman");
            if (r1.next()) lblTotalPinjam.setText(r1.getInt(1) + " Transaksi");

            ResultSet r2 = c.createStatement().executeQuery("SELECT COUNT(*) FROM peminjaman WHERE status = 'Dipinjam'");
            if (r2.next()) lblBelumKembali.setText(r2.getInt(1) + " Buku");
        } catch (Exception e) {
            // Reset ke angka 0 jika query gagal dihitung
            lblTotalPinjam.setText("0 Transaksi");
            lblBelumKembali.setText("0 Buku");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormHistori().setVisible(true));
    }
}