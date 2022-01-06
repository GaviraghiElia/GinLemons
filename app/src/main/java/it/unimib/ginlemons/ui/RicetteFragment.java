package it.unimib.ginlemons.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.FragmentRicetteAdapter;
import it.unimib.ginlemons.databinding.FragmentRicetteBinding;

public class RicetteFragment extends Fragment {

    private FragmentRicetteAdapter adapter;
    private FragmentManager fm;
    private FragmentRicetteBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment (layout fragment toolbar Discover - Preferiti)
        mBinding = FragmentRicetteBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        // controllo fragment figli
        fm = getChildFragmentManager();
        adapter = new FragmentRicetteAdapter(fm, getLifecycle());
        mBinding.viewPager2Ricette.setAdapter(adapter);

        // Listener per le interazioni con la tab
        mBinding.tabsRicette.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewPager2Ricette.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Cattura l'evento generato dallo swipe left/right tra tab
        mBinding.viewPager2Ricette.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mBinding.tabsRicette.selectTab(mBinding.tabsRicette.getTabAt(position));
            }
        });

        return view;
    }
}