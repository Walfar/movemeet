package com.sdp.movemeet.view.chat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This class is used to automatically scroll to the bottom of the view of the {@link ChatActivity}
 * when new messages are added.
 */
public class MyScrollToBottomObserver extends RecyclerView.AdapterDataObserver {

    private final RecyclerView recyclerViiew;
    private final RecyclerView.Adapter<?> adapter;
    private final LinearLayoutManager manager;

    public MyScrollToBottomObserver(
            RecyclerView recycler,
            RecyclerView.Adapter<?> adapter,
            LinearLayoutManager manager) {
        this.recyclerViiew = recycler;
        this.adapter = adapter;
        this.manager = manager;
    }

    /**
     * Scroll down to the position of the last added message
     *
     * @param positionStart Position of the last added message
     * @param itemCount     Number of messages
     */
    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        int count = adapter.getItemCount();
        int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
        // If the recycler view is initially being loaded or the
        // user is at the bottom of the list, scroll to the bottom
        // of the list to show the newly added message.

        boolean loading = lastVisiblePosition == -1;
        boolean atBottom = positionStart >= (count - 1) && lastVisiblePosition == (positionStart - 1);
        if (loading || atBottom) {
            recyclerViiew.scrollToPosition(positionStart);
        }
    }

}
