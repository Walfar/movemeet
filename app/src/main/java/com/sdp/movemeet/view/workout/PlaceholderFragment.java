package com.sdp.movemeet.view.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdp.movemeet.R;
import com.sdp.movemeet.models.Workout;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    ListView listView;
    ListViewAdapter adapter;
    String[] title;
    String[] description;
    int[] icon;
    ArrayList<Workout> arrayList = new ArrayList<Workout>();
    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workout, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrayList.clear();

        title = new String[]{"Workout #1: Abs", "Workout #2: Legs", "Workout #3: Upper Body"};
        description = new String[]{"Get in shape for the summer!", "Pump your legs!", "Build some muscle!"};
        icon = new int[]{R.drawable.ic_baseline_fitness_center_24, R.drawable.ic_baseline_fitness_center_24, R.drawable.ic_baseline_fitness_center_24};


        for (int i = 0; i < title.length; i++) {
            Workout model = new Workout(title[i], description[i], icon[i]);
            arrayList.add(model);
        }


        listView = view.findViewById(R.id.listView);

        adapter = new ListViewAdapter(getActivity(), arrayList);

        listView.setAdapter(adapter);
    }
}