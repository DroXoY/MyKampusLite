package com.example.mykampuslite.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mykampuslite.R;
import com.example.mykampuslite.model.Dosen;
import com.example.mykampuslite.model.EvaluasiAkademik;
import com.example.mykampuslite.model.Mahasiswa;
import com.example.mykampuslite.model.Person;
import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    // List menampung tipe 'Person' (bisa Mahasiswa atau Dosen)
    private final List<Person> personList = new ArrayList<>();

    public void submitList(List<? extends Person> list) {
        personList.clear();
        personList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = personList.get(position);

        // Polymorphism: getRole() akan otomatis menyesuaikan objek aslinya[cite: 14]
        holder.tvNamaRole.setText(person.getNama() + " - " + person.getRole());

        if (person instanceof Mahasiswa) {
            holder.tvId.setText("NIM: " + ((Mahasiswa) person).getNim());
        } else if (person instanceof Dosen) {
            holder.tvId.setText("NIDN: " + ((Dosen) person).getNidn());
        }

        // Polymorphism: Memanggil metode dari Interface EvaluasiAkademik[cite: 14]
        if (person instanceof EvaluasiAkademik) {
            holder.tvStatusAkademik.setText("Status: " + ((EvaluasiAkademik) person).getStatusAkademik());
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(person);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(person);
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaRole, tvId, tvStatusAkademik;
        Button btnEdit, btnDelete;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaRole = itemView.findViewById(R.id.tvNamaRole);
            tvId = itemView.findViewById(R.id.tvId);
            tvStatusAkademik = itemView.findViewById(R.id.tvStatusAkademik);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    // 1. Buat interface callback di dalam PersonAdapter
    public interface OnItemClickListener {
        void onEditClick(Person person);
        void onDeleteClick(Person person);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
