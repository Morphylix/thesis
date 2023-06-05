package com.morphylix.android.petkeeper.presentation.leave_comment

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.morphylix.android.petkeeper.databinding.FragmentLeaveCommentBinding
import com.morphylix.android.petkeeper.domain.model.domain.Comment
import com.morphylix.android.petkeeper.domain.model.domain.User
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import com.morphylix.android.petkeeper.presentation.base.BaseState
import kotlinx.coroutines.launch

class LeaveCommentFragment : BaseFragment<FragmentLeaveCommentBinding>({ inflater, container ->
    FragmentLeaveCommentBinding.inflate(inflater, container, false)
}) {

    private val leaveCommentViewModel: LeaveCommentViewModel by activityViewModels()
    private var senderEmail: String = ""
    private val args: LeaveCommentFragmentArgs by navArgs()
    private var currentRating: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        leaveCommentViewModel.loadUserInfo()
        binding.ratingbar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                if (fromUser) {
                    ratingBar.rating = rating
                    currentRating = rating.toDouble()
                }
            }
        binding.submitButton.setOnClickListener {
            val comment = Comment(
                -1,
                senderEmail,
                receiverEmail = args.userEmail,
                body = binding.commentEditText.text.toString(),
                rating = currentRating
                )
            leaveCommentViewModel.postComment(comment)
        }

        lifecycleScope.launchWhenStarted {
            leaveCommentViewModel.state.collect { state ->
                when (state) {
                    is BaseState.Loading -> {}
                    is BaseState.Success -> {
                        if (state.data is User) {
                            senderEmail = state.data.email
                        }
                    }
                    is BaseState.Error -> {}
                }
            }
        }
    }

}