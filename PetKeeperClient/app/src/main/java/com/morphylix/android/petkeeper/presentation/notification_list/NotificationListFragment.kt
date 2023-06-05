package com.morphylix.android.petkeeper.presentation.notification_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.morphylix.android.petkeeper.databinding.FragmentNotificationListBinding
import com.morphylix.android.petkeeper.domain.model.domain.Comment
import com.morphylix.android.petkeeper.domain.model.domain.Notification
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import com.morphylix.android.petkeeper.presentation.base.BaseState
import com.morphylix.android.petkeeper.presentation.comment_list.CommentListAdapter
import com.morphylix.android.petkeeper.presentation.comment_list.CommentListFragmentArgs
import com.morphylix.android.petkeeper.presentation.comment_list.CommentListViewModel

class NotificationListFragment: BaseFragment<FragmentNotificationListBinding>({ inflater, container ->
    FragmentNotificationListBinding.inflate(inflater, container, false)
}) {

    private val notificationListViewModel: NotificationListViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        notificationListViewModel.fetchUserNotifications()

        lifecycleScope.launchWhenStarted {
            notificationListViewModel.state.collect { state ->
                when (state) {
                    is BaseState.Loading -> {}
                    is BaseState.Success -> {
                        if (state.data is List<*> && ((state.data.firstOrNull() is Notification) || state.data.isEmpty())) {
                            binding.commentsRecyclerView.adapter = NotificationAdapter(state.data as List<Notification>)
                            notificationListViewModel.setLoadingState()
                        }
                    }
                    is BaseState.Error -> {}
                }
            }
        }

    }

}