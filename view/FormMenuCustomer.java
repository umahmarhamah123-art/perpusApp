package view;

import config.Koneksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FormMenuCustomer extends JFrame {

    private JPanel panelKatalogGrid;
    private JLabel lblNamaUser, lblBukuDipinjam;
    private int idAnggotaLogin = 5; // Contoh ID Anggota

    public FormMenuCustomer() {
        setTitle("BooksHub - Customer Digital Library Browser");
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== 1. HEADER ATAS =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 240, 225));
        header.setBorder(new EmptyBorder(15, 30, 15, 30));

        JPanel panelKiriHeader = new JPanel(new GridLayout(2, 1));
        panelKiriHeader.setOpaque(false);
        JLabel lblWelcome = new JLabel("Welcome Back, Reader! ✨");
        lblWelcome.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        lblNamaUser = new JLabel("kelompok 3");
        lblNamaUser.setFont(new Font("Georgia", Font.BOLD, 20));
        lblNamaUser.setForeground(new Color(194, 43, 38));
        panelKiriHeader.add(lblWelcome);
        panelKiriHeader.add(lblNamaUser);

        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelCari.setOpaque(false);
        JTextField txtCari = new JTextField("Cari judul buku atau penulis...");
        txtCari.setPreferredSize(new Dimension(250, 30));
        txtCari.setForeground(Color.GRAY);

        JButton btnKeluar = new JButton("Logout");
        btnKeluar.setBackground(new Color(194, 43, 38));
        btnKeluar.setForeground(Color.WHITE);
        btnKeluar.addActionListener(e -> { dispose(); System.exit(0); });

        panelCari.add(txtCari);
        panelCari.add(btnKeluar);

        header.add(panelKiriHeader, BorderLayout.WEST);
        header.add(panelCari, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== 2. SIDEBAR KANAN =====
        JPanel sidebarKanan = new JPanel();
        sidebarKanan.setLayout(new BoxLayout(sidebarKanan, BoxLayout.Y_AXIS));
        sidebarKanan.setPreferredSize(new Dimension(280, getHeight()));
        sidebarKanan.setBackground(new Color(248, 249, 250));
        sidebarKanan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
                new EmptyBorder(25, 20, 20, 20)
        ));

        JLabel lblRakTitle = new JLabel("Rak Pinjaman Kamu");
        lblRakTitle.setFont(new Font("Georgia", Font.BOLD, 15));
        lblRakTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblBukuDipinjam = new JLabel("<html><ol><li>Laut Bercerita (Batas: 15 Juni)</li><li>The Atomic Love (Batas: 17 Juni)</li></ol></html>");
        lblBukuDipinjam.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        lblBukuDipinjam.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblBukuDipinjam.setBorder(new EmptyBorder(15, 0, 15, 0));

        JPanel infoDendaCard = new JPanel(new BorderLayout());
        infoDendaCard.setBackground(new Color(19, 107, 107, 20));
        infoDendaCard.setBorder(BorderFactory.createLineBorder(new Color(19, 107, 107), 1));
        infoDendaCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel lblDenda = new JLabel("Status Denda: Rp 0 (Bersih)", SwingConstants.CENTER);
        lblDenda.setFont(new Font("Century Gothic", Font.BOLD, 12));
        lblDenda.setForeground(new Color(19, 107, 107));
        infoDendaCard.add(lblDenda, BorderLayout.CENTER);

        sidebarKanan.add(lblRakTitle);
        sidebarKanan.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarKanan.add(lblBukuDipinjam);
        sidebarKanan.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebarKanan.add(infoDendaCard);
        add(sidebarKanan, BorderLayout.EAST);

        // ===== 3. AREA KATALOG GRID BUKU INTERAKTIF =====
        panelKatalogGrid = new JPanel(new GridLayout(0, 3, 25, 25));
        panelKatalogGrid.setBackground(Color.WHITE);
        panelKatalogGrid.setBorder(new EmptyBorder(30, 30, 30, 30));

        JScrollPane scrollKatalog = new JScrollPane(panelKatalogGrid);
        scrollKatalog.setBorder(null);
        add(scrollKatalog, BorderLayout.CENTER);

        tampilkanKatalogMasyarakat();
    }

    private void tampilkanKatalogMasyarakat() {
        panelKatalogGrid.removeAll();

        String query = "SELECT * FROM buku ORDER BY id_buku DESC";
        try (Connection c = Koneksi.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(query)) {

            while (r.next()) {
                int idBuku = r.getInt("id_buku");
                String judul = r.getString("judul");
                String pengarang = r.getString("pengarang");

                // Amankan kolom gambar jika belum dibuat di phpMyAdmin
                String namaGambar = "";
                try {
                    namaGambar = r.getString("gambar");
                } catch (Exception e) {
                    namaGambar = "";
                }

                String penerbit = "";
                try { penerbit = r.getString("penerbit"); } catch (Exception e) { penerbit = "Unknown Publisher"; }

                buatCardBuku(idBuku, judul, pengarang, penerbit, namaGambar);
            }

        } catch (Exception e) {
            // Mode Cadangan Otomatis jika database offline / kolom belum siap
            buatCardBuku(1, "The Atomic Love", "Alvia Glenn", "BooksHub Media", "atomic.jpg");
            buatCardBuku(2, "Laut Bercerita", "Leila S. Chudori", "KPG", "laut.jpg");
            buatCardBuku(3, "Siddhartha", "Hermann Hesse", "Bentang Budaya", "siddhartha.jpg");
        }

        panelKatalogGrid.revalidate();
        panelKatalogGrid.repaint();
    }

    private void buatCardBuku(int idBuku, String judul, String author, String penerbit, String namaGambar) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(253, 253, 251));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 225, 210), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Label Gambar Cover Buku
        JLabel lblCover = new JLabel("", SwingConstants.CENTER);
        lblCover.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCover.setPreferredSize(new Dimension(140, 180));
        lblCover.setMaximumSize(new Dimension(140, 180));

        // Membaca gambar dari folder src/img/
        String folderGambar = "src/img/";
        java.io.File fileGambar = new java.io.File(folderGambar + namaGambar);

        if (namaGambar != null && !namaGambar.isEmpty() && fileGambar.exists()) {
            try {
                ImageIcon iconAsli = new ImageIcon(fileGambar.getAbsolutePath());
                Image imgAsli = iconAsli.getImage();
                Image imgResized = imgAsli.getScaledInstance(140, 180, Image.SCALE_SMOOTH);
                lblCover.setIcon(new ImageIcon(imgResized));
            } catch (Exception e) {
                lblCover.setText("📕 " + judul);
            }
        } else {
            // Kotak Cadangan Aesthetic
            lblCover.setText("<html><center>📕<br><br>" + judul + "</center></html>");
            lblCover.setOpaque(true);
            lblCover.setBackground(new Color(194, 43, 38, 20));
            lblCover.setFont(new Font("Georgia", Font.BOLD, 12));
            lblCover.setForeground(new Color(194, 43, 38));
        }

        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(new Font("Georgia", Font.BOLD, 14));
        lblJudul.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblJudul.setBorder(new EmptyBorder(10, 0, 2, 0));

        JLabel lblAuthor = new JLabel(author);
        lblAuthor.setFont(new Font("Century Gothic", Font.ITALIC, 11));
        lblAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAuthor.setForeground(Color.GRAY);

        JLabel lblKlikDetail = new JLabel("✨ Klik untuk detail sinopsis");
        lblKlikDetail.setFont(new Font("Century Gothic", Font.PLAIN, 10));
        lblKlikDetail.setForeground(new Color(19, 107, 107));
        lblKlikDetail.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblKlikDetail.setBorder(new EmptyBorder(5, 0, 5, 0));

        card.add(lblCover);
        card.add(lblJudul);
        card.add(lblAuthor);
        card.add(lblKlikDetail);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bukaPopupDetailBuku(idBuku, judul, author, penerbit);
            }
        });

        panelKatalogGrid.add(card);
    }

    private void bukaPopupDetailBuku(int idBuku, String judul, String author, String penerbit) {
        JDialog popup = new JDialog(this, "Book Detail - " + judul, true);
        popup.setSize(500, 420);
        popup.setLocationRelativeTo(this);
        popup.setLayout(new BorderLayout());

        JPanel pnlKonten = new JPanel();
        pnlKonten.setLayout(new BoxLayout(pnlKonten, BoxLayout.Y_AXIS));
        pnlKonten.setBackground(Color.WHITE);
        pnlKonten.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblPopJudul = new JLabel(judul);
        lblPopJudul.setFont(new Font("Georgia", Font.BOLD, 22));
        lblPopJudul.setForeground(new Color(194, 43, 38));

        JLabel lblPopAuthor = new JLabel("Ditulis oleh: " + author + "  |  Penerbit: " + penerbit);
        lblPopAuthor.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        lblPopAuthor.setForeground(Color.GRAY);
        lblPopAuthor.setBorder(new EmptyBorder(5, 0, 15, 0));

        JSeparator line = new JSeparator();
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));

        JLabel lblSinopsisTitle = new JLabel("Sinopsis Buku :");
        lblSinopsisTitle.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 13));
        lblSinopsisTitle.setBorder(new EmptyBorder(15, 0, 8, 0));

        String teksSinopsis = ambilDataSinopsis(judul);
        JTextArea txtSinopsis = new JTextArea(teksSinopsis);
        txtSinopsis.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSinopsis.setLineWrap(true);
        txtSinopsis.setWrapStyleWord(true);
        txtSinopsis.setEditable(false);
        txtSinopsis.setBackground(new Color(250, 250, 248));
        txtSinopsis.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 225, 210)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollSinopsis = new JScrollPane(txtSinopsis);
        scrollSinopsis.setBorder(null);

        pnlKonten.add(lblPopJudul);
        pnlKonten.add(lblPopAuthor);
        pnlKonten.add(line);
        pnlKonten.add(lblSinopsisTitle);
        pnlKonten.add(scrollSinopsis);
        popup.add(pnlKonten, BorderLayout.CENTER);

        JPanel pnlTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlTombol.setBackground(new Color(245, 240, 225));

        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> popup.dispose());

        JButton btnKonfirmasiPinjam = new JButton("Ajukan Pinjaman Buku 📖");
        btnKonfirmasiPinjam.setBackground(new Color(19, 107, 107));
        btnKonfirmasiPinjam.setForeground(Color.WHITE);
        btnKonfirmasiPinjam.setFocusPainted(false);
        btnKonfirmasiPinjam.addActionListener(e -> {
            popup.dispose();
            prosesPinjamMandiri(idBuku, judul);
        });

        pnlTombol.add(btnTutup);
        pnlTombol.add(btnKonfirmasiPinjam);
        popup.add(pnlTombol, BorderLayout.SOUTH);

        popup.setVisible(true);
    }

    private String ambilDataSinopsis(String judulBuku) {
        if (judulBuku.equalsIgnoreCase("Laut Bercerita")) {
            return "Laut Bercerita menceritakan kisah pilu keluarga kehilangan, sekelompok sahabat yang merasakan kekosongan di dada, serta sekelompok remaja yang dipaksa bungkam demi menegakkan keadilan di masa rezim Orde Baru.";
        } else if (judulBuku.equalsIgnoreCase("The Atomic Love")) {
            return "Sebuah novel roman sejarah berlatar belakang era Perang Dingin. Mengisahkan intrik intelijen, rahasia pembuatan senjata bom atom, dan pergulatan hati seorang ilmuwan wanita muda dalam memilih kesetiaan negara atau cinta.";
        } else if (judulBuku.equalsIgnoreCase("Siddhartha")) {
            return "Karya sastra legendaris dari Hermann Hesse yang menceritakan perjalanan spiritual seorang pemuda bernama Siddhartha dalam mencari makna hidup sejati, kedamaian jiwa, dan pencerahan di tanah India kuno.";
        }
        return "Buku berjudul \"" + judulBuku + "\" ini merupakan koleksi premium terbaru di BooksHub Perpustakaan.";
    }

    private void prosesPinjamMandiri(int idBuku, String judulBuku) {
        String query = "INSERT INTO peminjaman (id_anggota, id_buku, tgl_pinjam, status) VALUES (?, ?, CURDATE(), 'Dipinjam')";
        try (Connection c = Koneksi.getConnection();
             java.sql.PreparedStatement p = c.prepareStatement(query)) {
            p.setInt(1, idAnggotaLogin);
            p.setInt(2, idBuku);
            p.executeUpdate();

            JOptionPane.showMessageDialog(this, "Sukses meminjam \"" + judulBuku + "\"!\nSilakan ambil fisik buku di meja petugas.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Sukses mengajukan simulasi peminjaman \"" + judulBuku + "\"!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormMenuCustomer().setVisible(true));
    }
}