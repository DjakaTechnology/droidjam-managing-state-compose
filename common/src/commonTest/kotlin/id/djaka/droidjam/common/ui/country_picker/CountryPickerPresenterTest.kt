package id.djaka.droidjam.common.ui.country_picker

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.testIn
import id.djaka.droidjam.common.framework.CoreObjectMocker
import id.djaka.droidjam.common.domain.SaveRecentCountryUseCase
import id.djaka.droidjam.common.domain.SearchCountryUseCases
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var presenter: CountryPickerPresenter
    private lateinit var events: MutableSharedFlow<CountryPickerPresenter.Event>

    private val searchCountryUseCases: SearchCountryUseCases = mockk()
    private val saveRecentCountryUseCase: SaveRecentCountryUseCase = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        presenter = CountryPickerPresenter(searchCountryUseCases, saveRecentCountryUseCase)
        events = MutableSharedFlow()
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
        val expectedState = CountryPickerPresenter.UIState.CountryListState.Success(
            expectedInitialState
        )
        assertEquals(expectedState, lastState.countryListState)
        turbine.cancel()
    }


    @Test
    fun `should change query when query changed`() = runTest {
        // Setup
        every { searchCountryUseCases.getSearchCountryCodeInitialStateFlow() } returns flowOf(listOf())
        coEvery { searchCountryUseCases.searchCountryCodeFilter(any()) } returns listOf()

        val turbine = moleculeFlow(RecompositionClock.Immediate) { presenter.present(events) }.testIn(this)
        turbine.expectMostRecentItem()

        // Given
        val expectedQuery = "query"

        // When
        events.emit(CountryPickerPresenter.Event.SearchBoxChanged(expectedQuery))

        // Then
        val lastState = turbine.expectMostRecentItem()
        assertEquals(expectedQuery, lastState.searchBox)

        turbine.cancel()
    }

    @Test
    fun `should set country codes with filtered result when query changed`() = runTest {
        // Setup
        every { searchCountryUseCases.getSearchCountryCodeInitialStateFlow() } returns flowOf(listOf())
        val turbine = moleculeFlow(RecompositionClock.Immediate) { presenter.present(events) }.testIn(this)
        turbine.expectMostRecentItem()

        // Given
        val expectedQuery = "query"
        val expectedItems = listOf(CoreObjectMocker.mockCountryPickerItemPicker(CoreObjectMocker.mockCountryCodeModel(name = "2")))
        coEvery { searchCountryUseCases.searchCountryCodeFilter(expectedQuery) } returns expectedItems

        // When
        events.emit(CountryPickerPresenter.Event.SearchBoxChanged(expectedQuery))

        // Then
        val lastState = turbine.expectMostRecentItem()
        val expectedState = CountryPickerPresenter.UIState.CountryListState.Success(
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
        events.emit(CountryPickerPresenter.Event.ItemClicked(expectedSelectedCountry))

        // Then
        val lastState = turbine.expectMostRecentItem()
        assertEquals(expectedSelectedCountry, lastState.selectedCountry)

        turbine.cancel()
    }

    @Test
    fun `should save selected country to recent item when item clicked`() = runTest {
        // Setup
        every { searchCountryUseCases.getSearchCountryCodeInitialStateFlow() } returns flowOf(listOf())
        every { saveRecentCountryUseCase(any()) } just runs

        val turbine = moleculeFlow(RecompositionClock.Immediate) { presenter.present(events) }.testIn(this)
        turbine.expectMostRecentItem()

        // Given
        val expectedSelectedCountry = CoreObjectMocker.mockCountryCodeModel()

        // When
        events.emit(CountryPickerPresenter.Event.ItemClicked(expectedSelectedCountry))

        // Then
        turbine.expectMostRecentItem()
        verify(exactly = 1) { saveRecentCountryUseCase(expectedSelectedCountry.code) }

        turbine.cancel()
    }

}