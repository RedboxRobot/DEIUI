package com.delicloud.app.deiui.utils

import android.text.TextUtils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.experimental.and

/**
 * 字符串工具类，提供一些字符串相关的便捷方法
 */
class StringUtil private constructor() {

    init {
        throw AssertionError()
    }

    companion object {

        /**
         * is null or its length is 0 or it is made by space
         *
         *
         * <pre>
         * isBlank(null) = true;
         * isBlank(&quot;&quot;) = true;
         * isBlank(&quot;  &quot;) = true;
         * isBlank(&quot;a&quot;) = false;
         * isBlank(&quot;a &quot;) = false;
         * isBlank(&quot; a&quot;) = false;
         * isBlank(&quot;a b&quot;) = false;
        </pre> *
         *
         * @param str str
         * @return if string is null or its size is 0 or it is made by space, return
         * true, else return false.
         */
        fun isBlank(str: String?): Boolean {

            return str == null || str.trim { it <= ' ' }.length == 0
        }

        /**
         * get length of CharSequence
         *
         *
         * <pre>
         * length(null) = 0;
         * length(\"\") = 0;
         * length(\"abc\") = 3;
        </pre> *
         *
         * @param str str
         * @return if str is null or empty, return 0, else return [ ][CharSequence.length].
         */
        fun length(str: CharSequence?): Int {

            return str?.length ?: 0
        }


        /**
         * null Object to empty string
         *
         *
         * <pre>
         * nullStrToEmpty(null) = &quot;&quot;;
         * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
         * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
        </pre> *
         *
         * @param str str
         * @return String
         */
        fun nullStrToEmpty(str: Any?): String {

            return if (str == null)
                ""
            else
                str as? String ?: str.toString()
        }


        /**
         * @param str str
         * @return String
         */
        fun capitalizeFirstLetter(str: String): String? {

            if (isEmpty(str)) {
                return str
            }

            val c = str[0]
            return if (!Character.isLetter(c) || Character.isUpperCase(c))
                str
            else
                StringBuilder(str.length).append(
                    Character.toUpperCase(c)
                )
                    .append(str.substring(1))
                    .toString()
        }


        /**
         * encoded in utf-8
         *
         * @param str 字符串
         * @return 返回一个utf8的字符串
         */
        fun utf8Encode(str: String): String? {

            if (!isEmpty(str) && str.toByteArray().size != str.length) {
                try {
                    return URLEncoder.encode(str, "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    throw RuntimeException(
                        "UnsupportedEncodingException occurred. ", e
                    )
                }

            }
            return str
        }


        /**
         * @param href 字符串
         * @return 返回一个html
         */
        fun getHrefInnerHtml(href: String): String {

            if (isEmpty(href)) {
                return ""
            }

            val hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*"
            val hrefPattern = Pattern.compile(
                hrefReg,
                Pattern.CASE_INSENSITIVE
            )
            val hrefMatcher = hrefPattern.matcher(href)
            return if (hrefMatcher.matches()) {
                hrefMatcher.group(1)
            } else href
        }


        /**
         * @param source 字符串
         * @return 返回htmL到字符串
         */
        fun htmlEscapeCharsToString(source: String): String? {

            return if (StringUtil.isEmpty(source))
                source
            else
                source.replace("&lt;".toRegex(), "<")
                    .replace("&gt;".toRegex(), ">")
                    .replace("&amp;".toRegex(), "&")
                    .replace("&quot;".toRegex(), "\"")
        }


        /**
         * @param s str
         * @return String
         */
        fun fullWidthToHalfWidth(s: String): String? {

            if (isEmpty(s)) {
                return s
            }

            val source = s.toCharArray()
            for (i in source.indices) {
                if (source[i].toInt() == 12288) {
                    source[i] = ' '
                    // } else if (source[i] == 12290) {
                    // source[i] = '.';
                } else if (source[i].toInt() >= 65281 && source[i].toInt() <= 65374) {
                    source[i] = (source[i].toInt() - 65248).toChar()
                } else {
                    source[i] = source[i]
                }
            }
            return String(source)
        }


        /**
         * @param s 字符串
         * @return 返回的数值
         */
        fun halfWidthToFullWidth(s: String): String? {

            if (isEmpty(s)) {
                return s
            }

            val source = s.toCharArray()
            for (i in source.indices) {
                if (source[i] == ' ') {
                    source[i] = 12288.toChar()
                    // } else if (source[i] == '.') {
                    // source[i] = (char)12290;
                } else if (source[i].toInt() >= 33 && source[i].toInt() <= 126) {
                    source[i] = (source[i].toInt() + 65248).toChar()
                } else {
                    source[i] = source[i]
                }
            }
            return String(source)
        }


        /**
         * @param str 资源
         * @return 特殊字符串切换
         */

        fun replaceBlanktihuan(str: String?): String {

            var dest = ""
            if (str != null) {
                val p = Pattern.compile("\\s*|\t|\r|\n")
                val m = p.matcher(str)
                dest = m.replaceAll("")
            }
            return dest
        }


        /**
         * 判断给定的字符串是否为null或者是空的
         *
         * @param string 给定的字符串
         */
        fun isEmpty(string: String?): Boolean {
            return string == null || "" == string.trim { it <= ' ' }
        }


        /**
         * 判断给定的字符串是否不为null且不为空
         *
         * @param string 给定的字符串
         */
        fun isNotEmpty(string: String): Boolean {
            return !isEmpty(string)
        }


        /**
         * 判断给定的字符串数组中的所有字符串是否都为null或者是空的
         *
         * @param strings 给定的字符串
         */
        fun isEmpty(vararg strings: String): Boolean {
            var result = true
            for (string in strings) {
                if (isNotEmpty(string)) {
                    result = false
                    break
                }
            }
            return result
        }


        /**
         * 判断给定的字符串数组中是否全部都不为null且不为空
         *
         * @param strings 给定的字符串数组
         * @return 是否全部都不为null且不为空
         */
        fun isNotEmpty(vararg strings: String): Boolean {
            var result = true
            for (string in strings) {
                if (isEmpty(string)) {
                    result = false
                    break
                }
            }
            return result
        }


        /**
         * 如果字符串是null或者空就返回""
         */
        fun filterEmpty(string: String): String {
            return if (StringUtil.isNotEmpty(string)) string else ""
        }


        /**
         * 在给定的字符串中，用新的字符替换所有旧的字符
         *
         * @param string  给定的字符串
         * @param oldchar 旧的字符
         * @param newchar 新的字符
         * @return 替换后的字符串
         */
        fun replace(string: String, oldchar: Char, newchar: Char): String {
            val chars = string.toCharArray()
            for (w in chars.indices) {
                if (chars[w] == oldchar) {
                    chars[w] = newchar
                    break
                }
            }
            return String(chars)
        }


        /**
         * 把给定的字符串用给定的字符分割
         *
         * @param string 给定的字符串
         * @param ch     给定的字符
         * @return 分割后的字符串数组
         */
        fun split(string: String, ch: Char): Array<String> {
            val stringList = ArrayList<String>()
            val chars = string.toCharArray()
            var nextStart = 0
            for (w in chars.indices) {
                if (ch == chars[w]) {
                    stringList.add(String(chars, nextStart, w - nextStart))
                    nextStart = w + 1
                    if (nextStart == chars.size) {    //当最后一位是分割符的话，就再添加一个空的字符串到分割数组中去
                        stringList.add("")
                    }
                }
            }
            if (nextStart < chars.size) {    //如果最后一位不是分隔符的话，就将最后一个分割符到最后一个字符中间的左右字符串作为一个字符串添加到分割数组中去
                stringList.add(
                    String(
                        chars, nextStart,
                        chars.size - 1 - nextStart + 1
                    )
                )
            }
            return stringList.toTypedArray()
        }


        /**
         * 计算给定的字符串的长度，计算规则是：一个汉字的长度为2，一个字符的长度为1
         *
         * @param string 给定的字符串
         * @return 长度
         */
        fun countLength(string: String): Int {
            var length = 0
            val chars = string.toCharArray()
            for (w in 0 until string.length) {
                val ch = chars[w]
                if (ch >= '\u0391' && ch <= '\uFFE5') {
                    length++
                    length++
                } else {
                    length++
                }
            }
            return length
        }


        private fun getChars(chars: CharArray, startIndex: Int): CharArray {
            var endIndex = startIndex + 1
            //如果第一个是数字
            if (Character.isDigit(chars[startIndex])) {
                //如果下一个是数字
                while (endIndex < chars.size && Character.isDigit(chars[endIndex])) {
                    endIndex++
                }
            }
            val resultChars = CharArray(endIndex - startIndex)
            System.arraycopy(chars, startIndex, resultChars, 0, resultChars.size)
            return resultChars
        }


        /**
         * 是否全是数字
         */
        fun isAllDigital(chars: CharArray): Boolean {
            var result = true
            for (w in chars.indices) {
                if (!Character.isDigit(chars[w])) {
                    result = false
                    break
                }
            }
            return result
        }

        /**
         * 判断是否为整数
         * @param str 传入的字符串
         * @return 是整数返回true,否则返回false
         */
        fun isNumeric(str: String): Boolean {
            val pattern = Pattern.compile("[0-9]*")
            return pattern.matcher(str).matches()
        }

        /**
         * 删除给定字符串中所有的旧的字符
         *
         * @param string 源字符串
         * @param ch     要删除的字符
         * @return 删除后的字符串
         */
        fun removeChar(string: String, ch: Char): String {
            val sb = StringBuffer()
            for (cha in string.toCharArray()) {
                if (cha != '-') {
                    sb.append(cha)
                }
            }
            return sb.toString()
        }


        /**
         * 删除给定字符串中给定位置处的字符
         *
         * @param string 给定字符串
         * @param index  给定位置
         */
        fun removeChar(string: String, index: Int): String {
            var result: String? = null
            val chars = string.toCharArray()
            if (index == 0) {
                result = String(chars, 1, chars.size - 1)
            } else if (index == chars.size - 1) {
                result = String(chars, 0, chars.size - 1)
            } else {
                result = String(chars, 0, index) + String(chars, index + 1, chars.size - index)
            }
            return result
        }


        /**
         * 删除给定字符串中给定位置处的字符
         *
         * @param string 给定字符串
         * @param index  给定位置
         * @param ch     如果同给定位置处的字符相同，则将给定位置处的字符删除
         */
        fun removeChar(string: String, index: Int, ch: Char): String {
            var result: String? = null
            val chars = string.toCharArray()
            if (chars.size > 0 && chars[index] == ch) {
                if (index == 0) {
                    result = String(chars, 1, chars.size - 1)
                } else if (index == chars.size - 1) {
                    result = String(chars, 0, chars.size - 1)
                } else {
                    result = String(chars, 0, index) + String(chars, index + 1, chars.size - index)
                }
            } else {
                result = string
            }
            return result
        }


        /**
         * 对给定的字符串进行空白过滤
         *
         * @param string 给定的字符串
         * @return 如果给定的字符串是一个空白字符串，那么返回null；否则返回本身。
         */
        fun filterBlank(string: String): String? {
            return if ("" == string) {
                null
            } else {
                string
            }
        }


        /**
         * 将给定字符串中给定的区域的字符转换成小写
         *
         * @param str        给定字符串中
         * @param beginIndex 开始索引（包括）
         * @param endIndex   结束索引（不包括）
         * @return 新的字符串
         */
        fun toLowerCase(str: String, beginIndex: Int, endIndex: Int): String {
            return str.replaceFirst(
                str.substring(beginIndex, endIndex).toRegex(), str.substring(beginIndex, endIndex)
                    .toLowerCase(Locale.getDefault())
            )
        }


        /**
         * 将给定字符串中给定的区域的字符转换成大写
         *
         * @param str        给定字符串中
         * @param beginIndex 开始索引（包括）
         * @param endIndex   结束索引（不包括）
         * @return 新的字符串
         */
        fun toUpperCase(str: String, beginIndex: Int, endIndex: Int): String {
            return str.replaceFirst(
                str.substring(beginIndex, endIndex).toRegex(), str.substring(beginIndex, endIndex)
                    .toUpperCase(Locale.getDefault())
            )
        }


        /**
         * 将给定字符串的首字母转为小写
         *
         * @param str 给定字符串
         * @return 新的字符串
         */
        fun firstLetterToLowerCase(str: String): String {
            return toLowerCase(str, 0, 1)
        }


        /**
         * 将给定字符串的首字母转为大写
         *
         * @param str 给定字符串
         * @return 新的字符串
         */
        fun firstLetterToUpperCase(str: String): String {
            return toUpperCase(str, 0, 1)
        }


        /**
         * 将给定的字符串MD5加密
         *
         * @param string 给定的字符串
         * @return MD5加密后生成的字符串
         */
        fun MD5(string: String): String? {
            var result: String? = null
            try {
                val charArray = string.toCharArray()
                val byteArray = ByteArray(charArray.size)
                for (i in charArray.indices) {
                    byteArray[i] = charArray[i].toByte()
                }

                val hexValue = StringBuffer()
                val md5Bytes = MessageDigest.getInstance("MD5")
                    .digest(byteArray)
                for (i in md5Bytes.indices) {
                    val `val` = md5Bytes[i].toInt() and 0xff
                    if (`val` < 16) {
                        hexValue.append("0")
                    }
                    hexValue.append(Integer.toHexString(`val`))
                }

                result = hexValue.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }


        /**
         * 判断给定的字符串是否以一个特定的字符串开头，忽略大小写
         *
         * @param sourceString 给定的字符串
         * @param newString    一个特定的字符串
         */
        fun startsWithIgnoreCase(sourceString: String, newString: String): Boolean {
            val newLength = newString.length
            val sourceLength = sourceString.length
            if (newLength == sourceLength) {
                return newString.equals(sourceString, ignoreCase = true)
            } else if (newLength < sourceLength) {
                val newChars = CharArray(newLength)
                sourceString.toCharArray(newChars, 0, 0, newLength)
                return newString.equals(String(newChars), ignoreCase = true)
            } else {
                return false
            }
        }


        /**
         * 判断给定的字符串是否以一个特定的字符串结尾，忽略大小写
         *
         * @param sourceString 给定的字符串
         * @param newString    一个特定的字符串
         */
        fun endsWithIgnoreCase(sourceString: String, newString: String): Boolean {
            val newLength = newString.length
            val sourceLength = sourceString.length
            if (newLength == sourceLength) {
                return newString.equals(sourceString, ignoreCase = true)
            } else if (newLength < sourceLength) {
                val newChars = CharArray(newLength)
                sourceString.toCharArray(
                    newChars, 0, sourceLength - newLength, sourceLength
                )
                return newString.equals(String(newChars), ignoreCase = true)
            } else {
                return false
            }
        }


        /**
         * 检查字符串长度，如果字符串的长度超过maxLength，就截取前maxLength个字符串并在末尾拼上appendString
         */
        @JvmOverloads
        fun checkLength(string: String, maxLength: Int, appendString: String? = "…"): String {
            var string = string
            if (string.length > maxLength) {
                string = string.substring(0, maxLength)
                if (appendString != null) {
                    string += appendString
                }
            }
            return string
        }

        /**
         * 判断是否是中文字符
         * // GENERAL_PUNCTUATION 判断中文的“号
         * // CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
         * // HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
         */
        fun isChinese(c: Char): Boolean {
            val ub = Character.UnicodeBlock.of(c)
            return if (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
            ) {
                true
            } else false
        }

        /**
         * 判断字符串是否是中文字符串
         * @param chineseStr
         * @return
         */
        fun isChinese(chineseStr: String): Boolean {
            val charArray = chineseStr.toCharArray()
            for (i in charArray.indices) {
                if (charArray[i].toInt() >= 0x4e00 && charArray[i].toInt() <= 0x9fbb) {
                    return true
                }
            }
            return false
        }

        /**
         * 判断用户名是否合法，中英文数字字母加下划线
         * @param nameStr
         * @return
         */
        fun isLegalName(nameStr: String): Boolean {
            val regex = "^[a-zA-Z0-9\u4E00-\u9FA5]+$"
            val pattern = Pattern.compile(regex)
            val match = pattern.matcher(nameStr)

            return match.matches()
        }

        private val emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
        private val phone = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")

        /**
         * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
         */
        fun isEmpty(input: CharSequence?): Boolean {
            if (input == null || "" == input)
                return true

            for (i in 0 until input.length) {
                val c = input[i]
                if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                    return false
                }
            }
            return true
        }

        /**
         * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
         */
        fun isEmpty(vararg strs: CharSequence): Boolean {
            for (str in strs) {
                if (isEmpty(str)) {
                    return true
                }
            }
            return false
        }

        /**
         * 判断是不是一个合法的电子邮件地址
         */
        fun isEmail(email: CharSequence): Boolean {
            return if (isEmpty(email)) false else emailer.matcher(email).matches()
        }

        /**
         * 判断是不是一个合法的手机号码
         */
        fun isPhone(phoneNum: CharSequence): Boolean {
            return if (isEmpty(phoneNum)) false else phone.matcher(phoneNum).matches()
        }

        /**
         * 返回当前系统时间
         */
        fun getDataTime(format: String): String {
            val df = SimpleDateFormat(format)
            return df.format(Date())
        }

        /**
         * 字符串转整数
         *
         * @param str
         * @param defValue
         * @return
         */
        fun toInt(str: String, defValue: Int): Int {
            try {
                return Integer.parseInt(str)
            } catch (e: Exception) {
            }

            return defValue
        }

        /**
         * 对象转整
         *
         * @param obj
         * @return 转换异常返回 0
         */
        fun toInt(obj: Any?): Int {
            return if (obj == null) 0 else toInt(obj.toString(), 0)
        }

        /**
         * String转long
         *
         * @param obj
         * @return 转换异常返回 0
         */
        fun toLong(obj: String): Long {
            try {
                return java.lang.Long.parseLong(obj)
            } catch (e: Exception) {
            }

            return 0
        }

        /**
         * String转double
         *
         * @param obj
         * @return 转换异常返回 0
         */
        fun toDouble(obj: String): Double {
            try {
                return java.lang.Double.parseDouble(obj)
            } catch (e: Exception) {
            }

            return 0.0
        }

        /**
         * 字符串转布尔
         *
         * @param b
         * @return 转换异常返回 false
         */
        fun toBool(b: String): Boolean {
            try {
                return java.lang.Boolean.parseBoolean(b)
            } catch (e: Exception) {
            }

            return false
        }

        /**
         * 判断一个字符串是不是数字
         */
        fun isNumber(str: CharSequence): Boolean {
            try {
                Integer.parseInt(str.toString())
            } catch (e: Exception) {
                return false
            }

            return true
        }

        /**
         * byte[]数组转换为16进制的字符串。
         *
         * @param data
         * 要转换的字节数组。
         * @return 转换后的结果。
         */
        fun byteArrayToHexString(data: ByteArray): String {
            val sb = StringBuilder(data.size * 2)
            for (b in data) {
                val v = b and (0xff).toByte()
                if (v < 16) {
                    sb.append('0')
                }
                sb.append(Integer.toHexString(v.toInt()))
            }
            return sb.toString().toUpperCase(Locale.getDefault())
        }

        /**
         * 16进制表示的字符串转换为字节数组。
         *
         * @param s
         * 16进制表示的字符串
         * @return byte[] 字节数组
         */
        fun hexStringToByteArray(s: String): ByteArray {
            val len = s.length
            val d = ByteArray(len / 2)
            var i = 0
            while (i < len) {
                // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
                d[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character
                    .digit(s[i + 1], 16)).toByte()
                i += 2
            }
            return d
        }

        fun strToByteArray(str: String?): ByteArray? {
            return str?.toByteArray()
        }

        fun bytesToHexString(bArr: ByteArray): String {
            val sb = StringBuffer(bArr.size)
            var sTmp: String

            for (i in bArr.indices) {
                sTmp = Integer.toHexString(0xFF and bArr[i].toInt())
                if (sTmp.length < 2)
                    sb.append(0)
                sb.append(sTmp)
            }

            return sb.toString()
        }


        private val dateFormater = object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            }
        }

        private val dateFormater2 = object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("yyyy-MM-dd")
            }
        }

        /**
         * 以友好的方式显示时间
         *
         * @param sdate
         * @return
         */
        fun friendlyTime(sdate: String): String {
            var time: Date? = null

            if (isInEasternEightZones) {
                time = toDate(sdate)
            } else {
                time = transformTime(
                    toDate(sdate), TimeZone.getTimeZone("GMT+08"),
                    TimeZone.getDefault()
                )
            }

            if (time == null) {
                return "Unknown"
            }
            var ftime = ""
            val cal = Calendar.getInstance()

            // 判断是否是同一天
            val curDate = dateFormater2.get()!!.format(cal.time)
            val paramDate = dateFormater2.get()!!.format(time)
            if (curDate == paramDate) {
                val hour = ((cal.timeInMillis - time.time) / 3600000).toInt()
                if (hour == 0)
                    ftime = Math.max(
                        (cal.timeInMillis - time.time) / 60000, 1
                    ).toString() + "分钟前"
                else
                    ftime = hour.toString() + "小时前"
                return ftime
            }

            val lt = time.time / 86400000
            val ct = cal.timeInMillis / 86400000
            val days = (ct - lt).toInt()
            if (days == 0) {
                val hour = ((cal.timeInMillis - time.time) / 3600000).toInt()
                if (hour == 0)
                    ftime = Math.max(
                        (cal.timeInMillis - time.time) / 60000, 1
                    ).toString() + "分钟前"
                else
                    ftime = hour.toString() + "小时前"
            } else if (days == 1) {
                ftime = "昨天"
            } else if (days == 2) {
                ftime = "前天 "
            } else if (days in 3..30) {
                ftime = days.toString() + "天前"
            } else if (days >= 31 && days <= 2 * 31) {
                ftime = "一个月前"
            } else if (days > 2 * 31 && days <= 3 * 31) {
                ftime = "2个月前"
            } else if (days > 3 * 31 && days <= 4 * 31) {
                ftime = "3个月前"
            } else {
                ftime = dateFormater2.get()!!.format(time)
            }
            return ftime
        }

        @JvmOverloads
        fun toDate(sdate: String, dateFormater: SimpleDateFormat = SimpleDateFormat()): Date? {
            try {
                return dateFormater.parse(sdate)
            } catch (e: ParseException) {
                return null
            }

        }

        /**
         * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
         *
         * @return
         */
        val isInEasternEightZones: Boolean
            get() {
                var defaultVaule = true
                if (TimeZone.getDefault() === TimeZone.getTimeZone("GMT+08"))
                    defaultVaule = true
                else
                    defaultVaule = false
                return defaultVaule
            }

        /**
         * 根据不同时区，转换时间 2014年7月31日
         */
        fun transformTime(
            date: Date?, oldZone: TimeZone,
            newZone: TimeZone
        ): Date? {
            var finalDate: Date? = null
            if (date != null) {
                val timeOffset = oldZone.getOffset(date.time) - newZone.getOffset(date.time)
                finalDate = Date(date.time - timeOffset)
            }
            return finalDate
        }


        fun getPercentString(percent: Float): String {
            return String.format(Locale.US, "%d%%", (percent * 100).toInt())
        }

        /**
         * 删除字符串中的空白符
         *
         * @param content
         * @return String
         */
        fun removeBlanks(content: String?): String? {
            if (content == null) {
                return null
            }
            val buff = StringBuilder()
            buff.append(content)
            for (i in buff.length - 1 downTo 0) {
                if (' ' == buff[i] || '\n' == buff[i] || '\t' == buff[i]
                    || '\r' == buff[i]
                ) {
                    buff.deleteCharAt(i)
                }
            }
            return buff.toString()
        }

        /**
         * 获取32位uuid
         *
         * @return
         */
        val UUID_32:String
        get()
        {
            return UUID.randomUUID().toString().replace("-".toRegex(), "")
        }

        /**
         * 生成唯一号
         *
         * @return
         */
        val UUID_36:String
        get()
        {
            val uuid = UUID.randomUUID()
            return uuid.toString()
        }

        fun filterUCS4(str: String): String {
            if (TextUtils.isEmpty(str)) {
                return str
            }

            if (str.codePointCount(0, str.length) == str.length) {
                return str
            }

            val sb = StringBuilder()

            var index = 0
            while (index < str.length) {
                val codePoint = str.codePointAt(index)
                index += Character.charCount(codePoint)
                if (Character.isSupplementaryCodePoint(codePoint)) {
                    continue
                }

                sb.appendCodePoint(codePoint)
            }

            return sb.toString()
        }

        /**
         * counter ASCII character as one, otherwise two
         *
         * @param str
         * @return count
         */
        fun counterChars(str: String): Int {
            // return
            if (TextUtils.isEmpty(str)) {
                return 0
            }
            var count = 0
            for (i in 0 until str.length) {
                val tmp = str[i].toInt()
                if (tmp > 0 && tmp < 127) {
                    count += 1
                } else {
                    count += 2
                }
            }
            return count
        }
    }
}
/**
 * 检查字符串长度，如果字符串的长度超过maxLength，就截取前maxLength个字符串并在末尾拼上…
 */
/**
 * 将字符串转位日期类型
 *
 * @param sdate
 * @return
 */