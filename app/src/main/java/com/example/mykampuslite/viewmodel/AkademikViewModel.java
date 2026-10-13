package com.example.mykampuslite.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.mykampuslite.data.AkademikRepository;
import com.example.mykampuslite.model.Dosen;
import com.example.mykampuslite.model.Mahasiswa;
import java.util.List;

public class AkademikViewModel extends ViewModel {

    // Bergantung pada Interface Repository, bukan implementasi DAO (Dependency Inversion)
    private final AkademikRepository repository;

    private final LiveData<List<Mahasiswa>> listMahasiswa;
    private final LiveData<List<Dosen>> listDosen;

    public AkademikViewModel(AkademikRepository repository) {
        this.repository = repository;
        // Data langsung dimuat saat ViewModel diciptakan dan akan survive saat rotasi layar
        this.listMahasiswa = repository.getAll();
        this.listDosen = repository.getAllDosen();
    }

    // Method Getter LiveData untuk di-observe oleh UI (Fragment)
    public LiveData<List<Mahasiswa>> getAllMahasiswa() {
        return listMahasiswa;
    }

    public LiveData<List<Dosen>> getAllDosen() {
        return listDosen;
    }

    // Method Insert (UI tinggal panggil ini tanpa tahu proses Background Thread di belakangnya)
    public void insertMahasiswa(Mahasiswa m) {
        repository.insertMahasiswa(m);
    }

    public void insertDosen(Dosen d) {
        repository.insertDosen(d);
    }
    public void updateMahasiswa(Mahasiswa m) { repository.updateMahasiswa(m); }
    public void deleteMahasiswa(Mahasiswa m) { repository.deleteMahasiswa(m); }
    public void updateDosen(Dosen d) { repository.updateDosen(d); }
    public void deleteDosen(Dosen d) { repository.deleteDosen(d); }
}
