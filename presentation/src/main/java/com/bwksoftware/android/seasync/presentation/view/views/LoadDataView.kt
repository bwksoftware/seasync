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

package com.bwksoftware.android.seasync.presentation.view.views

import android.app.Activity
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import com.bwksoftware.android.seasync.presentation.R

/**
 * Created by ansel on 10/11/2017.
 */
interface LoadDataView {
    fun activity(): Activity
    fun showNoInternet() {
        if (activity() != null) {
            val snackbar = Snackbar.make(
                    activity().findViewById<CoordinatorLayout>(R.id.coordinator),
                    R.string.error_no_internet,
                    Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }
}