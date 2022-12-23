package com.flyfishxu.onionote.tile

import androidx.room.Room
import androidx.wear.tiles.*
import androidx.wear.tiles.LayoutElementBuilders.LayoutElement
import com.flyfishxu.onionote.database.NoteDatabase
import com.google.android.horologist.tiles.CoroutinesTileService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val RESOURCES_VERSION = "0"

class ShowNoteTileService : CoroutinesTileService() {


    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ResourceBuilders.Resources {
        return ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .build()
    }

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest
    ): TileBuilders.Tile {
        val text: String
        withContext(Dispatchers.Default) {
            val db = Room.databaseBuilder(
                this@ShowNoteTileService,
                NoteDatabase::class.java, "notes"
            ).build()
            val noteDao = db.noteDao()
            val sp = getSharedPreferences("data", MODE_PRIVATE)
            val noteId = sp.getInt("tileNoteId", -1)
            text =
                if (noteId == -1) getString(com.flyfishxu.onionote.R.string.there_is_nothing)
                else noteDao.getNoteById(noteId).content
        }
        val singleTileTimeline = TimelineBuilders.Timeline.Builder()
            .addTimelineEntry(
                TimelineBuilders.TimelineEntry.Builder()
                    .setLayout(
                        LayoutElementBuilders.Layout.Builder()
                            .setRoot(tileLayout(text))
                            .build()
                    )
                    .build()
            )
            .build()

        return TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTimeline(singleTileTimeline)
            .build()
    }

    private fun tileLayout(text: String): LayoutElement {
        return LayoutElementBuilders.Box.Builder()
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(
                LayoutElementBuilders.Text.Builder()
                    .setText(text)
                    .build()
            )
            .build()
    }
}
