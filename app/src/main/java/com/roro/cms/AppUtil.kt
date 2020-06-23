package com.roro.cms

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


fun String.getDesiredTimeFormat() :String{

    val currentFormat : DateFormat =  SimpleDateFormat("HH:mm", Locale.getDefault());
    val desiredFormat : DateFormat =  SimpleDateFormat("hh:mm aa", Locale.getDefault());

    val date: Date? = currentFormat.parse(this);

    date?.let {
        return desiredFormat.format(date).toString();

    }
    return ""
}

fun getCurrentDate( formatYouWant: String = "dd/MM/yyyy" ) :String{
    val df: DateFormat = SimpleDateFormat(formatYouWant, Locale.getDefault())
    val dateobj = Date()
    return df.format(dateobj)
}

fun String.getDesiredDateFormat( formatYouWant: String = "dd-MM-yyyy", currentFormat: String = "dd/MM/yyyy" ) :String{
    val currentFormat: DateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
    val desiredFormat : DateFormat =  SimpleDateFormat(formatYouWant, Locale.getDefault());

    val date: Date? = currentFormat.parse(this);
    date?.let {
        return desiredFormat.format(date).toString();

    }
    return ""
}

fun String.getNextPreviousDate(nextOrPrevious : Int) :String{
    val calendar = Calendar.getInstance()

    val date :Date= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(this)
    calendar.time = date

    calendar.add(Calendar.DAY_OF_YEAR, nextOrPrevious)
    val desiredFormat : DateFormat =  SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    val nextOrPreviousTime =  desiredFormat.format(calendar.time)
    //  val nextOrPreviousTime = calendar.time

    println(":::: Modified Date: $nextOrPreviousTime")
    return  nextOrPreviousTime
}

fun compareTwoDates(dateFromCompare: String, dateFormat: String= "dd/MM/yyyy") : Int{

    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
   val currentDate =  sdf.format(Date())
    var dTo: Date? = null
    var dFrom: Date? = null
    try {
        dFrom = sdf.parse(dateFromCompare)
        dTo = sdf.parse(currentDate)


        if (dTo.compareTo(dFrom) > 0) {
            return 1
        } else if (dTo.compareTo(dFrom) < 0) {
            return -1
        } else if (dTo.compareTo(dFrom) == 0) {
            return 0
        } else {
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return 2
}