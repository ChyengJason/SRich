package com.jscheng.srich;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jscheng.srich.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class OutLinesRecyclerViewAdapter extends RecyclerView.Adapter {

    private final static int TYPE_ITEM_DATE_BAR = 1;
    private final static int TYPE_ITEM_NOTE = 2;
    private final static int ITEM_DATE_BAR_COUNT = 1;

    private LayoutInflater mLayoutInfater = null;
    private List<Note> mNotes = null;

    public OutLinesRecyclerViewAdapter(Context context) {
        mLayoutInfater = LayoutInflater.from(context);
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_ITEM_DATE_BAR : TYPE_ITEM_NOTE;
    }

    @Override
    public int getItemCount() {
        return ITEM_DATE_BAR_COUNT + mNotes.size();
    }

    private Note getData(int position) {
        int index = position - ITEM_DATE_BAR_COUNT;
        if (index >= 0 && index < mNotes.size()) {
            return mNotes.get(index);
        }
        return null;
    }

    private class ItemDateViewHolder extends RecyclerView.ViewHolder {

        public ItemDateViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
