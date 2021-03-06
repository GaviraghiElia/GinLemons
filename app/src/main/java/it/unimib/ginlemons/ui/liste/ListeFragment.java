package it.unimib.ginlemons.ui.liste;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.FragmentListeAdapter;

public class ListeFragment extends Fragment
{
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentListeAdapter adapter;
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment (Layout della tab Mie Liste - Completate)
        View view = inflater.inflate(R.layout.fragment_liste, container, false);

        // Controllo fragment figli
        tabLayout = (TabLayout) view.findViewById(R.id.tabs_liste);
        viewPager2 = (ViewPager2) view.findViewById(R.id.view_pager2_liste);

        fm = getChildFragmentManager();
        adapter = new FragmentListeAdapter(fm, getLifecycle());
        viewPager2.setAdapter(adapter);

        // Listener per le interazioni con la Tab (Liste - Completate)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
                viewPager2.setCurrentItem(tab.getPosition());
            }
        });

        // Cattura l'evento del cambio tab (tap una sezione)
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return view;
    }
}