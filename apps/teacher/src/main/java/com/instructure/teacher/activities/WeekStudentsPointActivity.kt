package com.instructure.teacher.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.instructure.canvasapi2.models.DiscussionTopicHeader
import com.instructure.canvasapi2.models.uchproc.PointJournal
import com.instructure.interactions.router.Route
import com.instructure.interactions.router.RouterParams
import com.instructure.pandautils.activities.BasePresenterActivity
import com.instructure.pandautils.utils.Const
import com.instructure.teacher.R
import com.instructure.teacher.factory.WeekStudentsPointPresenterFactory
import com.instructure.teacher.presenters.WeekStudentsPointPresenter
import com.instructure.teacher.viewinterface.WeekStudentsPointView
import instructure.androidblueprint.PresenterFactory
import java.util.ArrayList

class WeekStudentsPointActivity : BasePresenterActivity<WeekStudentsPointPresenter, WeekStudentsPointView>(), WeekStudentsPointView {

    private val courseId: Long by lazy { intent.extras!!.getLong(Const.COURSE_ID) }
    private val pointJournal: PointJournal? by lazy { intent.extras!!.getParcelable<PointJournal>(Const.UCHPROC_POINT_JOURNAL) }
    private val initialSelection: Int by lazy { intent.extras!!.getInt(Const.SELECTED_ITEM, 0) }
    private var currentSelection = 0
    private var previousSelection = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_students_point)
    }

    override fun unBundle(extras: Bundle) = Unit

    override fun onReadySetGo(presenter: WeekStudentsPointPresenter) {
        presenter.setupData()
    }

    override fun getPresenterFactory() = WeekStudentsPointPresenterFactory(courseId, pointJournal!!)

    override fun onPresenterPrepared(presenter: WeekStudentsPointPresenter) = Unit

    override fun onDataSet(pointJournal: PointJournal) {

    }

    override fun onErrorSettingData() {
        //TODO("Not yet implemented")
    }

    companion object {

        private const val TUTORIAL_DELAY = 400L

        /**
         * The number of submissions to be bundled (pre-cached) to either side of the selected
         * submission. If this value is too high it may result in Android throwing a
         * TransactionTooLargeException when creating SpeedGraderActivity.
         */
        private const val MAX_CACHED_ADJACENT = 6

        /**
         * The maximum submission history depth allowed for a submission to be eligible for
         * pre-caching. If this value is too high it may result in Android throwing a
         * TransactionTooLargeException when creating SpeedGraderActivity.
         */
        private const val MAX_HISTORY_THRESHOLD = 8

        fun makeBundle(courseId: Long, pointJournal: PointJournal, selectedIdx: Int): Bundle {
            return Bundle().apply {
                putLong(Const.COURSE_ID, courseId)
                putInt(Const.SELECTED_ITEM, selectedIdx)
                putParcelable(Const.UCHPROC_POINT_JOURNAL, pointJournal)
            }
        }

        fun createIntent(context: Context, route: Route): Intent {
            return Intent(context, WeekStudentsPointActivity::class.java).apply {
                if(!route.arguments.isEmpty) {
                    println(route.arguments.toString())
                    putExtras(route.arguments)
                } else {
                    // Try to get the information from the route that this activity needs. This happens
                    // when we come from a push notification
                    putExtras(Bundle().apply {
                        putLong(Const.COURSE_ID, route.paramsHash[RouterParams.COURSE_ID]?.toLong() ?: 0)
                    })
                }
            }
        }

        fun createIntent(context: Context, courseId: Long, assignmentId: Long, submissionId: Long) = Intent(context, WeekStudentsPointActivity::class.java).apply {
            // We've come from a push notification, we'll use these ids to grab the data we need later
            putExtras(Bundle().apply {
                putLong(Const.COURSE_ID, courseId)
            })
        }
    }
}