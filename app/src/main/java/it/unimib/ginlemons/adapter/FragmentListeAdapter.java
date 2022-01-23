package it.unimib.ginlemons.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import it.unimib.ginlemons.ui.liste.ListeCompleteFragment;
import it.unimib.ginlemons.ui.liste.ListeHomeFragment;

public class FragmentListeAdapter extends FragmentStateAdapter
{
    public FragmentListeAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle)
    {
        super(fragmentManager, lifecycle);
    }

    // Associa il fragment da inserire nel viewholder in base alla sezione selezionata nella tab (Mie Liste - Completate)
    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        if (position == 1)
            return new ListeCompleteFragment();

        return new ListeHomeFragment();
    }

    @Override
    public int getItemCount()
    {
        return 2;
    }
}
