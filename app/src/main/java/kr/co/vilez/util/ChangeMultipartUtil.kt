package kr.co.vilez.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


class ChangeMultipartUtil {
    fun changeAbsolutelyPath(path: Uri?, context: Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)
        return result!!
    }

    fun changeUriPath(path: String, context: Context) : Uri {
        val photoId: Long
        val photoUri = MediaStore.Images.Media.getContentUri("external")
        val projection = arrayOf(MediaStore.Images.ImageColumns._ID)
        val cursor: Cursor? = context.contentResolver.query(
            photoUri,
            projection,
            MediaStore.Images.ImageColumns.DATA + " LIKE ?",
            arrayOf<String>(path),
            null
        )
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
        photoId = cursor?.getLong(columnIndex!!)!!

        cursor.close()
        return Uri.parse("$photoUri/$photoId")
    }
}