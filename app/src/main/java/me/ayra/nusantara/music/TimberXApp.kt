/*
 * Copyright (c) 2019 Naman Dwivedi.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
@file:Suppress("unused")

package me.ayra.nusantara.music

import android.app.Application
import me.ayra.nusantara.music.BuildConfig.DEBUG
import me.ayra.nusantara.music.db.roomModule
import me.ayra.nusantara.music.logging.FabricTree
import me.ayra.nusantara.music.network.lastFmModule
import me.ayra.nusantara.music.network.lyricsModule
import me.ayra.nusantara.music.network.networkModule
import me.ayra.nusantara.music.notifications.notificationModule
import me.ayra.nusantara.music.permissions.permissionsModule
import me.ayra.nusantara.music.playback.mediaModule
import me.ayra.nusantara.music.repository.repositoriesModule
import me.ayra.nusantara.music.ui.viewmodels.viewModelsModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class TimberXApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(FabricTree())
        }

        val modules = listOf(
                mainModule,
                permissionsModule,
                mediaModule,
                prefsModule,
                networkModule,
                roomModule,
                notificationModule,
                repositoriesModule,
                viewModelsModule,
                lyricsModule,
                lastFmModule
        )
        startKoin(
                androidContext = this,
                modules = modules
        )
    }
}
