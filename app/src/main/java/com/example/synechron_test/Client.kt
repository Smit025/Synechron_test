package com.example.synechron_test

import okhttp3.*
import org.json.JSONObject


class Client {
    companion object {
        fun sendRequest(): OkHttpClient {
            val client = OkHttpClient.Builder()
            val okHttpClient = client.build()
            return okHttpClient
        }

        fun getRequest(url: String): Request {
            val request = Request.Builder()
                .url(url)
                .build()
            return request
        }


    }
}