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

package com.ygmodesto.modernfit.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ygmodesto.modernfit.converters.GsonConverterFactory;
import com.ygmodesto.modernfit.services.ResponseCallback;
import com.ygmodesto.modernfit.services.ModernfitException;
import com.ygmodesto.modernfit.volley.AndroidModernfitContext;
import com.google.gson.GsonBuilder;


public class MainActivity extends AppCompatActivity {

    private static final UpdateUserTO updateUserTO = new UpdateUserTO(23L, "Lamar", "lamar");

    /**
     * The rest service returns in the response body what has been sent to it in the x-pong-response header.
     */
    private PongRepository pongRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.bodyTextView);
        textView.setText(updateUserTO.toString());

        final TextView responseTextView = (TextView) findViewById(R.id.responseTextView);

        AndroidModernfitContext.setContext(this.getApplicationContext());


        //GsonBuilder gsonBuilder = new GsonBuilder();
        //Customize gson properties, serializers or deserializer
        //Gson gson = gsonBuilder.create();
        //GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        //Default implementation, if you need a custom gson uncomment
        //code for gsonConverterFactory
        pongRepository = PongRepositoryImpl.builder()
        //        .addConverterFactory(gsonConverterFactory)
                .build();
        pongRepository.postPongResponse(updateUserTO, new ResponseCallback<User>(){

            @Override
            public void onSuccess(User response) throws ModernfitException {
                Log.d("ModernfitAndroid", "user post: " + response.toString());
                responseTextView.setText(response.toString());
            }

            @Override
            public void onFailure(ModernfitException e) {
                Log.d("ModernfitAndroid", "Exception: " + e);
            }
        });

        Log.d("ModernfitAndroid", "end");
    }

}
