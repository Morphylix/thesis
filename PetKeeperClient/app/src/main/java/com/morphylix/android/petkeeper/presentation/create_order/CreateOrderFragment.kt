package com.morphylix.android.petkeeper.presentation.create_order


import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.morphylix.android.petkeeper.R
import com.morphylix.android.petkeeper.databinding.FragmentCreateOrderBinding
import com.morphylix.android.petkeeper.domain.model.domain.Order
import com.morphylix.android.petkeeper.domain.model.domain.Pet
import com.morphylix.android.petkeeper.presentation.base.BaseFragment
import com.morphylix.android.petkeeper.presentation.base.BaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


private const val TAG = "CreateOrderFragment"

class CreateOrderFragment : BaseFragment<FragmentCreateOrderBinding>({ inflater, container ->
    FragmentCreateOrderBinding.inflate(inflater, container, false)
}) {

    private val createOrderViewModel: CreateOrderViewModel by activityViewModels()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var imgInputStream: InputStream? = null
    private val args: CreateOrderFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    Log.i(TAG, "data is ${data}")
                    val imageUri: Uri = result.data!!.data!!

                    val contentResolver = requireContext().contentResolver
                    val inputStream = contentResolver.openInputStream(imageUri)
                    imgInputStream = inputStream
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            val inputStream = requireContext().assets.open("pet_pic_placeholder.png")
            //createOrderViewModel.postPetImage(inputStream)
            Log.i(TAG, "input stream is $inputStream")
            petPictureImageView.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                resultLauncher.launch(intent)
            }
            submitOrderButton.setOnClickListener {
                val petName = petNameEditText.text.toString()
                Log.i(TAG, "pet name is $petName")
                val pet = Pet(
                    name = petNameEditText.text.toString(),
                    type = petTypeSpinner.selectedItem.toString(),
                    breed = "", // TODO: REPLACE WITH AUTOCOMPLETE TEXTVIEW
                    gender = getRadioGroupValue(petGenderRadioGroup),
                    years = petYearsEditText.text.toString().toInt(),
                    months = petMonthsEditText.text.toString().toInt(),
                    weight = petWeightEditText.text.toString().toDouble(),
                    isFriendlyToPeople = getBooleanRadioGroupValue(otherPeopleRadioGroup),
                    isFriendlyToChildren = getBooleanRadioGroupValue(childrenRadioGroup),
                    isFriendlyToCats = getBooleanRadioGroupValue(catsRadioGroup),
                    isFriendlyToDogs = getBooleanRadioGroupValue(dogsRadioGroup),
                    isFriendlyToOtherAnimals = getBooleanRadioGroupValue(otherAnimalsRadioGroup),
                    canLiveWithDogs = dogsCheckbox.isChecked,
                    canLiveWithCats = catsCheckbox.isChecked,
                    canLiveWithOtherAnimals = otherAnimalsCheckbox.isChecked,
                    isLitterBoxTrained = getBooleanRadioGroupValue(litterBoxRadioGroup)!!,
                    marksThings = getBooleanRadioGroupValue(markThingsRadioGroup)!!,
                    chewsThings = getBooleanRadioGroupValue(chewThingsRadioGroup)!!,
                    isVaccinated = getBooleanRadioGroupValue(isVaccinatedRadioGroup)!!,
                    isSterilised = getBooleanRadioGroupValue(isSterilisedRadioGroup)!!,
                    diseases = petDiseasesEditText.text.toString(),
                    canStayAlone = getBooleanRadioGroupValue(stayAloneRadioGroup)!!,
                    foodToEat = petFoodEditText.text.toString(),
                    foodToAvoid = petBadFoodEditText.text.toString(),
                    howOftenToFeed = petFoodPeriodEditText.text.toString(),
                    needsWalking = getBooleanRadioGroupValue(petWalkRadioGroup)!!,
                    howOftenToWalk = if (getBooleanRadioGroupValue(petWalkRadioGroup)!!) petWalkEditText.text.toString() else null,
                    warningsWhileWalking = if (getBooleanRadioGroupValue(petWalkRadioGroup)!!) petWalkWarningsEditText.text.toString() else null,
                    needsPlaying = getBooleanRadioGroupValue(petPlayRadioGroup)!!,
                    howToPlay = if (getBooleanRadioGroupValue(petPlayRadioGroup)!!) petPlayEditText.text.toString() else null,
                    howToWash = petWashEditText.text.toString(),
                    otherRecs = petRecsEditText.text.toString(),
                    picture = null
                )
                val order = Order(
                    pet = pet,
                    createDate = Calendar.getInstance().time,
                    isCancelled = false,
                    isTemporary = args.startDate != -1L, // if startDate exists then order is temporary
                    startDate = if (args.startDate != -1L) Date(args.startDate) else null,
                    endDate = if (args.startDate != -1L) Date(args.endDate) else null
                )
                createOrderViewModel.postOrder(order)
            }
        }

        lifecycleScope.launchWhenStarted {
            createOrderViewModel.state.collect { state ->
                when(state) {
                    is BaseState.Loading -> {}
                    is BaseState.Success -> {
                        if (state.data is Order) {
                            Log.i(TAG, "inputStream is $imgInputStream")
                            if (imgInputStream != null) {
                                Log.i(TAG, "posting image")
                                createOrderViewModel.postPetImage(imgInputStream!!, state.data.pet!!.id)
                            }
                        }
                    }
                    is BaseState.Error -> {}
                }
            }
        }
    }

    private fun getRadioGroupValue(radioGroup: RadioGroup): String {
        return requireActivity().findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
    }

    private fun getBooleanRadioGroupValue(radioGroup: RadioGroup): Boolean? {
        return when(getRadioGroupValue(radioGroup).lowercase()) {
            requireContext().getString(R.string.yes).lowercase() -> true
            requireContext().getString(R.string.no).lowercase() -> false
            else -> null

        }
    }

}