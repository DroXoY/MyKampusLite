package com.example.mykampuslite.data;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mykampuslite.model.Dosen;
import com.example.mykampuslite.model.Mahasiswa;
import java.util.List;

@Dao
public interface AkademikDao {
    // Sesuai dengan instruksi PDF Poin 3
    @Query("SELECT * FROM mahasiswa_table")
    LiveData<List<Mahasiswa>> getAll();

    @Insert
    void insertMahasiswa(Mahasiswa mahasiswa);

    // Tambahan untuk Dosen agar bisa dirender di UI
    @Query("SELECT * FROM dosen_table")
    LiveData<List<Dosen>> getAllDosen();

    @Insert
    void insertDosen(Dosen dosen);
    @Update
    void updateMahasiswa(Mahasiswa mahasiswa);

    @Delete
    void deleteMahasiswa(Mahasiswa mahasiswa);

    @Update
    void updateDosen(Dosen dosen);

    @Delete
    void deleteDosen(Dosen dosen);
}
