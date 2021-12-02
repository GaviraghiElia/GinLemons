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

public class RicetteFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentRicetteAdapter adapter;
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment (layout fragment toolbar Discover - Preferiti)
        View view = inflater.inflate(R.layout.fragment_ricette, container, false);

        // controllo fragment figli
        tabLayout = (TabLayout) view.findViewById(R.id.tabs_ricette);
        viewPager2 = (ViewPager2) view.findViewById(R.id.view_pager2_ricette);

        fm = getChildFragmentManager();
        adapter = new FragmentRicetteAdapter(fm, getLifecycle());
        viewPager2.setAdapter(adapter);

        // Listener per le interazioni con la tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Cattura l'evento generato dallo swipe left/right tra tab
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return view;
    }
}