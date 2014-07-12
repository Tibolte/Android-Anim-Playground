package fr.northborders.AnimationPlayground.FragmentAnimation.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import fr.northborders.AnimationPlayground.FragmentAnimation.Listeners.Fragment1TouchListener;
import fr.northborders.AnimationPlayground.FragmentAnimation.Listeners.Fragment2TouchListener;
import fr.northborders.AnimationPlayground.R;

/**
 * Created by thibaultguegan on 12/07/2014.
 */
public class BaseFragment extends Fragment {

    private Fragment1TouchListener fragment1TouchListener;
    private Fragment2TouchListener fragment2TouchListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment, container,  false);

        return view;
    }

    @Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState)
    {
        final RelativeLayout fragment1Container = (RelativeLayout) view.findViewById(R.id.selectFragment1Container);
        final RelativeLayout fragment2Container = (RelativeLayout) view.findViewById(R.id.selectFragment2Container);

        fragment1Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment1TouchListener.onFragment1Touch(getView(), fragment1Container, true);
            }
        });

        fragment2Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment2TouchListener.onFragment2Touch(getView(), fragment2Container, true);
            }
        });
    }

    public void setFragment2TouchListener(Fragment2TouchListener fragment2TouchListener) {
        this.fragment2TouchListener = fragment2TouchListener;
    }

    public void setFragment1TouchListener(Fragment1TouchListener fragment1TouchListener) {
        this.fragment1TouchListener = fragment1TouchListener;
    }
}
