<?php

use App\SpigotBackendPlayer;
use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});
Route::middleware('auth:api')->post('/create_spigotbackendplayer', 'SpigotBackendPlayerController@handle');

Route::middleware('auth:api')->post('/get_spigotbackendplayer', 'SpigotBackendPlayerController@get');
Route::middleware('auth:api')->post('/get_bungeebackendplayer', function (Request $request) {
    return \App\SpigotBackendPlayer::first()->toJson();
});
Route::middleware('auth:api')->post('/get_serverpermsplayer', function (Request $request) {
    return \App\SpigotBackendPlayer::first()->toJson();
});
