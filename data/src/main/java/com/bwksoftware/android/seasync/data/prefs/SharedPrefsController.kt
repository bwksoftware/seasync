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

package com.bwksoftware.android.seasync.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.bwksoftware.android.seasync.data.BuildConfig
import javax.inject.Inject


class SharedPrefsController @Inject
constructor(val context: Context) {

    enum class Preference(internal var defaultValue: String) {
        CURRENT_USER_TOKEN(""),
        CURRENT_USER_ACCOUNT("None"),
        DISABLE_CONTROLS_FOR_BEGINNER("true"),
        GRID_VIEW_DIRECTORIES("true"),
        SYNCED_LIBRARIES(""),
        CACHE_LAST_UPDATE("0")
    }

    fun getPreference(preference: Preference): SharedPreferences? {
        return context.getSharedPreferences(
                APP_IDENTIFIER, Context.MODE_PRIVATE)
    }

    fun getPreferenceValue(preference: Preference): String {
        val sharedPref = context.getSharedPreferences(
                APP_IDENTIFIER, Context.MODE_PRIVATE)
        return sharedPref.getString(preference.toString(), preference.defaultValue)
    }

    fun getPreferenceList(preference: Preference) {
        val sharedPref = context.getSharedPreferences(
                APP_IDENTIFIER, Context.MODE_PRIVATE)
        sharedPref.getStringSet(preference.toString(), setOf(preference.defaultValue))
    }

    fun preferenceExists(preference: Preference): Boolean {
        val sharedPref = context.getSharedPreferences(
                APP_IDENTIFIER, Context.MODE_PRIVATE)
        return sharedPref.contains(preference.toString())
    }

    fun setPreference(preference: Preference, value: String) {
        val sharedPref = context.getSharedPreferences(
                APP_IDENTIFIER, Context.MODE_PRIVATE)
        sharedPref.edit().putString(preference.toString(), value).apply()
    }

    companion object {
        private val APP_IDENTIFIER = BuildConfig.APPLICATION_ID
    }

}