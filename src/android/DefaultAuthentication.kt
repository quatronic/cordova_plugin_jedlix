/*
 * Copyright 2022 Jedlix B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.quatronic.jedlixplugin

import android.content.Context
import android.content.SharedPreferences

class DefaultAuthentication(private val context: Context) : Authentication {

    companion object {
        private const val PREFERENCE_FILE_KEY = "com.jedlix.api.example.DEFAULT_AUTHENTICATION_MANAGER"
        private const val HAS_CREDENTIALS_KEY = "HAS_CREDENTIALS"
        private const val TOKEN_KEY = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik5VTTJPRGxFT0VSQlJqVkNPRFEwTVRVeVJrTkdRMFF4UTBWRk9EQXpRek0wUWprelF6SkZRZyJ9.eyJpc3MiOiJodHRwczovL2plZGxpeC1iMmUuZXUuYXV0aDAuY29tLyIsInN1YiI6IjlQTjlpRzhFMDZvZDZtZzlRRTRZcHEwbTBBeDZDTFJsQGNsaWVudHMiLCJhdWQiOiJodHRwczovL3FhLXNtYXJ0Y2hhcmdpbmcuamVkbGl4LmNvbSIsImlhdCI6MTY4NTUyNjQ4NCwiZXhwIjoxNjg2NzM2MDg0LCJhenAiOiI5UE45aUc4RTA2b2Q2bWc5UUU0WXBxMG0wQXg2Q0xSbCIsInNjb3BlIjoicmVhZDphbGwtc2Vzc2lvbnMgdXNlci1yZXNvdXJjZXMgcmVhZDphbGwtY2hhcmdpbmdsb2NhdGlvbnMgY3JlYXRlOnVzZXIgcmVhZDphbGwtdmVoaWNsZXMgcmVhZDphbGwtdXNlcnMgdGVuYW50Om5leHRlbmVyZ3kiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.QlxtpIdFHHALwg9CmU-0_0P-usuA0b_Jx4lirOnri5F_xkLYbdF6UGXW_mIK1fm4mLOIZuB6bDAUkjit1_HCITJq2tjxx2BXi2GgzLiNj8kM10jl8_mzEgAkp3ypFYeN8ItSArpkgqLYo8Y4KirYImlNqdc5Y-lwgYPCPknvwL2YNv4_diZFt8OHQQVVorFYhUDZVk0tP0ftiqvETXQFiKXyMAe_IHQ8fTMFlo-u7a0iX67ZAv1ZQqp_XaIov_C2Uemw8WvbQ0KdUFtgx55sH7xky8812EPJ4qudeN5hSkmIgpX3KfiEFGJ4IGdiYtKQ_0VGalOCzISZe44lgOobQQ"
        private const val USER_IDENTIFIER_KEY = "161fa08b-5e5a-403e-9372-bcf5e9378451"
    }

    private data class Credentials(val accessToken: String, val userIdentifier: String)

    private fun Context.sharedAuthenticationPreference(): SharedPreferences =
        applicationContext.getSharedPreferences(
            PREFERENCE_FILE_KEY,
            Context.MODE_PRIVATE
        )

    private fun Context.updateAuthenticationPreference(action: SharedPreferences.Editor.() -> Unit) =
        with(applicationContext.sharedAuthenticationPreference().edit()) {
            action()
            apply()
        }

    private var credentials: Credentials? = context.sharedAuthenticationPreference().let {
        if (it.getBoolean(HAS_CREDENTIALS_KEY, false)) {
            Credentials(it.getString(TOKEN_KEY, "")!!, it.getString(USER_IDENTIFIER_KEY, "")!!)
        } else {
            null
        }
    }

    override val isAuthenticated: Boolean
        get() = credentials != null

    override fun deauthenticate() {
        credentials = null
        context.updateAuthenticationPreference {
            clear()
        }
    }

    override suspend fun getAccessToken(): String? = credentials?.accessToken

    override suspend fun getUserIdentifier(): AuthenticationResponse =
        credentials?.let { AuthenticationResponse.Success(it.userIdentifier) }
            ?: AuthenticationResponse.Failure("No Credentials")

    fun setCredentials(token: String, userIdentifier: String): AuthenticationResponse.Success {
        credentials = Credentials(token, userIdentifier)
        context.updateAuthenticationPreference {
            putBoolean(HAS_CREDENTIALS_KEY, true)
            putString(TOKEN_KEY, token)
            putString(USER_IDENTIFIER_KEY, userIdentifier)
        }
        return AuthenticationResponse.Success(userIdentifier)
    }
}
