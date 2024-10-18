package com.zhihu.matisse.internal.loader

import android.provider.MediaStore

/**
 * 支持加载非 gif 格式的图片
 */
object LoaderHelper {

    @JvmStatic
    fun getSelection(albumId: String?): String {
        val holder = loadNoAnimImagesHolder(albumId)
        return holder.first
    }

    @JvmStatic
    fun getSelectionArgs(albumId: String?): Array<String> {
        val holder = loadNoAnimImagesHolder(albumId)
        return holder.second
    }

    @JvmStatic
    private fun loadNoAnimImagesHolder(albumId: String?): Pair<String, Array<String>> {
        return buildSelectionAndArgs(listOf("image/png", "image/jpeg", "image/x-ms-bmp", "image/webp"), albumId)
    }

    private fun buildSelectionAndArgs(mimeTypes: List<String>, albumId: String?): Pair<String, Array<String>> {
        var selection: String
        val selectionArgs: Array<String>

        if (mimeTypes.isEmpty()) {
            // 如果没有指定 MIME 类型，则返回一个始终为真的条件（例如大小大于0）
            selection = MediaStore.MediaColumns.SIZE + " > 0"
            selectionArgs = arrayOf()
        } else {
            // 构建 MIME 类型的条件
            val placeholders = mimeTypes.joinToString(", ") { "?" }
            selection =
                MediaStore.Images.Media.MIME_TYPE + " IN ($placeholders)" + " AND " + MediaStore.MediaColumns.SIZE + " > 0"

            if (albumId != null) {
                selection = "$selection AND  bucket_id=?"
                val temp = ArrayList<String>(mimeTypes)
                temp.add(albumId)
                selectionArgs = temp.toTypedArray()
            } else {
                selectionArgs = mimeTypes.toTypedArray()
            }
        }


        return Pair(selection, selectionArgs)
    }
}