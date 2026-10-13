package com.example.mykampuslite.adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.mykampuslite.ui.DosenFragment;
import com.example.mykampuslite.ui.MahasiswaFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Posisi 0 untuk Tab Mahasiswa, Posisi 1 untuk Tab Dosen
        if (position == 1) {
            return new DosenFragment();
        }
        return new MahasiswaFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // Hanya ada 2 tab
    }
}
