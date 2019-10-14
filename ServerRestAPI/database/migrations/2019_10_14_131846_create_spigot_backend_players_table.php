<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSpigotBackendPlayersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('spigot_backend_players', function (Blueprint $table) {
            $table->bigIncrements('id');
            $table->string('uuid');
            $table->text('data');
            $table->text('lobbyPlayer');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('spigot_backend_players');
    }
}
