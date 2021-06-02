package de.aiarena.community

import org.junit.Test

class WarZoneTest {

    @Test
    fun check_for_winning_condition_in_row() {
        val warZone = WarZone()
        warZone.updateField(1, Coins.ME)
        warZone.updateField(2, Coins.ME)
        warZone.updateField(3, Coins.ME)
        warZone.updateField(4, Coins.ME)
        warZone.printField()
        assert(warZone.winInRowFor(Coins.ME))
    }


    @Test
    fun check_for_winning_condition_in_col() {
        val warZone = WarZone()
        warZone.updateField(1, Coins.ME);
        warZone.updateField(1, Coins.ME);
        warZone.updateField(1, Coins.ME);
        warZone.updateField(1, Coins.ME);
        warZone.printField()

        assert(warZone.winInColFor(Coins.ME))

    }

    @Test
    fun clone_class() {
        val warZone = WarZone()
        warZone.updateField(2, Coins.ME)
        val warZone2 = warZone.copy()
        warZone2.updateField(1, Coins.ME)

        assert(warZone.getRow(warZone.getNumberOfRows()-1)[1] == Coins.EMPTY)
        assert(warZone2.getRow(warZone2.getNumberOfRows()-1)[1] == Coins.ME)
    }

    @Test
    fun check_for_winning_condition_anywhere() {
        val warZone = WarZone()
        warZone.updateField(2, Coins.ME)
        warZone.updateField(2, Coins.ME)
        warZone.updateField(2, Coins.ME)
        warZone.printField()

        assert(CalculateWinning(warZone).runMatrix(Coins.ME) == 2)
    }

    @Test
    fun check_depth_Calc() {
        val warZone = WarZone()
        warZone.updateField(2, Coins.ME)

        val test = CalculateWinning(warZone).calcTurn(warZone, Coins.ME)
        println(test)
    }

}
