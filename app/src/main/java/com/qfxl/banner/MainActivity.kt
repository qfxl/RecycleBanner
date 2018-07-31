package com.qfxl.banner

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.leochuan.GalleryLayoutManager
import com.leochuan.RotateLayoutManager
import com.leochuan.ScaleLayoutManager
import com.qfxl.view.*

class MainActivity : AppCompatActivity() {
    lateinit var banner: Banner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        banner = findViewById(R.id.banner)
        banner.imageLoader = object : IBannerImageLoader {
            override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                Glide.with(context).load(path).into(imageView)
            }
        }
        banner.setBannerClickListener { position, _ ->
            toast("click position = $position")
        }

        banner.urlList = arrayListOf("https://i0.hdslb.com/bfs/archive/9fff0328cbfec3e16a8de0af570399e4870f0fa8.jpg", "https://i0.hdslb.com/bfs/archive/b8efabf3031888db680f39dda98a41a7f987e4a9.jpg"
                , "https://i0.hdslb.com/bfs/archive/a6be3a5d4a4adc4193a09b748d2d8266c56b118b.jpg", "https://i0.hdslb.com/bfs/sycp/tmaterial/201807/26c8f387a0777961a9ed804bd88accb5.png")

        val bannerVertical = findViewById<Banner>(R.id.banner_vertical)
        bannerVertical.imageLoader = object : IBannerImageLoader {
            override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                Glide.with(context).load(path).into(imageView)
            }
        }
        bannerVertical.setBannerClickListener { position, _ ->
            toast("click position = $position")
        }

        bannerVertical.urlList = arrayListOf("https://i0.hdslb.com/bfs/archive/9fff0328cbfec3e16a8de0af570399e4870f0fa8.jpg", "https://i0.hdslb.com/bfs/archive/b8efabf3031888db680f39dda98a41a7f987e4a9.jpg"
                , "https://i0.hdslb.com/bfs/archive/a6be3a5d4a4adc4193a09b748d2d8266c56b118b.jpg", "https://i0.hdslb.com/bfs/sycp/tmaterial/201807/26c8f387a0777961a9ed804bd88accb5.png")
    }

}

