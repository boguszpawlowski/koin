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
package org.koin.android.ext.android

import android.content.ComponentCallbacks
import org.koin.android.ext.koin.context
import org.koin.core.KoinContext
import org.koin.core.parameter.ParameterDefinition
import org.koin.core.parameter.emptyParameterDefinition
import org.koin.standalone.StandAloneContext

/**
 * ComponentCallbacks extensions for Android
 *
 * @author Arnaud Giuliani
 */

/**
 * inject lazily given dependency for Android component
 * @param name - bean name / optional
 * @param parameters - injection parameters
 */
inline fun <reified T> ComponentCallbacks.inject(
    name: String = "",
    module: String? = null,
    noinline parameters: ParameterDefinition = emptyParameterDefinition()
) = lazy { (StandAloneContext.koinContext as KoinContext).get<T>(name, module, parameters) }

/**
 * get given dependency for Android component
 * @param name - bean name
 * @param parameters - injection parameters
 */
inline fun <reified T> ComponentCallbacks.get(
    name: String = "",
    module: String? = null,
    noinline parameters: ParameterDefinition = emptyParameterDefinition()
): T = (StandAloneContext.koinContext as KoinContext).get(name, module, parameters)

/**
 * lazy inject given property for Android component
 * @param key - key property
 * throw MissingPropertyException if property is not found
 */
inline fun <reified T> ComponentCallbacks.property(key: String): Lazy<T> =
    lazy { (StandAloneContext.koinContext as KoinContext).getProperty<T>(key) }

/**
 * lazy inject  given property for Android component
 * give a default value if property is missing
 *
 * @param key - key property
 * @param defaultValue - default value if property is missing
 *
 */
inline fun <reified T> ComponentCallbacks.property(key: String, defaultValue: T): Lazy<T> =
    lazy { (StandAloneContext.koinContext as KoinContext).getProperty(key, defaultValue) }

/**
 * Set a property
 *
 * @param key
 * @param value
 */
fun ComponentCallbacks.setProperty(key: String, value: Any): Unit =
    context().setProperty(key, value)

/**
 * Release a Module from given Path
 * @param path
 */
fun ComponentCallbacks.release(path: String): Unit = context().release(path)