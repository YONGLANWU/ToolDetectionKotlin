package com.example.fox_04.tooldetectionkotlin.utils

import java.util.ArrayList
import java.util.regex.Pattern

/**
 * Created by fox_04 on 2018/3/22.
 */
class StringFormatUtil {
    companion object {
        fun parseStringFormat(txt: String?): List<String>? {
            val list = ArrayList<String>()
            try {
                if (txt != null && txt.length > 0) {
                    val ss = txt.split("string=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    for (i in 1 until ss.size) {
                        if (i == ss.size - 1) {
                            list.add(ss[i].substring(0, ss[i].length - 3))
                        } else {
                            list.add(ss[i].substring(0, ss[i].length - 2))
                        }
                    }
                }
            } catch (e: Exception) {
                return null
            }

            return list
        }

        /**
         * 去除字符串中的特殊字符
         * @param str
         * @return
         */
        fun replaceBlank(str: String?): String {
            var dest = ""
            if (str != null) {
                val p = Pattern.compile("\\s*|\t|\r|\n")
                val m = p.matcher(str)
                dest = m.replaceAll("")
            }
            return dest
        }
    }
}