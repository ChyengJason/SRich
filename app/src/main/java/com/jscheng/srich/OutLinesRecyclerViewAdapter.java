package com.jscheng.srich;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jscheng.srich.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class OutLinesRecyclerViewAdapter extends RecyclerView.Adapter {
    private final static String TAG = "Adapter";
    private final static int TYPE_ITEM_DATE_BAR = 1;
    private final static int TYPE_ITEM_NOTE = 2;
    private final static int ITEM_DATE_BAR_COUNT = 1;

    private LayoutInflater mLayoutInfater = null;
    private List<Note> mNotes = null;
    private LinearLayoutManager mLayoutManager = null;

    public OutLinesRecyclerViewAdapter(Context context, LinearLayoutManager layoutManager) {
        mLayoutInfater = LayoutInflater.from(context);
        mLayoutManager = layoutManager;
        mNotes = new ArrayList();
    }

    public void setData(List<Note> notes) {
        if (notes != null && notes.size() > 0) {
            this.mNotes = notes;
        }
        notifyDataSetChanged();// 后续用DiffUtil优化
    }

    public List<Note> getData() {
        return mNotes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        RecyclerView.ViewHolder itemViewHolder = null;
        if (type == TYPE_ITEM_DATE_BAR) {
            View itemView = mLayoutInfater.inflate(R.layout.outline_item_view_date, parent, false);
            itemViewHolder = new ItemDateViewHolder(itemView);
        } else {
            View itemView = mLayoutInfater.inflate(R.layout.outline_item_view_note, parent, false);
            itemViewHolder = new ItemNoteViewHolder(itemView);
        }
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_ITEM_DATE_BAR) {
            ItemDateViewHolder dateViewHolder = (ItemDateViewHolder)viewHolder;
            Note note = getData(1);
            bindDateView(dateViewHolder, note);
        } else {
            ItemNoteViewHolder noteViewHolder = (ItemNoteViewHolder)viewHolder;
            Note note = getData(position);
            bindNoteView(noteViewHolder, note);
        }
    }

    private void bindDateView(ItemDateViewHolder dateViewHolder, Note note) {
        if (note != null) {
            dateViewHolder.dateTextView.setText("1 : 2");
        }
    }

    private void bindNoteView(ItemNoteViewHolder noteViewHolder, Note note) {
        if (note != null) {
            noteViewHolder.titleText.setText(note.getTitle());
        }
    }

    public Note getFirstVisibleData() {
        int firstVisiblePosition =  mLayoutManager.findFirstVisibleItemPosition();
        Log.e(TAG, "getFirstVisiableData: " + firstVisiblePosition);
        if (firstVisiblePosition <= 0 ) { // first time show
            return null;
        }
        return getData(firstVisiblePosition);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_ITEM_DATE_BAR : TYPE_ITEM_NOTE;
    }

    @Override
    public int getItemCount() {
        if (!mNotes.isEmpty()) {
            return ITEM_DATE_BAR_COUNT + mNotes.size();
        }
        return 0;
    }

    private Note getData(int position) {
        int index = position - ITEM_DATE_BAR_COUNT;
        if (index >= 0 && index < mNotes.size()) {
            return mNotes.get(index);
        }
        return null;
    }

    private class ItemDateViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public ItemDateViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text);
        }
    }

    private class ItemNoteViewHolder extends RecyclerView.ViewHolder {
//        public TextView weekText;
        public TextView titleText;
//        public TextView summaryText;
//        public ImageView summaryImage;

        public ItemNoteViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_text);
        }
    }
}
