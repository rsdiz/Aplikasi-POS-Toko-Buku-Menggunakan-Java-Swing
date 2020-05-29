# Views pada Database

## Ringkasan

View merupakan salah satu objek basis data yang merepresentasikan sub himpunan dari data yang berasal dari satu atau lebih tabel.

View adalah perintah query yang disimpan pada basis data dengan suatu nama tertentu sehingga bisa digunakan setiap saat untuk melihat data tanpa menuliskan ulang query tersebut. Sehingga dengan demikian view bisa disebut juga dengan virtual tabel.

---

## Konten

### View ***data_buku()***

**Query:**
```sql
CREATE VIEW data_buku AS
SELECT tb_buku.* , nama_kategori, rak
FROM tb_buku INNER JOIN tb_kategori USING(kode_kategori);
```
[💾 Download gambar](https://github.com/rsdiz/Sistem-Kasir-menggunakan-Java-Swing/raw/master/screenshots/SS%202020-05-28%20071329%20-%20View%20data_buku.jpg)

---

**Pemanggilan:** 
```sql
SELECT * FROM data_buku;
```
[💾 Download gambar](https://github.com/rsdiz/Sistem-Kasir-menggunakan-Java-Swing/raw/master/screenshots/SS%202020-05-28%20104616%20-%20Query%20Pemanggilan%20View%20data_buku.jpg)

---

**Implementasi di Kode:**
 - **Tanpa parameter** [(💾 Download gambar)](https://github.com/rsdiz/Sistem-Kasir-menggunakan-Java-Swing/raw/master/screenshots/implement%20view%20data_buku%20in%20code.png)
    ```java
    public List<Buku> loadAll() throws SQLException {
      List<Buku> listBuku = new ArrayList<>();
      try {
        String sql = "SELECT * FROM data_buku";
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(sql);
        while (res.next()) {
          Buku buku = new Buku();
          addBukuToList(buku, res);
          listBuku.add(buku);
        }
      } catch (SQLException e) {
        System.out.println("Error Fetching Data Buku!\n" + e);
      }
      return listBuku;
    }
    ```

 - **Dengan Parameter** [(💾 Download gambar)](https://github.com/rsdiz/Sistem-Kasir-menggunakan-Java-Swing/raw/master/screenshots/implement%20view%20data_buku%20with%20param%20in%20code.png)
    ```java
    @Override
    public List<Buku> loadAllBy(String type) throws SQLException {
        List<Buku> listBuku = new ArrayList<>();
        if (
                "judul_buku".equalsIgnoreCase(type) | 
                "isbn".equalsIgnoreCase(type) | 
                "penulis".equalsIgnoreCase(type) | 
                "penerbit".equalsIgnoreCase(type)
           ) {
            try {
                String sql = "SELECT * FROM data_buku ORDER BY " + type;
                Statement statement = con.createStatement();
                ResultSet res = statement.executeQuery(sql);
                while (res.next()) {
                    Buku buku = new Buku();
                    addBukuToList(buku, res);
                    listBuku.add(buku);
                }
            } catch (SQLException e) {
                System.out.println("Error Fetching Data Buku!\n" + e);
            }
        } else {

        }
        return listBuku;
    }
    ```

---

**Screenshot Hasil Implementasi:**

<p align="center"><img src="screenshots/SS 2020-05-29 125810 - Hasil Implementasi Buku.jpg" alt="Hasil Implementasi Buku">
<br><i>Gambar Tampilan Daftar Buku</i></p>

---

### View ***data_transaksi()***

**Query:** 
```sql
CREATE VIEW data_transaksi AS
SELECT 
  tb_transaksi.id_transaksi ,
  tb_transaksi.id_pelanggan ,
  tb_transaksi.id_kasir , 
  tb_pelanggan.nama_pelanggan , 
  tb_pegawai.nama nama_petugas , 
  tb_transaksi.tanggal , 
  tb_transaksi.bayar
FROM tb_transaksi 
  INNER JOIN tb_pelanggan
    USING(id_pelanggan)
  INNER JOIN tb_pegawai
    USING(id_kasir);
```
[💾 Download Gambar](https://github.com/rsdiz/Sistem-Kasir-menggunakan-Java-Swing/raw/master/screenshots/SS%202020-05-28%20071449%20-%20View%20data_transaksi.jpg)

---

**Pemanggilan:** 
```sql
SELECT * FROM data_transaksi;
```
[💾 Download Gambar](https://github.com/rsdiz/Sistem-Kasir-menggunakan-Java-Swing/raw/master/screenshots/SS%202020-05-28%20111729%20-%20Query%20Pemanggilan%20View%20data_transaksi.jpg)

---

**Implementasi di kode:** 
```java
@Override
public List<Transaksi> load() throws SQLException {
    String sql = "SELECT * FROM data_transaksi";
    try (Statement statement = con.createStatement()) {
        ResultSet res = statement.executeQuery(sql);
        while (res.next()) {
            Transaksi transaksi = new Transaksi();
            addTransaksiToList(transaksi, res);
            listTransaksis.add(transaksi);
        }
    }
    return listTransaksis;
}
```
[💾 Download Gambar](https://github.com/rsdiz/Sistem-Kasir-menggunakan-Java-Swing/raw/master/screenshots/implement%20view%20data_transaksi%20in%20code.png)

---

**Screenshot Hasil Implementasi:**

<p align="center"><img src="screenshots/SS 2020-05-29 125706 - Hasil Implementasi Transaksi.jpg" alt="Hasil Implementasi Transaksi">
<br><i>Gambar Tampilan Daftar Transaksi</i></p>

---

### View ***data_detail_trx()***

**Query:** 
```sql
CREATE VIEW data_detail_trx AS
SELECT
  id_detail_transaksi id_detail,
  id_transaksi id_trx,
  tb_buku.isbn ,
  judul_buku ,
  banyak,
  harga
FROM tb_detail_transaksi
  INNER JOIN tb_buku
    USING(isbn)
  INNER JOIN tb_transaksi
    USING(id_transaksi)
WHERE
  tb_detail_transaksi.id_transaksi = tb_transaksi.id_transaksi;
```
[💾 Download Gambar](https://github.com/rsdiz/Sistem-Kasir-menggunakan-Java-Swing/raw/master/screenshots/SS%202020-05-28%20071601%20-%20View%20data_detail_trx.jpg)

---

**Pemanggilan:** 
```sql
SELECT * FROM data_detail_trx WHERE id_trx = [ANGKA];
```
_Catatan: [ANGKA] diubah sesuai dengan id_trx yang diinginkan_

[💾 Download Gambar](https://github.com/rsdiz/Sistem-Kasir-menggunakan-Java-Swing/raw/master/screenshots/SS%202020-05-28%20111811%20-%20Query%20Pemanggilan%20View%20data_detail_trx.jpg)

---

**Implementasi di kode:** 
```java
@Override
public List<DetailTransaksi> load(int select_id) throws SQLException {
    String sql = "SELECT * FROM data_detail_trx WHERE id_trx = ?";
    try(PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, select_id);
        ResultSet res = ps.executeQuery();
        while (res.next()) {
            DetailTransaksi detailTransaksi = new DetailTransaksi();
            addDetailTrxToList(detailTransaksi, res);
            listDetailTransaksis.add(detailTransaksi);
        }
    }
    return listDetailTransaksis;
}
```
[💾 Download Gambar](https://github.com/rsdiz/Sistem-Kasir-menggunakan-Java-Swing/raw/master/screenshots/implement%20view%20data_detail_trx%20in%20code.png)

---

**Screenshot Hasil Implementasi:**

<p align="center"><img src="screenshots/SS 2020-05-29 125857 - Hasil Implementasi Detail Trx.jpg" alt="Hasil Implementasi Detail Transaksi">
<br><i>Gambar Tampilan Detail Transaksi</i></p>
