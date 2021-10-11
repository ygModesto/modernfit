/*
 * Copyright 2020 Yago Modesto González Diéguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ygmodesto.modernfit.volley;

import android.content.Context;

import com.ygmodesto.modernfit.services.ModernfitException;

/**
 * Class to set de default Context.
 * Used to create a default ClientVolley.
 */
public class AndroidModernfitContext {

    private static Context context;

    public static Context getContext() {
        if (context == null) {
            throw new ModernfitException("context = null; It is necessary to call AndroidModernfitContext.setContext before");
        }

        return context;
    }

    public static void setContext(Context context) {
        AndroidModernfitContext.context = context;
    }


}
