package it.unimib.ginlemons.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.FragmentListeAdapter;

public class ListeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentListeAdapter adapter;
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    //@Override
    //public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    //    super.onCreateOptionsMenu(menu, inflater);
    //}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liste, container, false);


        // controllo fragment figli
        tabLayout = (TabLayout) view.findViewById(R.id.tabs_liste);
        viewPager2 = (ViewPager2) view.findViewById(R.id.view_pager2_liste);

        fm = getChildFragmentManager();
        adapter = new FragmentListeAdapter(fm, getLifecycle());
        viewPager2.setAdapter(adapter);

        // listener Tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Quando voglio cambiare tab devo catturare l'evento
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
                /* PROBLEMA
                 come ottenere il riferimento al fragment in base alla posizione (position) del tab?
                 mi servirebbe per settare la toolbar in base a dove sono, perché se clicco e torno
                  indietro non cambia più*/
            }
        });


        /* EVENTUALI ANIMAZIONI
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        viewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {

                page.setPivotX(position < 0 ? 0 : page.getWidth());
                page.setScaleX(position < 0 ? 1f + position : 1f - position);

            }
        });*/


        return view;
    }
}