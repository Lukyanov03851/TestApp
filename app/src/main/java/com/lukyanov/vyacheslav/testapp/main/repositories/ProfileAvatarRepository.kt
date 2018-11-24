package com.lukyanov.vyacheslav.testapp.main.repositories

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import com.lukyanov.vyacheslav.testapp.App
import com.lukyanov.vyacheslav.testapp.R
import com.lukyanov.vyacheslav.testapp.util.PrefUtil
import com.lukyanov.vyacheslav.testapp.interfaces.LoadProfileAvatarCallback
import com.lukyanov.vyacheslav.testapp.util.isConnectedToInternet
import java.io.IOException
import java.net.URL

class ProfileAvatarRepository {

    fun getAvatar(callback: LoadProfileAvatarCallback) {
        UploadAvatarAsyncTask(callback).execute()
    }

    private class UploadAvatarAsyncTask internal constructor(private val callback: LoadProfileAvatarCallback) : AsyncTask<Void, Void, Drawable>() {
        override fun doInBackground(vararg params: Void): Drawable? {
            if (isConnectedToInternet()){
                try {
                    val prefUtil = PrefUtil(App.applicationContext())
                    val url = URL(prefUtil.getFacebookProfilePicture())
                    val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    val croppedBitmap = getCroppedBitmap(bitmap)
                    return BitmapDrawable(App.applicationContext().resources, croppedBitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            return App.applicationContext().resources.getDrawable(R.drawable.profile_image, App.applicationContext().theme)
        }

        override fun onPostExecute(result: Drawable?) {
            super.onPostExecute(result)
            callback.onAvatarLoaded(avatar = result)
        }

        private fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
            val output = Bitmap.createBitmap(bitmap.width,
                    bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            canvas.drawCircle((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
                    (bitmap.width / 2).toFloat(), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }
    }
}