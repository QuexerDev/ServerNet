<?php

namespace App\Http\Controllers;

use App\SpigotBackendPlayer;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class SpigotBackendPlayerController extends Controller
{
    public function get(Request $request) {

        $uuid = $request->input('uuid');
        $backendPlayer = DB::collection('spigot_backend_players')->where('uuid', $uuid)->first();
        if ($backendPlayer !== null) {
            return json_encode($backendPlayer);
        } else {

            $data = collect(json_decode('{"uuid":"'.$uuid.'","data":{"coins":"10000","keys":"3","cpr":"464","elo":1000,"nick":false},"lobbyPlayer":{"gadgets":[],"chests":[]}}', true));
            $backendPlayer = SpigotBackendPlayer::create(['uuid' => $uuid, 'data' => $data->get("data"), 'lobbyPlayer' => $data->get("lobbyPlayer")]);
            return json_encode($backendPlayer);
        }
    }

    public function update(Request $request) {
        $uuid = $request->input('uuid');
        $backendPlayer = DB::collection('spigot_backend_players')->where('uuid', $uuid)->first();
        if ($backendPlayer !== null) {

            return json_encode($backendPlayer);
        } else {
            return json_encode('Dieser Spieler existiert nicht in der Datenbank');
        }
    }
}
