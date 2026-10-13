package com.example.mykampuslite.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mykampuslite.R;
import com.example.mykampuslite.adapter.PersonAdapter;
import com.example.mykampuslite.data.AkademikDao;
import com.example.mykampuslite.data.MahasiswaRepository;
import com.example.mykampuslite.data.MyKampusDatabase;
import com.example.mykampuslite.model.Mahasiswa;
import com.example.mykampuslite.model.Person; // Jangan lupa import ini untuk callback
import com.example.mykampuslite.viewmodel.AkademikViewModel;
import com.example.mykampuslite.viewmodel.ViewModelFactory;

public class MahasiswaFragment extends Fragment {

    private AkademikViewModel viewModel;
    private PersonAdapter adapter;
    private Mahasiswa mahasiswaToEdit = null; // Penanda mode insert atau update

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mahasiswa, container, false);

        // 1. Dependency Injection: Merakit Database -> DAO -> Repository -> Factory -> ViewModel
        AkademikDao dao = MyKampusDatabase.getInstance(requireContext()).akademikDao();
        MahasiswaRepository repository = new MahasiswaRepository(dao);
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(AkademikViewModel.class);

        // 2. Deklarasi UI Input (Dipindah ke atas agar bisa dipanggil oleh Adapter saat Edit)
        EditText etNama = view.findViewById(R.id.etNama);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etNim = view.findViewById(R.id.etNim);
        EditText etJurusan = view.findViewById(R.id.etJurusan);
        EditText etIpk = view.findViewById(R.id.etIpk);
        Button btnSimpan = view.findViewById(R.id.btnSimpan);


        // 3. Setup RecyclerView & Listener Edit/Delete
        RecyclerView rv = view.findViewById(R.id.rvMahasiswa);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PersonAdapter();

        adapter.setOnItemClickListener(new PersonAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Person person) {
                if (person instanceof Mahasiswa) {
                    mahasiswaToEdit = (Mahasiswa) person;
                    // Tarik data ke form input
                    etNama.setText(mahasiswaToEdit.getNama());
                    etEmail.setText(mahasiswaToEdit.getEmail());
                    etNim.setText(mahasiswaToEdit.getNim());
                    etJurusan.setText(mahasiswaToEdit.getJurusan());
                    etIpk.setText(String.valueOf(mahasiswaToEdit.getIpk()));
                    btnSimpan.setText("Update Mahasiswa");
                }
            }

            @Override
            public void onDeleteClick(Person person) {
                if (person instanceof Mahasiswa) {
                    viewModel.deleteMahasiswa((Mahasiswa) person); // Eksekusi Hapus
                    Toast.makeText(getContext(), "Data Dihapus!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rv.setAdapter(adapter);

        // 4. Observe LiveData: UI akan auto-update saat ada data baru/dihapus
        viewModel.getAllMahasiswa().observe(getViewLifecycleOwner(), mahasiswaList -> {
            adapter.submitList(mahasiswaList);
        });

        // 5. Logic Tombol Simpan (Menangani Insert Baru ATAU Update Data Lama)
        btnSimpan.setOnClickListener(v -> {
            try {
                if (mahasiswaToEdit == null) {
                    // MODE INSERT: Buat objek baru
                    Mahasiswa m = new Mahasiswa();
                    m.setNama(etNama.getText().toString());
                    m.setEmail(etEmail.getText().toString()); // Set email
                    m.setNim(etNim.getText().toString());
                    m.setJurusan(etJurusan.getText().toString()); // Set jurusan
                    m.setIpk(Double.parseDouble(etIpk.getText().toString()));

                    viewModel.insertMahasiswa(m);
                    Toast.makeText(getContext(), "Berhasil disimpan!", Toast.LENGTH_SHORT).show();
                } else {
                    // MODE UPDATE: Timpa objek lama
                    mahasiswaToEdit.setNama(etNama.getText().toString());
                    mahasiswaToEdit.setEmail(etEmail.getText().toString()); // Update email
                    mahasiswaToEdit.setNim(etNim.getText().toString());
                    mahasiswaToEdit.setJurusan(etJurusan.getText().toString()); // Update jurusan
                    mahasiswaToEdit.setIpk(Double.parseDouble(etIpk.getText().toString()));

                    viewModel.updateMahasiswa(mahasiswaToEdit);
                    Toast.makeText(getContext(), "Berhasil diupdate!", Toast.LENGTH_SHORT).show();

                    mahasiswaToEdit = null;
                    btnSimpan.setText("Simpan Mahasiswa");
                }

                // CLEAR SEMUA FORM INPUT
                etNama.setText("");
                etEmail.setText("");
                etNim.setText("");
                etJurusan.setText("");
                etIpk.setText("");

            } catch (IllegalArgumentException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Pastikan format angka benar!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}