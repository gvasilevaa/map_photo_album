package com.android.project.photo_album;

import com.android.project.model.AlbumItem;
import com.android.project.model.ApplicationConstants;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.widget.EditText;

public class EditDetailsActivity extends Activity  implements ApplicationConstants{

	private AlbumItem item;
	private EditText title_edittxt;
	private EditText address_edittxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_details);
		
		
		item = (AlbumItem) getIntent().getSerializableExtra(ITEMS);
		
		
		populateItem();
	}

	private void populateItem(){
		
		
		
	}
	

}
