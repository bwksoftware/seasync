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

package com.bwksoftware.android.seasync.data.repository

import com.bwksoftware.android.seasync.data.authentication.SeafAccountManager
import com.bwksoftware.android.seasync.data.datamanager.StorageManager
import com.bwksoftware.android.seasync.data.datastore.DataStoreFactory
import com.bwksoftware.android.seasync.data.entity.EntityDataMapper
import com.bwksoftware.android.seasync.data.entity.Item
import com.bwksoftware.android.seasync.data.net.RestApiImpl
import com.bwksoftware.android.seasync.domain.AccountTemplate
import com.bwksoftware.android.seasync.domain.AvatarTemplate
import com.bwksoftware.android.seasync.domain.ItemTemplate
import com.bwksoftware.android.seasync.domain.RepoTemplate
import com.bwksoftware.android.seasync.domain.repository.Repository
import io.reactivex.Observable
import javax.inject.Inject


class DataRepository @Inject constructor(val restService: RestApiImpl,
                                         val storageManager: StorageManager,
                                         val seafAccountManager: SeafAccountManager,
                                         val dataStoreFactory: DataStoreFactory,
                                         val entityDataMapper: EntityDataMapper) : Repository {


    override fun getDirectoryEntries(authToken: String, repoId: String,
                                     directory: String): Observable<List<ItemTemplate>> {
        val currAccount = seafAccountManager.getCurrentAccount()
        val serverAddress = seafAccountManager.getServerAddress(currAccount)
        return dataStoreFactory.createDirectoryDataStore(
                currAccount.name,
                repoId, directory).getDirectoryEntries(currAccount.name, serverAddress!!,
                authToken, repoId, directory).
                map { entityDataMapper.transformItemList(it, repoId, directory) }
    }


    override fun getAvatar(username: String, token: String): Observable<AvatarTemplate> {
        val currAccount = seafAccountManager.getCurrentAccount()
        val serverAddress = seafAccountManager.getServerAddress(currAccount)!!
        return dataStoreFactory.createAvatarDataStore(username).getAvatar(username, serverAddress,
                token).map(
                entityDataMapper::transformAvatar)
    }

    override fun getRepoList(authToken: String): Observable<List<RepoTemplate>> {
        val currAccount = seafAccountManager.getCurrentAccount()
        val serverAddress = seafAccountManager.getServerAddress(currAccount)!!

        return dataStoreFactory.createRepoDataStore(currAccount.name).getRepoList(currAccount.name,
                serverAddress, authToken)
                .map(entityDataMapper::transformRepoList)
    }

    override fun getAccountToken(username: String, password: String): Observable<AccountTemplate> {
        val currAccount = seafAccountManager.getAccountByName(username)
        val serverAddress = seafAccountManager.getServerAddress(currAccount!!)!!
        return restService.getAccountToken(username, serverAddress, password).map(
                entityDataMapper::transformAccountToken)
    }

    override fun syncItem(authToken: String, repoId: String, directory: String, name: String,
                          storage: String, type: String): Observable<ItemTemplate> {
        return Observable.fromCallable {
            entityDataMapper.transformItem(
                    storageManager.createNewSync(authToken, repoId, directory, name, storage, type),
                    repoId, directory)
        }
    }

    override fun unsyncItem(repoId: String, directory: String,
                            name: String): Observable<ItemTemplate> {
        return Observable.fromCallable {
            entityDataMapper.transformItem(storageManager.unsyncItem(repoId, directory, name),
                    repoId, directory)
        }
    }

    override fun syncRepo(authToken: String, repoId: String, storage: String): Observable<RepoTemplate> {
        return Observable.fromCallable {
            entityDataMapper.transformRepo(storageManager.createNewRepoSync(authToken, repoId, storage))
        }
    }

    override fun unsyncRepo(authToken: String, repoId: String, storage: String): Observable<RepoTemplate> {
        return Observable.fromCallable {
            entityDataMapper.transformRepo(storageManager.createNewRepoSync(authToken, repoId, storage))
        }
    }

    override fun cacheFile(authToken: String, repoID: String, directory: String,
                           fileName: String): Observable<ItemTemplate> {

        return Observable.fromCallable {
            val item = Item()
            item.name = fileName
            item.path = directory
            entityDataMapper.transformItem(storageManager.getCachedFile(repoID, authToken, item)!!,
                    repoID, directory)
        }
    }


}