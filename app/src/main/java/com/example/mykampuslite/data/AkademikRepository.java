package com.example.mykampuslite.data;
import androidx.lifecycle.LiveData;
import com.example.mykampuslite.model.Dosen;
import com.example.mykampuslite.model.Mahasiswa;
import java.util.List;

public interface AkademikRepository {
    LiveData<List<Mahasiswa>> getAll();
    void insertMahasiswa(Mahasiswa m);
    void updateMahasiswa(Mahasiswa m);
    void deleteMahasiswa(Mahasiswa m);

    LiveData<List<Dosen>> getAllDosen();
    void insertDosen(Dosen d);
    void updateDosen(Dosen d);
    void deleteDosen(Dosen d);
}
