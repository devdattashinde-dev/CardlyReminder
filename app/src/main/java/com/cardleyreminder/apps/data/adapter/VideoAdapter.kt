package com.cardleyreminder.apps.data.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import com.cardleyreminder.apps.data.model.insights.VideoCardData
import com.cardleyreminder.apps.databinding.VideoItemLayoutBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class VideoAdapter(val context: Context): RecyclerView.Adapter<VideoViewHolder>() {
    private var address = mutableListOf<VideoCardData>()

    fun setListData(addressList: List<VideoCardData>) {
        this.address = addressList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(VideoItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {

        address[position].let {video ->
            holder.binding.videoTitle.text = video.heading

            if (video.videoType=="1" || video.video?.contains("youtube.com") == true) {
                holder.binding.youtubePlayerView.visibility = View.VISIBLE
                holder.binding.webView.visibility = View.GONE


                holder.binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        val videoId = extractYouTubeVideoId(video.video!!)
                        videoId?.let {
                            youTubePlayer.cueVideo(it, 0f)
                        }
                    }
                })
            } else {
                holder.binding.youtubePlayerView.visibility = View.GONE
                holder.binding.webView.visibility = View.VISIBLE

                holder.binding.webView.settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                }
                holder.binding.webView.webViewClient = WebViewClient()

                // Load the raw iFrame HTML code
                val htmlData = """
                    <html>
                    <body style="margin:0;padding:0;">
                        ${video.video}
                    </body>
                    </html>
                """


                holder.binding.webView.loadData( video.video.toString(), "text/html", "UTF-8")
            }

            if(video.adsType.equals("2") && !video.markitLink.isNullOrEmpty()){
                holder.binding.btApplyNow.visibility = View.VISIBLE

                holder.binding.btApplyNow.setOnClickListener {
                    val url = video.markitLink
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            }else{
                holder.binding.btApplyNow.visibility = View.GONE
            }

        }


    }

    override fun getItemCount() = address.size
}

private fun extractYouTubeVideoId(url: String): String? {
    val regex = Regex("""(?:youtube\.com/(?:embed/|watch\?v=))([a-zA-Z0-9_-]{11})""")

    val matchResult = regex.find(url)
    return matchResult?.groups?.get(1)?.value
}
class VideoViewHolder(val binding: VideoItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)