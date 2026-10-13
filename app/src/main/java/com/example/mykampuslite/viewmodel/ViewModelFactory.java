package com.example.mykampuslite.viewmodel;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.mykampuslite.data.AkademikRepository;

// Class ini memastikan kita bisa mengirim Repository ke dalam ViewModel dengan aman
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final AkademikRepository repository;

    public ViewModelFactory(AkademikRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AkademikViewModel.class)) {
            return (T) new AkademikViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
