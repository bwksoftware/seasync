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
package com.bwksoftware.android.seasync.data.datastore

import com.bwksoftware.android.seasync.data.entity.Avatar
import com.bwksoftware.android.seasync.data.entity.Item
import com.bwksoftware.android.seasync.data.entity.Repo
import io.reactivex.Observable


interface DataStore {
    fun getRepoList(account: String, serverAddress: String,
                    authToken: String): Observable<List<Repo>>

    fun getDirectoryEntries(account: String, serverAddress: String, authToken: String,
                            repoId: String,
                            directory: String): Observable<List<Item>>

    fun getAvatar(username: String, serverAddress: String, authToken: String): Observable<Avatar>
}