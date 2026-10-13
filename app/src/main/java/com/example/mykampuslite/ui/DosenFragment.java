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
import com.example.mykampuslite.data.AkademikRepository;
import com.example.mykampuslite.data.MahasiswaRepository;
import com.example.mykampuslite.data.MyKampusDatabase;
import com.example.mykampuslite.model.Dosen;
import com.example.mykampuslite.model.Mahasiswa;
import com.example.mykampuslite.model.Person;
import com.example.mykampuslite.viewmodel.AkademikViewModel;
import com.example.mykampuslite.viewmodel.ViewModelFactory;

public class


DosenFragment extends Fragment {

    private AkademikViewModel viewModel;
    private PersonAdapter adapter;
    private Dosen dosenToEdit = null; // Penanda untuk mode Update

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dosen, container, false);

        // 1. Setup Arsitektur MVVM (Menggunakan 1 Repository Gabungan)[cite: 26, 32]
        AkademikDao dao = MyKampusDatabase.getInstance(requireContext()).akademikDao();
        AkademikRepository repository = new MahasiswaRepository(dao); // Cukup 1 repository ini saja

        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(AkademikViewModel.class);

        // 2. Hubungkan ID UI Input dari fragment_dosen.xml
        EditText etNama = view.findViewById(R.id.etNamaDosen);
        EditText etEmail = view.findViewById(R.id.etEmailDosen);
        EditText etNidn = view.findViewById(R.id.etNidn);
        EditText etMatkul = view.findViewById(R.id.etMatkul);
        Button btnSimpan = view.findViewById(R.id.btnSimpanDosen);


        // 3. Setup RecyclerView & Listener Edit/Delete
        RecyclerView rv = view.findViewById(R.id.rvDosen);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PersonAdapter();

        adapter.setOnItemClickListener(new PersonAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Person person) {
                if (person instanceof Dosen) {
                    dosenToEdit = (Dosen) person;

                    // Tarik data lama ke form input agar bisa diedit
                    etNama.setText(dosenToEdit.getNama());
                    etEmail.setText(dosenToEdit.getEmail());
                    etNidn.setText(dosenToEdit.getNidn());
                    etMatkul.setText(String.valueOf(dosenToEdit.getMataKuliahDiampu()));

                    btnSimpan.setText("Update Dosen");
                }
            }

            @Override
            public void onDeleteClick(Person person) {
                if (person instanceof Dosen) {
                    viewModel.deleteDosen((Dosen) person); // Eksekusi Hapus via Background Thread
                    Toast.makeText(getContext(), "Data Dosen Dihapus!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rv.setAdapter(adapter);

        // 4. Observe LiveData: UI akan auto-refresh jika ada data yang dihapus/diupdate
        viewModel.getAllDosen().observe(getViewLifecycleOwner(), dosenList -> {
            adapter.submitList(dosenList);
        });

        // 5. Logic Tombol Simpan (Menangani Insert Baru ATAU Update Data Lama)
        btnSimpan.setOnClickListener(v -> {
            try {
                if (dosenToEdit == null) {
                    // MODE INSERT
                    Dosen d = new Dosen();
                    d.setNama(etNama.getText().toString());
                    d.setEmail(etEmail.getText().toString());
                    d.setNidn(etNidn.getText().toString());
                    d.setMataKuliahDiampu(Integer.parseInt(etMatkul.getText().toString()));

                    viewModel.insertDosen(d);
                    Toast.makeText(getContext(), "Dosen berhasil disimpan!", Toast.LENGTH_SHORT).show();
                } else {
                    // MODE UPDATE
                    dosenToEdit.setNama(etNama.getText().toString());
                    dosenToEdit.setEmail(etEmail.getText().toString());
                    dosenToEdit.setNidn(etNidn.getText().toString());
                    dosenToEdit.setMataKuliahDiampu(Integer.parseInt(etMatkul.getText().toString()));

                    viewModel.updateDosen(dosenToEdit);
                    Toast.makeText(getContext(), "Dosen berhasil diupdate!", Toast.LENGTH_SHORT).show();

                    // Kembalikan ke mode awal setelah sukses
                    dosenToEdit = null;
                    btnSimpan.setText("Simpan Dosen");
                }

                // CLEAR SEMUA FORM INPUT
                etNama.setText("");
                etEmail.setText("");
                etNidn.setText("");
                etMatkul.setText("");

            } catch (IllegalArgumentException e) {
                // Menangkap pesan error dari validasi Encapsulation di setter Dosen.java[cite: 14]
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Pastikan kolom Matkul diisi dengan angka bulat!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
