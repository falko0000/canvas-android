package com.instructure.canvasapi2.apis

import com.instructure.canvasapi2.StatusCallback
import com.instructure.canvasapi2.builders.RestBuilder
import com.instructure.canvasapi2.builders.RestParams
import com.instructure.canvasapi2.models.uchproc.PointJournal
import com.instructure.canvasapi2.models.uchproc.WeekPoints
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

object PointJournalAPI {
    internal interface PointJournalInterface{
        @GET("courses/{courseId}/point_journal")
        fun getPointJournalGroup(@Path("courseId") courseId: Long?): Call<PointJournal>
        @PUT("courses/{courseId}/students/{studentId}/points")
        fun updateStudentPoints(@Path("courseId") courseId: Long?, @Path("studentId") studentId: Long?, @Body weekPoints: WeekPoints) : Call<WeekPoints>
    }
    fun getPointJournalGroup(adapter: RestBuilder, params: RestParams, courseId: Long?, callback: StatusCallback<PointJournal>) {
        callback.addCall(adapter.build(PointJournalAPI.PointJournalInterface::class.java, params).getPointJournalGroup(courseId)).enqueue(callback)
    }
    fun updateStudentPoints(courseId: Long, studentId: Long, weekPoints: WeekPoints, adapter: RestBuilder, callback: StatusCallback<WeekPoints>, params: RestParams) {
        callback.addCall(adapter.build(PointJournalAPI.PointJournalInterface::class.java, params).updateStudentPoints(courseId, studentId, weekPoints)).enqueue(callback)
    }
}