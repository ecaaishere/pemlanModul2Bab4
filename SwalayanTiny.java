import java.util.Scanner;
import java.util.ArrayList;

public class SwalayanTiny {
    static ArrayList<Akun> akunList = new ArrayList<>();
    static Scanner input = new Scanner(System.in);
    static Akun akunAktif = null;
    static int percobaanGlobal = 0;
    static String nopelTerakhir = null;

    public static void main(String[] args) {

        akunList.add(new Akun("3812345678", "Reva Razania", "2716", 500000, "Silver"));
        akunList.add(new Akun("5612345678", "Vanini Aulia", "5649", 2000000, "Gold"));
        akunList.add(new Akun("7412345678", "Andini Cahya", "1905", 5000000, "Platinum"));
        akunList.add(new Akun("5602062005", "Nadira Yusha", "1667", 1500000, "Gold"));

        System.out.println("==== SWALAYAN TINY ====");

        while (akunAktif == null) {
            System.out.print("Masukkan nomor pelanggan: ");
            String nopel = input.nextLine();
            
            Akun akun = cariAkun(nopel);
            
            if (akun == null) {
                System.out.println("Nomor pelanggan tidak ditemukan");
                continue;
            }
            
            if (!akun.aktif) {
                System.out.println("Akun telah diblokir karena kesalahan PIN 3 kali");
                continue;
            }

            if (nopelTerakhir == null || !nopelTerakhir.equals(nopel)) {
                percobaanGlobal = 0;
                nopelTerakhir = nopel;
            }

            boolean pinBenar = false;
            
            while (percobaanGlobal < 3 && !pinBenar) {
                System.out.print("Masukkan PIN: ");
                String pin = input.nextLine();
                
                if (akun.pin.equals(pin)) {
                    pinBenar = true;
                    akunAktif = akun;
                    percobaanGlobal = 0;
                    nopelTerakhir = null;
                    System.out.println("\nLogin berhasil! Selamat datang, " + akun.nama);
                } else {
                    percobaanGlobal++;
                    if (percobaanGlobal < 3) {
                        System.out.println("PIN salah. Percobaan ke-" + percobaanGlobal);
                        break;
                    }
                }
            }
            
            if (!pinBenar && percobaanGlobal >= 3) {
                akun.aktif = false;
                System.out.println("Akun telah diblokir karena kesalahan PIN 3 kali");
                percobaanGlobal = 0;
                nopelTerakhir = null;
            }
        }
        
        boolean menu = true;
        while (menu) {
            System.out.println("\n==== MENU TRANSAKSI ====");
            System.out.println("1. Top Up");
            System.out.println("2. Pembelian");
            System.out.println("3. Cek Saldo");
            System.out.println("4. Keluar");
            System.out.print("\nPilih menu: ");
            
            int pilih = input.nextInt();
            input.nextLine();
            
            switch (pilih) {
                case 1:
                    topUp();
                    break;
                case 2:
                    pembelian();
                    break;
                case 3:
                    cekSaldo();
                    break;
                case 4:
                    menu = false;
                    System.out.println("\nTerima kasih telah berbelanja di Swalayan Tiny!!!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid");
            }
        }
    }
    
    static void topUp() {
        System.out.print("Masukkan jumlah top up: Rp ");
        double jumlah = input.nextDouble();
        input.nextLine();
        
        System.out.print("Masukkan PIN: ");
        String pin = input.nextLine();
        
        if (akunAktif.pin.equals(pin)) {
            akunAktif.saldo += jumlah;
            System.out.println("\nTop up berhasil!\n");
            System.out.println("Saldo saat ini: Rp " + formatRupiah(akunAktif.saldo));
        } else {
            System.out.println("PIN salah! Top up dibatalkan");
        }
    }
    
    static void pembelian() {
        System.out.print("Masukkan jumlah pembelian: Rp ");
        double belanja = input.nextDouble();
        input.nextLine();
        
        if (belanja <= 0) {
            System.out.println("Jumlah pembelian tidak valid");
            return;
        }
        
        double cashback = 0;

        if (akunAktif.jenis.equals("Silver")) {
            if (belanja > 1000000) {
                cashback = belanja * 0.05;
                System.out.println("Cashback 5%");
            }
        } else if (akunAktif.jenis.equals("Gold")) {
            if (belanja > 1000000) {
                cashback = belanja * 0.07;
                System.out.println("Cashback 7%");
            } else {
                cashback = belanja * 0.02;
                System.out.println("Cashback 2%");
            }
        } else if (akunAktif.jenis.equals("Platinum")) {
            if (belanja > 1000000) {
                cashback = belanja * 0.10;
                System.out.println("Cashback 10%");
            } else {
                cashback = belanja * 0.05;
                System.out.println("Cashback 5%");
            }
        }
        
        double totalBayar = belanja - cashback;
        double saldoAkhir = akunAktif.saldo - totalBayar;
        
        if (saldoAkhir < 10000) {
            System.out.println("\nTransaksi gagal!\n");
            System.out.println("Saldo minimal Rp10.000 harus dipenuhi");
            System.out.println("Saldo saat ini: Rp " + formatRupiah(akunAktif.saldo));
            System.out.println("Total yang harus dibayar: Rp " + formatRupiah(totalBayar));
            System.out.println("Saldo setelah transaksi: Rp " + formatRupiah(saldoAkhir));
        } else {
            akunAktif.saldo = saldoAkhir;
            System.out.println("\nTransaksi berhasil!\n");
            System.out.println("====== DETAIL TRANSAKSI ======");
            System.out.println("Total belanja : Rp " + formatRupiah(belanja));
            System.out.println("Cashback      : Rp " + formatRupiah(cashback));
            System.out.println("Total dibayar : Rp " + formatRupiah(totalBayar));
            System.out.println("Saldo akhir   : Rp " + formatRupiah(akunAktif.saldo));
        }
    }
    
    static void cekSaldo() {
        System.out.print("Masukkan PIN: ");
        String pin = input.nextLine();
        
        if (akunAktif.pin.equals(pin)) {
            System.out.println("\n====== INFORMASI SALDO ======");
            System.out.println("Nama      : " + akunAktif.nama);
            System.out.println("Jenis     : " + akunAktif.jenis);
            System.out.println("Saldo     : Rp " + formatRupiah(akunAktif.saldo));
        } else {
            System.out.println("PIN salah! Tidak dapat menampilkan saldo");
        }
    }
    
    static Akun cariAkun(String nopel) {
        for (Akun a : akunList) {
            if (a.nopel.equals(nopel)) {
                return a;
            }
        }
        return null;
    }
    
    static String formatRupiah(double angka) {
        return String.format("%,.0f", angka).replace(",", ".");
    }
}

class Akun {
    String nopel, nama, pin, jenis;
    double saldo;
    boolean aktif = true;
    
    Akun(String nopel, String nama, String pin, double saldo, String jenis) {
        this.nopel = nopel;
        this.nama = nama;
        this.pin = pin;
        this.saldo = saldo;
        this.jenis = jenis;
    }
}