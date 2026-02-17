# Tucil1_13524093

## Deskripsi Program

Program ini merupakan solver dengan metode Brute Force untuk **Queens Puzzle**, yaitu puzzle menempatkan N buah ratu catur pada board berukuran N×N yang sudah diwarnai dengan huruf A-Z. Tujuannya adalah menemukan konfigurasi penempatan ratu yang memenuhi semua constraint berikut:

1. **Constraint Total**: Tepat N buah ratu pada board
2. **Constraint Baris**: Setiap baris berisi tepat 1 ratu
3. **Constraint Kolom**: Setiap kolom berisi tepat 1 ratu
4. **Constraint Warna**: Setiap warna (huruf) berisi tepat 1 ratu
5. **Constraint Neighbor**: Tidak ada 2 ratu yang bertetangga (atas/bawah/kiri/kanan)

Program ini mengimplementasikan **dua algoritma** berbeda:

- **Brute Force**: Iterasi semua kombinasi C(N²,N) tanpa optimasi
- **Optimized Brute Force**: Iterasi mirip dengan algoritma yang pertama, hanya saja dioptimasi dengan setiap queen tidak akan berpindah baris

Program menyediakan **GUI berbasis JavaFX** dengan dua mode:

- **Text Mode**: Input manual atau Load file .txt, solve dan export hasil ke .txt
- **Image Mode**: Load file .txt, solve, dan export hasil ke PNG

Kedua mode tersebut dilengkapi live update visualization

## Requirements

Program ini membutuhkan:

1. **Java Development Kit (JDK) 21 atau lebih tinggi**
   - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) atau [OpenJDK](https://adoptium.net/)
   - Verifikasi instalasi: `java -version`

2. **Apache Maven 3.9.0 atau lebih tinggi**
   - Download: [Maven](https://maven.apache.org/download.cgi)
   - Verifikasi instalasi: `mvn -version`

3. **JavaFX 21** (otomatis di-download oleh Maven)

### Catatan Platform

- **Windows**: Gunakan PowerShell atau Command Prompt
- **Linux**: Gunakan Terminal

## Cara Kompilasi

Program menggunakan **Apache Maven** sebagai build tool. Untuk mengkompilasi program:

```powershell
# Di direktori root project (yang berisi pom.xml)
mvn clean compile
```

Perintah ini akan:

- Membersihkan build sebelumnya (`clean`)
- Mengkompilasi semua source code (`compile`)
- Men-download dependencies yang diperlukan (JavaFX, dll)

### Compile dan Package (JAR)

Untuk membuat executable JAR:

```powershell
mvn clean package
```

JAR akan dibuat di folder `target/` dengan nama `queens-puzzle-1.0-SNAPSHOT.jar`

## Cara Menjalankan Program

### Menggunakan Maven

```powershell
mvn clean javafx:run
```

Perintah ini akan:

- Mengkompilasi program
- Menjalankan aplikasi GUI secara otomatis

## Cara Menggunakan Program

### Mode Text

1. **Klik tab "Text Mode"** di bagian atas
2. **Input Board**:
   - Masukkan ukuran board (contoh: `4` untuk board 4×4)
   - Masukkan board baris per baris (huruf A-Z tanpa spasi)
   - Contoh input board 4×4:
     ```
     ABCD
     DCBA
     BADC
     CDAB
     ```
3. **Pilih Algoritma**
4. **Klik "Solve"**
5. **Hasil**:
   - Board akan ditampilkan dengan simbol `#` untuk queen
   - Statistik: jumlah iterasi dan waktu eksekusi
   - Live update visualization (optional)

### Mode Image

1. **Klik tab "Image Mode"** di bagian atas
2. **Load File**:
   - Klik "Load Board from File"
   - Pilih file `.txt` dengan format:
     ```
     4
     ABCD
     DCBA
     BADC
     CDAB
     ```
     (Baris pertama = ukuran N, baris berikutnya = board)
3. **Pilih Algoritma**
4. **Klik "Solve"**
5. **Hasil**:
   - Board ditampilkan sebagai gambar berwarna
   - Queen ditampilkan dengan crown overlay
   - Dapat di-export ke PNG dengan klik "Export to PNG"

### Contoh File Input

File input harus berformat `.txt` dengan struktur:

```
N
baris1
baris2
...
barisN
```

Contoh file `test/board_4x4.txt`:

```
4
ABCD
DCBA
BADC
CDAB
```

### Hasil Output

- **Text Mode**: Hasil ditampilkan di GUI (board dengan `#` = queen)
- **Image Mode**: Hasil dapat di-export ke file `.png` dengan warna sesuai huruf A-Z

## Struktur Direktori

```
tucil1_queens/
├── src/
│   ├── main/
│   │   ├── java/com/app/
│   │   │   ├── controller/       # Controller (MVC)
│   │   │   ├── model/            # Model (algoritma + data structures)
│   │   │   └── view/             # View (JavaFX GUI)
│   │   └── resources/
│   │       └── crown.png         # Crown image untuk queen
│   └── test/
│       └── java/
├── test/                          # Test cases (.txt files)
├── docs/                          # Laporan
├── pom.xml                        # Maven configuration
└── README.md                      # File ini
```

## Teknologi

- **Bahasa**: Java 25
- **GUI Framework**: JavaFX 21
- **Build Tool**: Apache Maven 3.9.11
- **Arsitektur**: Model-View-Controller (MVC)

## Author

**Nama**: Reinsen Silitonga  
**NIM**: 13524093  
**Program Studi**: Teknik Informatika - Institut Teknologi Bandung

---

**Tugas Kecil 1 - IF2211 Strategi Algoritma**  
Semester Genap Tahun 2025/2026
