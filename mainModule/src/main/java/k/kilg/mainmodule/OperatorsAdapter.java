package k.kilg.mainmodule;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import k.kilg.mainmodule.entity.RxOperator;

public class OperatorsAdapter extends RecyclerView.Adapter<OperatorsAdapter.OperatorsViewHolder>{

    private List<RxOperator> mData = new ArrayList<>();
    private OperatorAdapterCallback mListener;

    public OperatorsAdapter(Fragment fragment) {
        try {
            this.mListener = (OperatorAdapterCallback) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement OperatorAdapterCallback");
        }
    }

    public void setData(List<RxOperator> data) {
        this.mData = data;
    }

    @Override
    public OperatorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_operator, parent, false);
        return new OperatorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OperatorsViewHolder holder, int position) {
        RxOperator item = mData.get(position);
        holder.bind(item);
        holder.btnRun.setOnClickListener(v -> mListener.onRunClick(item.getClassName()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * ViewHolder
     */
    class OperatorsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.code)
        TextView code;

        @BindView(R.id.btn_run)
        Button btnRun;


        public OperatorsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(RxOperator rxOperator) {
            title.setText(rxOperator.getTitle());
            code.setText(rxOperator.getCode());
        }

    }

    public interface OperatorAdapterCallback {
        void onRunClick(String operator);
    }
}
