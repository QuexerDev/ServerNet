<?php

namespace App;

use Jenssegers\Mongodb\Eloquent\Model as Model;

class SpigotBackendPlayer extends Model
{
    protected $collection = 'spigot_backend_players';
    protected $fillable = ['uuid', 'data', 'lobbyPlayer'];



}
