package com.journaler.api

import com.journaler.model.Note
import com.journaler.model.Todo
import retrofit2.Call
import retrofit2.http.*

interface JournalerBackendService {

    @POST("user/authenticate")
    fun login(
            @HeaderMap headers: Map<String, String>,
            @Body payload: UserLoginRequest
    ): Call<JournalerApiToken>

    @GET("entity/note")
    fun getNotes(
            @HeaderMap headers: Map<String, String>
    ): Call<List<Note>>

    @GET("entity/todo")
    fun getTodos(
            @HeaderMap headers: Map<String, String>
    ): Call<List<Todo>>

    @PUT("entity/note")
    fun publishNotes(
            @HeaderMap headers: Map<String, String>,
            @Body payload: List<Note>
    ): Call<Unit>

    @PUT("entity/todo")
    fun publishTodos(
            @HeaderMap headers: Map<String, String>,
            @Body payload: List<Todo>
    ): Call<Unit>

    @DELETE("entity/note")
    fun removeNotes(
            @HeaderMap headers: Map<String, String>,
            @Body payload: List<Note>
    ): Call<Unit>

    @DELETE("entity/todo")
    fun removeTodos(
            @HeaderMap headers: Map<String, String>,
            @Body payload: List<Todo>
    ): Call<Unit>

}