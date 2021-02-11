package com.instructure.teacher.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.slider.Slider
import com.instructure.canvasapi2.models.CanvasContext
import com.instructure.canvasapi2.models.uchproc.*
import com.instructure.pandautils.fragments.BaseSyncFragment
import com.instructure.pandautils.utils.*
import com.instructure.teacher.R
import com.instructure.teacher.adapters.PointJournalRecyclerAdapter
import com.instructure.teacher.adapters.WeekStudentsPointContentAdapter
import com.instructure.teacher.dialog.RadioButtonDialog
import com.instructure.teacher.events.PointJournalFilterChangedEvent
import com.instructure.teacher.factory.PointJournalPresenterFactory
import com.instructure.teacher.holders.PointJournalViewHolder
import com.instructure.teacher.interfaces.AdapterToFragmentCallback
import com.instructure.teacher.presenters.PointJournalPresenter
import com.instructure.teacher.utils.RecyclerViewUtils
import com.instructure.teacher.utils.setupBackButton
import com.instructure.teacher.viewinterface.PointJournalView
import com.pspdfkit.ui.inspector.views.SliderPickerInspectorView
import kotlinx.android.synthetic.main.activity_week_students_point.*
import kotlinx.android.synthetic.main.fragment_uchproc_point_journal.*
import kotlinx.android.synthetic.main.fragment_uchproc_point_journal.pointJournalListToolbar
import kotlinx.android.synthetic.main.fragment_uchproc_point_students_journal.*
import kotlinx.android.synthetic.main.fragment_uchproc_point_students_journal.studentPointContentPager
import kotlinx.android.synthetic.main.recycler_swipe_refresh_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.NumberFormat
import kotlinx.android.synthetic.main.recycler_swipe_refresh_layout.recyclerView as peopleRecyclerView
import java.util.ArrayList

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class UchprocPointStudentsJournal : BaseSyncFragment<PointJournalGroup, PointJournalPresenter, PointJournalView, PointJournalViewHolder, PointJournalRecyclerAdapter>(), PointJournalView, Slider.OnChangeListener {
    var selectedWeek: Int = 1
    var initPosition = 0
    var weekPoints: WeekPoints? = null
    var reting: Int = 1
    var isPager: Boolean = true
    override fun layoutResId(): Int = R.layout.fragment_uchproc_point_students_journal
    override fun onCreateView(view: View) {}
    override fun onReadySetGo(presenter: PointJournalPresenter) {
        recyclerView?.adapter = adapter
        selectedWeek = nonNullArgs.getInt(Const.UCHPROC_SELECTED_WEEK)
        presenter.mPointJournal = nonNullArgs.getParcelable<PointJournal>(Const.UCHPROC_POINT_JOURNAL)
        initPosition = nonNullArgs.getInt(Const.POSITION)
        initData(initPosition)
        setupViews()
        presenter.loadData(false)
    }
    override fun onHandleBackPressed() = pointJournalListToolbar.closeSearch()

    private fun setupViews() {
        studentPointContentPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                isPager = true
                if (weekPoints != null){
                    val divided = getDivPoints()
                    if (divided != weekPoints!!.pointDivided){
                        var index = indexWeek()
                        val points = weekPoints!!.copy(sumPoint(), divided)
                        presenter.updateStudentPoints(canvasContext!!.id, initPosition, index, reting, points)
                    }
                }
                initPosition = position
                initData(position)
                isPager = false
            }
        })
        editKmd.setOnKeyListener { v, keyCode, event ->
            when {
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
                    if (presenter.mPointJournal!!.points.size > initPosition){
                        pointMainScroll.fullScroll(ScrollView.FOCUS_UP)
                        studentPointContentPager.currentItem = initPosition + 1
                    }
                    return@setOnKeyListener true
                }
                else -> false
            }
        }
        editLectureAttendance.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty())
                    setMaxValue(editLectureAttendance, s, presenter.mPointJournal!!.header!!.maxLectureAttendance!!)
            }
         })
        editPracticalAttendance.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty())
                    setMaxValue(editPracticalAttendance, s, presenter.mPointJournal!!.header!!.maxPracticalAttendance!!)
            }
        })
        editPracticalActivity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty())
                    setMaxValue(editPracticalActivity, s, presenter.mPointJournal!!.header!!.maxPracticalActive!!)
            }
        })
        editKmdroAttendance.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty())
                    setMaxValue(editKmdroAttendance, s, presenter.mPointJournal!!.header!!.maxKMDROAttendance!!)
            }
        })
        editKmdroActivity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty())
                    setMaxValue(editKmdroActivity, s, presenter.mPointJournal!!.header!!.maxKMDROActive!!)
            }
        })
        editKmd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty())
                    setMaxValue(editKmd, s, presenter.mPointJournal!!.header!!.maxKMD!!)
            }
        })
        editLectureAttendance.filters = arrayOf<InputFilter>(MinMaxFilter(0.0, presenter.mPointJournal!!.header!!.maxLectureAttendance!!))
        editPracticalAttendance.filters = arrayOf<InputFilter>(MinMaxFilter(0.0, presenter.mPointJournal!!.header!!.maxPracticalAttendance!!))
        editPracticalActivity.filters = arrayOf<InputFilter>(MinMaxFilter(0.0, presenter.mPointJournal!!.header!!.maxPracticalActive!!))
        editKmdroAttendance.filters = arrayOf<InputFilter>(MinMaxFilter(0.0, presenter.mPointJournal!!.header!!.maxKMDROAttendance!!))
        editKmdroActivity.filters = arrayOf<InputFilter>(MinMaxFilter(0.0, presenter.mPointJournal!!.header!!.maxKMDROActive!!))
        editKmd.filters = arrayOf<InputFilter>(MinMaxFilter(0.0, presenter.mPointJournal!!.header!!.maxKMD!!))

        toolbarStudentsJournal.setTitle(R.string.tab_point_journal)
        toolbarStudentsJournal.subtitle = getString(R.string.tab_week) + " " + selectedWeek.toString()
        ViewStyler.themeToolbar(requireActivity(), toolbarStudentsJournal, ColorKeeper.getOrGenerateColor(canvasContext), Color.WHITE)
        toolbarStudentsJournal.setupBackButton(this)
    }
    private fun initData(position: Int){
        val weekPointStudent: ArrayList<WeekPoints> = presenter.mPointJournal!!.points[position].rating!!.firstRatingPoints!!.pointWeeks!!.filter { it.weekNumber ==  selectedWeek } as ArrayList<WeekPoints>
        if (weekPointStudent.size > 0)
            reting = 1
        else
            reting = 2
        weekPointStudent.addAll(presenter.mPointJournal!!.points[position].rating!!.secondRatingPoints!!.pointWeeks!!.filter { it.weekNumber == selectedWeek })
        editLectureAttendance.setText(weekPointStudent.get(0).pointDivided!!.lectureAttendance.toString())
        editPracticalAttendance.setText(weekPointStudent.get(0).pointDivided.practicalAttendance.toString())
        editPracticalActivity.setText(weekPointStudent.get(0).pointDivided.practicalActive.toString())
        editKmdroAttendance.setText(weekPointStudent.get(0).pointDivided.kmdroAttendance.toString())
        editKmdroActivity.setText(weekPointStudent.get(0).pointDivided.kmdroActive.toString())
        editKmd.setText(weekPointStudent.get(0).pointDivided.kmd.toString())
        weekPoints = weekPointStudent.get(0)
        var ratingSetting: RatingSetting? = null
        if (reting == 1)
            ratingSetting = presenter.mPointJournal!!.header!!.ratings!!.firstReting!!.get(indexWeek())
        else
            ratingSetting = presenter.mPointJournal!!.header!!.ratings!!.secondReting!!.get(indexWeek())

        if (!ratingSetting.isEditable || presenter.mPointJournal!!.points[position].absenceThreshold)
            setEditable(false, false, false, false)
        else
            setEditable(ratingSetting.checkTheme!!.lecture, ratingSetting.checkTheme!!.practical, ratingSetting.checkTheme!!.kmdro, true)

    }
    private fun getWeeksName(): ArrayList<String>{
        val weeksName : ArrayList<String> = ArrayList()
        presenter.getWeeksNumber().forEach {
            weeksName.add(getString(R.string.tab_week) + " " + it.toString())
        }
        return  weeksName
    }
    private fun setMaxValue(editText: EditText, s: CharSequence?, maxValue: Double){
        if (!isPager) {
            if (s.toString().equals("."))
                editText.setText("0.0")
            else if (s.toString().toDouble() > maxValue)
                editText.setText(maxValue.toString())
            else if (presenter.mPointJournal!!.header!!.maxWeekPoint!! + s.toString().toDouble() - sumPoint() < s.toString().toDouble())
                editText.setText(Math.abs(presenter.mPointJournal!!.header!!.maxWeekPoint!! + s.toString().toDouble() - sumPoint()).toString())
        }
    }
    private fun sliderProperties(slider : Slider, stepSize : Float, from : Float, to : Float, value: Float){
        slider.stepSize = stepSize
        slider.value = value
        slider.valueFrom = from
        slider.valueTo = to
    }

    private fun getDivPoints() : PointDivided{
        var lectureAtt: Double
        var practicalAtt: Double
        var practicalAct: Double
        var kmdroAtt: Double
        var kmdroAct: Double
        var kmd: Double

        if (editLectureAttendance.text.toString().isNullOrBlank() || editLectureAttendance.text.toString().equals("."))
            lectureAtt = 0.0
        else
            lectureAtt = editLectureAttendance.text.toString().toDouble()

        if (editPracticalAttendance.text.toString().isNullOrBlank() || editPracticalAttendance.text.toString().equals("."))
            practicalAtt = 0.0
        else
            practicalAtt = editPracticalAttendance.text.toString().toDouble()

        if (editPracticalActivity.text.toString().isNullOrBlank() || editPracticalActivity.text.toString().equals("."))
            practicalAct = 0.0
        else
            practicalAct = editPracticalActivity.text.toString().toDouble()

        if (editKmdroAttendance.text.toString().isNullOrBlank() || editKmdroAttendance.text.toString().equals("."))
            kmdroAtt = 0.0
        else
            kmdroAtt = editKmdroAttendance.text.toString().toDouble()

        if (editKmdroActivity.text.toString().isNullOrBlank() || editKmdroActivity.text.toString().equals("."))
            kmdroAct = 0.0
        else
            kmdroAct = editKmdroActivity.text.toString().toDouble()
        if (editKmd.text.toString().isNullOrBlank() || editKmd.text.toString().equals("."))
            kmd = 0.0
        else
            kmd = editKmd.text.toString().toDouble()

        val divided = PointDivided(lectureAtt, practicalAtt, practicalAct, kmdroAtt, kmdroAct, kmd)

        return  divided
    }
    private fun indexWeek() : Int{
        var index = 0;
        if (reting == 1) {
            for (weekPoints in presenter.mPointJournal!!.points[0].rating!!.firstRatingPoints!!.pointWeeks!!) {
                if (weekPoints.weekNumber == selectedWeek)
                    return index
            }
        }
        else{
            for (weekPoints in presenter.mPointJournal!!.points[0].rating!!.secondRatingPoints!!.pointWeeks!!) {
                if (weekPoints.weekNumber == selectedWeek)
                    return index
            }
        }
        return 0;
    }
    private fun sumPoint() : Double{
        val divided = getDivPoints()
        return divided.lectureAttendance + divided.practicalAttendance + divided.practicalActive + divided.kmdroAttendance + divided.kmdroActive + divided.kmd
    }

    private fun setEditable(att: Boolean, act: Boolean, kmdro: Boolean, kmd: Boolean){
        editLectureAttendance.isEnabled = att
        editPracticalAttendance.isEnabled = act
        editPracticalActivity.isEnabled = act
        editKmdroAttendance.isEnabled = kmdro
        editKmdroActivity.isEnabled = kmdro
        editKmd.isEnabled = kmd
    }
    override val recyclerView: RecyclerView? get() = peopleRecyclerView

    override fun getPresenterFactory() = PointJournalPresenterFactory(nonNullArgs.getParcelable<Parcelable>(Const.CANVAS_CONTEXT) as CanvasContext)

    override fun onPresenterPrepared(presenter: PointJournalPresenter) {
        val adapter = WeekStudentsPointContentAdapter(nonNullArgs.getParcelable<PointJournal>(Const.UCHPROC_POINT_JOURNAL)!!)
        studentPointContentPager.adapter = adapter
        studentPointContentPager.currentItem = nonNullArgs.getInt(Const.POSITION)
    }
    override fun createAdapter(): PointJournalRecyclerAdapter {
        return PointJournalRecyclerAdapter(requireContext(), presenter, object : AdapterToFragmentCallback<PointJournalGroup> {
            override fun onRowClicked(model: PointJournalGroup, position: Int) {
                val canvasContext = nonNullArgs.getParcelable<CanvasContext>(Const.CANVAS_CONTEXT)!!
                if (canvasContext.isDesigner()) {
                    showToast(R.string.errorIsDesigner)
                    return
                }
                /*val bundle = WeekStudentsPointActivity.makeBundle(presenter.canvasContext.id, presenter.mPointJournal!! , position)
                RouteMatcher.route(requireContext(), Route(bundle, RouteContext.WEEK_STUDENTS_POINT))*/
            }
        })
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPointJournalFilterChanged(event: PointJournalFilterChangedEvent) {
            setFilter(event.filterIndex)
        }

    private fun setFilter(filterIndex: Int = -1){
        presenter.setFilter(filterIndex)
        setWeekName(getWeeksName()[presenter.getWeeksNumber().indexOf(presenter.getSelectedValue())])
    }
    private fun setWeekName(name: String){
        weekName.text = name
    }

    override fun onRefreshFinished() {
/*        swipeRefreshLayout.isRefreshing = false
        weekName.visibility = View.VISIBLE
        setWeekName(getWeeksName()[presenter.getWeeksNumber().indexOf(presenter.getSelectedValue())])*/
    }

    override fun onRefreshStarted() {
       /* if(!swipeRefreshLayout.isRefreshing) {
            emptyPandaView.visibility  = View.VISIBLE
        }
        emptyPandaView.setLoading()*/
    }

    override fun checkIfEmpty() {
//        RecyclerViewUtils.checkIfEmpty(emptyPandaView, recyclerView, swipeRefreshLayout, adapter, presenter.isEmpty)
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    companion object {

        fun newInstance(canvasContext: CanvasContext, args: Bundle): UchprocPointStudentsJournal {
            val arguments = args
            arguments.putParcelable(Const.CANVAS_CONTEXT, canvasContext)
            val fragment = UchprocPointStudentsJournal()
            fragment.arguments = arguments
            return fragment
        }
        fun makeBundle(selectionPosition: Int, selectedWeek: Int, pointJournal: PointJournal): Bundle {
            val args = Bundle()
            args.putInt(Const.POSITION, selectionPosition)
            args.putInt(Const.UCHPROC_SELECTED_WEEK, selectedWeek)
            args.putParcelable(Const.UCHPROC_POINT_JOURNAL, pointJournal)
            return args
        }

    }
    inner class MinMaxFilter() : InputFilter {
        private var intMin: Double = 0.0
        private var intMax: Double = 0.0
        constructor(minValue: Double, maxValue: Double) : this() {
            this.intMin = minValue
            this.intMax = maxValue
        }
        override fun filter(
                source: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned,
                dStart: Int,
                dEnd: Int
        ): CharSequence? {
            try {
                val input = dest.toString() + source.toString()
                if (isInRange(intMin, intMax, input.toDouble())) {
                    return null
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }
        private fun isInRange(a: Double, b: Double, c: Double): Boolean {
            return  (b > a) && c in a..b
        }
    }

    override fun onValueChange(slider: Slider?, value: Float) {
        if (slider!!.id == R.id.seekbarLectureAttendance){
            editLectureAttendance.setText(value.toString())
        }
        else if (slider!!.id == R.id.seekbarPracticalAttendance){
            editPracticalAttendance.setText(value.toString())
        }
        else if (slider!!.id == R.id.seekbarPracticalActivity){
            editPracticalActivity.setText(value.toString())
        }
        else if (slider!!.id == R.id.seekbarKmdroAttendance){
            editKmdroAttendance.setText(value.toString())
        }
        else if (slider!!.id == R.id.seekbarKmdroActivity){
            editKmdroActivity.setText(value.toString())
        }
        else{
            editKmd.setText(value.toString())
        }
    }
}
