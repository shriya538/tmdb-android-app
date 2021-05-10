package com.example.uscfilms.ui.watchlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscfilms.R;

public class WatchlistFragment extends Fragment {

    private WatchlistViewModel notificationsViewModel;
    private View root;
    private SharedPreferences sharedPref;
    private RecyclerView watchlistRV;
    private WatchlistItemRecyclerView watchlistRVAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_watchlist, container, false);
        sharedPref = getActivity().getSharedPreferences("watchlist_shared_pref", Context.MODE_PRIVATE);
        watchlistRV = root.findViewById(R.id.watchlist_recycler_view);
        renderWatchList();
        attachDragDropFunctionality();
        return root;
    }

    public void attachDragDropFunctionality(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.START | ItemTouchHelper.END, 0
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                watchlistRVAdapter.swapItems(fromPosition, toPosition);
                watchlistRVAdapter.notifyItemRangeChanged(Math.min(fromPosition, toPosition), Math.abs(fromPosition - toPosition) + 1);
                watchlistRVAdapter.notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(watchlistRV);

    }

    public void renderWatchList(){
        watchlistRVAdapter = new WatchlistItemRecyclerView(root, root.getContext(), sharedPref);
        watchlistRV.setAdapter(watchlistRVAdapter);
        watchlistRV.setLayoutManager(new GridLayoutManager(root.getContext(), 3));
    }
}