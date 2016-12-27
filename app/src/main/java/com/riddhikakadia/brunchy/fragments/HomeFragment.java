package com.riddhikakadia.brunchy.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.riddhikakadia.brunchy.adapter.HomeListAdapter;
import com.riddhikakadia.brunchy.R;
import com.riddhikakadia.brunchy.ui.RecipesListActivity;
import com.riddhikakadia.brunchy.util.RecipesInfo;

public class HomeFragment extends Fragment {

    final String LOG_TAG = HomeFragment.class.getSimpleName();
    final String RECIPE_TO_SEARCH = "RECIPE_TO_SEARCH";

    private OnFragmentInteractionListener mListener;

    ListView homeListView;
    HomeListAdapter homeListAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeListView = (ListView) rootView.findViewById(R.id.home_list_view);
        homeListView.setDivider(null);
        homeListView.setDividerHeight(0);
        homeListAdapter = new HomeListAdapter(getActivity(), RecipesInfo.homeRecipeLabels, RecipesInfo.homeRecipeImages);
        homeListView.setAdapter(homeListAdapter);

        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getContext(), RecipesListActivity.class);
                Log.d(LOG_TAG, RECIPE_TO_SEARCH + " " + RecipesInfo.homeRecipeLabels[position]);
                intent.putExtra(RECIPE_TO_SEARCH, RecipesInfo.homeRecipeLabels[position]);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
