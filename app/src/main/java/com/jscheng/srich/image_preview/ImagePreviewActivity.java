package com.jscheng.srich.image_preview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.jscheng.annotations.Route;
import com.jscheng.srich.BaseActivity;
import com.jscheng.srich.R;
import com.jscheng.srich.image_loader.ImageLoader;
import com.jscheng.srich.widget.PinchImageView;

import java.util.List;

/**
 * 图片预览
 * Created By Chengjunsen on 2019/3/15
 */
@Route("imagePreview")
public class ImagePreviewActivity extends BaseActivity {

    PinchImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getColor(R.color.image_preview_background_color));

        mImageView = findViewById(R.id.preview_imageview);
        mImageView.setImageResource(R.mipmap.ic_note_edit_loading);
        loadImage();
    }

    private void loadImage() {
        Intent intent = getIntent();
        List<String> urls = intent.getStringArrayListExtra("urls");
        int index = intent.getIntExtra("index", 0);
       if (urls.isEmpty() || index >= urls.size()) {
           return;
       }
       String url = urls.get(index);
       ImageLoader.with(this).load(url, mImageView);
    }
}
