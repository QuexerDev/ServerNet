<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateBungeeBackendPlayersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('bungee_backend_players', function (Blueprint $table) {
            $table->bigIncrements('id');
            $table->string('uuid');
            $table->text('banPlayer');
            $table->text('mutePlayer');
            $table->text('friendPlayer');
            $table->text('date');
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
        Schema::dropIfExists('bungee_backend_players');
    }
}
