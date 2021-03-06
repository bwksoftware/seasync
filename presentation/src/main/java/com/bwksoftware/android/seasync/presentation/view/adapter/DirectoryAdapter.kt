/*
 *    Copyright 2018 BWK Technik GbR
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.bwksoftware.android.seasync.presentation.view.adapter

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bwksoftware.android.seasync.data.utils.FileUtils
import com.bwksoftware.android.seasync.presentation.R
import com.bwksoftware.android.seasync.presentation.model.DirectoryItem
import com.bwksoftware.android.seasync.presentation.model.FileItem
import com.bwksoftware.android.seasync.presentation.model.Item
import com.bwksoftware.android.seasync.presentation.model.Item.Companion.TYPE_DIRECTORY
import com.bwksoftware.android.seasync.presentation.model.Item.Companion.TYPE_FILE
import com.bwksoftware.android.seasync.presentation.utils.IconUtils
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener


class DirectoryAdapter(val onItemClickLister: OnItemClickListener,
                       var isGridView: Boolean,
                       val address: String,
                       val repoId: String, val directory: String, val account: String,
                       val token: String,
                       val context: Context) : Adapter<RecyclerView.ViewHolder>() {

    private val mItems: ArrayList<Item> = ArrayList()

    fun setItems(newItems: List<Item>) {
        mItems.clear()
        mItems.addAll(newItems)
    }

    fun setItem(position: Int, item: Item) {
        mItems[position] = item
    }

    fun getItem(position: Int): Item = mItems[position]

    override fun getItemViewType(position: Int): Int {
        return mItems[position].type
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            TYPE_FILE -> {
                view = LayoutInflater.from(parent?.context).inflate(getFileLayout(isGridView),
                        parent, false)
                FileHolder(view)
            }
            TYPE_DIRECTORY -> {
                view = LayoutInflater.from(parent?.context).inflate(getDirectoryLayout(isGridView),
                        parent, false)
                DirectoryHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent?.context).inflate(getDirectoryLayout(isGridView),
                        parent, false)
                DirectoryHolder(view)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mItems[position]

        if (holder is FileHolder && item is FileItem) {
            holder.syncedImg.visibility = if (item.synced) View.VISIBLE else View.GONE
            holder.cachedImg.visibility = if (item.isCached) View.VISIBLE else View.GONE

            holder.itemName.text = item.name
            holder.itemDateSize.text = FileUtils.readableFileSize(
                    item.size!!) + ", " + FileUtils.translateCommitTime(item.mtime!! * 1000,
                    context)
            if (FileUtils.isViewableImage(item.name!!)) {
                holder.itemName.visibility = if (isGridView) View.GONE else View.VISIBLE

                val url = FileUtils.getThumbnailUrl(address, repoId, item.name, item.storage,
                        directory, account,
                        100)
                ImageLoader.getInstance().displayImage(url, holder.itemImg,
                        getDisplayImageOptions.build(),
                        object : ImageLoadingListener {
                            override fun onLoadingComplete(imageUri: String?, view: View?,
                                                           loadedImage: Bitmap?) {
                            }

                            override fun onLoadingStarted(imageUri: String?, view: View?) {}

                            override fun onLoadingCancelled(imageUri: String?, view: View?) {}

                            override fun onLoadingFailed(imageUri: String?, view: View?,
                                                         failReason: FailReason?) {
                                ImageLoader.getInstance().displayImage(url, holder.itemImg,
                                        getDisplayImageOptions.delayBeforeLoading(1000).build())
                            }
                        }
                )

            } else {
                val iconRes = IconUtils.getIconFromName(context, item.name)
                holder.itemImg.setImageResource(iconRes)
                holder.itemName.visibility = View.VISIBLE

            }

        } else if (holder is DirectoryHolder && item is DirectoryItem) {
            holder.syncedImg.visibility = if (item.synced) View.VISIBLE else View.GONE

            holder.itemImg.setImageResource(R.drawable.folder)
            holder.itemName.text = item.name
            holder.itemDateMod.text = FileUtils.translateCommitTime(item.mtime!! * 1000, context)
        }

        //holder.repoImg.setImageDrawable(context.getDrawable(item.drawable!!))

    }

    inner class FileHolder(itemView: View) : RecyclerView.ViewHolder(
            itemView), View.OnClickListener, View.OnLongClickListener {



        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val item: Item = mItems[layoutPosition]
            onItemClickLister.onFileClicked(item, layoutPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            val item: Item = mItems[layoutPosition]
            onItemClickLister.onFileLongClicked(item, layoutPosition)
            return true
        }

        val syncedImg: ImageView = itemView.findViewById(R.id.synced_img)
        val cachedImg: ImageView = itemView.findViewById(R.id.cached_img)

        val itemImg: ImageView = itemView.findViewById(R.id.file_img)
        val itemName: TextView = itemView.findViewById(R.id.file_name)
        val itemDateSize: TextView = itemView.findViewById(R.id.file_datesize)

    }

    inner class DirectoryHolder(itemView: View) : RecyclerView.ViewHolder(
            itemView), View.OnClickListener, View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val item: Item = mItems[layoutPosition]
            onItemClickLister.onDirectoryClicked(item, layoutPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            val item: Item = mItems[layoutPosition]
            onItemClickLister.onDirectoryLongClicked(item, layoutPosition)
            return true
        }

        val itemImg: ImageView = itemView.findViewById(R.id.directory_img)
        val itemName: TextView = itemView.findViewById(R.id.directory_name)
        val syncedImg: ImageView = itemView.findViewById(R.id.synced_img)
        val itemDateMod: TextView = itemView.findViewById(R.id.directory_datemodified)
    }

    interface OnItemClickListener {
        fun onDirectoryClicked(item: Item, position: Int)
        fun onDirectoryLongClicked(item: Item, position: Int)
        fun onFileClicked(item: Item, position: Int)
        fun onFileLongClicked(item: Item, position: Int)

    }


    companion object {
        private val GRID_LAYOUT_FILE = R.layout.file_item_grid
        private val LIST_LAYOUT_FILE = R.layout.file_item
        private val GRID_LAYOUT_DIRECTORY = R.layout.directory_item_grid
        private val LIST_LAYOUT_DIRECTORY = R.layout.directory_item

        fun getFileLayout(isGridView: Boolean): Int {
            return if (isGridView) GRID_LAYOUT_FILE else LIST_LAYOUT_FILE
        }

        fun getDirectoryLayout(isGridView: Boolean): Int {
            return if (isGridView) GRID_LAYOUT_DIRECTORY else LIST_LAYOUT_DIRECTORY
        }
    }

    private var getDisplayImageOptions: DisplayImageOptions.Builder =
            DisplayImageOptions.Builder()
                    .extraForDownloader(token)
                    .delayBeforeLoading(50)
                    .resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.empty_profile)
                    .showImageOnFail(R.drawable.empty_profile)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .considerExifParams(true)


}