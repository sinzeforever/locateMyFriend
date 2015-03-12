package hkec.yahoo.locatemyfriends;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by sinze on 3/11/15.
 */
public class Util {
    public static void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static boolean getAPIResponseStatus(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.getInt("res") == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("myLog", "Error parsing api.. response - " + response);
            return false;
        }
    }

    public static JSONObject getAPIResponseData(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            return jsonObject.getJSONObject("data");
        } catch (Exception e) {
            Log.d("myLog", "Error parsing api.. response - " + response);
            return null;
        }
    }

    public static JSONArray getAPIResponseArrayData(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            return jsonObject.getJSONArray("data");
        } catch (Exception e) {
            Log.d("myLog", "Error parsing api.. response - " + response);
            return null;
        }
    }
}
