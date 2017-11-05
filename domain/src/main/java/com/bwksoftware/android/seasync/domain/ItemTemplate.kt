package com.bwksoftware.android.seasync.domain

/**
 * Created by anselm.binninger on 12/10/2017.
 */
class ItemTemplate(val id: String?, val type: String?, val name: String?, val mtime: Long?,
                   val size: Long, val storage: String, val synced: Boolean, val isCached: Boolean,
                   val isRootSync: Boolean)