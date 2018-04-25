package xyz.ankitsiva.teamcaesium.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import xyz.ankitsiva.teamcaesium.R;
import xyz.ankitsiva.teamcaesium.model.User;

public class ViewUsersActivity extends AppCompatActivity {

	private Intent intent;
	private DatabaseReference mDatabase;
	private final GenericTypeIndicator<ArrayList<HashMap<String, Object>>> t =
			new GenericTypeIndicator<ArrayList<HashMap<String,Object>>>() {};
	private ArrayList<HashMap<String, Object>> dataList;
	private Iterator<HashMap<String, Object>> dataIterator;
	private List<User> userList;
	private List<String> keyList;
	private TextView mText;
	private EditText mEdit;
	private Context context;
	private CharSequence text;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_users);

		context = getApplicationContext();
		intent = getIntent();
		mText = (TextView) findViewById(R.id.AdminViewUsers);
		mEdit = (EditText) findViewById(R.id.SelectUser);

		userList = new ArrayList<>();
		keyList = new ArrayList<>();
		mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(
				"https://cs2340-49af4.firebaseio.com/");

		mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				// This method is called once with the initial value and again
				// whenever data at this location is updated.
				dataList = dataSnapshot.child("users").getValue(t);
				assert dataList != null;
				dataIterator = dataList.iterator();
				while (dataIterator.hasNext()) {
					User user = new User(dataIterator.next());
					if ("User".equals(user.getType())) {
						userList.add(user);
						keyList.add(user.getKey());
					}
				}
				populateUsers();
			}
			@Override
			public void onCancelled(DatabaseError databaseError) {
				// Failed to read value
				Log.w("Login", "Failed to read value.", databaseError.toException());
			}
		});


	}

	private void populateUsers() {
		String text = "";
		for (int i = 0; i < userList.size(); i++) {
			User curUser = userList.get(i);
			text = text + curUser.getKey() + "> " + curUser.getUsername() + " -> " +
					curUser.getBanned() + "\n";
		}
		mText.setText(text);
	}
	@Override
	public void onBackPressed() {
		setResult(Activity.RESULT_OK, intent);
		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return false;
	}

	public void banUser(View view) {
		String banName = mEdit.getText().toString();
		if (keyList.contains(banName)) {
			 mDatabase.child("users").child(banName).child("Banned").setValue("Banned");
			 text = "User has been banned.";
			 Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			 onBackPressed();
		} else {
			text = "This user does not exist";
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		}
	}

	public void unbanUser(View view) {
		String unbanName = mEdit.getText().toString();
		if (keyList.contains(unbanName)) {
			mDatabase.child("users").child(unbanName).child("Banned").setValue("Not Banned");
			text = "User has been unbanned.";
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			onBackPressed();
		} else {
			text = "This user does not exist";
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		}
	}
}
