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
import me.ayra.nusantara.music.R
import me.ayra.nusantara.music.playback.TimberMusicService.Companion.TYPE_PLAYLIST
import me.ayra.nusantara.music.constants.Constants.CATEGORY_SONG_DATA
import me.ayra.nusantara.music.databinding.FragmentCategorySongsBinding
import me.ayra.nusantara.music.extensions.addOnItemClick
import me.ayra.nusantara.music.extensions.argument
import me.ayra.nusantara.music.extensions.filter
import me.ayra.nusantara.music.extensions.getExtraBundle
import me.ayra.nusantara.music.extensions.inflateWithBinding
import me.ayra.nusantara.music.extensions.observe
import me.ayra.nusantara.music.extensions.safeActivity
import me.ayra.nusantara.music.extensions.toSongIds
import me.ayra.nusantara.music.models.CategorySongData
import me.ayra.nusantara.music.models.Song
import me.ayra.nusantara.music.ui.adapters.SongsAdapter
import me.ayra.nusantara.music.ui.fragments.base.MediaItemFragment
import me.ayra.nusantara.music.util.AutoClearedValue
import kotlinx.android.synthetic.main.fragment_album_detail.recyclerView

class CategorySongsFragment : MediaItemFragment() {
    private lateinit var songsAdapter: SongsAdapter
    private lateinit var categorySongData: CategorySongData
    var binding by AutoClearedValue<FragmentCategorySongsBinding>(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categorySongData = argument(CATEGORY_SONG_DATA)
        binding = inflater.inflateWithBinding(R.layout.fragment_category_songs, container)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.categorySongData = categorySongData

        songsAdapter = SongsAdapter().apply {
            popupMenuListener = mainViewModel.popupMenuListener
            if (categorySongData.type == TYPE_PLAYLIST) {
                playlistId = categorySongData.id
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(safeActivity)
            adapter = songsAdapter
            addOnItemClick { position: Int, _: View ->
                val extras = getExtraBundle(songsAdapter.songs.toSongIds(), categorySongData.title)
                mainViewModel.mediaItemClicked(songsAdapter.songs[position], extras)
            }
        }

        mediaItemFragmentViewModel.mediaItems
                .filter { it.isNotEmpty() }
                .observe(this) { list ->
                    @Suppress("UNCHECKED_CAST")
                    songsAdapter.updateData(list as List<Song>)
                }
    }
}
