# Penjelasan singkat pada Database yang dibuat
<a id="table-of-contents"></a>
## [Table of Contents](#table-of-contents)

1. [Skema Database](#db-scheme)

1. [Penjelasan Tabel](#db-explanation)

    - [Tabel Kategori](#tb-kategori)
    
    - [Tabel Buku](#tb-buku)
    
    - [Tabel Pegawai (Kasir)](#tb-pegawai)
    
    - [Tabel Pelanggan](#tb-pelanggan)
    
    - [Tabel Transaksi](#tb-transaksi)
    
    - [Tabel Detail Transaksi](#tb-detail-trx)
    
    - [Tabel Log History Update Buku](#tb-log-updatebuku)
    
    - [Tabel Log Tabel Transaksi](#tb-log-transaksi)
    
    - [Tabel Laporan](#tb-laporan)
    
---
    
## Database

<a id="db-scheme"></a>

### [Skema Database](#db-scheme)

  <img src="screenshots/SS Full Database.png" alt="Skema Database" width="100%">
  
---

<a id="db-explanation"></a>

### [Penjelasan Tabel](#db-explanation)

<a id="tb-kategori"></a>

### [Tabel Kategori](#tb-kategori)

  <img src="screenshots/SS 2020-05-28 065912 - DB tb_kategori.jpg" alt="Tabel Kategori" width="650">
  
Tabel kategori menyimpan nama-nama kategori suatu buku, dalam tabel ini juga menyimpan letak rak dari kategori buku tersebut.

 - `kode_kategori` => Primary Key
 - `nama_kategori` => Menyimpan nama kategori
 - `rak`           => Menyimpan letak rak dari tiap kategori

**Query SQL:**
  ```sql
  CREATE TABLE `tb_kategori` (
    `kode_kategori` int(11) NOT NULL AUTO_INCREMENT,
    `nama_kategori` varchar(30) NOT NULL,
    `rak` varchar(30) NOT NULL,
    PRIMARY KEY (`kode_kategori`)
  ) ENGINE=InnoDB DEFAULT CHARSET=latin1
  ```

---

<a id="tb-buku"></a>

### [Tabel Buku](#tb-buku)

  <img src="screenshots/SS 2020-05-28 065729 - DB tb_buku.jpg" alt="Tabel Buku" width="650">
  
Tabel buku menyimpan data-data buku, jumlah stok, dan harga pokok maupun harga jual.

 - `isbn` => Primary key
 - `kode_kategori` => Foreign Key tabel kategori
 - `judul_buku` => Menyimpan judul buku
 - `penulis` => Menyimpan nama penulis buku
 - `penerbit` => Menyimpan nama penerbit buku
 - `tahun` => Menyimpan tahun dari penerbitan buku
 - `stok` => Menyimpan jumlah stok buku
 - `harga_pokok` => Menyimpan nominal harga pokok atau harga beli penjual
 - `harga_jual` => Menyimpan nominal harga yang akan dijual ke pembeli
 - `gambar` => Menyimpan gambar cover atau sampul buku
 
**Query SQL:**
  ```sql
  CREATE TABLE `tb_buku` (
    `isbn` varchar(15) NOT NULL,
    `kode_kategori` int(11) DEFAULT NULL,
    `judul_buku` varchar(50) NOT NULL,
    `penulis` varchar(50) NOT NULL,
    `penerbit` varchar(50) NOT NULL,
    `tahun` year(4) NOT NULL,
    `stok` int(20) NOT NULL,
    `harga_pokok` bigint(20) NOT NULL,
    `harga_jual` bigint(20) NOT NULL,
    `gambar` blob,
    PRIMARY KEY (`isbn`),
    KEY `kode_kategori` (`kode_kategori`),
    CONSTRAINT `tb_buku_ibfk` FOREIGN KEY (`kode_kategori`) REFERENCES `tb_kategori` (`kode_kategori`) ON DELETE SET NULL ON UPDATE CASCADE
  ) ENGINE=InnoDB DEFAULT CHARSET=latin1
  ```

---
  
<a id="tb-pegawai"></a>

### [Tabel Pegawai (Kasir)](#tb-pegawai)

  <img src="screenshots/SS 2020-05-28 070116 - DB tb_pegawai.jpg" alt="Tabel Pegawai" width="650">
  
Tabel pegawai menyimpan data dari pegawai kasir.

 - `id_kasir` => Primary key
 - `nama` => Menyimpan nama pegawai
 - `alamat` => Menyimpan alamat pegawai
 - `telepon` => Menyimpan nomor telepon pegawai
 - `shif` => Menyimpan penetapan jam kerja
 - `username` => Menyimpan username yang digunakan untuk masuk ke sistem
 - `pwd` => Menyimpan data password yang sudah di hash dengan salt
 - `salt` => Menyimpan bit acak yang digunakan sebagai kunci untuk memecahkan password yang sudah di hash
 - `akses` => Menyimpan jenis pegawai, apakah sebagai kasir biasa atau admin
 
**Query SQL:**
  ```sql
  CREATE TABLE `tb_pegawai` (
    `id_kasir` int(11) NOT NULL AUTO_INCREMENT,
    `nama` varchar(50) NOT NULL,
    `alamat` varchar(30) NOT NULL,
    `telepon` varchar(13) NOT NULL,
    `shif` enum('Pagi','Sore') NOT NULL,
    `username` varchar(20) NOT NULL,
    `pwd` varchar(255) NOT NULL,
    `salt` varchar(255) NOT NULL,
    `akses` enum('admin','kasir') NOT NULL,
    PRIMARY KEY (`id_kasir`)
  ) ENGINE=InnoDB DEFAULT CHARSET=latin1
  ```
  
---
  
<a id="tb-pelanggan"></a>

### [Tabel Pelanggan](#tb-pelanggan)

  <img src="screenshots/SS 2020-05-28 070217 - DB tb_pelanggan.jpg" alt="Tabel Pelanggan" width="650">
  
Tabel pelanggan menyimpan data pelanggan yang melakukan pembelian

 - `id_pelanggan` => Primary key
 - `nama_pelanggan` => Menyimpan nama pelanggan
 - `alamat` => Menyimpan alamat pelanggan
 - `telepon` => Menyimpan nomor telepon pelanggan
  
**Query SQL:**
  ```sql
  CREATE TABLE `tb_pelanggan` (
    `id_pelanggan` INT(11) NOT NULL AUTO_INCREMENT,
    `nama_pelanggan` VARCHAR(50) NOT NULL,
    `alamat` VARCHAR(50) NOT NULL,
    `telepon` VARCHAR(13) NOT NULL,
    PRIMARY KEY (`id_pelanggan`)
  ) ENGINE=INNODB DEFAULT CHARSET=latin1
  ```

---
  
<a id="tb-transaksi"></a>

### [Tabel Transaksi](#tb-transaksi)

  <img src="screenshots/SS 2020-05-28 070254 - DB tb_transaksi.jpg" alt="Tabel Transaksi" width="650">
  
Tabel transaksi menyimpan data transaksi setiap ada pembelian, setiap pelanggan dapat melakukan banyak transaksi, dan setiap transaksi, buku yang dibeli bisa lebih dari satu.

 - `id_transaksi` => Primary key
 - `id_pelanggan` => Foreign key tabel pelanggan
 - `id_kasir` => Foreign key tabel pegawai
 - `tanggal` => Menyimpan tanggal kapan terjadinya transaksi
 - `bayar` => Menyimpan Nominal uang yang dibayarkan oleh pelanggan
 
**Query SQL:**
  ```sql
  CREATE TABLE `tb_transaksi` (
    `id_transaksi` int(11) NOT NULL AUTO_INCREMENT,
    `id_pelanggan` int(11) NOT NULL,
    `id_kasir` int(11) NOT NULL,
    `tanggal` date NOT NULL,
    `bayar` int(255) DEFAULT NULL,
    PRIMARY KEY (`id_transaksi`),
    KEY `id_pelanggan` (`id_pelanggan`),
    KEY `id_kasir` (`id_kasir`),
    CONSTRAINT `fk_tb_pegawai` FOREIGN KEY (`id_kasir`) REFERENCES `tb_pegawai` (`id_kasir`) ON UPDATE CASCADE,
    CONSTRAINT `fk_tb_pelanggan` FOREIGN KEY (`id_pelanggan`) REFERENCES `tb_pelanggan` (`id_pelanggan`) ON UPDATE CASCADE
  ) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1
  ```

---
  
<a id="tb-detail-trx"></a>

### [Tabel Detail Transaksi](#tb-detail-trx)

  <img src="screenshots/SS 2020-05-28 065956 - DB tb_detail_transaksi.jpg" alt="Tabel Detail Transaksi" width="650">
  
Tabel detail transaksi menyimpan detail dari buku yang dibeli, setiap transaksi dapat memiliki banyak detail transaksi.

 - `id_detail_transaksi` => Primary key
 - `id_transaksi` => Foreign key tabel transaksi
 - `isbn` => Foreign key tabel buku
 - `banyak` => Menyimpan kuantitas buku yang dibeli
 - `harga` => Menyimpan harga dari buku yang dibeli

**Query SQL:**
  ```sql
  CREATE TABLE `tb_detail_transaksi` (
    `id_detail_transaksi` INT(11) NOT NULL AUTO_INCREMENT,
    `id_transaksi` INT(11) NOT NULL,
    `isbn` VARCHAR(15) NOT NULL,
    `banyak` INT(11) NOT NULL,
    `harga` INT(255) NOT NULL,
    PRIMARY KEY (`id_detail_transaksi`),
    KEY `id_transaksi` (`id_transaksi`),
    KEY `isbn` (`isbn`),
    CONSTRAINT `fk_tb_buku` FOREIGN KEY (`isbn`) REFERENCES `tb_buku` (`isbn`) ON UPDATE CASCADE,
    CONSTRAINT `fk_tb_transaksi` FOREIGN KEY (`id_transaksi`) REFERENCES `tb_transaksi` (`id_transaksi`) ON DELETE CASCADE ON UPDATE CASCADE
  ) ENGINE=INNODB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1
  ```

---
  
<a id="tb-log-updatebuku"></a>

### [Tabel Log History Update Buku](#tb-log-updatebuku)

  <img src="screenshots/SS 2020-05-28 070339 - DB log_history_updatebuku.jpg" alt="Tabel Log History Update Buku" width="650">
  
Tabel ini menyimpan data buku sebelum terjadi update pada tabel buku.

**Query SQL:**
  ```sql
  CREATE TABLE `log_history_updatebuku` (
    `id_log_updatebuku` INT(11) NOT NULL AUTO_INCREMENT,
    `isbn` VARCHAR(15) DEFAULT NULL,
    `kode_kategori` INT(11) DEFAULT NULL,
    `judul_buku` VARCHAR(50) DEFAULT NULL,
    `penulis` VARCHAR(50) DEFAULT NULL,
    `penerbit` VARCHAR(50) DEFAULT NULL,
    `tahun` YEAR(4) DEFAULT NULL,
    `harga_pokok` FLOAT DEFAULT NULL,
    `harga_jual` FLOAT DEFAULT NULL,
    `gambar` BLOB,
    PRIMARY KEY (`id_log_updatebuku`),
    KEY `fk_history_buku` (`isbn`),
    KEY `fk_history_kategori` (`kode_kategori`)
  ) ENGINE=INNODB DEFAULT CHARSET=latin1
  ```
  
---
  
<a id="tb-log-transaksi"></a>

### [Tabel Log Tabel Transaksi](#tb-log-transaksi)

  <img src="screenshots/SS 2020-05-28 070433 - DB log_tb_transaksi.jpg" alt="Tabel Log Transaksi" width="650">
  
Tabel ini menyimpan setiap peristiwa tiap kali adanya aktifitas yang terjadi pada tabel transaksi, aktifitas yang dicatat adalah `tambah`, `ubah`, dan `hapus`.

- `id_log` => Primary key
- `aktifitas` => Menyimpan jenis aktifitas (tambah, ubah, dan hapus)
- `tanggal_log` => Menyimpan tanggal ketika terjadinya peristiwa
- `id_kasir` => Foreign key tabel pegawai

**Query SQL:**
  ```sql
  CREATE TABLE `log_tb_transaksi` (
    `id_log` INT(11) NOT NULL AUTO_INCREMENT,
    `aktifitas` ENUM('tambah','ubah','hapus') DEFAULT NULL,
    `tanggal_log` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `id_kasir` INT(11) DEFAULT NULL,
    PRIMARY KEY (`id_log`),
    KEY `fk_kasir` (`id_kasir`),
    CONSTRAINT `fk_kasir` FOREIGN KEY (`id_kasir`) REFERENCES `tb_pegawai` (`id_kasir`) ON DELETE NO ACTION ON UPDATE CASCADE
  ) ENGINE=INNODB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1
  ```

---
  
<a id="tb-laporan"></a>

### [Tabel Laporan](#tb-laporan)

  <img src="screenshots/SS 2020-05-28 070039 - DB tb_laporan.jpg" alt="Tabel Log Transaksi" width="650">
  
Tabel ini menyimpan laporan penjualan setiap harinya.

 - `id_laporan` => Primary key
 - `terjual` => Menyimpan total buku yang terjual
 - `pemasukan` => Menyimpan total pemasukan uang
 - `tanngal` => Menyimpan tanggal laporan

**Query SQL:**
  ```sql
  CREATE TABLE `tb_laporan` (
    `id_laporan` INT(11) NOT NULL AUTO_INCREMENT,
    `terjual` INT(11) NOT NULL DEFAULT '0',
    `pemasukan` DOUBLE NOT NULL DEFAULT '0',
    `tanggal` DATETIME NOT NULL,
    PRIMARY KEY (`id_laporan`)
  ) ENGINE=INNODB DEFAULT CHARSET=latin1
  ```

---

**[⬆ back to top](#table-of-contents)**
