package pg.pgapp.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pg.pgapp.R;

public class MainMenuFragment extends Fragment implements View.OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
		View view = inflater.inflate(R.layout.main_menu, container, false);
		Button b = view.findViewById(R.id.runOptionsButton);
		b.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.runOptionsButton:
				Intent intent = new Intent(getActivity(), OptionsActivity.class);
				startActivity(intent);
				break;
		}
	}
}