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
import com.jscheng.srich.model.OutLine;
import com.jscheng.srich.uitl.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class OutLinesRecyclerViewAdapter extends RecyclerView.Adapter {
    private final static String TAG = "Adapter";
    private final static int TYPE_ITEM_DATE_BAR = 1;
    private final static int TYPE_ITEM_NOTE = 2;

    private LayoutInflater mLayoutInfater = null;
    private List<Note> mNotes = null;
    private List<OutLine> mOutlines = null;
    private LinearLayoutManager mLayoutManager = null;

    public OutLinesRecyclerViewAdapter(Context context, LinearLayoutManager layoutManager) {
        mLayoutInfater = LayoutInflater.from(context);
        mLayoutManager = layoutManager;
        mNotes = new ArrayList();
    }

    public void setData(List<Note> notes) {
        if (notes != null && notes.size() > 0) {
            this.mNotes = notes;
            this.mOutlines = OutLineFactory.build(mNotes);
        }
        notifyDataSetChanged();// 后续用DiffUtil优化
    }

    public List<Note> getData() {
        return mNotes;
    }

    public long getFirstVisibleDateTime() {
        int position = mLayoutManager.findFirstVisibleItemPosition();
        if (position >= 0) {
            OutLine outLine = mOutlines.get(position);
            if (outLine.getType() == OutLine.Type.Date) {
                return outLine.getTime();
            } else {
                return outLine.getNote().getModifyTime();
            }
        }
        return 0;
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
        OutLine outline = mOutlines.get(position);
        if (viewType == TYPE_ITEM_DATE_BAR) {
            ItemDateViewHolder dateViewHolder = (ItemDateViewHolder) viewHolder;
            bindDateViewHlder(dateViewHolder, outline);
        } else {
            ItemNoteViewHolder noteViewHolder = (ItemNoteViewHolder) viewHolder;
            bindNoteViewHolder(noteViewHolder, outline);
        }
    }

    private void bindDateViewHlder(ItemDateViewHolder dateViewHolder, OutLine outline) {
        String date = DateUtil.formatDate(outline.getTime());
        dateViewHolder.dateTextView.setText(date);
    }

    private void bindNoteViewHolder(ItemNoteViewHolder noteViewHolder, OutLine outline) {
        String title = outline.getNote().getTitle();
        noteViewHolder.titleText.setText(title);
    }

    @Override
    public int getItemViewType(int position) {
        return mOutlines.get(position).getType() == OutLine.Type.Date ? TYPE_ITEM_DATE_BAR : TYPE_ITEM_NOTE;
    }

    @Override
    public int getItemCount() {
        return mOutlines.size();
    }

    private class ItemDateViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public ItemDateViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text);
        }
    }

    private class ItemNoteViewHolder extends RecyclerView.ViewHolder {
        public TextView weekText;
        public TextView titleText;
        public TextView summaryText;
        public ImageView summaryImage;

        public ItemNoteViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_text);
        }
    }
}
