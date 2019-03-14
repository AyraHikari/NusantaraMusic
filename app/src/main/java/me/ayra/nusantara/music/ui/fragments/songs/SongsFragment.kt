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
package me.ayra.nusantara.music.ui.fragments.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.rxkprefs.Pref
import me.ayra.nusantara.music.PREF_SONG_SORT_ORDER
import me.ayra.nusantara.music.R
import me.ayra.nusantara.music.constants.SongSortOrder
import me.ayra.nusantara.music.constants.SongSortOrder.SONG_A_Z
import me.ayra.nusantara.music.constants.SongSortOrder.SONG_DURATION
import me.ayra.nusantara.music.constants.SongSortOrder.SONG_YEAR
import me.ayra.nusantara.music.constants.SongSortOrder.SONG_Z_A
import me.ayra.nusantara.music.extensions.addOnItemClick
import me.ayra.nusantara.music.extensions.filter
import me.ayra.nusantara.music.extensions.getExtraBundle
import me.ayra.nusantara.music.extensions.inflateTo
import me.ayra.nusantara.music.extensions.observe
import me.ayra.nusantara.music.extensions.disposeOnDetach
import me.ayra.nusantara.music.extensions.ioToMain
import me.ayra.nusantara.music.extensions.safeActivity
import me.ayra.nusantara.music.extensions.toSongIds
import me.ayra.nusantara.music.models.Song
import me.ayra.nusantara.music.ui.adapters.SongsAdapter
import me.ayra.nusantara.music.ui.fragments.base.MediaItemFragment
import me.ayra.nusantara.music.ui.listeners.SortMenuListener
import kotlinx.android.synthetic.main.layout_recyclerview.recyclerView
import org.koin.android.ext.android.inject

class SongsFragment : MediaItemFragment() {
    private lateinit var songsAdapter: SongsAdapter
    private val sortOrderPref by inject<Pref<SongSortOrder>>(name = PREF_SONG_SORT_ORDER)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflateTo(R.layout.layout_recyclerview, container)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        songsAdapter = SongsAdapter().apply {
            showHeader = true
            popupMenuListener = mainViewModel.popupMenuListener
            sortMenuListener = sortListener
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(safeActivity)
            adapter = songsAdapter
            addOnItemClick { position: Int, _: View ->
                songsAdapter.getSongForPosition(position)?.let { song ->
                    val extras = getExtraBundle(songsAdapter.songs.toSongIds(), getString(R.string.all_songs))
                    mainViewModel.mediaItemClicked(song, extras)
                }
            }
        }

        mediaItemFragmentViewModel.mediaItems
                .filter { it.isNotEmpty() }
                .observe(this) { list ->
                    @Suppress("UNCHECKED_CAST")
                    songsAdapter.updateData(list as List<Song>)
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Auto trigger a reload when the sort order pref changes
        sortOrderPref.observe()
                .ioToMain()
                .subscribe { mediaItemFragmentViewModel.reloadMediaItems() }
                .disposeOnDetach(view)
    }

    private val sortListener = object : SortMenuListener {
        override fun shuffleAll() {
            songsAdapter.songs.shuffled().apply {
                val extras = getExtraBundle(toSongIds(), getString(R.string.all_songs))
                mainViewModel.mediaItemClicked(this[0], extras)
            }
        }

        override fun sortAZ() = sortOrderPref.set(SONG_A_Z)

        override fun sortDuration() = sortOrderPref.set(SONG_DURATION)

        override fun sortYear() = sortOrderPref.set(SONG_YEAR)

        override fun numOfSongs() {}

        override fun sortZA() = sortOrderPref.set(SONG_Z_A)
    }
}
