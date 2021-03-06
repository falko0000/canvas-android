/*
 * Copyright (C) 2016 - present Instructure, Inc.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.instructure.student.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.instructure.student.R
import com.instructure.student.util.Const
import kotlinx.android.synthetic.main.activity_video_view.player_view as simpleExoPlayerView

@Suppress("DEPRECATION")
class VideoViewActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var mediaDataSourceFactory: DataSource.Factory? = null
    private var mainHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
        simpleExoPlayerView.requestFocus()
        mediaDataSourceFactory = buildDataSourceFactory(true)
        mainHandler = Handler()
        val videoTrackSelectionFactory: TrackSelection.Factory = AdaptiveTrackSelection.Factory(BANDWIDTH_METER)
        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, DefaultLoadControl())
        simpleExoPlayerView.player = player
        player?.playWhenReady = true
        player?.prepare(buildMediaSource(Uri.parse(intent?.extras?.getString(Const.URL))))
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) player?.release()
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) player?.release()
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return when (val type = Util.inferContentType(uri.lastPathSegment)) {
            C.TYPE_SS -> SsMediaSource(
                uri, buildDataSourceFactory(false),
                DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, null
            )
            C.TYPE_DASH -> DashMediaSource(
                uri, buildDataSourceFactory(false),
                DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, null
            )
            C.TYPE_HLS -> HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, null)
            C.TYPE_OTHER -> ExtractorMediaSource(
                uri, mediaDataSourceFactory,
                DefaultExtractorsFactory(), mainHandler, null
            )
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set [BANDWIDTH_METER] as a listener to the new DataSource factory.
     * @return A new DataSource factory.
     */
    private fun buildDataSourceFactory(useBandwidthMeter: Boolean): DataSource.Factory {
        val meter = if (useBandwidthMeter) BANDWIDTH_METER else null
        return DefaultDataSourceFactory(this, meter, DefaultHttpDataSourceFactory("candroid", meter))
    }

    companion object {
        private val BANDWIDTH_METER = DefaultBandwidthMeter()

        fun createIntent(context: Context?, url: String?): Intent {
            val bundle = Bundle()
            bundle.putString(Const.URL, url)
            val intent = Intent(context, VideoViewActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }
    }
}
