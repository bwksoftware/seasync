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

package com.bwksoftware.android.seasync.domain.repository

import com.bwksoftware.android.seasync.domain.AccountTemplate
import com.bwksoftware.android.seasync.domain.AvatarTemplate
import com.bwksoftware.android.seasync.domain.ItemTemplate
import com.bwksoftware.android.seasync.domain.RepoTemplate
import io.reactivex.Observable


interface Repository {
    fun getRepoList(authToken: String): Observable<List<RepoTemplate>>
    fun getAvatar(username: String, authToken: String): Observable<AvatarTemplate>
    fun getAccountToken(username: String, password: String): Observable<AccountTemplate>
    fun getDirectoryEntries(authToken: String, repoId: String,
                            directory: String): Observable<List<ItemTemplate>>

    fun syncItem(authToken: String, repoId: String, directory: String, name: String,
                 storage: String, type: String): Observable<ItemTemplate>

    fun syncRepo(authToken: String, repoId: String, storage: String): Observable<RepoTemplate>

    fun unsyncItem(repoId: String, directory: String, name: String): Observable<ItemTemplate>
    fun unsyncRepo(authToken: String, repoId: String, storage: String): Observable<RepoTemplate>

    fun cacheFile(authToken: String, repoID: String, directory: String,
                  fileName: String): Observable<ItemTemplate>
}