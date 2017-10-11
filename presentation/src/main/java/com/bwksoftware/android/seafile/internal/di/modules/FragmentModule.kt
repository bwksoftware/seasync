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

package com.bwksoftware.android.seafile.internal.di.modules

import android.support.v4.app.Fragment
import com.bwksoftware.android.seafile.internal.di.scope.PerFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val baseFragment: Fragment) {

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @PerFragment
    fun provideFragment(): Fragment {
        return this.baseFragment
    }
}