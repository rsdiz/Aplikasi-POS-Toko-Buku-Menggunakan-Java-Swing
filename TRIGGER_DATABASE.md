# Trigger pada Database

## Ringkasan

Trigger adalah objek basis data yang berhubungan langsung dengan sebuah tabel dan akan aktif apabila suatu kejadian terjadi pada tabel tersebut.

---

## Konten

### Trigger Before Update ***tb_buku***

**Query:**
```sql
DELIMITER $$
CREATE TRIGGER add_history_updatebuku
BEFORE UPDATE ON tb_buku FOR EACH ROW
BEGIN
 INSERT INTO log_history_updatebuku
 SET
  id_log_updatebuku = NULL,
  isbn = old.isbn,
  kode_kategori = old.kode_kategori,
  judul_buku = old.judul_buku,
  penulis = old.penulis,
  penerbit = old.penerbit,
  tahun = old.tahun,
  harga_pokok = old.harga_pokok,
  harga_jual = old.harga_jual,
  gambar = old.gambar;
END$$
DELIMITER ;
```

**Penjelasan:**
Trigger ini akan aktif ketika terdapat perubahan data (update) pada tabel buku, query dalam trigger tersebut akan dijalankan sebelum terjadinya update. Trigger ini berfungsi untuk menyimpan data suatu buku sebelum buku tersebut diubah (update), sehingga apabila terjadi kesalahan, admin bisa mengecek kembali data buku tersebut sebelum diubah dan juga mengembalikan (undo) data tersebut.

---

### Trigger After Insert ***tb_transaksi***

**Query:**
```sql
DELIMITER $$
CREATE TRIGGER log_insert_transaksi
AFTER INSERT ON tb_transaksi FOR EACH ROW
BEGIN
 INSERT INTO log_tb_transaksi
 SET
  id_log = NULL,
  aktifitas = 'tambah',
  tanggal_log = NOW(),
  id_kasir = get_last_idkasir();
END$$
DELIMITER ;
```

**Penjelasan:**
Trigger ini akan aktif ketika terdapat penambahan data (insert) pada tabel transaksi, query dalam trigger tersebut akan dijalankan setelah terjadinya insert. Trigger ini berfungsi untuk menyimpan peristiwa ketika terdapat penambahan data, admin akan tahu siapa saja yang menambahkan data transaksi setiap harinya.

---

### Trigger After Update ***tb_transaksi***

**Query:**
```sql
DELIMITER $$
CREATE TRIGGER log_insert_transaksi
AFTER INSERT ON tb_transaksi FOR EACH ROW
BEGIN
 INSERT INTO log_tb_transaksi
 SET
  id_log = NULL,
  aktifitas = 'tambah',
  tanggal_log = NOW(),
  id_kasir = get_last_idkasir();
END$$
DELIMITER ;
```

**Penjelasan:**
Trigger ini akan aktif ketika terdapat perubahan data (update) pada tabel transaksi, query dalam trigger tersebut akan dijalankan setelah terjadinya update. Trigger ini berfungsi untuk menyimpan peristiwa ketika terdapat perubahan data, admin akan tahu siapa saja yang merubah data transaksi. 

---

### Trigger After Delete ***tb_transaksi***

**Query:**
```sql
DELIMITER $$
CREATE TRIGGER log_delete_transaksi
AFTER DELETE ON tb_transaksi FOR EACH ROW
BEGIN
 INSERT INTO log_tb_transaksi
 SET
  id_log = NULL,
  aktifitas = 'hapus',
  tanggal_log = NOW(),
  id_kasir = old.id_kasir;
END$$
DELIMITER ;
```

**Penjelasan:**
Trigger ini akan aktif ketika terdapat penghapusan data (delete) pada tabel transaksi, query dalam trigger tersebut akan dijalankan setelah terjadinya delete. Trigger ini berfungsi untuk menyimpan peristiwa ketika terdapat penghapusan data, admin akan tahu siapa saja yang menghapus data transaksi tersebut.

---
