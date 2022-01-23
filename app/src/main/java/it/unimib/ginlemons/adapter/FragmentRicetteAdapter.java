package it.unimib.ginlemons.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import it.unimib.ginlemons.ui.ricette.RicetteDiscoverFragment;
import it.unimib.ginlemons.ui.ricette.RicettePreferitiFragment;

public class FragmentRicetteAdapter extends FragmentStateAdapter
{

    public FragmentRicetteAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle)
    {
        super(fragmentManager, lifecycle);
    }

    // Associa il fragment da inserire nel viewholder in base alla sezione selezionata nella tab (Esplora - Preferiti)
    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        if (position == 1)
            return new RicettePreferitiFragment();

        return new RicetteDiscoverFragment();
    }

    @Override
    public int getItemCount()
    {
        return 2;
    }
}
