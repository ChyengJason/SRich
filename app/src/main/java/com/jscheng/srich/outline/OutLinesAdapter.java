package com.jscheng.srich.outline;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jscheng.srich.R;
import com.jscheng.srich.image_loader.NoteImageListener;
import com.jscheng.srich.image_loader.NetworkImagePool;
import com.jscheng.srich.image_loader.NoteImageLoader;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.OutLine;
import com.jscheng.srich.route.Router;
import com.jscheng.srich.route.RouterConfig;
import com.jscheng.srich.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class OutLinesAdapter extends RecyclerView.Adapter implements NoteImageListener {
    private final static String TAG = "Adapter";
    private final static int TYPE_ITEM_DATE_BAR = 1;
    private final static int TYPE_ITEM_NOTE = 2;

    private LayoutInflater mLayoutInfater;
    private List<Note> mNotes;
    private List<OutLine> mOutlines;
    private LinearLayoutManager mLayoutManager;
    private Context mContext;
    private View mParentView;

    public OutLinesAdapter(View parentView, LinearLayoutManager layoutManager) {
        mParentView = parentView;
        mContext = parentView.getContext();
        mLayoutInfater = LayoutInflater.from(mContext);
        mLayoutManager = layoutManager;
        mNotes = new ArrayList();
        mOutlines = new ArrayList<>();
    }

    public void setData(List<Note> notes) {
        this.mNotes.clear();
        this.mNotes.addAll(notes);
        this.mOutlines = OutLineFactory.build(mNotes);
        notifyDataSetChanged();// 后续用DiffUtil优化
    }

    public void addData(List<Note> notes) {
        if (notes != null && notes.size() >0 ) {
            this.mNotes.addAll(notes);
            this.mOutlines.addAll(OutLineFactory.build(notes));
        }
        notifyDataSetChanged();
    }

    public List<Note> getData() {
        return mNotes;
    }

    public long getFirstVisibleDateTime() {
        int position = mLayoutManager.findFirstVisibleItemPosition();
        if (getItemCount() > 0 && position >= 0) {
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

    private void bindNoteViewHolder(ItemNoteViewHolder noteViewHolder, final OutLine outline) {
        String title = outline.getNote().getTitle();
        String summary = outline.getNote().getSummary();
        String summaryImageUrl = outline.getNote().getSummaryImageUrl();
        String date = DateUtil.formatDate(outline.getNote().getModifyTime());
        noteViewHolder.titleText.setText(title);
        noteViewHolder.weekText.setText(date);
        noteViewHolder.summaryText.setText(summary);
        if (TextUtils.isEmpty(summaryImageUrl)) {
            noteViewHolder.summaryImage.setVisibility(View.INVISIBLE);
        } else {
//            int width = noteViewHolder.summaryImage.getMeasuredWidth();
//            Bitmap bitmap = NoteImageLoader.with(mContext).getBitmap(summaryImageUrl, width);
//            if (bitmap != null) {
//                noteViewHolder.summaryImage.setVisibility(View.VISIBLE);
//                noteViewHolder.summaryImage.setImageBitmap(bitmap);
//            } else {
//                noteViewHolder.summaryImage.setVisibility(View.INVISIBLE);
//                noteViewHolder.summaryImage.setTag(summaryImageUrl);
//            }
        }
        noteViewHolder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.with(mContext)
                        .route(RouterConfig.EditNoteActivityUri)
                        .intent("id", outline.getNote().getId())
                        .go();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mOutlines.get(position).getType() == OutLine.Type.Date ? TYPE_ITEM_DATE_BAR : TYPE_ITEM_NOTE;
    }

    @Override
    public int getItemCount() {
        return mOutlines.size();
    }

    @Override
    public void onNoteImageSuccess(String url) {
        ImageView summaryImageView = mParentView.findViewWithTag(url);
        if (summaryImageView != null) {
            Bitmap bitmap = NoteImageLoader.with(mContext).getBitmap(url, summaryImageView.getWidth());
            summaryImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onNoteImageFailed(String url, String err) {

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
        public ConstraintLayout contentLayout;

        public ItemNoteViewHolder(View itemView) {
            super(itemView);
            contentLayout = itemView.findViewById(R.id.content_layout);
            weekText = itemView.findViewById(R.id.date_text);
            titleText = itemView.findViewById(R.id.title_text);
            summaryText = itemView.findViewById(R.id.summary_text);
            summaryImage = itemView.findViewById(R.id.summary_image_view);
        }
    }
}
