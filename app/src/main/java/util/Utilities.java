package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mlara on 10/1/2015.
 */
public class Utilities {

    //for pattern  expectedPattern = "MM/dd/yyyy";
    public static Date convertStringToDate(String dateInString) throws ParseException {
        String expectedPattern = "dd/MM/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        Date date = formatter.parse(dateInString);
        return date;
    }


    public static HashMap<String,String> dateToDayMonthYear(Date date){
        HashMap<String,String> dateValues=new HashMap<String,String>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int lenghtDay=String.valueOf(day).length();
        int lengthMonth = String.valueOf(month+1).length();
        String DayFormatted=String.valueOf(day);
        String MonthFormatted=String.valueOf(month+1);
        if (lenghtDay==1){
            DayFormatted= "0"+day;
        }
        if(lengthMonth==1){
            MonthFormatted= "0"+(month+1);
        }
        dateValues.put(Constants.day,String.valueOf(DayFormatted));
        dateValues.put(Constants.month,String.valueOf(MonthFormatted));
        dateValues.put(Constants.year,String.valueOf(year));
        return dateValues;
    }


    //to crop imageView to Oval Form
    public static Bitmap getclip(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static byte[] bitMapToBytes(Bitmap bitMap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Bitmap bytesToBitMap(byte[] bytes){
        Bitmap bitMap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitMap;
    }

    //finds the indexes in the array of positions that the user had already chosen
    public static ArrayList<Integer> convertPositionNamesToPositionIndexesInResourcesArray(ArrayList<String> positionsFromDatabase, String[] allPositionsOfSport){
        ArrayList<Integer> positionsOutput=new ArrayList<>();
       for (int i=0;i<positionsFromDatabase.size();i++){
           String positionNameFromDatabase=positionsFromDatabase.get(i);
           for ( int j=0;j<allPositionsOfSport.length;j++){
               if(positionNameFromDatabase.equals(allPositionsOfSport[j])){
                   positionsOutput.add(j);
               }
           }
       }
        return positionsOutput;
    }
}
