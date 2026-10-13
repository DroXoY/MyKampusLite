package com.example.mykampuslite.model;
import androidx.room.Entity;
import java.util.Objects;

@Entity(tableName = "dosen_table")
public class Dosen extends Person implements EvaluasiAkademik {
    private String nidn;
    private int mataKuliahDiampu; // Menggunakan int untuk mempermudah perhitungan validasi

    // Constructor kosong wajib untuk Room
    public Dosen() {}

    @Override
    public String getRole() {
        return "Dosen";
    }

    // Implementasi Polymorphism: Logika berbeda dari Mahasiswa
    @Override
    public String getStatusAkademik() {
        if (mataKuliahDiampu > 3) {
            return "Beban Lebih";
        }
        return "Normal";
    }

    public String getNidn() { return nidn; }

    // Validasi NIDN (opsional untuk tambahan keamanan)
    public void setNidn(String nidn) {
        if (nidn == null || nidn.trim().isEmpty()) {
            throw new IllegalArgumentException("NIDN tidak boleh kosong");
        }
        this.nidn = nidn;
    }

    public int getMataKuliahDiampu() { return mataKuliahDiampu; }

    public void setMataKuliahDiampu(int mataKuliahDiampu) {
        if (mataKuliahDiampu < 0) {
            throw new IllegalArgumentException("Jumlah mata kuliah tidak bisa negatif");
        }
        this.mataKuliahDiampu = mataKuliahDiampu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dosen dosen = (Dosen) o;
        return Objects.equals(nidn, dosen.nidn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nidn);
    }
}