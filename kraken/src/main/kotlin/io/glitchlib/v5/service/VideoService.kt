package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.internal.http.completed
import io.glitchlib.model.OrdinalList
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.model.MaxUploadSizeExceededException
import io.glitchlib.v5.model.json.Video
import io.glitchlib.v5.model.json.VideoBody
import io.glitchlib.v5.model.json.VideoCreate
import io.glitchlib.v5.service.request.FollowedVideosRequest
import io.glitchlib.v5.service.request.TopVideosRequest
import io.glitchlib.v5.service.request.VideoUploadRequest
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.rxkotlin.zipWith
import java.io.File

class VideoService(client: GlitchClient) : AbstractKrakenService(client) {

    fun getTopVideos(request: TopVideosRequest.() -> Unit = {}) =
        get<OrdinalList<Video>>("/videos/top", TopVideosRequest().apply(request)())
            .bodyFlowable


    fun getVideo(id: Long) =
        get<Video>("/videos/$id").bodySingle

    fun getFollowedVideos(credential: Credential, request: FollowedVideosRequest.() -> Unit = {}) =
        if (credential.scopeCheck(Scope.USER_READ))
            get<OrdinalList<Video>>("/videos/followed", FollowedVideosRequest(credential).apply(request)()).bodyFlowable
        else scopeIsMissing<Video>(Scope.USER_READ).toFlowable()

    /**
     * Start and return uploaded video to specific channel
     *
     * [file] is splitted into 5MB parts for creating a uploading [progress].
     * The [file] must be met requirements for successfully upload to the specific [channel][channelId]:
     * * Must have AAC audio coded and H264 compression video codec
     * * Bitrate must be up to 10Mb/s
     * * Video size must be up to 1080p
     * * Max frame-rate for uploaded video 60fps
     *
     * I do recommend use some 3rd party applications to manage those requirements above mentioned
     *
     * @param credential [Credential] with required scope: [Scope.CHANNEL_EDITOR] and access to specific [channel][channelId].
     * @param channelId Channel ID. Channel must be **Partner, Affiliate or Developer** to uploading video
     * @param title Video title - max 100 characters. **Will be trimmed.**
     * @param file Existed video file with supported extensions `*.mp4`, `*.avi`, `*.mov`, `*.flv`. Maximum file size is **10GB**
     * @param request additional parameters to this request
     * @param progress your own custom upload progress
     * @return a completely successful uploaded video
     */
    fun uploadVideo(
        credential: Credential,
        channelId: Long,
        title: String,
        file: File,
        request: VideoUploadRequest.() -> Unit = {},
        progress: (Int, Int) -> Unit = { actual, all -> }
    ): Single<Video> =
        if (credential.scopeCheck(Scope.CHANNEL_EDITOR))
            Single.create<File> { file.check(it) }.zipWith(
                post<VideoCreate>(
                    "/videos",
                    VideoUploadRequest(credential, channelId, title).apply(request)()
                ).bodySingle
            ).map {
                Triple(
                    file.readBytes().toList().chunked(5 * 1024 * 1024) { it.toByteArray() },
                    it.second.upload,
                    it.second.video.get("_id").asString.substring(1).toLong()
                )
            }.flatMap { m ->
                Completable.create { sink ->
                    val allParts = m.first.size
                    m.first.forEachIndexed { i, b ->
                        (client as GlitchClientImpl).http.put<Unit>(m.second.url) {
                            addQueryParameters("part", (i + 1).toString())
                            addQueryParameters("upload_token", m.second.token)
                        }.completed.subscribe({
                            progress(i, allParts)
                            if (i == m.first.size - 1)
                                sink.onComplete()
                        }, sink::onError)
                    }
                }.andThen((client as GlitchClientImpl).http.post<Unit>("${m.second.url}/complete") {
                    addQueryParameters("upload_token", m.second.token)
                }.completed).andThen(getVideo(m.third))
            }
        else scopeIsMissing(Scope.CHANNEL_EDITOR)

    fun updateVideo(credential: Credential, id: Long, body: VideoBody.() -> Unit) =
        if (credential.scopeCheck(Scope.CHANNEL_EDITOR))
            put<Video>("/videos/$id") {
                addHeaders("Authorization", "OAuth ${credential.accessToken}")
                setBody(VideoBody().apply(body))
            }.bodySingle
        else scopeIsMissing(Scope.CHANNEL_EDITOR)

    fun deleteVideo(credential: Credential, id: Long) =
        if (credential.scopeCheck(Scope.CHANNEL_EDITOR))
            delete<Unit>("/videos/$id") {
                addHeaders("Authorization", "OAuth ${credential.accessToken}")
            }.completed
        else scopeIsMissing<Unit>(Scope.CHANNEL_EDITOR).ignoreElement()


    private fun File.check(sink: SingleEmitter<File>) {
        if (exists()) {
            sink.onError(NullPointerException("File not found: $this"))
            return
        }

        if (extension.toLowerCase() in arrayOf("mp4", "mov", "avi", "flv")) {
            sink.onError(IllegalArgumentException("Only *.mp4, *.mov, *.avi and *.flv are supported - Detected: $extension"))
            return
        }

        if (length() > 10_737_418_240L) {
            sink.onError(MaxUploadSizeExceededException(10_737_418_240L))
            return
        }

        sink.onSuccess(this)
        return
    }
}
