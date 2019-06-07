package org.reginpaul.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.reginpaul.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class EventsFragment extends Fragment {

    private static EditText eventName, eventDate, eventLocation;
    private Button saveEvent;
    private ImageView imgSource;
    private Spinner sType;
    String event[] = {"WORKSHOP", "CONFERENCE", "SYMPOSIUM", "OFF CAMPUS", "CULTURAL EVENTS", "PLACEMENT ACTIVITIES", "OTHER EVENTS"};
    ArrayAdapter eventArray;
    int mYear, mMonth, mDay;
    private String str1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        eventName = rootView.findViewById(R.id.event_name);
        eventDate = rootView.findViewById(R.id.event_date);
        eventLocation = rootView.findViewById(R.id.event_location);
        imgSource = rootView.findViewById(R.id.source);
        sType = rootView.findViewById(R.id.event_type);
        saveEvent = rootView.findViewById(R.id.event_send);

        eventArray = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, event);
        eventArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sType.setAdapter(eventArray);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new SelectDate();
                dFragment.show(getActivity().getFragmentManager(), "Date Picker");
            }
        });

        imgSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });


        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveEventInformation();
            }
        });


        return rootView;

    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Event Source!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                thumbnail = getResizedBitmap(thumbnail, 400);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

                FileOutputStream fos;
                try {
                    destination.createNewFile();
                    fos = new FileOutputStream(destination);
                    fos.write(bytes.toByteArray());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgSource.setImageBitmap(thumbnail);

                /*File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);
                    imgSource.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getApplicationContext().getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail=getResizedBitmap(thumbnail, 400);
                Log.w("path of image", picturePath+"");
                imgSource.setImageBitmap(thumbnail);
                BitMapToString(thumbnail);
            }
        }
    }
    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        str1 = Base64.encodeToString(b, Base64.DEFAULT);
        return str1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public static class SelectDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar cal1 = Calendar.getInstance();
            int year = cal1.get(Calendar.YEAR);
            int month = cal1.get(Calendar.MONTH);
            int day = cal1.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), R.style.timePickerTheme, this, year, month, day);
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return dpd;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // TODO Auto-generated method stub
            Calendar str1 = Calendar.getInstance();
            str1.setTimeInMillis(0);
            str1.set(year, month, day, 0, 0, 0);
            Date chosenDate = str1.getTime();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            String res = df.format(chosenDate);
            eventDate.setText(res);
        }
    }


    private void SaveEventInformation() {
        String strName, strDate, strLoc;
        strName = eventName.getText().toString();
        strDate = eventDate.getText().toString();
        strLoc = eventLocation.getText().toString();
        if (TextUtils.isEmpty(strName)) {
            Toast.makeText(getActivity(), "Please enter event name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(strDate)) {
            Toast.makeText(getActivity(), "Please enter event date", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(strLoc)) {
            Toast.makeText(getActivity(), "Please enter event location", Toast.LENGTH_SHORT).show();
        } else {
            eventName.setText("");
            eventDate.setText("");
            eventLocation.setText("");
            Toast.makeText(getActivity().getApplicationContext(), "Event Information Saved", Toast.LENGTH_SHORT).show();
        }
    }

}
