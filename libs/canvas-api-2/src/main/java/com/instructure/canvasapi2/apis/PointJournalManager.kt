package com.instructure.canvasapi2.apis

import com.instructure.canvasapi2.StatusCallback
import com.instructure.canvasapi2.builders.RestBuilder
import com.instructure.canvasapi2.builders.RestParams
import com.instructure.canvasapi2.models.uchproc.PointJournal

object PointJournalManager {
    fun getPointJournal(courseId: Long, forceNetwork: Boolean, callback: StatusCallback<PointJournal>) {
        val adapter = RestBuilder(callback)
        val params = RestParams(isForceReadFromNetwork = forceNetwork)
        PointJournalAPI.getPointJournalGroup(adapter, params, courseId, callback)
    }
}