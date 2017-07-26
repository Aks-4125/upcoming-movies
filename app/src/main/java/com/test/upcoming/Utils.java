package com.test.upcoming;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;


public class Utils {

    private static final String NO_CONNECTION_ERROR = "Connection failed. Please check your internet connection.";
    private static final String NO_RESPONSE_TIMEOUT = "No response received within reply timeout.";
    private Context mContext;
    private String[] mDateformat;
    private String mDate, mTime;


    public Utils(Context context) {

        this.mContext = context;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void showSnackbarLong(String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
        snackbar.show();
    }

    public void showSnackbarIndefinite(String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
        snackbar.show();
    }

    public void showSnackbarShort(String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
        snackbar.show();
    }

    public Snackbar showSnackbar(View view, @StringRes int errorMessage, Exception e,
                                 @StringRes int actionLabel, View.OnClickListener clickListener) {
        String error = (e == null) ? "" : e.getMessage();
        boolean noConnection = NO_CONNECTION_ERROR.equals(error);
        boolean timeout = error.startsWith(NO_RESPONSE_TIMEOUT);
        if (noConnection || timeout) {
            return showSnackbar(view, R.string.check_internet, actionLabel, clickListener);
        } else if (errorMessage == 0) {
            return showSnackbar(view, error, actionLabel, clickListener);
        } else if (error.equals("")) {
            return showSnackbar(view, errorMessage, view.getContext().getString(R.string.check_internet), actionLabel, clickListener);
        } else {
            return showSnackbar(view, errorMessage, error, actionLabel, clickListener);
        }
    }

    public Snackbar showSnackbar(View view, @StringRes int errorMessage, String error,
                                 @StringRes int actionLabel, View.OnClickListener clickListener) {
        String errorMessageString = view.getContext().getString(errorMessage);
        String message = String.format("%s: %s", errorMessageString, error);
        return showSnackbar(view, message, actionLabel, clickListener);
    }

    public Snackbar showSnackbar(View view, @StringRes int message,
                                 @StringRes int actionLabel,
                                 View.OnClickListener clickListener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(actionLabel, clickListener);
        snackbar.show();
        return snackbar;
    }

    private Snackbar showSnackbar(View view, String message,
                                  @StringRes int actionLabel,
                                  View.OnClickListener clickListener) {
        Snackbar snackbar = Snackbar.make(view, message.trim(), Snackbar.LENGTH_INDEFINITE);
        if (clickListener != null) {
            snackbar.setAction(actionLabel, clickListener);
        }
        snackbar.show();
        return snackbar;
    }

    public void showAlertDialog(Context mActivity, String title, String message, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener, boolean cancelable) {
        Dialog dialog;
        if (!isEmpty(negativeButtonText)) {
            dialog = new AlertDialog.Builder(mActivity)
                    .setIcon(ContextCompat.getDrawable(mActivity, R.mipmap.ic_launcher))
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positiveButtonText, okListener)
                    .setNegativeButton(negativeButtonText, cancelListener)
                    .setCancelable(cancelable)
                    .create();
        } else {
            dialog = new AlertDialog.Builder(mActivity)
                    .setIcon(ContextCompat.getDrawable(mActivity, R.mipmap.ic_launcher))
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positiveButtonText, okListener)
                    .setCancelable(cancelable)
                    .create();
        }
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.show();
    }

    public ProgressDialog showProgressDialog(Context mContext, ProgressDialog pd) {
        if (pd != null) {
            return pd;
        }
        pd = new ProgressDialog(mContext);
        pd.setMessage(mContext.getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setIndeterminate(true);
        return pd;
    }

    public void hideProgressDialog(ProgressDialog pd) {
        if (pd != null) {

            pd.dismiss();
        }
    }

    public String getPathFromMediaUri(Activity activity, Uri uri, String projection) {
        ContentResolver contetResolver = activity.getContentResolver();
        Cursor cursor = null;
        String path = "";
        try {
            String[] proj = {
                    projection
            };
            cursor = contetResolver.query(uri, proj, null, null, null);
            int column_index;
            if (cursor != null) {
                column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

//    public boolean checkDateValidation(String start, String end) {
//
//        Date start_date = convertStringToDate(start, "hh:mm a");
//        Date end_date = convertStringToDate(end, "hh:mm a");
//
//        if (end_date.getTime() > start_date.getTime()) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean CheckTimeDate(String date, String time) {
//
//        StringBuilder builder = new StringBuilder();
//        builder.append(date);
//        builder.append(" ");
//        builder.append(time);
//        Log.e("toDate", builder.toString());
//        Date toDate = convertStringToDate(builder.toString(), "MM/dd/yyyy hh:mm a");
//        Log.e("toDate", toDate.toString());
//
//        Calendar calendar = Calendar.getInstance();
//
//        if (calendar.getTimeInMillis() < toDate.getTime()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//    public boolean checkPlayServices(Activity activity) {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
//                        .show();
//            } else {
//                Log.i("GCM", "This device is not supported.");
//            }
//            return false;
//        }
//        return true;
//    }

    public void copytoClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);

    }

    public String getDeviceId() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
//
//    public RequestBody toRequestBody(String value) {
//        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
//        return body;
//    }

    public boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str) || str.toString().equals("null");
    }

    public boolean isEmailValid(String emailstring) {
        if (isEmpty(emailstring)) {
            return false;
        }
        Pattern emailPattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(emailstring);
        return emailMatcher.matches();
    }

    public boolean isValidPhone(String phone) {

        return (!isEmpty(phone)) && android.util.Patterns.PHONE.matcher(phone).matches() && (!(phone.length() < 10 || phone.length() > 16));

    }

    public boolean isMarshmallow() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public String convertFileToBase64(File file) {
        String base64String = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            bytes = output.toByteArray();
            base64String = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64String;
    }

    public Observable<Uri> getBitmapUriFromUrlRx(final String url, final String fileType) {
        return Observable.create(new Observable.OnSubscribe<Uri>() {
            @Override
            public void call(Subscriber<? super Uri> subscriber) {
                try {
                    Bitmap bitmap = Glide.with(mContext)
                            .load(url)
                            .asBitmap()
                            .into(-1, -1)
                            .get();

                    subscriber.onNext(getLocalBitmapUri(bitmap, fileType));
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    subscriber.onError(e);
                } catch (ExecutionException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp, String fileType) {
        Bitmap.CompressFormat format;
        String type;
        if (fileType.contains("png")) {
            type = ".png";
            format = Bitmap.CompressFormat.PNG;
        } else if (fileType.contains("jpeg")) {
            type = ".jpg";
            format = Bitmap.CompressFormat.JPEG;
        } else {
            type = ".png";
            format = Bitmap.CompressFormat.PNG;
        }

        Uri bmpUri = null;
        try {
            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + type);
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(format, 100, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public String getAppFolderPath() {
        File myDirectory = new File(Environment.getExternalStorageDirectory(), mContext.getResources().getString(R.string.app_name));

        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }

        return myDirectory.getAbsolutePath();
    }

    public String copyFile(File sourceLocation, File targetLocation) throws IOException {

        if (!targetLocation.exists()) {
            targetLocation.mkdir();
        }

        File newFile = new File(targetLocation, sourceLocation.getName());

        FileChannel outputChannel = null;
        FileChannel inputChannel = null;

        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(sourceLocation).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();

        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }
        return newFile.getPath();
    }

    public String compressImage(String filePath) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
//        String filename = getFilename();
        try {
            out = new FileOutputStream(filePath);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filePath;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void saveBitmapToFile(Bitmap bmp, File file) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Date convertStringToDate(String strDate, String currentFormat) {
        try {
            return new SimpleDateFormat(currentFormat, Locale.getDefault()).parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            //If It can not be parsed, return today's date instead of null. So return value of this method does not create null pointer exception.
            return new Date();
        }
    }

    public String convertDateStringToString(String strDate, String currentFormat,
                                            String parseFormat) {
        if (isDateStringProperlyFormatted(strDate, currentFormat)) {
            try {
                return convertDateToString(convertStringToDate(strDate, currentFormat),
                        parseFormat);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return strDate;
        }
    }

    public String convertDateToString(Date objDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat, Locale.getDefault()).format(objDate);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean isDateStringProperlyFormatted(String dateString, String dateFormat) {
        boolean isProperlyFormatted = false;
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());
        //SetLenient meanse dateString will be checked strictly with dateFormat. Otherwise it will format TaskDetailActivity per wish.
        format.setLenient(false);
        try {
            Date date = format.parse(dateString);
            isProperlyFormatted = true;
        } catch (ParseException e) {
            //exception means dateString is not parsable by dateFormat. Thus dateIsProperlyFormatted.
        }
        return isProperlyFormatted;
    }

    public int daysBetween(Calendar day1, Calendar day2) {
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }

    public boolean isArrayListEmpty(ArrayList arraylist) {
        return arraylist == null || arraylist.isEmpty();
    }

    public boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public Drawable getTintedDrawable(Context context,
                                      @DrawableRes int drawableResId, @ColorRes int colorResId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
        int color = ContextCompat.getColor(context, colorResId);
        drawable.mutate();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }


    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = mContext.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public Object convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        if (!isEmpty(path)) {
            return Uri.parse(path);
        } else {
            return Uri.EMPTY;
        }
    }

    public Bitmap doResize(Bitmap b, int height, int width) {
        try {
            int newHeight;
            int newWidth;
            int Height = b.getHeight();
            int Width = b.getWidth();
            newHeight = height;
            newWidth = width;
            float scaleWidth = ((float) newWidth) / Width;
            float scaleHeight = ((float) newHeight) / Height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(b, 0, 0, Width, Height, matrix, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getFormattedDateforTaskComment(String mFullDate) {
        mDateformat = mFullDate.split("-");
        mDate = mDateformat[0];
        mTime = mDateformat[1];
        String mTodayDate = convertDateToString(new Date(), "MM/dd/yy");
        if (!mTodayDate.equalsIgnoreCase(mDate))
            return mTime + "\n" + mDate;
        else
            return mTime;

    }


    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        return animator;
    }

}
