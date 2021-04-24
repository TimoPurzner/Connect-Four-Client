package de.aiarena.community

fun main(args: Array<String>) {
    SimpleConnectFourClient("ai-arena.de", 1805, "6eb9662f5cf3a220432df2f474a0ecbaa777889f7b038a0ea47649f023eef3bf")

    while (true) {
        Thread.sleep(5000)
    }
}

class SimpleConnectFourClient(host: String, port: Int, secret: String) {
    private val client: AMTPClient

    init {
        client = AMTPClient(host, port, secret, this::onMessage, debug = true)
    }

    private val warZone = WarZone()
    private fun onMessage(msg: MessageFromServer, myTurn: Boolean) {
        val rnd = java.util.Random()
        println("msg: " + msg.code)
        println("msg: " + msg.headers.toString())
        println("myTurn: $myTurn")

        if (msg.headers.containsKey("Winner")) {
            if (!myTurn) {
                warZone.updateField(msg.headers["Column"]?.toInt(), Coins.ENEMY)
                println("Enemy put Coin in " + msg.headers["Column"])
                warZone.printField()
            }
            if (myTurn) {
                println("MEEEE WOOOOONNN!!! ¯\\_(ツ)_/¯")
            } else {
                println("YOU LOOSE FATALITY")
            }
            warZone.printField()
        }

        if(!myTurn) return

        // Wenn mein turn ist hat der gegner vorher sein zug gesetzt und ich erhalte ihn jetzt hier
        warZone.updateField(msg.headers["Column"]?.toInt(), Coins.ENEMY)
        println("Enemy put Coin in " + msg.headers["Column"])
        warZone.printField()

        var column = CalculateWinning(warZone).runMatrix()

        // do random if no win is possible
        if (column == null) {
            println("Gernarte Random Number!")
            column = rnd.nextInt(7)
            while (warZone.map[0][column!!] != Coins.EMPTY) {
                column = rnd.nextInt(7)
            }
        }

        val myAction = buildMessageToServer(column)
        warZone.updateField(column, Coins.ME)
        println("Me put Coin in $column")
        warZone.printField()

        sendTurn(myAction)
    }

    private fun sendTurn(myAction: MessageToServer) {
        client.send(myAction)
        { resp
            ->
            if (resp.code != 0) {
                println("PANIC " + resp.code)
            }
        }
    }

    private fun buildMessageToServer(column: Int) = MessageToServer(
        "GAME",
        "AMTP/0.0",
        hashMapOf(
            "Action" to "Put",
            "Column" to column.toString()
        )
    )

}
