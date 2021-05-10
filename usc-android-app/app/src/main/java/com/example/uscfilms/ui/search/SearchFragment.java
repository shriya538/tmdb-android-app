package com.example.uscfilms.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uscfilms.MySingleton;
import com.example.uscfilms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {


    private View root;
    private RecyclerView searchRV;
    private SearchAdapter searchRVAdapter;
    private ArrayList<SearchDataModel> searchData = new ArrayList<>();
    SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);
        searchRV = root.findViewById(R.id.search_recycler_view);
        searchRVAdapter = new SearchAdapter(root.getContext());
        searchRV.setAdapter(searchRVAdapter);
        searchRV.setLayoutManager(new LinearLayoutManager(root.getContext()));
        setSearchListener();
        return root;
    }

    public void setSearchListener() {
        searchView = root.findViewById(R.id.search_page_search_query_text);
//        searchView.setQueryHint("Search Movies and TV");
//        searchView.setIconified(false);
        searchView.setQueryHint("Search Movies and TV");
        searchView.setIconifiedByDefault(false);
        EditText searchEditText = searchView.findViewById(getResources().getIdentifier("android:id/search_src_text", null, null));
        searchEditText.setTextColor(getResources().getColor(R.color.white, null));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white, null));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getContext(), newText, Toast.LENGTH_SHORT).show();
                handleSearch(newText);
                return false;
            }
        });
    }

    public void handleSearch(String query) {
        if (query.length() == 0) {
            root.findViewById(R.id.no_results_found).setVisibility(View.INVISIBLE);
            createSearchData(new JSONArray());
        } else {

            JsonObjectRequest searchData = new JsonObjectRequest(
                    Request.Method.GET, getString(R.string.network_host) + "/multi_search/" + query, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (searchView.getQuery().length() > 0) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            if (data.length() == 0) {
                                createSearchData(new JSONArray());
                                root.findViewById(R.id.no_results_found).setVisibility(View.VISIBLE);
                            } else {
                                root.findViewById(R.id.no_results_found).setVisibility(View.INVISIBLE);
                                createSearchData(data);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }
            );

            MySingleton.getInstance().addToRequestQueue(searchData);

        }
    }

    public void createSearchData(JSONArray data){
        searchData = new ArrayList<>();
        for (int i=0; i< data.length(); i++){
            try {
                JSONObject currentSearchResult = data.getJSONObject(i);
                if (currentSearchResult.getString("backdrop_path").equals(getString(R.string.default_backdrop))){
                    continue;
                }
                searchData.add(new SearchDataModel(currentSearchResult));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        searchRVAdapter.setSearchData(searchData);



    }
}