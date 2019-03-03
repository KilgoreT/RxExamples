package k.kilg.mainmodule.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import k.kilg.mainmodule.R;
import k.kilg.mainmodule.ui.MainMvpView;
import k.kilg.mainmodule.ui.base.BaseFragment;

public class OperatorResultFragment extends BaseFragment implements OperatorResultMvpView {

    private static final String ARG_OPERATOR = "operator_arg";

    private String mOperator;

    private OnFragmentInteractionListener mListener;

    @Inject
    OperatorResultMvpPresenter<OperatorResultMvpView> mPresenter;

    @BindView(R.id.displayResult)
    public TextView tvTitle;

    public OperatorResultFragment() {
        // Required empty public constructor
    }

    public static OperatorResultFragment newInstance(String param1) {
        OperatorResultFragment fragment = new OperatorResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OPERATOR, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOperator = getArguments().getString(ARG_OPERATOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operator_result, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRoot().getComponent().injectOperatorResultFragment(this);
        mPresenter.onAttach(this);
        mPresenter.runOperator(mOperator);
    }

    @OnClick(R.id.btn_close)
    public void onCloseClick(){
        mListener.onCloseClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListOperatorFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mPresenter.onDetach();
    }

    @Override
    public MainMvpView getRoot() {
        return (MainMvpView) getActivity();
    }

    @Override
    public void displayResult(String longIndex) {
        showText(longIndex);
    }

    private void showText(Integer integer) {
        showText(String.valueOf(integer));
    }

    private void showText(Long longIndex) {
        showText(String.valueOf(longIndex));
    }

    private void showText(String string) {
        if (tvTitle.getText().toString().isEmpty()) {
            tvTitle.setText(">  " + string);
        } else {
            tvTitle.setText(tvTitle.getText().toString().concat("\n>  " + string));
        }
    }

    public interface OnFragmentInteractionListener {
        void onCloseClick();
    }
}
