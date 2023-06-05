package com.morphylix.android.petkeeper.presentation.user_profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.morphylix.android.petkeeper.R
import com.morphylix.android.petkeeper.databinding.FragmentUserProfileBinding
import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.domain.model.domain.User
import com.morphylix.android.petkeeper.presentation.MainActivity
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import com.morphylix.android.petkeeper.presentation.base.BaseState
import com.morphylix.android.petkeeper.presentation.order_list.OrderListAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "UserProfileFragment"

class UserProfileFragment: BaseFragment<FragmentUserProfileBinding>({ inflater, container ->
    FragmentUserProfileBinding.inflate(inflater, container, false)
}) {

    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private var user: User? = null
    private var orderListAdapter: OrderListAdapter = OrderListAdapter()
    private val args: UserProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "usr email is ${args.userEmail}")
        if (args.userEmail == null) {
            userProfileViewModel.fetchUserInfo()
        } else {
            userProfileViewModel.fetchUserInfoByEmail(args.userEmail!!)
        }
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.ordersRecyclerView.adapter = orderListAdapter

        binding.ratingCardView.setOnClickListener {
            val action = UserProfileFragmentDirections.actionUserProfileFragmentToCommentListFragment(args.userEmail!!)
            findNavController().navigate(action)
        }

        lifecycleScope.launchWhenStarted {
            userProfileViewModel.state.collect { state ->
                when (state) {
                    is BaseState.Loading -> {}
                    is BaseState.Success -> {
                        if (state.data is User) {
                            user = state.data
                            setupProfileInfo(user!!)
                            userProfileViewModel.fetchUserOrders(user?.email!!, OrderType.PLACED)
                        }
                        if (state.data is List<*>) {
                            if (state.data.firstOrNull() is Order) {
                                Log.i(TAG, "orderlist is ${state.data.first()}")
                                orderListAdapter.submitList(state.data as List<Order>)
                            }
                        }
                    }
                    is BaseState.Error -> {}
                }
            }
        }
    }

    private fun setupProfileInfo(user: User) {
        with(binding) {
            userNameTextView.text = getString(R.string.name_surname, user.name, user.surname)
            phoneNumberTextView.text = user.phone.ifEmpty { getString(R.string.no_phone) }
            ratingTextView.text = user.rating.toString()
            commentsTextView.text = getString(R.string.comments, user.totalVotes) // Replace with total comments
            if (args.userEmail != null) {
                editProfileButton.text = getString(R.string.leave_comment)
                editProfileButton.setOnClickListener {
                    val action = UserProfileFragmentDirections.actionUserProfileFragmentToLeaveCommentFragment(args.userEmail!!)
                    findNavController().navigate(action)
                }
            } else {
                editProfileButton.setOnClickListener {
                    val action = UserProfileFragmentDirections.actionUserProfileFragmentToEditProfileFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }

}