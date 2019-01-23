package k.kilg.mainmodule.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import k.kilg.mainmodule.OperatorsAdapter;
import k.kilg.mainmodule.R;
import k.kilg.mainmodule.entity.RxOperator;
import k.kilg.mainmodule.ui.MainMvpView;
import k.kilg.mainmodule.ui.base.BaseFragment;

public class ListOperatorFragment extends BaseFragment implements ListOperationMvpView, OperatorsAdapter.OperatorAdapterCallback {

    private static final int CLASS_INDEX = 0;
    private static final int CATEGORY_INDEX = 1;
    private static final int DOCS_INDEX = 2;
    private static final int CODE_INDEX = 3;

    private OperatorsAdapter mAdapter;
    private OnListOperatorFragmentListener mListener;

    @Inject
    ListOperationMvpPresenter<ListOperationMvpView> mPresenter;

    @BindView(R.id.recycler)
    public RecyclerView recyclerView;

    public ListOperatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ListOperatorFragment.
     */
    public static ListOperatorFragment newInstance() {
        ListOperatorFragment fragment = new ListOperatorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_operator, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRoot().getComponent().injectListOperationFragment(this);

        mPresenter.onAttach(this);
        mPresenter.test();

        mAdapter = new OperatorsAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<RxOperator> operatorList = new ArrayList<>();
        List<String> titleList = Arrays.asList(getResources().getStringArray(R.array.operators));
        for (String title : titleList) {
            List<String> properties = Arrays
                    .asList(getResources().getStringArray(getResources().getIdentifier(title, "array", getActivity().getPackageName())));
            operatorList.add(new RxOperator(properties.get(CLASS_INDEX), title, properties.get(DOCS_INDEX), properties.get(CODE_INDEX)));
        }
        mAdapter.setData(operatorList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListOperatorFragmentListener) {
            mListener = (OnListOperatorFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListOperatorFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public MainMvpView getRoot() {
        return (MainMvpView) getActivity();
    }

    @Override
    public void onRunClick(String operator) {
        mListener.onRunClick(operator);
    }

    public interface OnListOperatorFragmentListener {
        void onRunClick(String operator);
    }
}
