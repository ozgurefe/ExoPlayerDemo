package dev.mobiler.exoplayerdemo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util


class MainActivity : AppCompatActivity(), Player.EventListener {
    private lateinit var player:SimpleExoPlayer
    lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerView = findViewById(R.id.playerView);

        initializePlayer()


    }

    override fun onStop() {
        super.onStop()
        player.playWhenReady = false
    }


    override fun onStart() {
        super.onStart()
        player.playWhenReady = true
    }

    private fun initializePlayer(){
        player = SimpleExoPlayer.Builder(this).build()
        playerView.player = player

        val uri = Uri.parse(getString(R.string.media_url_mp3))
        val mediaSource = buildMediaSource(uri)
        mediaSource?.let { player.prepare(it, false, false) }
        player.playWhenReady = true


    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun buildHlsMediaSource(uri:Uri): MediaSource{
        val dataSourceFactory: DataSource.Factory =
            DefaultHttpDataSourceFactory(
                Util.getUserAgent(
                    this,
                    getString(R.string.app_name)
                )
            )
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    override fun onPlayerStateChanged(
        playWhenReady: Boolean,
        playbackState: Int
    ) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
            else -> "UNKNOWN_STATE             -"
        }
        Log.d(
            "MainActivity", "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady
        )
    }


    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        Log.e("MainActivity", error.message)

    }
}
