package com.azzahraa.paboosyar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.azzahraa.paboosyar.RetrofitModels.Response;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button enableBtn;
    boolean accepted;
    TextView resultTv;
    TextView mName;

    GridLayout mGrid;

    ConstraintLayout root;
    Response response;

    public void setType(boolean b) {
        accepted = b;
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (accepted)
            getContext().setTheme(R.style.CorrectTheme);
        else
            getContext().setTheme(R.style.WrongTheme);



        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        resultTv = rootView.findViewById(R.id.frg_result_message_text_view);
        mName = rootView.findViewById(R.id.frg_result_name_text_view);
        mGrid = rootView.findViewById(R.id.frg_result_grid);
        root = rootView.findViewById(R.id.frg_result_root);


//        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
//        root.startAnimation(aniFade);

        response = mListener.getResponse();

        resultTv.setText(response.getMessage());

        if (response.getUser() != null) {
            mName.setText(response.getUser().getName());
            List<String> lables = new ArrayList<>();
            lables.add(getString(R.string.father_name));
            lables.add(getString(R.string.phone_number));
            lables.add(getString(R.string.train_no));
            lables.add(getString(R.string.wagon_no));
            lables.add(getString(R.string.coupe_no));
            List<Object> details = new ArrayList<>();
            details.add(response.getUser().getFatherName());
            details.add(response.getUser().getMobile());
            details.add(response.getUser().getTrain());
            details.add(response.getUser().getWagon());
            details.add(response.getUser().getCoupe());

            for (int i = 0 ; i < details.size() ; i++) {
                if (details.get(i) == null )
                    continue;
                String s = details.get(i).toString();
                if (s.equals(""))
                    continue;
                TextView labelTv = new TextView(getContext());
                labelTv.setText(lables.get(i));
                labelTv.setPadding(0, 10, 50, 0);
                labelTv.requestLayout();
                TextView detailTv = new TextView(getContext());
                detailTv.setPadding(0, 10, 50, 0);
                detailTv.requestLayout();
                detailTv.setText(s);
                mGrid.addView(labelTv);
                mGrid.addView(detailTv);

                if (i == 1) {
                    detailTv.setInputType(InputType.TYPE_CLASS_PHONE);
                    detailTv.setOnClickListener(this::call);
                    detailTv.setTextColor(getResources().getColor(R.color.blue));
                    detailTv.setPaintFlags(detailTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
            }

        }

        enableBtn = rootView.findViewById(R.id.frg_done_button);
        enableBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStackImmediate();
            fragmentTransaction.commit();
            if (mListener != null) {
                mListener.onFragmentFinished();
            }
        });
        return rootView;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        Response getResponse();

        void onFragmentFinished();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener.onFragmentFinished();
    }

    public void call(View view) {
        String phone_no= ((TextView)view).getText().toString().replaceAll("-", "");
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:+98" + phone_no.substring(1)));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
        enableBtn.callOnClick();
    }

}