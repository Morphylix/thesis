package com.morphylix.android.petkeeper.presentation.comment_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.morphylix.android.petkeeper.databinding.FragmentCommentListBinding
import com.morphylix.android.petkeeper.domain.model.domain.Comment
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import com.morphylix.android.petkeeper.presentation.base.BaseState

class CommentListFragment: BaseFragment<FragmentCommentListBinding>({ inflater, container ->
    FragmentCommentListBinding.inflate(inflater, container, false)
}) {

    private val commentListViewModel: CommentListViewModel by activityViewModels()
    private val args: CommentListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        commentListViewModel.fetchUserComments(args.email)

        lifecycleScope.launchWhenStarted {
            commentListViewModel.state.collect { state ->
                when (state) {
                    is BaseState.Loading -> {}
                    is BaseState.Success -> {
                        if (state.data is List<*> && ((state.data.firstOrNull() is Comment) || state.data.isEmpty())) {
                            binding.commentsRecyclerView.adapter = CommentListAdapter(state.data as List<Comment>)
                            commentListViewModel.setLoadingState()
                        }
                    }
                    is BaseState.Error -> {}
                }
            }
        }

    }

}