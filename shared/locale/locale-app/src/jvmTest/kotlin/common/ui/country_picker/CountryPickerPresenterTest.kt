package common.ui.country_picker

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.testIn
import common.framework.CoreObjectMocker
import common.framework.mockCoroutineDispatcher
import id.djaka.droidjam.shared.locale.app.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.shared.locale.app.domain.SearchCountryUseCases
import id.djaka.droidjam.shared.locale.app.presenter.country_picker.CountryPickerPresenterImpl
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerEvent
import id.djaka.droidjam.shared.locale.presentation.api.model.country_picker.CountryPickerModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CountryPickerPresenterTest {
    private lateinit var presenter: CountryPickerPresenterImpl
    private lateinit var events: MutableSharedFlow<CountryPickerEvent>

    private lateinit var searchCountryUseCases: SearchCountryUseCases
    private lateinit var saveRecentCountryUseCase: SaveRecentCountryUseCase
    private val testDispatcher = UnconfinedTestDispatcher()
    @Before
    fun setup() {
        mockCoroutineDispatcher(testDispatcher)

        events = MutableSharedFlow()
        searchCountryUseCases = CoreObjectMocker.mockSearchCountryUseCases()
        saveRecentCountryUseCase = CoreObjectMocker.mockSaveRecentCountryUseCase()

        presenter = CountryPickerPresenterImpl(searchCountryUseCases, saveRecentCountryUseCase)
    }

    @After
    fun clear() {
        clearAllMocks()
    }

    @Test
    fun `should populate initial search on load`() = runTest {
        // Given
        val expectedInitialState = listOf(
            CoreObjectMocker.mockCountryPickerItemPicker(
                CoreObjectMocker.mockCountryCodeModel(name = "1")
            )
        )
        every { searchCountryUseCases.getSearchCountryCodeInitialStateFlow() } returns flowOf(expectedInitialState)

        // When
        val turbine = moleculeFlow(RecompositionClock.Immediate) { presenter.present(events) }.testIn(this)

        // Then
        val lastState = turbine.expectMostRecentItem()
        val expectedState = CountryPickerModel.CountryListState.Success(
            expectedInitialState
        )
        assertEquals(expectedState, lastState.countryListState)

        turbine.cancel()
    }


    @Test
    fun `should change query when query changed`() = runTest {
        // Setup

        val turbine = moleculeFlow(RecompositionClock.Immediate) { presenter.present(events) }.testIn(this)
        turbine.expectMostRecentItem()

        // Given
        val expectedQuery = "query"

        // When
        events.emit(CountryPickerEvent.SearchBoxChanged(expectedQuery))

        // Then
        val lastState = turbine.expectMostRecentItem()
        assertEquals(expectedQuery, lastState.searchBox)

        turbine.cancel()
    }

    @Test
    fun `should set country codes with filtered result when query changed`() = runTest {
        // Setup
        val turbine = moleculeFlow(RecompositionClock.Immediate) { presenter.present(events) }.testIn(this)
        turbine.expectMostRecentItem()

        // Given
        val expectedQuery = "query"
        val expectedItems = listOf(CoreObjectMocker.mockCountryPickerItemPicker(CoreObjectMocker.mockCountryCodeModel(name = "2")))
        coEvery { searchCountryUseCases.searchCountryCodeFilter(expectedQuery) } returns expectedItems

        // When
        events.emit(CountryPickerEvent.SearchBoxChanged(expectedQuery))

        // Then
        testDispatcher.scheduler.advanceUntilIdle()
        val lastState = turbine.expectMostRecentItem()
        val expectedState = CountryPickerModel.CountryListState.Success(
            expectedItems
        )
        assertEquals(expectedState, lastState.countryListState)

        turbine.cancel()
    }


    @Test
    fun `should change selected country when item clicked`() = runTest {
        // Setup
        every { searchCountryUseCases.getSearchCountryCodeInitialStateFlow() } returns flowOf(listOf())
        every { saveRecentCountryUseCase(any()) } just runs

        val turbine = moleculeFlow(RecompositionClock.Immediate) { presenter.present(events) }.testIn(this)
        turbine.expectMostRecentItem()

        // Given
        val expectedSelectedCountry = CoreObjectMocker.mockCountryCodeModel()

        // When
        events.emit(CountryPickerEvent.ItemClicked(expectedSelectedCountry))

        // Then
        val lastState = turbine.expectMostRecentItem()
        assertEquals(expectedSelectedCountry, lastState.selectedCountry)

        turbine.cancel()
    }

    @Test
    fun `should save selected country to recent item when item clicked`() = runTest {
        // Setup
        every { saveRecentCountryUseCase(any()) } just runs

        val turbine = moleculeFlow(RecompositionClock.Immediate) { presenter.present(events) }.testIn(this)
        turbine.expectMostRecentItem()

        // Given
        val expectedSelectedCountry = CoreObjectMocker.mockCountryCodeModel()

        // When
        events.emit(CountryPickerEvent.ItemClicked(expectedSelectedCountry))

        // Then
        turbine.expectMostRecentItem()
        verify(exactly = 1) { saveRecentCountryUseCase(expectedSelectedCountry.code) }

        turbine.cancel()
    }

}