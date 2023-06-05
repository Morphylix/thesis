package com.morphylix.android.petkeeper.presentation.order_details

import android.app.ActionBar
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.morphylix.android.petkeeper.R
import com.morphylix.android.petkeeper.databinding.FragmentOrderDetailsBinding
import com.morphylix.android.petkeeper.databinding.SendMessagePopupWindowBinding
import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.domain.model.domain.Response
import com.morphylix.android.petkeeper.domain.model.domain.User
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import com.morphylix.android.petkeeper.presentation.base.BaseState
import com.morphylix.android.petkeeper.presentation.order_list.OrderListAdapter
import com.morphylix.android.petkeeper.presentation.order_list.OrderListFragmentDirections
import kotlinx.coroutines.launch

private const val TAG = "OrderDetailsFragment"

class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding>({ inflater, container ->
    FragmentOrderDetailsBinding.inflate(inflater, container, false)
}) {

    private val args: OrderDetailsFragmentArgs by navArgs()
    private val orderDetailsViewModel: OrderDetailsViewModel by activityViewModels()
    private var currentUserEmail: String = ""
    private var currentUser: User? = null
    private var orderUserEmail: String = ""
    private lateinit var popupWindowBinding: SendMessagePopupWindowBinding
    private lateinit var popupWindow: PopupWindow

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderDetailsViewModel.fetchUserInfo()
        binding.userCardView.setOnClickListener {
            val action = OrderDetailsFragmentDirections.actionOrderDetailsFragmentToUserProfileFragment(orderUserEmail)
            findNavController().navigate(action)
        }
        lifecycleScope.launchWhenStarted {
            orderDetailsViewModel.state.collect { state ->
                when (state) {
                    is BaseState.Loading -> {}
                    is BaseState.Success -> {
                        if (state.data is User) {
                            currentUser = state.data
                            currentUserEmail = state.data.email
                            orderDetailsViewModel.fetchOrderById(args.orderId)
                        }
                        if (state.data is Order) {
                            orderUserEmail = state.data.user?.email ?: ""
                            bindOrderData(state.data, currentUserEmail != state.data.user?.email)
                            if (currentUserEmail != state.data.user?.email) {
                                Log.i(TAG, "THIS ORDER IS YOURS")
                            } else {
                                Log.i(TAG, "THIS ORDER IS NOT YOURS")
                            }
                        }
                        if (state.data is List<*> && state.data.firstOrNull() is Response) {
                            val responsesAdapter = ResponsesAdapter(state.data as List<Response>, object: ResponseClickListener {
                                override fun onResponseClicked(response: Response) {
                                    showPopupWindow(binding.petPictureImageView)
                                }

                            })
                            binding.responsesRecyclerView.adapter = responsesAdapter
                        }
                        orderDetailsViewModel.setLoadingState()
                    }
                    is BaseState.Error -> {}
                }
            }
        }
    }

    private fun bindOrderData(order: Order, otherUser: Boolean) {
        with(binding) {
            val byteArray = order.pet?.picture
            Log.i(TAG, "byteArray is $byteArray")

            if (byteArray != null) {
                val image = BitmapDrawable(
                    binding.root.resources,
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                )
                petPictureImageView.setImageDrawable(image)
            } else {
                petPictureImageView.setImageResource(R.drawable.pet_pic_placeholder)
            }

            petNameTextView.text = order.pet?.name

            userNameTextView.text = order.user?.name

            userRatingTextView.text = order.user?.rating.toString()

            petBreedAndWeightTextView.text = getString(
                R.string.pet_breed_and_weight,
                order.pet?.breed,
                order.pet?.weight
            )

            petTypeAndAgeTextView.text = getString(
                R.string.pet_type_and_age,
                order.pet?.type,
                order.pet?.years,
                order.pet?.months
            )

            petIsFriendlyToTextView.text = getString(R.string.pet_is_friendly_to, order.pet!!.name)

            var sb = StringBuilder()
            createFriendlyList(sb, order.pet!!.isFriendlyToChildren, R.string.to_children, true)
            createFriendlyList(sb, order.pet!!.isFriendlyToCats, R.string.to_cats, true)
            createFriendlyList(sb, order.pet!!.isFriendlyToDogs, R.string.to_dogs, true)
            createFriendlyList(sb, order.pet!!.isFriendlyToPeople, R.string.to_other_people, true)
            createFriendlyList(sb, order.pet!!.isFriendlyToOtherAnimals, R.string.other_animals, true)
            petIsFriendlyListTextView.text = sb.toString()

            petIsNotFriendlyToTextView.text = getString(R.string.pet_is_not_friendly_to, order.pet!!.name)

            sb = StringBuilder()
            createFriendlyList(sb, order.pet!!.isFriendlyToChildren, R.string.to_children, false)
            createFriendlyList(sb, order.pet!!.isFriendlyToCats, R.string.to_cats, false)
            createFriendlyList(sb, order.pet!!.isFriendlyToDogs, R.string.to_dogs, false)
            createFriendlyList(sb, order.pet!!.isFriendlyToPeople, R.string.to_other_people, false)
            createFriendlyList(sb, order.pet!!.isFriendlyToOtherAnimals, R.string.to_other_animals, false)
            petIsNotFriendlyListTextView.text = sb.toString()

            sb = StringBuilder()
            if (order.pet!!.canLiveWithCats) sb.append(getString(R.string.cats_lc))
            if (sb.isNotEmpty()) sb.append(" ")
            if (order.pet!!.canLiveWithDogs) sb.append(getString(R.string.dogs_lc))
            if (sb.isNotEmpty()) sb.append(" ")
            if (order.pet!!.canLiveWithOtherAnimals) sb.append(getString(R.string.other_animals_lc))
            if (sb.isEmpty()) sb.append(" -")
            petCanLiveWithTextView.text = getString(R.string.pet_can_live_with, order.pet!!.name, sb.toString())

            sb = StringBuilder()
            if (order.pet!!.isLitterBoxTrained) sb.append(getString(R.string.litter_box_trained))
            else sb.append(getString(R.string.not_litter_box_trained))
            sb.append(", ")
            if (order.pet!!.chewsThings) sb.append(getString(R.string.chews_things))
            else sb.append(getString(R.string.doesnt_chew_things))
            sb.append(" ")
            if (order.pet!!.marksThings) sb.append(getString(R.string.marks_things))
            else sb.append(getString(R.string.doesnt_mark_things))
            litterboxMarksChewsTextView.text = getString(R.string.pet_name_and_text, order.pet!!.name, sb.toString())

            sb = StringBuilder()
            if (order.pet!!.isVaccinated) sb.append(getString(R.string.vaccinated))
            else sb.append(getString(R.string.not_vaccinated))
            sb.append(" ")
            if (order.pet!!.isSterilised) sb.append(getString(R.string.sterilized))
            else sb.append(getString(R.string.not_sterilized))
            vaccinatedSterilizedTextView.text = getString(R.string.pet_name_and_text, order.pet!!.name, sb.toString())

            petDiseasesTextView.text = order.pet!!.diseases

            sb = StringBuilder()
            if (order.pet!!.canStayAlone) sb.append(getString(R.string.can_stay_alone))
            else sb.append(getString(R.string.can_not_stay_alone))
            canStayAloneTextView.text = getString(R.string.pet_name_and_text, order.pet!!.name, sb.toString())

            foodToEatHeaderTextView.text = getString(R.string.what_food_does_pet_eat, order.pet!!.name)

            foodToEatTextView.text = order.pet!!.foodToEat

            foodToAvoidHeaderTextView.text = getString(R.string.what_food_to_avoid)

            foodToAvoidTextView.text = order.pet!!.foodToAvoid

            howOftenToFeedTextView.text = order.pet!!.howOftenToFeed

            needsWalkingTextView.text =
                if (order.pet!!.needsWalking) getString(R.string.needs_walking, order.pet!!.name)
                else getString(R.string.doesnt_need_walking, order.pet!!.name)

            if (order.pet!!.needsWalking) {
                howOftenToWalkTextView.text = order.pet!!.howOftenToWalk
                warningsWhileWalkingTextView.text = order.pet!!.warningsWhileWalking
            }
            else howOftenToWalkTextView.visibility = View.GONE

            needsPlayingTextView.text =
                if (order.pet!!.needsPlaying) getString(R.string.needs_playing, order.pet!!.name)
                else getString(R.string.doesnt_need_playing, order.pet!!.name)

            if (order.pet!!.needsPlaying) howToPlayTextView.text = order.pet!!.howToPlay
            else howToPlayTextView.visibility = View.GONE

            howToWashTextView.text = order.pet!!.howToWash

            otherRecsTextView.text = order.pet!!.otherRecs

            if (!otherUser) {
                respondButton.visibility = View.GONE
                respondInputLayout.visibility = View.GONE
                responsesRecyclerView.visibility = View.VISIBLE
                responsesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                orderDetailsViewModel.fetchOrderResponses(order.orderId)
                closeOrderButton.visibility = View.VISIBLE
                closeOrderButton.setOnClickListener {
                    createAlertDeleteDialog(object : AlertDialogCallback {
                        override fun onYesButtonPressed() {
                            orderDetailsViewModel.closeOrder(order.orderId)
                        }

                        override fun onNoButtonPressed() {

                        }

                    }, R.string.close_order)
                }
            } else {
                respondButton.setOnClickListener {
                    val response = Response(
                        -1,
                        currentUserEmail,
                        args.orderId,
                        respondEditText.text.toString(),
                        false
                    )
                    orderDetailsViewModel.postResponse(response)
                }
            }
        }
    }

    private fun createAlertDeleteDialog(
        alertDialogCallback: AlertDialogCallback,
        alertStringId: Int
    ) {
        val alertDialog: AlertDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.attention)
                setMessage(alertStringId)
                setPositiveButton(R.string.yes) { _, _ ->
                    alertDialogCallback.onYesButtonPressed()
                }
                setNegativeButton(R.string.no) { _, _ ->
                    // User cancelled the dialog
                }
            }
            builder.create()
        }
        alertDialog.show()
    }

    interface AlertDialogCallback {
        fun onYesButtonPressed()
        fun onNoButtonPressed()
    }

    private fun createFriendlyList(sb: StringBuilder, isFriendly: Boolean?, strRes: Int, toBeFriendly: Boolean) {
        if (isFriendly != null && isFriendly == toBeFriendly) {
            sb.append("- " + getString(strRes) + "\n")
        }
    }


    private fun showPopupWindow(anchorView: View) {
        popupWindowBinding = SendMessagePopupWindowBinding.inflate(layoutInflater, binding.root, false)

        popupWindow = PopupWindow(
            popupWindowBinding.root,
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )

        popupWindow.isFocusable = true
        popupWindow.update()

        with(popupWindowBinding) {

            applyButton.setOnClickListener {

            }

            closeButton.setOnClickListener {
                popupWindow.dismiss()
            }
        }

        popupWindow.showAtLocation(
            anchorView,
            Gravity.NO_GRAVITY,
            0,
            0
        )
    }
}