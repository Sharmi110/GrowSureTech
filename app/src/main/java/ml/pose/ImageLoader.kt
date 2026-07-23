package ml.pose

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.growsuretech.R

class ImageLoader {

    fun loadSampleImage(
        context: Context
    ): Bitmap {

        return BitmapFactory.decodeResource(
            context.resources,
            R.drawable.sample_child
        )
    }
}