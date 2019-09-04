import java.lang.IllegalArgumentException
import java.util.HashSet
import kotlin.math.max

fun checkSimiliarity2(template: String, query: String): Double? {
    val s1 = query.length
    val s2 = template.length
    val range = (max(s1, s2) / 2) - 1
    val match1 = mutableMapOf<Int, Char>()
    val match2 = mutableMapOf<Int, Char>()
    var prefixes = 0

    fun cekForwardAndBack(i: Int) {
        val fPoint = if (i.plus(range) > s1) s2 else i.plus(range)
        val bPoint = if (i.minus(range) <= 0) 0 else i.minus(range)
        fcek@ for (fcek in i.plus(1) until (fPoint)) {
            if (query[i] == template[fcek]) {
                match1[i] = query[i]
                match2[fcek] = template[fcek]
                break@fcek
            } else {
                query[i] == template[fcek]
            }
        }

        if (i.minus(1) >= 0) {
            bcek@ for (bcek in i.minus(1) downTo (bPoint)) {
                if (query[i] == template[bcek]) {
                    println("query :  ${query[i]}  template : ${template[bcek]} ")
                    match1[i] = query[i]
                    match2[bcek] = template[bcek]
                    break@bcek
                }
            }
        }
    }

    if (s1 <= s2) { // lakukan pengecekan hanya jika query lebih pendek atau sama panjang dengan template
        for (i in 0 until s1) {
            if (query[i] == template[i]) {
                match1[i] = query[i]
                match2[i] = template[i]
                prefixes++
                continue
            } else if (i <= range) {
                cekForwardAndBack(i)
                continue
            } else if (i >= range) {
                cekForwardAndBack(i)
                continue
            }
        }

        val sortedMatch1 = match1.toSortedMap()
        val sortedMatch2 = match2.toSortedMap()

        var nonExactMatch = 0
        var transposition = 0
        val compareValue =sortedMatch1.values.toString() == sortedMatch2.values.toString()

        if (compareValue) {
            transposition = 0
        } else {
            for (a in 0..sortedMatch1.size) {
                //println("match 1 : ${sortedMatch1[a]} match 2 : ${sortedMatch2[a]}  ")
                if ( sortedMatch1[a] != sortedMatch2[a]) nonExactMatch++
            }
            transposition = nonExactMatch /2
        }

        if (match1.isEmpty()) {
            return 0.0
        }
        else {
            val s1 = s1.toDouble()
            val s2 = s2.toDouble()
            val m = match1.size.toDouble()
            val sm1 = m / s1
            val sm2 = m / s2
            val sm3: Double = (m - transposition) / m

            val jaro = 0.33333 * (sm1 + sm2 + sm3)
            //println("jaro : $jaro")
            val p = 0.10
            val l = if (prefixes < 4) prefixes else 4
            val jw = jaro + (l * p) * (1 - jaro)
            //println(" jaro winkler : $jw")

            return jw
        }
    }
    return throw IllegalArgumentException("template length must longer than query")
}


fun main() {
    println(checkSimiliarity2("DICKSONX", "DIXON"))

}

