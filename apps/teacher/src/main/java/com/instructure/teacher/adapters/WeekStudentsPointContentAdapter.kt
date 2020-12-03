package com.instructure.teacher.adapters

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import com.instructure.canvasapi2.models.uchproc.PointJournal

class WeekStudentsPointContentAdapter(
        private val pointJournal: PointJournal
) : PagerAdapter(){
    var initialTabIdx = 0
    override fun getCount() = pointJournal.points.size

    override fun isViewFromObject(view: View, `object`: Any) = view === `object`

}