package com.example.mykampuslite.data;
import androidx.lifecycle.LiveData;
import com.example.mykampuslite.model.Dosen;
import com.example.mykampuslite.model.Mahasiswa;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MahasiswaRepository implements AkademikRepository {

    private final AkademikDao dao;
    // Menyediakan single thread executor untuk proses tulis/baca database
    private final Executor executor = Executors.newSingleThreadExecutor();

    public MahasiswaRepository(AkademikDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<Mahasiswa>> getAll() {
        return dao.getAll();
    }

    @Override
    public void insertMahasiswa(Mahasiswa m) {
        executor.execute(() -> dao.insertMahasiswa(m));
    }

    @Override
    public LiveData<List<Dosen>> getAllDosen() {
        return dao.getAllDosen();
    }

    @Override
    public void insertDosen(Dosen d) {
        executor.execute(() -> dao.insertDosen(d));
    }

    @Override
    public void updateMahasiswa(Mahasiswa m) {
        executor.execute(() -> dao.updateMahasiswa(m));
    }

    @Override
    public void deleteMahasiswa(Mahasiswa m) {
        executor.execute(() -> dao.deleteMahasiswa(m));
    }

    @Override
    public void updateDosen(Dosen d) {
        executor.execute(() -> dao.updateDosen(d));
    }

    @Override
    public void deleteDosen(Dosen d) {
        executor.execute(() -> dao.deleteDosen(d));
    }
}
