package SharedPreferences;

import android.content.Context;

public class SharedPreferences {
    android.content.SharedPreferences sharedPreferences;

    public SharedPreferences(Context context){
        sharedPreferences = context.getSharedPreferences("preferencias",Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state){ // Guarda el estado del tema oscuro
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }

    public Boolean loadNightModeState(){
        Boolean state = sharedPreferences.getBoolean("NightMode",false);
        return  state;
    }

}
