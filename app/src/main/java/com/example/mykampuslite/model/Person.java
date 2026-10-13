package com.example.mykampuslite.model;

public abstract class Person extends BaseEntity {
    private String nama;
    private String email;

    // Method abstract untuk dioverride oleh class child
    public abstract String getRole();

    public String getNama() { return nama; }

    // Validasi Encapsulation: Nama tidak boleh kosong & min 3 karakter
    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty() || nama.length() < 3) {
            throw new IllegalArgumentException("Nama tidak boleh kosong dan minimal 3 karakter");
        }
        this.nama = nama;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

}