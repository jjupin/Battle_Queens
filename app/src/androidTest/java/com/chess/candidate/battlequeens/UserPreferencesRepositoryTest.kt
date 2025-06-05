package com.chess.candidate.battlequeens

import androidx.test.platform.app.InstrumentationRegistry
import com.chess.candidate.battlequeens.features.preferences.UserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class UserPreferencesRepositoryTest {

    @get:Rule
    val temporaryFolder: TemporaryFolder = TemporaryFolder.builder()
        .assureDeletion()
        .build()

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.chess.candidate.battlequeens", appContext.packageName)
    }

    @Test
    fun readFirstTimePreference(): Unit = runTest {
        val userPreferencesRepository = UserPreferencesRepository.getRepository(appContext)

        // ...when the string is returned from the object under test...
        val result: Boolean = userPreferencesRepository.userPrefs.value.isFirstTime

        // ...then the result should be the expected one.
        assert(result) { "Expected isFirstTime to be true, but was $result" }

        // now we can set it to false
        userPreferencesRepository.updateUserPreferences(
            userPreferencesRepository.userPrefs.value.copy(
                isFirstTime = false
            )
        )

        // ...and check that it is now false
        val updatedResult: Boolean = userPreferencesRepository.userPrefs.value.isFirstTime
        assert(!updatedResult) { "Expected isFirstTime to be false, but was $updatedResult" }
    }

    @Test
    fun readAndUpdateWallpaperPreference(): Unit = runTest {
        val userPreferencesRepository = UserPreferencesRepository.getRepository(appContext)

        // ...when the wallpaper is returned from the object under test...
        val initialWallpaper = userPreferencesRepository.userPrefs.value.wallpaper

        // ...then the result should be the expected one.
        assertEquals(0, initialWallpaper)  // Assuming the initial wallpaper is set to 0 (default)

        // now we can set it to a new value
        val newWallpaper = 4
        userPreferencesRepository.updateUserPreferences(
            userPreferencesRepository.userPrefs.value.copy(
                wallpaper = newWallpaper
            )
        )

        // ...and check that it is now updated
        val updatedWallpaper = userPreferencesRepository.userPrefs.value.wallpaper
        assertEquals(newWallpaper, updatedWallpaper)
    }
}
