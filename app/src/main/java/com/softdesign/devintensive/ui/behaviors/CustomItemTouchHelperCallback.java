package com.softdesign.devintensive.ui.behaviors;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class CustomItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public interface ItemTouchHelperAdapter {
        /**
         * Called when an item has been dragged far enough to trigger a move. This is called every time
         * an item is shifted, and <strong>not</strong> at the end of a "drop" event.
         */
        boolean onItemMove(int fromPosition, int toPosition);

        /**
         * Called when an item has been dismissed by a swipe.
         */
        void onItemDismiss(int position);
    }

    private final ItemTouchHelperAdapter mAdapter;
    public static final float ALPHA_FULL = 1.0f;

    public CustomItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        // Notify the adapter of the move
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Notify the adapter of the dismissal
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
}
