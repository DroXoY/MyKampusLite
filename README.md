# Pertanyaan dan Jawaban

## Pertanyaan 1
**Apa yang terjadi jika kalian menghapus constructor kosong di Entity Room?**

### Jawaban
Room membuat objek Entity lewat kode hasil generate (`AkademikDao_Impl`). Saat membaca baris dari database, Room butuh cara untuk membuat instance. Cara umumnya adalah memanggil constructor kosong, lalu mengisi field lewat setter atau field publik.

Jika constructor kosong dihapus dan tidak ada constructor lain yang bisa dipakai Room:

- **Build gagal saat kompilasi.** Annotation processor Room melaporkan error seperti `Cannot find setter for field` atau `Entities and POJOs must have a usable public constructor`. Ini bukan crash saat runtime.
- **Room tidak bisa mengubah baris tabel menjadi objek** `Mahasiswa` atau `Dosen`. Akibatnya query yang mengembalikan Entity (`getAll()`, `getAllDosen()`) tidak bisa di-generate.

Ada dua cara memperbaikinya:

- Menyediakan kembali constructor kosong, dan menandai constructor lain yang berparameter dengan `@Ignore`.
- Membuat constructor berparameter yang nama dan tipe parameternya sama persis dengan field kolom, sehingga Room bisa memakainya.

Di proyek ini hal itu relevan karena `Person.setNama()` melempar `IllegalArgumentException` jika nama kurang dari 3 karakter. Room harus mengisi data lewat constructor dan setter yang valid.

---

## Pertanyaan 2
**Mengapa ViewModel tidak boleh meng-import `android.widget.TextView`? Hubungkan dengan prinsip Abstraction.**

### Jawaban
Ada tiga alasan:

1. **Memory leak.** `TextView` memegang referensi ke `Context` Activity atau Fragment. ViewModel hidup lebih lama daripada UI karena ia bertahan saat rotasi layar. Jika ViewModel menyimpan `TextView`, Activity lama tidak bisa di-garbage-collect.
2. **Tanggung jawab tercampur.** ViewModel mengurus data dan logika presentasi. Memanipulasi view (`setText`) adalah tugas UI (Fragment atau Adapter). Mencampurnya melanggar Single Responsibility.
3. **Sulit diuji.** ViewModel yang bergantung pada `android.widget` tidak bisa di-unit-test dengan JUnit biasa, karena butuh framework Android atau emulator.

**Hubungannya dengan Abstraction.** Abstraction berarti menyembunyikan detail dan hanya mengekspos hal yang penting. Di `AkademikViewModel`:

- ViewModel hanya mengekspos `LiveData<List<Mahasiswa>>` dan method `insertMahasiswa()`. Ia tidak tahu data ditampilkan di `TextView`, `RecyclerView`, atau Compose.
- ViewModel juga tidak tahu data disimpan lewat Room, API, atau yang lain. Ia hanya bergantung pada `AkademikRepository`.
- UI cukup meng-observe `LiveData` dan memutuskan sendiri cara menampilkannya.

Jika ViewModel meng-import `TextView`, ia mengetahui detail konkret lapisan UI, dan lapisan abstraksi antara data dan tampilan hilang.

---

## Pertanyaan 3
**Berikan 1 contoh nyata di studi kasus ini di mana Polymorphism menghemat baris kode kalian.**

### Jawaban
Contohnya ada di `PersonAdapter.java`. Adapter hanya memakai satu `List<Person>` untuk `Mahasiswa` dan `Dosen`:

```java
private final List<Person> personList = new ArrayList<>();

public void submitList(List<? extends Person> list) { ... }

@Override
public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
    Person person = personList.get(position);
    holder.tvNamaRole.setText(person.getNama() + " - " + person.getRole());
    ...
}
```

`person.getRole()` adalah method abstract di `Person`. Saat dijalankan, Java memanggil versi milik `Mahasiswa` atau `Dosen` sesuai objek aslinya. Karena itu:

- **Tidak perlu dua adapter.** Tanpa polymorphism kita butuh `MahasiswaAdapter` dan `DosenAdapter`, lengkap dengan dua ViewHolder dan dua layout. Sekarang cukup satu `PersonAdapter` dan satu `item_person.xml`. Itu menghemat sekitar 40 baris atau lebih.
- **Tidak perlu percabangan untuk role.** Kode seperti `if (mahasiswa) "Mahasiswa" else "Dosen"` tidak diperlukan, karena cukup `person.getRole()`.
- **Mudah ditambah.** Jika nanti ada `Staff extends Person`, `onBindViewHolder` tidak perlu diubah.

Untuk `getStatusAkademik()`, interface `EvaluasiAkademik` berperan serupa. Adapter cukup memanggil `((EvaluasiAkademik) person).getStatusAkademik()` tanpa peduli kelas konkretnya.
