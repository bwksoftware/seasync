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

package com.bwksoftware.android.seafile.data.entity

import com.bwksoftware.android.seafile.domain.AccountTemplate
import com.bwksoftware.android.seafile.domain.RepoTemplate


class EntityDataMapper {

    companion object {
        fun create(): EntityDataMapper {
            return EntityDataMapper()
        }
    }

    fun transformAccountToken(account: Account): AccountTemplate {
        return AccountTemplate(account.token!!)
    }

    fun transformRepo(repo: Repo): RepoTemplate {
        val newRepo = RepoTemplate(repo.name, repo.permission, repo.owner, repo.encrypted,
                repo.mtime, repo.size)
        return newRepo
    }

    fun transformRepoList(repoList: List<Repo>): List<RepoTemplate> {
        return repoList.mapTo(ArrayList<RepoTemplate>()) { transformRepo(it) }
    }
}

val entityDataMapper by lazy {
    EntityDataMapper.create()
}