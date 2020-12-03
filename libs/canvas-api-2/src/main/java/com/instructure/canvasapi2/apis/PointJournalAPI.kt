package com.instructure.canvasapi2.apis

import com.instructure.canvasapi2.StatusCallback
import com.instructure.canvasapi2.builders.RestBuilder
import com.instructure.canvasapi2.builders.RestParams
import com.instructure.canvasapi2.models.User
import com.instructure.canvasapi2.models.uchproc.PointJournal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

object PointJournalAPI {
    internal interface PointJournalInterface{
        @GET("courses/{courseId}/point_journal")
        fun getPointJournalGroup(@Path("courseId") courseId: Long?): Call<PointJournal>
    }
    fun getPointJournalGroup(adapter: RestBuilder, params: RestParams, courseId: Long?, callback: StatusCallback<PointJournal>) {
        callback.addCall(adapter.build(PointJournalAPI.PointJournalInterface::class.java, params).getPointJournalGroup(courseId)).enqueue(callback)
    }
}