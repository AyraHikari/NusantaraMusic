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
package me.ayra.nusantara.music.playback

import me.ayra.nusantara.music.playback.players.MusicPlayer
import me.ayra.nusantara.music.playback.players.Queue
import me.ayra.nusantara.music.playback.players.RealMusicPlayer
import me.ayra.nusantara.music.playback.players.RealQueue
import me.ayra.nusantara.music.playback.players.RealSongPlayer
import me.ayra.nusantara.music.playback.players.SongPlayer
import org.koin.dsl.module.module

val mediaModule = module {

    factory {
        RealMusicPlayer(get())
    } bind MusicPlayer::class

    factory {
        RealQueue(get(), get(), get())
    } bind Queue::class

    factory {
        RealSongPlayer(get(), get(), get(), get(), get())
    } bind SongPlayer::class
}
