package com.ffyeahh.glidethumbnailplaceholdertest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

public class MainActivity extends AppCompatActivity {

    public static final String urlOriginal = "http://ffyeahh.com/examples/images/test-large.jpg";
    public static final String urlThumb = "http://ffyeahh.com/examples/images/test-thumb.jpg";
    private RequestManager glideRequest;
    private boolean isLoadingImage;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGlideRequestBuilder();

        image = (ImageView) findViewById(R.id.image);
        loadImageWithBlurredThumbnail(image, urlThumb, urlOriginal);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoadingImage) {
                    loadImageWithBlurredThumbnail(image, urlThumb, urlOriginal);
                }
            }
        });
    }

    private void initGlideRequestBuilder(){
        glideRequest = Glide.with(this);
    }

    private void loadImageWithBlurredThumbnail(ImageView imageView, String thumbUrl, String originalUrl){
        isLoadingImage = true;
        glideRequest.load(originalUrl)
                .asBitmap()
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .thumbnail(Glide.with(this)
                                .load(thumbUrl)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .transform( new BlurTransformation(this, 4), new CenterCrop(this), new GrayscaleTransformation(this))
                )
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            Log.e("Glide", "Unknown Exception");

                        }
                        Toast.makeText(MainActivity.this, "Failed Loading Image, Click to refresh", Toast.LENGTH_LONG).show();
                        isLoadingImage = false;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.e("Glide", "Loading Finished");
                        Toast.makeText(MainActivity.this, "Loading Finished", Toast.LENGTH_LONG).show();
                        isLoadingImage = false;
                        return false;
                    }
                }).into(imageView);
    }
}
