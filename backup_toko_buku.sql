-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 22, 2020 at 11:54 AM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 7.3.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `toko_buku`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_buku` (`isbn_key` VARCHAR(15))  BEGIN
 DELETE FROM tb_buku WHERE isbn = isbn_key;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_detailtrx` (`id_key` INT(11))  BEGIN
 DELETE FROM tb_detail_transaksi WHERE id_detail_transaksi = id_key;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_kategori` (`id_key` INT(11))  BEGIN
 DELETE FROM tb_kategori WHERE kode_kategori = id_key;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_pegawai` (`id_key` INT(11))  BEGIN
 DELETE FROM tb_pegawai WHERE id_kasir = id_key;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_pelanggan` (`id_key` INT(11))  BEGIN
 DELETE FROM tb_pelanggan WHERE id_pelanggan = id_key;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_transaksi` (`id` INT(11))  BEGIN
 DELETE FROM tb_detail_transaksi WHERE id_transaksi = id;
 DELETE FROM tb_transaksi WHERE id_transaksi = id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_buku` (`isbn` VARCHAR(15), `kategori` VARCHAR(30), `judul_buku` VARCHAR(50), `penulis` VARCHAR(50), `penerbit` VARCHAR(50), `tahun` YEAR(4), `harga_pokok` FLOAT, `harga_jual` FLOAT, `gambar` BLOB)  BEGIN
 INSERT INTO tb_buku
  VALUES 
  (
   isbn,
   (
    SELECT kode_kategori FROM tb_kategori WHERE nama_kategori = kategori
   ), 
   judul_buku,
   penulis,
   penerbit,
   tahun,
   0,
   harga_pokok,
   harga_jual,
   gambar
  );
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_detailtrx` (`id_transaksi` INT(11), `isbn_key` VARCHAR(15), `banyak` INT(11))  BEGIN
 INSERT INTO tb_detail_transaksi
 VALUES (NULL, id_transaksi, isbn_key, banyak, (SELECT harga_jual FROM tb_buku WHERE isbn = isbn_key));
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_kategori` (`kode_kategori` INT(11), `nama_kategori` VARCHAR(30), `rak` VARCHAR(30))  BEGIN
 INSERT INTO tb_kategori
 VALUES (kode_kategori, nama_kategori, rak);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_pegawai` (`nama` VARCHAR(50), `alamat` VARCHAR(30), `telepon` VARCHAR(13), `shif` ENUM('Pagi','Sore'), `username` VARCHAR(20), `pwd` VARCHAR(255), `akses` ENUM('admin','kasir'))  BEGIN
 INSERT INTO tb_pegawai
 VALUES
 (
  null,
  nama, alamat, telepon, shif,
  username, pwd, akses
 );
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_pelanggan` (`id_pelanggan` INT(11), `nama_pelanggan` VARCHAR(50), `alamat` VARCHAR(50), `telepon` VARCHAR(13))  BEGIN
 INSERT INTO tb_pelanggan
 VALUES (id_pelanggan, nama_pelanggan, alamat, telepon);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_transaksi` (`id_transaksi` INT(11), `id_pelanggan` INT(11), `id_kasir` INT(11), `tanggal` DATE, `bayar` INT(255))  BEGIN
INSERT INTO tb_transaksi
VALUES (id_transaksi, id_pelanggan, id_kasir, tanggal, bayar);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `search_buku` (`search` VARCHAR(50))  BEGIN
DECLARE keyword VARCHAR(50);
SET keyword = CONCAT('%',search,'%');
SELECT * FROM data_buku
WHERE judul_buku LIKE keyword OR isbn LIKE keyword OR penulis LIKE keyword OR penerbit LIKE keyword;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `update_buku` (`isbn_old` VARCHAR(15), `isbn_new` VARCHAR(15), `kategori` VARCHAR(30), `judul_buku_new` VARCHAR(50), `penulis_new` VARCHAR(50), `penerbit_new` VARCHAR(50), `tahun_new` YEAR(4), `harga_pokok_new` FLOAT, `harga_jual_new` FLOAT, `gambar_new` BLOB)  BEGIN
 UPDATE tb_buku
 SET
 isbn = isbn_baru,
 kode_kategori = (SELECT kode_kategori FROM tb_kategori WHERE nama_kategori = kategori),
 judul_buku = judul_buku_new,
 penulis = penulis_new,
 penerbit = penerbit_new,
 tahun = tahun_new,
 harga_pokok = harga_pokok_new,
 harga_jual = harga_jual_new,
 gambar = gambar_new
 WHERE isbn = isbn_old;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `update_detailtrx` (`id_key` INT(11), `id_trx_new` INT(11), `isbn_new` VARCHAR(15), `banyak_new` INT(11))  BEGIN
 UPDATE tb_detail_transaksi
 SET
  id_transaksi = id_trx_new,
  isbn = isbn_new,
  banyak = banyak_new,
  harga = (SELECT harga_jual FROM tb_buku WHERE isbn = isbn_new);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `update_kategori` (`kode_kategori_key` INT(11), `kode_new` INT(11), `nama_kategori_new` VARCHAR(30), `rak_new` VARCHAR(30))  BEGIN
 UPDATE tb_kategori
 SET
  kode_kategori = kode_new,
  nama_kategori = nama_kategori_new,
  rak = rak_new
 WHERE kode_kategori = kode_kategori_key;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `update_pegawai` (`id_key` INT(11), `nama_new` VARCHAR(50), `alamat_new` VARCHAR(30), `telepon_new` VARCHAR(13), `shif_new` ENUM('Pagi','Sore'), `username_new` VARCHAR(20), `password_new` VARCHAR(255), `akses_new` ENUM('admin','kasir'))  BEGIN
 UPDATE tb_pegawai
 SET
  nama = nama_new, alamat = alamat_new, telepon = telepon_new,
  shif = shif_new, username = username_new, pwd = password_new,
  akses = akses_new
 WHERE id_kasir = id_key;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `update_pelanggan` (`id_key` INT(11), `nama_pelanggan_new` VARCHAR(50), `alamat_new` VARCHAR(50), `telepon_new` VARCHAR(13))  BEGIN
 UPDATE tb_pelanggan
 SET
  nama_pelanggan = nama_pelanggan_new,
  alamat = alamat_new,
  telepon = telepon_new
 WHERE id_pelanggan = id_key;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `update_stokbuku` (`isbn_key` VARCHAR(15), `total` INT(20))  BEGIN
 update tb_buku
 SET stok = 
  (SELECT calc_stokbuku(isbn_key, total))
 WHERE isbn = isbn_key;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `update_transaksi` (`id` INT(11), `id_pelanggan_new` INT(11), `id_kasir_new` INT(11), `tanggal_new` DATE, `bayar_new` INT(255))  BEGIN
UPDATE tb_transaksi SET id_pelanggan = id_pelanggan_new, id_kasir = id_kasir_new, tanggal = tanggal_new, bayar = bayar_new WHERE id_transaksi = id;
END$$

--
-- Functions
--
CREATE DEFINER=`root`@`localhost` FUNCTION `calc_stokbuku` (`isbn_key` VARCHAR(15), `total` INT(20)) RETURNS INT(20) BEGIN
 DECLARE stok_old INT;
 SELECT stok INTO stok_old FROM tb_buku WHERE isbn = isbn_key;
 RETURN stok_old - total;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `calc_totalbayar` (`idtrx` INT(11)) RETURNS INT(20) BEGIN
 DECLARE total_bayar INT;
 SELECT SUM(banyak*harga) INTO total_bayar FROM tb_detail_transaksi WHERE id_transaksi = idtrx;
 return total_bayar;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `get_last_idkasir` () RETURNS INT(11) BEGIN
 DECLARE id INT;
 SELECT id_kasir INTO id FROM tb_transaksi ORDER BY id_transaksi DESC LIMIT 1;
 return id;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Stand-in structure for view `data_buku`
-- (See below for the actual view)
--
CREATE TABLE `data_buku` (
`isbn` varchar(15)
,`kode_kategori` int(11)
,`judul_buku` varchar(50)
,`penulis` varchar(50)
,`penerbit` varchar(50)
,`tahun` year(4)
,`stok` int(20)
,`harga_pokok` float
,`harga_jual` float
,`gambar` blob
,`nama_kategori` varchar(30)
,`rak` varchar(30)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `data_detail_trx`
-- (See below for the actual view)
--
CREATE TABLE `data_detail_trx` (
`id_detail` int(11)
,`id_trx` int(11)
,`isbn` varchar(15)
,`judul_buku` varchar(50)
,`banyak` int(11)
,`harga` int(255)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `data_transaksi`
-- (See below for the actual view)
--
CREATE TABLE `data_transaksi` (
`id_transaksi` int(11)
,`id_pelanggan` int(11)
,`id_kasir` int(11)
,`nama_pelanggan` varchar(50)
,`nama_petugas` varchar(50)
,`tanggal` date
,`bayar` int(255)
,`totalbayar` int(20)
);

-- --------------------------------------------------------

--
-- Table structure for table `log_history_updatebuku`
--

CREATE TABLE `log_history_updatebuku` (
  `id_log_updatebuku` int(11) NOT NULL,
  `isbn` varchar(15) DEFAULT NULL,
  `kode_kategori` int(11) DEFAULT NULL,
  `judul_buku` varchar(50) DEFAULT NULL,
  `penulis` varchar(50) DEFAULT NULL,
  `penerbit` varchar(50) DEFAULT NULL,
  `tahun` year(4) DEFAULT NULL,
  `harga_pokok` float DEFAULT NULL,
  `harga_jual` float DEFAULT NULL,
  `gambar` blob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `log_history_updatebuku`
--

INSERT INTO `log_history_updatebuku` (`id_log_updatebuku`, `isbn`, `kode_kategori`, `judul_buku`, `penulis`, `penerbit`, `tahun`, `harga_pokok`, `harga_jual`, `gambar`) VALUES
(1, '978-602-1514-52', 2020044022, 'PEMROGRAMAN WEB DENGAN HTML', 'BETHA SIDIQ & HUSNI I. POHAN', 'INFORMATIKA', 2017, 75000, 60000, NULL),
(2, '978-602-1514-52', 2020044022, 'PEMROGRAMAN WEB DENGAN HTML', 'BETHA SIDIQ & HUSNI I. POHAN', 'INFORMATIKA', 2017, 75000, 60000, NULL),
(3, '978-602-1514-52', 2020044022, 'PEMROGRAMAN WEB DENGAN HTML', 'BETHA SIDIQ & HUSNI I. POHAN', 'INFORMATIKA', 2019, 75000, 60000, NULL),
(4, '978-602-1514-52', 2020044022, 'PEMROGRAMAN WEB DENGAN HTML', 'BETHA SIDIQ & HUSNI I. POHAN', 'INFORMATIKA', 2018, 75000, 60000, NULL),
(5, '978-602-254-172', 2020044011, 'PEMROGRAMAN BASIS DATA DI MATLAB DENGAN MYSQL DAN ', 'HERLAWATI & RAHMADYA TRIA HANDAYANTO', 'INFORMATIKA', 2018, 60000, 48000, NULL),
(6, '978-602-6232-00', 2020044022, 'KUMPULAN SOLUSI PEMROGRAMAN PYTHON', 'BUDI RAHARJO', 'INFORMATIKA', 2018, 95000, 76000, NULL),
(7, '978-602-6232-06', 2020044011, 'BASIS DATA REVISI KETIGA', 'FATHANSYAH', 'INFORMATIKA', 2018, 12000, 96000, NULL),
(8, '978-602-6232-16', 2020044022, 'PEMROGRAMAN GUI DENGAN PYTHON & PYQT', 'BUDI RAHARJO', 'INFORMATIKA', 2018, 75000, 60000, NULL),
(9, '978-602-6232-33', 2020044022, 'MUDAH BELAJAR RUBY', 'BUDI RAHARJO', 'INFORMATIKA', 2018, 120000, 96000, NULL),
(10, '978-602-6232-38', 2020044011, 'MEMBUAT DATABASE DENGAN MICROSOFT ACCESS', 'IMAM HERYANTO', 'INFORMATIKA', 2018, 120000, 96000, NULL),
(11, '978-602-6232-49', 2020044022, 'ESENSI-ESENSI BAHASA PEMROGRAMAN JAVA (EDISI REVIS', 'DR. BAMBANG HARIYANTO', 'INFORMATIKA', 2018, 130000, 104000, NULL),
(12, '978-602-6232-67', 2020044022, 'BELAJAR MACHINE LEARNING (TEORI DAN PRKATIK)', 'RIFKIE PRIMARTHA', 'INFORMATIKA', 2018, 130000, 104000, NULL),
(13, '978-602-6232-97', 2020044011, 'DATA MINING (UNTUK KLARIFIKASI DAN KLASTERISASI DA', 'SUYANTO', 'INFORMATIKA', 2018, 100000, 80000, NULL),
(14, '978-602-8759-44', 2020044011, 'STRUKTUR DATA', 'ROSA A.S', 'MODULA', 2018, 130000, 104000, NULL),
(15, '978-602-1514-92', NULL, 'KUMPULAN SOLUSI PEMROGRAMAN RUBY', 'BUDI RAHARJO', 'Penerbit Informatika', 2018, 80000, 100000, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `log_tb_transaksi`
--

CREATE TABLE `log_tb_transaksi` (
  `id_log` int(11) NOT NULL,
  `aktifitas` enum('tambah','ubah','hapus') DEFAULT NULL,
  `tanggal_log` datetime DEFAULT CURRENT_TIMESTAMP,
  `id_kasir` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `log_tb_transaksi`
--

INSERT INTO `log_tb_transaksi` (`id_log`, `aktifitas`, `tanggal_log`, `id_kasir`) VALUES
(1, 'ubah', '2020-05-18 16:47:06', 2),
(2, 'ubah', '2020-05-18 16:47:16', 3),
(3, 'ubah', '2020-05-18 16:47:19', 2),
(4, 'ubah', '2020-05-18 16:47:33', 3),
(5, 'ubah', '2020-05-18 16:47:57', 2),
(6, 'ubah', '2020-05-18 16:51:16', 3),
(7, 'hapus', '2020-05-18 16:51:20', 2),
(8, 'hapus', '2020-05-18 16:51:22', 3),
(9, 'tambah', '2020-05-18 16:54:42', 3),
(10, 'tambah', '2020-05-18 16:54:42', 2),
(11, 'hapus', '2020-05-19 10:43:41', 2),
(12, 'hapus', '2020-05-19 10:43:43', 3),
(13, 'tambah', '2020-05-19 10:45:22', 2),
(14, 'tambah', '2020-05-19 10:45:29', 3),
(15, 'ubah', '2020-05-19 10:45:38', 3),
(16, 'tambah', '2020-05-19 10:45:55', 2),
(17, 'tambah', '2020-05-22 04:14:17', 2),
(18, 'tambah', '2020-05-22 12:55:48', 2),
(19, 'tambah', '2020-05-22 13:02:26', 2),
(20, 'tambah', '2020-05-22 13:08:31', 2),
(21, 'tambah', '2020-05-22 13:23:49', 2),
(22, 'tambah', '2020-05-22 13:37:35', 2),
(23, 'hapus', '2020-05-22 13:39:26', 2),
(24, 'hapus', '2020-05-22 13:39:43', 2),
(25, 'hapus', '2020-05-22 13:39:48', 2),
(26, 'hapus', '2020-05-22 13:47:59', 2),
(27, 'tambah', '2020-05-22 13:52:19', 2),
(28, 'hapus', '2020-05-22 13:53:29', 2),
(29, 'tambah', '2020-05-22 13:59:06', 2),
(30, 'hapus', '2020-05-22 14:00:47', 2),
(31, 'tambah', '2020-05-22 14:08:44', 2),
(32, 'tambah', '2020-05-22 14:10:01', 2),
(33, 'tambah', '2020-05-22 14:13:18', 2),
(34, 'tambah', '2020-05-22 14:16:02', 2);

-- --------------------------------------------------------

--
-- Table structure for table `tb_buku`
--

CREATE TABLE `tb_buku` (
  `isbn` varchar(15) NOT NULL,
  `kode_kategori` int(11) DEFAULT NULL,
  `judul_buku` varchar(50) NOT NULL,
  `penulis` varchar(50) NOT NULL,
  `penerbit` varchar(50) NOT NULL,
  `tahun` year(4) NOT NULL,
  `stok` int(20) NOT NULL,
  `harga_pokok` float NOT NULL,
  `harga_jual` float NOT NULL,
  `gambar` blob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_buku`
--

INSERT INTO `tb_buku` (`isbn`, `kode_kategori`, `judul_buku`, `penulis`, `penerbit`, `tahun`, `stok`, `harga_pokok`, `harga_jual`, `gambar`) VALUES
('978-602-1514-12', 2020044011, 'PEMROGRAMAN & STRUKTUR DATA C + CD', 'R.H SIANIPAR', 'INFORMATIKA', 2018, 9, 55000, 55000, NULL),
('978-602-1514-44', 2020044022, 'ARTIFICIAL INTELIGENCE (REVISI KEDUA)', 'SUYANTO', 'INFORMATIKA', 2018, 4, 99000, 99000, NULL),
('978-602-1514-52', 2020044022, 'PEMROGRAMAN WEB DENGAN HTML', 'BETHA SIDIQ & HUSNI I. POHAN', 'INFORMATIKA', 2018, 9, 75000, 80000, NULL),
('978-602-1514-92', 2020044022, 'KUMPULAN SOLUSI PEMROGRAMAN RUBY', 'BUDI RAHARJO', 'Penerbit Informatika', 2018, 0, 80000, 100000, NULL),
('978-602-254-172', 2020044011, 'PEMROGRAMAN BASIS DATA DI MATLAB DENGAN MYSQL DAN ', 'HERLAWATI & RAHMADYA TRIA HANDAYANTO', 'INFORMATIKA', 2018, 8, 60000, 68000, NULL),
('978-602-6232-00', 2020044022, 'KUMPULAN SOLUSI PEMROGRAMAN PYTHON', 'BUDI RAHARJO', 'INFORMATIKA', 2018, 2, 95000, 100000, NULL),
('978-602-6232-06', 2020044011, 'BASIS DATA REVISI KETIGA', 'FATHANSYAH', 'INFORMATIKA', 2018, 6, 12000, 35000, NULL),
('978-602-6232-16', 2020044022, 'PEMROGRAMAN GUI DENGAN PYTHON & PYQT', 'BUDI RAHARJO', 'INFORMATIKA', 2018, 2, 75000, 82000, NULL),
('978-602-6232-33', 2020044022, 'MUDAH BELAJAR RUBY', 'BUDI RAHARJO', 'INFORMATIKA', 2018, 2, 120000, 130000, NULL),
('978-602-6232-38', 2020044011, 'MEMBUAT DATABASE DENGAN MICROSOFT ACCESS', 'IMAM HERYANTO', 'INFORMATIKA', 2018, 4, 120000, 129000, NULL),
('978-602-6232-49', 2020044022, 'ESENSI-ESENSI BAHASA PEMROGRAMAN JAVA (EDISI REVIS', 'DR. BAMBANG HARIYANTO', 'INFORMATIKA', 2018, 6, 130000, 140000, NULL),
('978-602-6232-67', 2020044022, 'BELAJAR MACHINE LEARNING (TEORI DAN PRKATIK)', 'RIFKIE PRIMARTHA', 'INFORMATIKA', 2018, 7, 130000, 140000, NULL),
('978-602-6232-97', 2020044011, 'DATA MINING (UNTUK KLARIFIKASI DAN KLASTERISASI DA', 'SUYANTO', 'INFORMATIKA', 2018, 2, 100000, 109000, NULL),
('978-602-8759-44', 2020044011, 'STRUKTUR DATA', 'ROSA A.S', 'MODULA', 2018, 8, 130000, 134000, NULL);

--
-- Triggers `tb_buku`
--
DELIMITER $$
CREATE TRIGGER `add_history_updatebuku` BEFORE UPDATE ON `tb_buku` FOR EACH ROW BEGIN
 INSERT INTO log_history_updatebuku
 SET
  id_log_updatebuku = null,
  isbn = old.isbn,
  kode_kategori = old.kode_kategori,
  judul_buku = old.judul_buku,
  penulis = old.penulis,
  penerbit = old.penerbit,
  tahun = old.tahun,
  harga_pokok = old.harga_pokok,
  harga_jual = old.harga_jual,
  gambar = old.gambar;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_detail_transaksi`
--

CREATE TABLE `tb_detail_transaksi` (
  `id_detail_transaksi` int(11) NOT NULL,
  `id_transaksi` int(11) NOT NULL,
  `isbn` varchar(15) NOT NULL,
  `banyak` int(11) NOT NULL,
  `harga` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_detail_transaksi`
--

INSERT INTO `tb_detail_transaksi` (`id_detail_transaksi`, `id_transaksi`, `isbn`, `banyak`, `harga`) VALUES
(1, 1, '978-602-1514-12', 2, 55000),
(2, 1, '978-602-1514-52', 1, 80000),
(3, 2, '978-602-6232-16', 1, 82000),
(4, 3, '978-602-8759-44', 1, 134000),
(5, 5, '978-602-1514-92', 1, 100000),
(6, 6, '978-602-1514-12', 1, 55000),
(7, 6, '978-602-6232-49', 2, 140000),
(8, 6, '978-602-8759-44', 1, 134000),
(9, 6, '978-602-1514-92', 1, 100000),
(10, 7, '978-602-254-172', 1, 68000),
(11, 7, '978-602-1514-52', 1, 80000),
(12, 8, '978-602-6232-06', 1, 35000),
(13, 8, '978-602-6232-67', 2, 140000),
(14, 9, '978-602-1514-44', 1, 99000),
(15, 9, '978-602-6232-38', 2, 129000),
(16, 10, '978-602-8759-44', 5, 134000);

-- --------------------------------------------------------

--
-- Table structure for table `tb_kategori`
--

CREATE TABLE `tb_kategori` (
  `kode_kategori` int(11) NOT NULL,
  `nama_kategori` varchar(30) NOT NULL,
  `rak` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_kategori`
--

INSERT INTO `tb_kategori` (`kode_kategori`, `nama_kategori`, `rak`) VALUES
(2020044011, 'BASIS DATA', '1'),
(2020044022, 'PEMROGRAMAN', '1.2'),
(2020044031, 'PYTHON', '1.1');

-- --------------------------------------------------------

--
-- Table structure for table `tb_laporan`
--

CREATE TABLE `tb_laporan` (
  `id_laporan` int(11) NOT NULL,
  `terjual` int(11) NOT NULL DEFAULT '0',
  `pemasukan` double NOT NULL DEFAULT '0',
  `tanggal` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tb_pegawai`
--

CREATE TABLE `tb_pegawai` (
  `id_kasir` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `alamat` varchar(30) NOT NULL,
  `telepon` varchar(13) NOT NULL,
  `shif` enum('Pagi','Sore') NOT NULL,
  `username` varchar(20) NOT NULL,
  `pwd` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `akses` enum('admin','kasir') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_pegawai`
--

INSERT INTO `tb_pegawai` (`id_kasir`, `nama`, `alamat`, `telepon`, `shif`, `username`, `pwd`, `salt`, `akses`) VALUES
(2, 'Mail-kun', 'Indramayu', '+628776067307', 'Pagi', 'taufik21', 'vhPv/MFzdYkRXdT9xva1PZPy2To2KJVyiQ9m7flmR2A=', '6WVOVfIIgq3ij1ICYh0vIuqASGxquPim', 'kasir'),
(3, 'Rosyid Iz', 'Temanggung', '08995104097', 'Sore', 'rosyidiz', 'tSoR7ZChTws8crB1j0EYwa1Q3dPPWd4uTZIIc7EAIuE=', '0kjBGPAH434dF5zQBkKH34OBdErm3k16', 'admin'),
(4, 'Didik', 'Jogja', '+628387575702', 'Pagi', 'didiknhw', 'EVRBuw4zSraJa4kM2wWDWMmXGr2XH2WAP4zxwyNhDo0=', 'unxueOk2Jt8eYKmBI3vQcT4P5fdmfQGx', 'kasir');

-- --------------------------------------------------------

--
-- Table structure for table `tb_pelanggan`
--

CREATE TABLE `tb_pelanggan` (
  `id_pelanggan` int(11) NOT NULL,
  `nama_pelanggan` varchar(50) NOT NULL,
  `alamat` varchar(50) NOT NULL,
  `telepon` varchar(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_pelanggan`
--

INSERT INTO `tb_pelanggan` (`id_pelanggan`, `nama_pelanggan`, `alamat`, `telepon`) VALUES
(1, 'Topik', 'Sleman', '08123456789'),
(2, 'Arief', 'Jakarta', '08951024011'),
(3, 'Juminten', 'Jogja', '085123456111'),
(4, 'Paijo', 'Semarang', '08913412123'),
(5, 'Jubaedah', 'Wonosobo', '082990990990'),
(6, 'Sarju', 'Solo', '08746375731'),
(7, 'Sukijan', 'Pati', '087123123123'),
(8, 'Paijan', 'Rembang', '089111111111'),
(9, 'Budi', 'Magelang', '081090909091');

-- --------------------------------------------------------

--
-- Table structure for table `tb_transaksi`
--

CREATE TABLE `tb_transaksi` (
  `id_transaksi` int(11) NOT NULL,
  `id_pelanggan` int(11) NOT NULL,
  `id_kasir` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `bayar` int(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_transaksi`
--

INSERT INTO `tb_transaksi` (`id_transaksi`, `id_pelanggan`, `id_kasir`, `tanggal`, `bayar`) VALUES
(1, 3, 2, '2020-05-17', 200000),
(2, 2, 3, '2020-05-18', 100000),
(3, 1, 2, '2020-05-19', 150000),
(5, 7, 2, '2020-05-22', 100000),
(6, 9, 2, '2020-05-22', 600000),
(7, 2, 2, '2020-05-22', 150000),
(8, 3, 2, '2020-05-22', 400000),
(9, 6, 2, '2020-05-22', 400000),
(10, 3, 2, '2020-05-22', 700000);

--
-- Triggers `tb_transaksi`
--
DELIMITER $$
CREATE TRIGGER `log_delete_transaksi` AFTER DELETE ON `tb_transaksi` FOR EACH ROW BEGIN
 INSERT INTO log_tb_transaksi
 SET
  id_log = null,
  aktifitas = 'hapus',
  tanggal_log = now(),
  id_kasir = old.id_kasir;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `log_insert_transaksi` AFTER INSERT ON `tb_transaksi` FOR EACH ROW BEGIN
 INSERT INTO log_tb_transaksi
 SET
  id_log = null,
  aktifitas = 'tambah',
  tanggal_log = now(),
  id_kasir = get_last_idkasir();
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `log_update_transaksi` AFTER UPDATE ON `tb_transaksi` FOR EACH ROW BEGIN
 INSERT INTO log_tb_transaksi
 SET
  id_log = null,
  aktifitas = 'ubah',
  tanggal_log = now(),
  id_kasir = old.id_kasir;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Structure for view `data_buku`
--
DROP TABLE IF EXISTS `data_buku`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `data_buku`  AS  select `tb_buku`.`isbn` AS `isbn`,`tb_buku`.`kode_kategori` AS `kode_kategori`,`tb_buku`.`judul_buku` AS `judul_buku`,`tb_buku`.`penulis` AS `penulis`,`tb_buku`.`penerbit` AS `penerbit`,`tb_buku`.`tahun` AS `tahun`,`tb_buku`.`stok` AS `stok`,`tb_buku`.`harga_pokok` AS `harga_pokok`,`tb_buku`.`harga_jual` AS `harga_jual`,`tb_buku`.`gambar` AS `gambar`,`tb_kategori`.`nama_kategori` AS `nama_kategori`,`tb_kategori`.`rak` AS `rak` from (`tb_buku` join `tb_kategori` on((`tb_buku`.`kode_kategori` = `tb_kategori`.`kode_kategori`))) ;

-- --------------------------------------------------------

--
-- Structure for view `data_detail_trx`
--
DROP TABLE IF EXISTS `data_detail_trx`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `data_detail_trx`  AS  select `tb_detail_transaksi`.`id_detail_transaksi` AS `id_detail`,`tb_detail_transaksi`.`id_transaksi` AS `id_trx`,`tb_buku`.`isbn` AS `isbn`,`tb_buku`.`judul_buku` AS `judul_buku`,`tb_detail_transaksi`.`banyak` AS `banyak`,`tb_detail_transaksi`.`harga` AS `harga` from ((`tb_detail_transaksi` join `tb_buku` on((`tb_detail_transaksi`.`isbn` = `tb_buku`.`isbn`))) join `tb_transaksi` on((`tb_detail_transaksi`.`id_transaksi` = `tb_transaksi`.`id_transaksi`))) where (`tb_detail_transaksi`.`id_transaksi` = `tb_transaksi`.`id_transaksi`) ;

-- --------------------------------------------------------

--
-- Structure for view `data_transaksi`
--
DROP TABLE IF EXISTS `data_transaksi`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `data_transaksi`  AS  select `tb_transaksi`.`id_transaksi` AS `id_transaksi`,`tb_transaksi`.`id_pelanggan` AS `id_pelanggan`,`tb_transaksi`.`id_kasir` AS `id_kasir`,`tb_pelanggan`.`nama_pelanggan` AS `nama_pelanggan`,`tb_pegawai`.`nama` AS `nama_petugas`,`tb_transaksi`.`tanggal` AS `tanggal`,`tb_transaksi`.`bayar` AS `bayar`,`calc_totalbayar`(`tb_transaksi`.`id_transaksi`) AS `totalbayar` from ((`tb_transaksi` join `tb_pelanggan` on((`tb_transaksi`.`id_pelanggan` = `tb_pelanggan`.`id_pelanggan`))) join `tb_pegawai` on((`tb_transaksi`.`id_kasir` = `tb_pegawai`.`id_kasir`))) ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `log_history_updatebuku`
--
ALTER TABLE `log_history_updatebuku`
  ADD PRIMARY KEY (`id_log_updatebuku`),
  ADD KEY `fk_history_buku` (`isbn`),
  ADD KEY `fk_history_kategori` (`kode_kategori`);

--
-- Indexes for table `log_tb_transaksi`
--
ALTER TABLE `log_tb_transaksi`
  ADD PRIMARY KEY (`id_log`),
  ADD KEY `fk_kasir` (`id_kasir`);

--
-- Indexes for table `tb_buku`
--
ALTER TABLE `tb_buku`
  ADD PRIMARY KEY (`isbn`),
  ADD KEY `kode_kategori` (`kode_kategori`);

--
-- Indexes for table `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  ADD PRIMARY KEY (`id_detail_transaksi`),
  ADD KEY `id_transaksi` (`id_transaksi`),
  ADD KEY `isbn` (`isbn`);

--
-- Indexes for table `tb_kategori`
--
ALTER TABLE `tb_kategori`
  ADD PRIMARY KEY (`kode_kategori`);

--
-- Indexes for table `tb_laporan`
--
ALTER TABLE `tb_laporan`
  ADD PRIMARY KEY (`id_laporan`);

--
-- Indexes for table `tb_pegawai`
--
ALTER TABLE `tb_pegawai`
  ADD PRIMARY KEY (`id_kasir`);

--
-- Indexes for table `tb_pelanggan`
--
ALTER TABLE `tb_pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`);

--
-- Indexes for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_pelanggan` (`id_pelanggan`),
  ADD KEY `id_kasir` (`id_kasir`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `log_history_updatebuku`
--
ALTER TABLE `log_history_updatebuku`
  MODIFY `id_log_updatebuku` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `log_tb_transaksi`
--
ALTER TABLE `log_tb_transaksi`
  MODIFY `id_log` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  MODIFY `id_detail_transaksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `tb_kategori`
--
ALTER TABLE `tb_kategori`
  MODIFY `kode_kategori` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2020044032;

--
-- AUTO_INCREMENT for table `tb_laporan`
--
ALTER TABLE `tb_laporan`
  MODIFY `id_laporan` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tb_pegawai`
--
ALTER TABLE `tb_pegawai`
  MODIFY `id_kasir` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tb_pelanggan`
--
ALTER TABLE `tb_pelanggan`
  MODIFY `id_pelanggan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `log_tb_transaksi`
--
ALTER TABLE `log_tb_transaksi`
  ADD CONSTRAINT `fk_kasir` FOREIGN KEY (`id_kasir`) REFERENCES `tb_pegawai` (`id_kasir`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `tb_buku`
--
ALTER TABLE `tb_buku`
  ADD CONSTRAINT `tb_buku_ibfk` FOREIGN KEY (`kode_kategori`) REFERENCES `tb_kategori` (`kode_kategori`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  ADD CONSTRAINT `fk_transaksi` FOREIGN KEY (`id_transaksi`) REFERENCES `tb_transaksi` (`id_transaksi`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tb_detail_transaksi_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `tb_buku` (`isbn`) ON UPDATE CASCADE;

--
-- Constraints for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD CONSTRAINT `tb_transaksi_ibfk_4` FOREIGN KEY (`id_pelanggan`) REFERENCES `tb_pelanggan` (`id_pelanggan`) ON UPDATE CASCADE,
  ADD CONSTRAINT `tb_transaksi_ibfk_5` FOREIGN KEY (`id_kasir`) REFERENCES `tb_pegawai` (`id_kasir`) ON UPDATE CASCADE;

DELIMITER $$
--
-- Events
--
CREATE DEFINER=`root`@`localhost` EVENT `event_laporan` ON SCHEDULE EVERY 1 DAY STARTS '2020-05-18 11:23:32' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    DECLARE yesterday DATE;
    DECLARE idtrx INT;
    DECLARE penjualan INT;
    DECLARE pemasukan DOUBLE;

    SELECT
        (CURDATE() - INTERVAL 1 DAY) INTO yesterday;
    
    SELECT
        id_transaksi INTO idtrx
    FROM
        tb_transaksi
    WHERE tanggal = yesterday;    
    
    SELECT
        SUM(banyak) into penjualan
    FROM
        tb_detail_transaksi
    WHERE id_transaksi = idtrx;
    IF penjualan = null
    THEN SET penjualan = 0;
    END IF;
    
    SELECT
        SUM(banyak * harga) INTO pemasukan
    FROM
        tb_detail_transaksi
    WHERE id_transaksi = idtrx;
    IF pemasukan = NULL
    THEN SET pemasukan = 0;
    END IF;
    
    INSERT INTO tb_laporan
    VALUES
        (
            null,
            penjualan,
            pemasukan,
            yesterday
        );
    
END$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
