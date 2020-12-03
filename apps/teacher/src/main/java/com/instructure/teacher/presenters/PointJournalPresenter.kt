package com.instructure.teacher.presenters

import com.instructure.canvasapi2.apis.PointJournalManager
import com.instructure.canvasapi2.managers.UserManager
import com.instructure.canvasapi2.models.CanvasContext
import com.instructure.canvasapi2.models.User
import com.instructure.canvasapi2.models.uchproc.PointJournal
import com.instructure.canvasapi2.models.uchproc.PointJournalGroup
import com.instructure.canvasapi2.models.uchproc.RatingSetting
import com.instructure.canvasapi2.utils.weave.awaitApis
import com.instructure.canvasapi2.utils.weave.catch
import com.instructure.canvasapi2.utils.weave.tryWeave
import com.instructure.teacher.viewinterface.PointJournalView
import instructure.androidblueprint.SyncPresenter
import kotlinx.coroutines.Job
import kotlin.collections.ArrayList


class PointJournalPresenter(val canvasContext: CanvasContext) : SyncPresenter<PointJournalGroup, PointJournalView>(PointJournalGroup::class.java) {

    private var mPointJournalGroupList = ArrayList<PointJournalGroup>()
    var mPointJournal: PointJournal? = null
    var mApiCalls: Job? = null
    private var mSelectedValue: Int = -1
    private var mWeeksNumber: ArrayList<Int> = ArrayList()

    override fun loadData(forceNetwork: Boolean) {
        //if (data.size() > 0 && !forceNetwork) return
        if(forceNetwork) {
            mSelectedValue = 1
            mWeeksNumber.clear()
            mPointJournal = null
        }
        if (mApiCalls?.isActive ?: false) {
            mApiCalls?.invokeOnCompletion { performLoad(forceNetwork) }
        } else {
            performLoad(forceNetwork)
        }
       // onRefreshStarted()
        //PointJournalManager.getPointJournal(canvasContext.id, forceNetwork, mPointJournalListCallback)
    }

    @Suppress("EXPERIMENTAL_FEATURE_WARNING")
    private fun performLoad(forceNetwork: Boolean) {
        mApiCalls = tryWeave {
            onRefreshStarted()
            // Get assignments and quizzes
            val (pointJournal, users) = awaitApis<PointJournal, List<User>>(
                    { PointJournalManager.getPointJournal(canvasContext.id, forceNetwork, it) },
                    { UserManager.getAllPeopleList(canvasContext, it, forceNetwork) }
            )
            mPointJournal = pointJournal
            if (forceNetwork || mSelectedValue < 0)
                fillWeeks()
            mPointJournalGroupList = pointJournal.points;
            for (point in mPointJournalGroupList){
                point.selectedWeek = mSelectedValue
                val user = users.filter { it.loginId == point.recordBook}
                if (user.size > 0){
                    point.pronouns = user[0].pronouns
                    point.avatarUrl = user[0].avatarUrl
                }
                point.selectedWeek = mSelectedValue
            }

            populateData()
        } catch {
            viewCallback?.onRefreshFinished()
            viewCallback?.checkIfEmpty()
        }
    }

    private fun fillWeeks() {
        for (week in getFirstRetingSettings()!!){
            if (week.isEditable)
                mSelectedValue = week.number
            mWeeksNumber.add(week.number)
        }
        for (week in getSecontRetingSettings()!!){
            if (week.isEditable)
                mSelectedValue = week.number
            mWeeksNumber.add(week.number)
        }
        if (mSelectedValue < 0)
            mSelectedValue = mWeeksNumber[mWeeksNumber.size - 1]
    }

    fun getFirstRetingSettings() : ArrayList<RatingSetting>? = mPointJournal!!.header?.ratings?.firstReting
    fun getSecontRetingSettings() : ArrayList<RatingSetting>? = mPointJournal!!.header?.ratings?.secondReting
    fun getWeeksNumber() : ArrayList<Int> = mWeeksNumber
    fun getSelectedValue() : Int = mSelectedValue

    private fun populateData() {
        val points = mPointJournalGroupList ?: return
        data.addOrUpdate(points)
        viewCallback?.onRefreshFinished()
        viewCallback?.checkIfEmpty()
    }


    override fun refresh(forceNetwork: Boolean) {
        mApiCalls?.cancel()
        clearData()
        loadData(forceNetwork)
    }
    fun setFilter(selectedIndex: Int) {
        if (selectedIndex == -1 ) return
        mSelectedValue = mWeeksNumber[selectedIndex - 1]
        refresh(false)
    }
}