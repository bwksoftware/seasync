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

package com.bwksoftware.android.seasync.presentation.presenter

import android.util.Log
import com.bwksoftware.android.seasync.domain.RepoTemplate
import com.bwksoftware.android.seasync.domain.interactor.DefaultObserver
import com.bwksoftware.android.seasync.domain.interactor.GetRepoList
import com.bwksoftware.android.seasync.data.authentication.Authenticator
import com.bwksoftware.android.seasync.domain.interactor.CreateRepoSync
import com.bwksoftware.android.seasync.domain.interactor.CreateSync
import com.bwksoftware.android.seasync.presentation.mapper.RepoModelMapper
import com.bwksoftware.android.seasync.presentation.model.Repo
import com.bwksoftware.android.seasync.presentation.view.views.RepoView
import javax.inject.Inject


class RepoPresenter @Inject constructor(val getRepoList: GetRepoList,
                                        val syncRepo: CreateRepoSync,
                                        val repoModelMapper: RepoModelMapper) {

    internal lateinit var repoView: RepoView
    @Inject lateinit var authenticator: Authenticator


    fun getRepos(accountName: String) {

        val authToken = authenticator.getCurrentUserAuthToken(accountName, repoView.activity())
        this.getRepoList.execute(RepoObserver(), GetRepoList.Params(false, authToken))
    }

    fun synchronizeRepo(accountName: String, storage: String, repo: Repo, position: Int) {
        val authToken = authenticator.getCurrentUserAuthToken(accountName, repoView.activity())
        syncRepo.execute(RepoSyncObserver(position), CreateRepoSync.Params(authToken,
                repo.id!!, storage))
    }

    private inner class RepoObserver : DefaultObserver<List<RepoTemplate>>() {

        override fun onComplete() {
            Log.d("AccountPresenter", "yolo complete")
        }

        override fun onError(exception: Throwable) {
            Log.d("AccountPresenter", "yolo error" + exception.localizedMessage)
        }

        override fun onNext(repoList: List<RepoTemplate>) {
            repoView.renderRepos(repoModelMapper.transformRepos(repoList))
        }
    }

    private inner class RepoSyncObserver(val position: Int) : DefaultObserver<RepoTemplate>() {
        override fun onError(exception: Throwable) {
            Log.d("RepoPresenter", exception.localizedMessage)
        }

        override fun onNext(t: RepoTemplate) {
            repoView.updateRepo(repoModelMapper.transformRepo(t),position)
        }
    }

}