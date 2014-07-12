package fr.northborders.AnimationPlayground.FragmentAnimation.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.northborders.AnimationPlayground.FragmentAnimation.Listeners.Fragment1TouchListener;
import fr.northborders.AnimationPlayground.R;

/**
 * Created by thibaultguegan on 12/07/2014.
 */
public class Fragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container,  false);

        return view;
    }

    @Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState)
    {

    }
}
