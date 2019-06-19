package org.reginpaul.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.reginpaul.MaterialsActivity;
import org.reginpaul.QuestionsActivity;
import org.reginpaul.R;

public class MaterialsTempFragment extends Fragment implements View.OnClickListener{

    String categ, semester, course;
    LinearLayout mrow1, mrow2, mrow3;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_materials_temp, container, false);

        savedInstanceState = getArguments();
        categ = savedInstanceState.getString("ctg");
        semester = savedInstanceState.getString("sem");
        course = savedInstanceState.getString("cour");

        mrow1 = rootView.findViewById(R.id.mrow1);
        mrow2 = rootView.findViewById(R.id.mrow2);
        mrow3 = rootView.findViewById(R.id.mrow3);

        mrow1.setOnClickListener(this);
        mrow2.setOnClickListener(this);
        mrow3.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mrow1:
                intent = new Intent(getActivity(),MaterialsActivity.class);
                intent.putExtra("strtext",categ);
                intent.putExtra("strSem", semester);
                intent.putExtra("strCour", course);
                intent.putExtra("type", "notes");
                startActivity(intent);
                break;
            case R.id.mrow2:
                intent = new Intent(getActivity(), QuestionsActivity.class);
                intent.putExtra("strtext",categ);
                intent.putExtra("strSem", semester);
                intent.putExtra("strCour", course);
                intent.putExtra("type", "question");
                startActivity(intent);
                break;
            case R.id.mrow3:
                intent = new Intent(getActivity(),QuestionsActivity.class);
                intent.putExtra("strtext",categ);
                intent.putExtra("strSem", semester);
                intent.putExtra("strCour", course);
                intent.putExtra("type", "qbank");
                startActivity(intent);
                break;
        }
    }
}