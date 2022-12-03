package com.flyfishxu.onionote.presentation.tile

import androidx.wear.tiles.*
import androidx.wear.tiles.LayoutElementBuilders.FONT_VARIANT_BODY
import androidx.wear.tiles.LayoutElementBuilders.LayoutElement
import com.google.android.horologist.tiles.CoroutinesTileService

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
        val singleTileTimeline = TimelineBuilders.Timeline.Builder()
            .addTimelineEntry(
                TimelineBuilders.TimelineEntry.Builder()
                    .setLayout(
                        LayoutElementBuilders.Layout.Builder()
                            .setRoot(tileLayout())
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

    private fun tileLayout(): LayoutElement {
        val text = "Tiles body"
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
