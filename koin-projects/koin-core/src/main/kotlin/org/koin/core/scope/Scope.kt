/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.koin.core.scope

import org.koin.core.Koin
import org.koin.core.definition.Properties
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

data class Scope(
    val id: ScopeID
) {
    var set: ScopeSet? = null
    var koin: Koin? = null
    val properties = Properties()
    private val callbacks = arrayListOf<ScopeCallback>()

    /**
     * Is Scope associated to Koin
     */
    internal fun isRegistered() = koin != null

    /**
     * Register in Koin instance
     */
    fun register(koin: Koin) {
        this.koin = koin
    }

    /**
     * Lazy inject a Koin instance
     * @param qualifier
     * @param parameters
     */
    inline fun <reified T> inject(
        qualifier: Qualifier? = null,
        scope: Scope = this,
        noinline parameters: ParametersDefinition? = null
    ): Lazy<T> =
        lazy { get<T>(qualifier, scope, parameters) }

    /**
     * Get a Koin instance
     * @param qualifier
     * @param parameters
     */
    inline fun <reified T> get(
        qualifier: Qualifier? = null,
        scope: Scope = this,
        noinline parameters: ParametersDefinition? = null
    ): T {
        return koin?.get(T::class, qualifier, scope, parameters)
            ?: error("$this is not registered - Koin is null")
    }

    /**
     * Retrieve a property
     * @param key
     */
    fun <T> getProperty(key: String): T {
        return koin?.getProperty(key) ?: error("No property '$key' found")
    }

    /**
     * Register a callback for this Scope Instance
     */
    fun registerCallback(callback: ScopeCallback) {
        callbacks += callback
    }

    /**
     * Close all instances from this scope
     */
    fun close() = synchronized(this) {
        set?.release(this)
        koin?.deleteScope(this.id)
        koin = null

        // call on close from callbacks
        callbacks.forEach { it.onScopeClose() }
        callbacks.clear()
    }

    override fun toString(): String {
        val scopeDef = set?.let { ",set:'${it.qualifier}'" } ?: ""
        return "Scope[id:'$id'$scopeDef]"
    }

    companion object {
        @JvmStatic
        val GLOBAL = Scope("-GLOBAL-")
    }
}

typealias ScopeID = String