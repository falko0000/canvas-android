package com.instructure.teacher.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.*
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.instructure.canvasapi2.models.CanvasContext
import com.instructure.canvasapi2.models.User
import com.instructure.canvasapi2.models.uchproc.PointJournalGroup
import com.instructure.interactions.router.Route
import com.instructure.interactions.router.RouteContext
import com.instructure.pandautils.fragments.BaseSyncFragment
import com.instructure.pandautils.utils.*
import com.instructure.teacher.R
import com.instructure.teacher.activities.SpeedGraderActivity
import com.instructure.teacher.activities.WeekStudentsPointActivity
import com.instructure.teacher.adapters.PeopleListRecyclerAdapter
import com.instructure.teacher.adapters.PointJournalRecyclerAdapter
import com.instructure.teacher.adapters.StudentContextFragment
import com.instructure.teacher.dialog.PeopleListFilterDialog
import com.instructure.teacher.dialog.RadioButtonDialog
import com.instructure.teacher.events.PointJournalFilterChangedEvent
import com.instructure.teacher.events.SubmissionCommentsUpdated
import com.instructure.teacher.events.SubmissionFilterChangedEvent
import com.instructure.teacher.factory.PointJournalPresenterFactory
import com.instructure.teacher.features.postpolicies.ui.PostPolicyFragment
import com.instructure.teacher.holders.PointJournalViewHolder
import com.instructure.teacher.interfaces.AdapterToFragmentCallback
import com.instructure.teacher.presenters.PointJournalPresenter
import com.instructure.teacher.router.RouteMatcher
import com.instructure.teacher.utils.RecyclerViewUtils
import com.instructure.teacher.utils.setupBackButton
import com.instructure.teacher.viewinterface.PointJournalView
import kotlinx.android.synthetic.main.fragment_people_list_layout.*
import kotlinx.android.synthetic.main.fragment_people_list_layout.filterTitleWrapper
import kotlinx.android.synthetic.main.fragment_uchproc_point_journal.*
import kotlinx.android.synthetic.main.recycler_swipe_refresh_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlinx.android.synthetic.main.recycler_swipe_refresh_layout.recyclerView as peopleRecyclerView
import java.util.ArrayList

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class UchprocPointJournal : BaseSyncFragment<PointJournalGroup, PointJournalPresenter, PointJournalView, PointJournalViewHolder, PointJournalRecyclerAdapter>(), PointJournalView {

    override fun layoutResId(): Int = R.layout.fragment_uchproc_point_journal
    override fun onCreateView(view: View) {}

    override fun onReadySetGo(presenter: PointJournalPresenter) {
        recyclerView?.adapter = adapter
        setupViews()
        presenter.loadData(false)
    }
    override fun onHandleBackPressed() = pointJournalListToolbar.closeSearch()
    private fun setupViews() {
        val canvasContext = nonNullArgs.getParcelable<CanvasContext>(Const.CANVAS_CONTEXT)
        pointJournalListToolbar.setTitle(R.string.tab_point_journal)
        pointJournalListToolbar.subtitle = canvasContext!!.name
        if (pointJournalListToolbar.menu.size() == 0) pointJournalListToolbar.inflateMenu(R.menu.menu_point_journal_week_filter)

        pointJournalListToolbar.menu.findItem(R.id.weeksFilter)?.setOnMenuItemClickListener {
            //let the user select the course/group they want to see
            val dialog = RadioButtonDialog.getInstance(requireActivity().supportFragmentManager, getString(R.string.academic_weeks),  getWeeksName(), presenter.getWeeksNumber().indexOf(presenter.getSelectedValue())) { idx ->
                EventBus.getDefault().post(PointJournalFilterChangedEvent(presenter.getWeeksNumber()[idx]))
            }
            dialog.show(requireActivity().supportFragmentManager, RadioButtonDialog::class.java.simpleName)
            false
        }

        ViewStyler.themeToolbar(requireActivity(), pointJournalListToolbar, ColorKeeper.getOrGenerateColor(canvasContext), Color.WHITE)

        pointJournalListToolbar.setupBackButton(this)
    }
    private fun getWeeksName(): ArrayList<String>{
        val weeksName : ArrayList<String> = ArrayList()
        presenter.getWeeksNumber().forEach {
            weeksName.add(getString(R.string.tab_week) + " " + it.toString())
        }
        return  weeksName
    }



    override val recyclerView: RecyclerView? get() = peopleRecyclerView

    override fun getPresenterFactory() = PointJournalPresenterFactory(nonNullArgs.getParcelable<Parcelable>(Const.CANVAS_CONTEXT) as CanvasContext)

    override fun onPresenterPrepared(presenter: PointJournalPresenter) {
        RecyclerViewUtils.buildRecyclerView(rootView, requireContext(), adapter,
                presenter, R.id.swipeRefreshLayout, R.id.recyclerView, R.id.emptyPandaView, getString(R.string.no_items_to_display_short))
        addSwipeToRefresh(swipeRefreshLayout!!)
    }
    override fun createAdapter(): PointJournalRecyclerAdapter {
        return PointJournalRecyclerAdapter(requireContext(), presenter, object : AdapterToFragmentCallback<PointJournalGroup> {
            override fun onRowClicked(model: PointJournalGroup, position: Int) {
                val canvasContext = nonNullArgs.getParcelable<CanvasContext>(Const.CANVAS_CONTEXT)!!
                if (canvasContext.isDesigner()) {
                    showToast(R.string.errorIsDesigner)
                    return
                }
                val bundle = WeekStudentsPointActivity.makeBundle(presenter.canvasContext.id, presenter.mPointJournal!! , position)
                RouteMatcher.route(requireContext(), Route(bundle, RouteContext.WEEK_STUDENTS_POINT))
               /* val bundle = StudentContextFragment.makeBundle(model.id, canvasContext.id, true)
                RouteMatcher.route(requireContext(), Route(null, StudentContextFragment::class.java, canvasContext, bundle))*/
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
        swipeRefreshLayout.isRefreshing = false
        weekName.visibility = View.VISIBLE
        setWeekName(getWeeksName()[presenter.getWeeksNumber().indexOf(presenter.getSelectedValue())])
    }

    override fun onRefreshStarted() {
        if(!swipeRefreshLayout.isRefreshing) {
            emptyPandaView.visibility  = View.VISIBLE
        }
        emptyPandaView.setLoading()
    }

    override fun checkIfEmpty() {
        RecyclerViewUtils.checkIfEmpty(emptyPandaView, recyclerView, swipeRefreshLayout, adapter, presenter.isEmpty)
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
        fun newInstance(canvasContext: CanvasContext): UchprocPointJournal {
            val args = Bundle()
            args.putParcelable(Const.CANVAS_CONTEXT, canvasContext)
            val fragment = UchprocPointJournal()
            fragment.arguments = args
            return fragment
        }
    }


}
